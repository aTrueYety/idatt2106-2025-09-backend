package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LevelOfPreparednessServiceTest {

  private FoodService foodService;
  private HouseholdService householdService;
  private HouseholdKitService householdKitService;
  private KitService kitService;
  private ExtraResidentService extraResidentService;
  private ExtraResidentTypeService extraResidentTypeService;
  private UserService userService;

  private LevelOfPreparednessService preparednessService;

  private ExtraResidentResponse extraResident1;
  private ExtraResidentResponse extraResident2;
  private ExtraResidentTypeResponse type1;
  private ExtraResidentTypeResponse type2;

  @BeforeEach
  void setup() {
    foodService = mock(FoodService.class);
    householdService = mock(HouseholdService.class);
    householdKitService = mock(HouseholdKitService.class);
    kitService = mock(KitService.class);
    extraResidentService = mock(ExtraResidentService.class);
    extraResidentTypeService = mock(ExtraResidentTypeService.class);
    userService = mock(UserService.class);

    preparednessService = new LevelOfPreparednessService(
        foodService, householdService, householdKitService,
        kitService, extraResidentService, extraResidentTypeService, userService);

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

    when(householdService
        .getWaterAmount(householdId)).thenReturn(6.0); // Only 6L of water available
    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse())); // 2 users
    when(extraResidentService.getAll()).thenReturn(List.of());

    // 40/6 = 0.66667

    double result = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.15, result); // (2 users * 20L = 40L) -> 6/40 = 0.15
  }

  @Test
  void testCalculateLevelOfPreparednessWater_withExtraResidents() {
    long householdId = 2L;

    when(householdService.getWaterAmount(householdId)).thenReturn(18.0); // 18L of water available
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

    double result = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.65454, result, 0.00001);
    // (1 user * 20L + (1L + 1.5L) * 3d = 27.5L) -> 18/27.5 = 0.65454545
  }

  @Test
  void testCalculateLevelOfPreparednessWater_noWater() {
    long householdId = 3L;

    when(householdService.getWaterAmount(householdId)).thenReturn(0.0); // No water available
    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse())); // 2 users
    when(extraResidentService.getAll()).thenReturn(List.of());

    double result = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.0, result); // (2 users * 2L * 3d = 12L) -> 0/12 = 0.0
  }

  @Test
  void testCalculateLevelOfPreparednessWater_noUsersOrResidents() {
    long householdId = 4L;

    when(householdService.getWaterAmount(householdId)).thenReturn(10.0); // 10L of water available
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of()); // No users
    when(extraResidentService.getAll()).thenReturn(List.of()); // No extra residents

    double result = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.0, result); // No users or residents -> no consumption -> 0/0 = 0.0
  }

  @Test
  void testCalculateLevelOfFoodPreparedness_basicScenario() {
    long householdId = 2L;

    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(6000.0);

    double result = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(1.0, result); // (1 user * 2000 * 3 = 6000) -> 6000 / 6000 = 1.0
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

    // Mock individual preparedness levels
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(6000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdService.getWaterAmount(householdId)).thenReturn(12.0);
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(),
            new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    double result = preparednessService.calculateOverallLevelOfPreparedness(householdId);
    assertEquals(0.86667, result, 0.00001); // all components return 1.0
  }

  @Test
  void testGetPreparednessForHousehold_returnsPerfectPreparedness() {
    long householdId = 5L;

    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(6000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdService.getWaterAmount(householdId)).thenReturn(40.0);
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(), new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    LevelOfPreparednessResponse response = preparednessService
        .getPreparednessForHousehold(householdId);

    assertNotNull(response);
    assertEquals(1.0, response.getLevelOfPreparedness());
    assertEquals(1.0, response.getLevelOfPreparednessFood());
    assertEquals(1.0, response.getLevelOfPreparednessWater());
    assertEquals(1.0, response.getLevelOfPreparednessKit());
  }

  @Test
  public void testGetPreparednessFOrHouseholdNotPerfectPreparedness() {
    long householdId = 6L;

    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(5000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdService.getWaterAmount(householdId)).thenReturn(2.0);
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(), new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    LevelOfPreparednessResponse response = preparednessService
        .getPreparednessForHousehold(householdId);

    assertNotNull(response);
    assertEquals(0.83333, response.getLevelOfPreparednessFood(), 0.0001);
    assertEquals(0.1, response.getLevelOfPreparednessWater(), 0.0001);
    assertEquals(1.0, response.getLevelOfPreparednessKit(), 0.0001);
    assertEquals(0.64445, response.getLevelOfPreparedness(), 0.0001);
  }

  @Test
  void testCalculateLevelOfPreparednessWater_waterDecreases() {
    long householdId = 7L;

    when(householdService.getWaterAmount(householdId)).thenReturn(6.0);
    when(userService.getUsersByHouseholdId(householdId))
        .thenReturn(List.of(new UserResponse(), new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());

    double initialResult = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.15, initialResult);

    // Water decreases
    when(householdService.getWaterAmount(householdId)).thenReturn(3.0);
    double decreasedResult = preparednessService.calculateLevelOfPreparednessWater(householdId);
    assertEquals(0.075, decreasedResult);
  }

  @Test
  void testCalculateLevelOfFoodPreparedness_foodDecreases() {
    long householdId = 8L;

    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(6000.0);

    double initialResult = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(1.0, initialResult); // (1 user * 2000 * 3 = 6000) -> 6000 / 6000 = 1.0

    // Food decreases
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(3000.0);
    double decreasedResult = preparednessService.calculateLevelOfFoodPreparedness(householdId);
    assertEquals(0.5, decreasedResult); // (1 user * 2000 * 3 = 6000) -> 3000 / 6000 = 0.5
  }

  @Test
  void testCalculateOverallLevelOfPreparedness_preparednessDecreases() {
    long householdId = 10L;

    // Initial state
    when(foodService.getCaloriesByHouseholdId(householdId)).thenReturn(6000.0);
    when(userService.getUsersByHouseholdId(householdId)).thenReturn(List.of(new UserResponse()));
    when(extraResidentService.getAll()).thenReturn(List.of());
    when(householdService.getWaterAmount(householdId)).thenReturn(20.0);
    when(householdKitService.getByHouseholdId(householdId))
        .thenReturn(List.of(new HouseholdKitResponse(),
            new HouseholdKitResponse(),
            new HouseholdKitResponse()));
    when(kitService.getAll()).thenReturn(List.of(new KitResponse(),
        new KitResponse(), new KitResponse()));

    double initialResult = preparednessService.calculateOverallLevelOfPreparedness(householdId);
    assertEquals(1.0, initialResult);

    // Water decreases
    when(householdService.getWaterAmount(householdId)).thenReturn(6.0); // Water reduced
    double decreasedResult = preparednessService.calculateOverallLevelOfPreparedness(householdId);
    assertTrue(decreasedResult < 1.0); // Overall preparedness should decrease
  }
}
