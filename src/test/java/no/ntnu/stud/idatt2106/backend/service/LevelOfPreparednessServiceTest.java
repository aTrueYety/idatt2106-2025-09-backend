package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.KitResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LevelOfPreparednessResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for LevelOfPreparedneddService.
 */
@ExtendWith(MockitoExtension.class)
public class LevelOfPreparednessServiceTest {

  @Mock
  private FoodService foodService;
  @Mock
  private HouseholdService householdService;
  @Mock
  private HouseholdKitService householdKitService;
  @Mock
  private KitService kitService;
  @Mock
  private ExtraResidentService extraResidentService;
  @Mock
  private ExtraResidentTypeService extraResidentTypeService;
  @Mock
  private UserService userService;

  @InjectMocks
  private LevelOfPreparednessService preparednessService;

  private ExtraResidentResponse extraResident1;
  private ExtraResidentResponse extraResident2;
  private ExtraResidentTypeResponse type1;
  private ExtraResidentTypeResponse type2;

  @BeforeEach
  void setup() {
    // Shared test data
    extraResident1 = new ExtraResidentResponse();
    extraResident1.setId(1L);
    extraResident1.setName("Erik");
    extraResident1.setTypeId(1L);
    extraResident1.setHouseholdId(2L);

    extraResident2 = new ExtraResidentResponse();
    extraResident2.setId(2L);
    extraResident2.setName("Anna");
    extraResident2.setTypeId(2L);
    extraResident2.setHouseholdId(2L);

    type1 = new ExtraResidentTypeResponse(1L, "Katt", 1.0f, 1000.0f); // 1L/day water
    type2 = new ExtraResidentTypeResponse(2L, "Hund", 1.5f, 1500.0f); // 1.5L/day water
  }

