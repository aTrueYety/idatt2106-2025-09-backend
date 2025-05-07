package no.ntnu.stud.idatt2106.backend.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.SharedFoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class SharedFoodServiceTest {

  @Mock
  private SharedFoodRepository sharedRepo;
  @Mock
  private FoodRepository foodRepo;
  @Mock
  private HouseholdRepository householdRepo;
  @Mock
  private FoodTypeRepository typeRepo;
  @Mock
  private GroupHouseholdRepository groupRepo;
  @Mock
  private JwtService jwtService;

  @InjectMocks
  private SharedFoodService service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateSharedFood() {
    final SharedFoodRequest request = new SharedFoodRequest(1L, 5.0f, 1L);
    final String token = "Bearer test";
    final Long userId = 10L;
    final Long householdId = 20L;

    Food food = new Food();
    food.setId(1L);
    food.setTypeId(2L);
    food.setHouseholdId(householdId);
    food.setExpirationDate(LocalDate.now());
    food.setAmount(100.0f);

    // Household
    Household household = new Household();
    household.setId(householdId);

    // GroupHousehold
    GroupHousehold group = new GroupHousehold();
    group.setId(3L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    when(jwtService.extractUserId("test")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(1L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);

    service.create(request, token);

    verify(sharedRepo).save(any(SharedFood.class));
  }

  @Test
  void shouldReturnAllSharedFoods() {
    SharedFood s = new SharedFood(new SharedFoodKey(1L, 2L), 3.0f);
    when(sharedRepo.findAll()).thenReturn(List.of(s));

    List<SharedFoodResponse> result = service.getAll();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFoodId()).isEqualTo(1L);
  }

  @Test
  void shouldUpdateSharedFood() {
    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setAmount(10f);
    request.setGroupId(1L);

    final Long userId = 5L;
    final Long householdId = 15L;

    GroupHousehold group = new GroupHousehold();
    group.setId(33L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    Household household = new Household();
    household.setId(householdId);

    final String token = "Bearer x";
    when(jwtService.extractUserId("x")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);
    when(sharedRepo.update(any())).thenReturn(true);

    boolean result = service.update(request, token);
    assertThat(result).isTrue();
  }



  @Test
  void shouldDeleteSharedFood() {
    final Long userId = 9L;
    final Long householdId = 30L;
  
    GroupHousehold group = new GroupHousehold();
    group.setId(99L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);
  
    Household household = new Household();
    household.setId(householdId);
  
    final String token = "Bearer del";
    when(jwtService.extractUserId("del")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);
    when(sharedRepo.deleteById(any())).thenReturn(true);
  
    boolean deleted = service.delete(5L, 1L, token);
  
    assertThat(deleted).isTrue();
  }

  
}