  @Test
  void testCalculateLevelOfPreparednessWater_notEnoughWater() {
    long householdId = 1L;
    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(6.0);

    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse())); // 2 users
    when(extraResidentService.getAll()).thenReturn(List.of());

    // 40/6 = 0.66667
    double result = preparednessService.calculateLevelOfPreparednessWater(householdResponse);

    assertEquals(0.15, result); // (2 users * 20L = 40L) -> 6/40 = 0.15
  }

  @Test
  void testCalculateLevelOfPreparednessWater_withExtraResidents() {
    long householdId = 2L;
    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(18.0);

    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse())); // 1 user

    ExtraResidentResponse extraResident1 = new ExtraResidentResponse();
    extraResident1.setId(1L);
    extraResident1.setHouseholdId(householdId);
    extraResident1.setTypeId(1);
    extraResident1.setName("Erik");
    ExtraResidentResponse extraResident2 = new ExtraResidentResponse();
    extraResident2.setId(2L);
    extraResident2.setHouseholdId(householdId);
    extraResident2.setTypeId(2);
    extraResident2.setName("Anna");

    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse())); // 1 user

    when(extraResidentService.getAll()).thenReturn(List.of(extraResident1, extraResident2));

    when(extraResidentTypeService.getById(1L))
        .thenReturn(Optional.of(type1)); // 1L/day for type 1
    when(extraResidentTypeService.getById(2L))
        .thenReturn(Optional.of(type2)); // 2L/day for type 2

    double result = preparednessService.calculateLevelOfPreparednessWater(householdResponse);
    assertEquals(0.48, result, 0.00001);
    // (1 user * 20L + (1L + 1.5L) * 3d = 27.5L) -> 18/27.5 = 0.65454545
  }

  @Test
  void testCalculateLevelOfPreparednessWater_noWater() {
    long householdId = 3L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(0.0);

    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse())); // 2 users
    when(extraResidentService.getAll()).thenReturn(List.of());

    double result = preparednessService.calculateLevelOfPreparednessWater(householdResponse);
    assertEquals(0.0, result); // (2 users * 2L * 3d = 12L) -> 0/12 = 0.0
  }

  @Test
  void testCalculateLevelOfPreparednessWater_noUsersOrResidents() {
    long householdId = 4L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(10.0);

    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of()); // No users
    when(extraResidentService.getAll()).thenReturn(List.of()); // No extra residents

    double result = preparednessService.calculateLevelOfPreparednessWater(householdResponse);
    assertEquals(0.0, result); // No users or residents -> no consumption -> 0/0 = 0.0
  }

  @Test
  void testCalculateLevelOfFoodPreparedness_basicScenario() {
    long householdId = 2L;

    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(70000.0);

    double result = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(1.0, result);
  }

  @Test
  void testCalculateLevelOfPreparednessKit_fullPreparedness() {
    long householdId = 3L;

    when(kitService.getAll()).thenReturn(List.of(
        new KitResponse(),
        new KitResponse(),
        new KitResponse())); // 3 kit types

    when(householdKitService.getByHouseholdId(householdId)).thenReturn(List.of(
        new HouseholdKitResponse(),
        new HouseholdKitResponse(),
        new HouseholdKitResponse()));

    double result = preparednessService.calculateLevelOfPreparednessKit(householdId);
    assertEquals(1.0, result);
  }

  @Test
  void testCalculateOverallLevelOfPreparedness_average() {
    long householdId = 4L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(12.0);

    // Mock individual preparedness data
    when(foodService.getCaloriesByHouseholdId(householdId))
        .thenReturn(4000.0); // 2/3 of required calories
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse())); // 2/3 of required kits
    when(kitService.getAll()).thenReturn(List.of(
        new KitResponse(), new KitResponse(), new KitResponse()));

    // Water preparedness: 12L / (1 user * 20L) = 0.6
    // Food preparedness: 4000 / 6000 = 0.66667
    // Kit preparedness: 2/3 = 0.66667
    // Overall preparedness: (0.6 + 0.66667 + 0.66667) / 3 = 0.64445

    double result = preparednessService.calculateOverallLevelOfPreparedness(householdResponse);
    assertEquals(0.51746, result, 0.00001);
  }

  @Test
  void testGetPreparednessForHousehold_returnsPerfectPreparedness() {
    long householdId = 5L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(40.0);

    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(14000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(), new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    LevelOfPreparednessResponse response = 
        preparednessService.getPreparednessForHousehold(householdResponse);

    assertNotNull(response);
    assertEquals(1.0, response.getLevelOfPreparednessFood());
    assertEquals(1.0, response.getLevelOfPreparednessWater());
    assertEquals(1.0, response.getLevelOfPreparednessKit());
    assertEquals(1.0, response.getLevelOfPreparedness());
  }

  @Test
  public void testGetPreparednessForHouseholdNotPerfectPreparedness() {
    long householdId = 6L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(2.0);

    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(5000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(), new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    LevelOfPreparednessResponse response = 
        preparednessService.getPreparednessForHousehold(householdResponse);

    assertNotNull(response);
    assertEquals(0.35714, response.getLevelOfPreparednessFood(), 0.0001);
    assertEquals(0.1, response.getLevelOfPreparednessWater(), 0.0001);
    assertEquals(1.0, response.getLevelOfPreparednessKit(), 0.0001);
    assertEquals(0.48571, response.getLevelOfPreparedness(), 0.0001);
  }

  @Test
  void testCalculateLevelOfPreparednessWater_waterDecreases() {
    long householdId = 7L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(6.0);

    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse())); // 2 users
    when(extraResidentService.getAll()).thenReturn(List.of()); // No extra residents

    // 2 users => 2 * 20L = 40L needed
    // Initial: 6 / 40 = 0.15
    double initialResult = preparednessService.calculateLevelOfPreparednessWater(householdResponse);
    assertEquals(0.15, initialResult, 0.0001);

    // Water decreases to 3L -> 3 / 40 = 0.075
    householdResponse.setWaterAmountLiters(3.0);
    double decreasedResult = 
        preparednessService.calculateLevelOfPreparednessWater(householdResponse);
    assertEquals(0.075, decreasedResult, 0.0001);
  }

  @Test
  void testCalculateLevelOfFoodPreparedness_foodDecreases() {
    long householdId = 8L;

    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(14000.0);

    double initialResult = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(1.0, initialResult); 

    // Food decreases
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(7000.0);
    double decreasedResult = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(0.5, decreasedResult); 
  }

  @Test
  void testCalculateOverallLevelOfPreparedness_preparednessDecreases() {
    long householdId = 10L;

    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(householdId);
    householdResponse.setWaterAmountLiters(20.0);

    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(14000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());

    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(
            new HouseholdKitResponse(),
            new HouseholdKitResponse(),
            new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(
        new KitResponse(),
        new KitResponse(),
        new KitResponse()));

    double initialResult = preparednessService
        .calculateOverallLevelOfPreparedness(householdResponse);
    assertEquals(1.0, initialResult, 0.0001);

    householdResponse.setWaterAmountLiters(6.0);
    double decreasedResult = preparednessService
        .calculateOverallLevelOfPreparedness(householdResponse);
    assertTrue(decreasedResult < 1.0);
    assertTrue(decreasedResult < initialResult);
  }

  @Test
  void testCalculatedLevelOfPreparednessKit_kitDecreases() {
    long householdId = 11L;

    when(kitService.getAll()).thenReturn(List.of(
        new KitResponse(),
        new KitResponse(),
        new KitResponse()));

    when(householdKitService.getByHouseholdId(householdId)).thenReturn(List.of(
        new HouseholdKitResponse(),
        new HouseholdKitResponse(),
        new HouseholdKitResponse()));

    double initialResult = preparednessService.calculateLevelOfPreparednessKit(householdId);
    assertEquals(1.0, initialResult);

    // Simulate a decrease in kits
    when(householdKitService.getByHouseholdId(householdId)).thenReturn(List.of(
        new HouseholdKitResponse(),
        new HouseholdKitResponse())); 

    double decreasedResult = preparednessService.calculateLevelOfPreparednessKit(householdId);
    assertEquals(0.66667, decreasedResult, 0.0001);
  }

}
