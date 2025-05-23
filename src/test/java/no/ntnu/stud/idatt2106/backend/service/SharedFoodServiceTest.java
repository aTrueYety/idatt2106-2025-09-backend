package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
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

/**
 * Contains tests for SharedFoodService.
 */
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

    Household household = new Household();
    household.setId(householdId);

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

  @Test
  void shouldReturnFalseWhenFoodToShareNotFound() {
    final String token = "Bearer fail";
    final Long userId = 1L;

    final SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(99L);
    request.setAmount(5f);
    request.setGroupId(1L);

    when(jwtService.extractUserId("fail")).thenReturn(userId);
    Household household = new Household();
    household.setId(10L);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(99L)).thenReturn(Optional.empty());

    boolean result = service.moveFoodToSharedGroup(request, token);
    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnFalseWhenSharedFoodNotFound() {
    final String token = "Bearer none";
    final Long userId = 2L;
    final Long householdId = 20L;

    final SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setAmount(5f);
    request.setGroupId(1L);

    Food food = new Food();
    food.setId(1L);
    food.setTypeId(7L);
    food.setHouseholdId(householdId);
    food.setExpirationDate(LocalDate.now());
    food.setAmount(50f);

    GroupHousehold group = new GroupHousehold();
    group.setId(99L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    Household household = new Household();
    household.setId(householdId);

    when(jwtService.extractUserId("none")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(1L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);
    when(sharedRepo.findById(new SharedFoodKey(1L, 99L))).thenReturn(Optional.empty());

    boolean result = service.moveFoodFromSharedGroup(request, token);
    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnFalseWhenAmountToUnshareIsTooHigh() {
    final String token = "Bearer over";
    final Long userId = 6L;
    final Long householdId = 12L;

    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setAmount(10f);
    request.setGroupId(1L);

    Food food = new Food();
    food.setId(1L);
    food.setTypeId(3L);
    food.setHouseholdId(householdId);
    food.setExpirationDate(LocalDate.now());
    food.setAmount(20f);

    GroupHousehold group = new GroupHousehold();
    group.setId(50L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    Household household = new Household();
    household.setId(householdId);

    when(jwtService.extractUserId("over")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(1L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);

    final SharedFood shared = new SharedFood(new SharedFoodKey(1L, 50L), 5f);
    when(sharedRepo.findById(new SharedFoodKey(1L, 50L))).thenReturn(Optional.of(shared));

    boolean result = service.moveFoodFromSharedGroup(request, token);
    assertThat(result).isFalse();
  }

  @Test
  void shouldCreateNewFoodWhenUnsharingAndNoExistingMatch() {
    final String token = "Bearer create";
    final Long userId = 7L;
    final Long householdId = 42L;

    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setAmount(3f);
    request.setGroupId(2L);

    Food food = new Food();
    food.setId(1L);
    food.setTypeId(5L);
    food.setHouseholdId(householdId);
    food.setExpirationDate(LocalDate.of(2025, 5, 10));
    food.setAmount(10f);

    GroupHousehold group = new GroupHousehold();
    group.setId(77L);
    group.setHouseholdId(householdId);
    group.setGroupId(2L);

    Household household = new Household();
    household.setId(householdId);

    when(jwtService.extractUserId("create")).thenReturn(userId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(1L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 2L)).thenReturn(group);

    final SharedFood shared = new SharedFood(new SharedFoodKey(1L, 77L), 5f);
    when(sharedRepo.findById(new SharedFoodKey(1L, 77L))).thenReturn(Optional.of(shared));
    when(foodRepo.findByTypeIdAndExpirationDateAndHouseholdId(
        5L, food.getExpirationDate(), householdId)).thenReturn(Optional.empty());
    boolean result = service.moveFoodFromSharedGroup(request, token);

    assertThat(result).isTrue();
    verify(foodRepo).save(any(Food.class));
    verify(sharedRepo).update(any());
  }

  @Test
  void shouldReturnSummaryByGroupId() {
    Long groupId = 1L;
    Long groupHouseholdId = 10L;
    final Long foodId = 5L;
    final Long typeId = 3L;

    GroupHousehold membership = new GroupHousehold();
    membership.setId(groupHouseholdId);
    membership.setGroupId(groupId);
    membership.setHouseholdId(100L);

    Food food = new Food();
    food.setId(foodId);
    food.setTypeId(typeId);
    food.setExpirationDate(LocalDate.of(2025, 12, 31));
    food.setAmount(10f);
    food.setHouseholdId(100L);

    final SharedFood shared = new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), 2f);

    FoodType type = new FoodType();
    type.setId(typeId);
    type.setName("Ris");
    type.setUnit("kg");
    type.setCaloriesPerUnit(350f);

    when(groupRepo.findByGroupId(groupId)).thenReturn(List.of(membership));
    when(sharedRepo.findByGroupHouseholdId(groupHouseholdId)).thenReturn(List.of(shared));
    when(foodRepo.findById(foodId)).thenReturn(Optional.of(food));
    when(typeRepo.findById(typeId)).thenReturn(Optional.of(type));

    List<FoodDetailedResponse> result = service.getSharedFoodSummaryByGroupId(groupId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTypeName()).isEqualTo("Ris");
    assertThat(result.get(0).getTotalCalories()).isEqualTo(700f);
  }

  @Test
  void shouldSkipEntriesWithoutFoodTypeInSummaryByGroupId() {
    Long groupId = 1L;
    Long groupHouseholdId = 10L;
    final Long foodId = 5L;
    final Long typeId = 3L;

    GroupHousehold membership = new GroupHousehold();
    membership.setId(groupHouseholdId);
    membership.setGroupId(groupId);
    membership.setHouseholdId(100L);

    Food food = new Food();
    food.setId(foodId);
    food.setTypeId(typeId);
    food.setExpirationDate(LocalDate.of(2025, 12, 31));
    food.setAmount(10f);
    food.setHouseholdId(100L);

    final SharedFood shared = new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), 2f);

    when(groupRepo.findByGroupId(groupId)).thenReturn(List.of(membership));
    when(sharedRepo.findByGroupHouseholdId(groupHouseholdId)).thenReturn(List.of(shared));
    when(foodRepo.findById(foodId)).thenReturn(Optional.of(food));
    when(typeRepo.findById(typeId)).thenReturn(Optional.empty());

    List<FoodDetailedResponse> result = service.getSharedFoodSummaryByGroupId(groupId);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldFailToMoveFoodToSharedGroupIfInsufficientAmount() {
    final String token = "Bearer test";
    final Long userId = 1L;
    final Long householdId = 2L;
    final float availableAmount = 3f;
    final float requestedAmount = 5f;

    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(10L);
    request.setAmount(requestedAmount);
    request.setGroupId(1L);

    Food food = new Food();
    food.setId(10L);
    food.setHouseholdId(householdId);
    food.setAmount(availableAmount);
    food.setExpirationDate(LocalDate.now());

    GroupHousehold group = new GroupHousehold();
    group.setId(99L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    when(jwtService.extractUserId("test")).thenReturn(userId);
    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(10L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);

    boolean result = service.moveFoodToSharedGroup(request, token);

    assertThat(result).isFalse();
    verify(sharedRepo, never()).save(any());
    verify(sharedRepo, never()).update(any());
    verify(foodRepo, never()).update(any());
  }

  @Test
  void shouldFailToMoveFoodFromSharedGroupIfNoSharedFoodExists() {
    final String token = "Bearer test";
    final Long userId = 7L;
    final Long householdId = 14L;

    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(50L);
    request.setAmount(3f);
    request.setGroupId(1L);

    Food food = new Food();
    food.setId(50L);
    food.setHouseholdId(householdId);
    food.setTypeId(20L);
    food.setExpirationDate(LocalDate.now());

    GroupHousehold group = new GroupHousehold();
    group.setId(200L);
    group.setHouseholdId(householdId);
    group.setGroupId(1L);

    when(jwtService.extractUserId("test")).thenReturn(userId);
    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(50L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, 1L)).thenReturn(group);
    when(sharedRepo.findById(new SharedFoodKey(50L, 200L))).thenReturn(Optional.empty());

    boolean result = service.moveFoodFromSharedGroup(request, token);

    assertThat(result).isFalse();
    verify(sharedRepo, never()).update(any());
    verify(foodRepo, never()).update(any());
  }

  @Test
  void shouldFailToMoveFoodFromSharedGroupIfFoodNotFound() {
    final String token = "Bearer test";
    final Long userId = 8L;
    final Long householdId = 22L;

    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(99L);
    request.setAmount(2f);
    request.setGroupId(1L);

    when(jwtService.extractUserId("test")).thenReturn(userId);
    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(userId)).thenReturn(Optional.of(household));
    when(foodRepo.findById(99L)).thenReturn(Optional.empty());

    boolean result = service.moveFoodFromSharedGroup(request, token);

    assertThat(result).isFalse();
    verify(sharedRepo, never()).findById(any());
    verify(groupRepo, never()).findByHouseholdIdAndGroupId(any(), any());
  }

  @Test
  void shouldReturnSummaryByGroupHouseholdId() {
    final Long groupHouseholdId = 7L;
    final Long foodId = 10L;
    final Long typeId = 3L;

    Food food = new Food();
    food.setId(foodId);
    food.setTypeId(typeId);
    food.setExpirationDate(LocalDate.of(2025, 10, 10));
    food.setAmount(20f);
    food.setHouseholdId(4L);

    FoodType type = new FoodType();
    type.setId(typeId);
    type.setName("Mel");
    type.setUnit("kg");
    type.setCaloriesPerUnit(360f);

    SharedFood shared = new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), 2f);
    when(sharedRepo.findByGroupHouseholdId(groupHouseholdId)).thenReturn(List.of(shared));
    when(foodRepo.findById(foodId)).thenReturn(Optional.of(food));
    when(typeRepo.findById(typeId)).thenReturn(Optional.of(type));

    List<FoodDetailedResponse> result = service.getSharedFoodSummaryByGroup(groupHouseholdId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTypeName()).isEqualTo("Mel");
    assertThat(result.get(0).getTotalAmount()).isEqualTo(2f);
    assertThat(result.get(0).getTotalCalories()).isEqualTo(720f);
  }

  @Test
  void shouldUnshareAllFromGroup() {
    Long householdId = 10L;
    Long groupId = 5L;
    Long groupHouseholdId = 20L;
    final Long foodId = 99L;
    final Long typeId = 7L;

    GroupHousehold groupHousehold = new GroupHousehold();
    groupHousehold.setId(groupHouseholdId);
    groupHousehold.setGroupId(groupId);
    groupHousehold.setHouseholdId(householdId);

    Food food = new Food();
    food.setId(foodId);
    food.setTypeId(typeId);
    food.setExpirationDate(LocalDate.of(2025, 8, 10));
    food.setHouseholdId(householdId);

    final SharedFood shared = new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), 4f);

    Food existing = new Food();
    existing.setId(100L);
    existing.setTypeId(typeId);
    existing.setExpirationDate(food.getExpirationDate());
    existing.setAmount(6f);
    existing.setHouseholdId(householdId);

    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(any())).thenReturn(Optional.of(household));

    when(groupRepo.findByHouseholdIdAndGroupId(householdId, groupId)).thenReturn(groupHousehold);
    when(sharedRepo.findByGroupHouseholdId(groupHouseholdId)).thenReturn(List.of(shared));
    when(foodRepo.findById(foodId)).thenReturn(Optional.of(food));
    when(foodRepo.findByTypeIdAndExpirationDateAndHouseholdId(typeId,
        food.getExpirationDate(), householdId))
        .thenReturn(Optional.of(existing));

    service.unshareAllFromGroup(householdId, groupId);

    verify(foodRepo).update(existing);
    assertThat(existing.getAmount()).isEqualTo(10f);
    verify(sharedRepo).deleteById(new SharedFoodKey(foodId, groupHouseholdId));
  }

  @Test
  void shouldNotUnshareIfGroupHouseholdNotFound() {
    Long householdId = 15L;
    Long groupId = 3L;

    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(any())).thenReturn(Optional.of(household));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, groupId)).thenReturn(null);

    service.unshareAllFromGroup(householdId, groupId);

    verify(sharedRepo, never()).findByGroupHouseholdId(any());
    verify(foodRepo, never()).update(any());
    verify(sharedRepo, never()).deleteById(any());
  }

  @Test
  void shouldSkipUnsharingIfFoodNotFound() {
    Long householdId = 20L;
    Long groupId = 4L;

    GroupHousehold groupHousehold = new GroupHousehold();
    groupHousehold.setId(100L);
    groupHousehold.setHouseholdId(householdId);
    groupHousehold.setGroupId(groupId);

    Household household = new Household();
    household.setId(householdId);
    when(householdRepo.findByUserId(any())).thenReturn(Optional.of(household));

    when(groupRepo.findByHouseholdIdAndGroupId(householdId, groupId)).thenReturn(groupHousehold);
    SharedFood shared = new SharedFood(new SharedFoodKey(99L, 100L), 3f);
    when(sharedRepo.findByGroupHouseholdId(100L)).thenReturn(List.of(shared));
    when(foodRepo.findById(99L)).thenReturn(Optional.empty());

    service.unshareAllFromGroup(householdId, groupId);

    verify(sharedRepo).findByGroupHouseholdId(100L);
    verify(foodRepo).findById(99L);
    verify(foodRepo, never()).update(any());
    verify(sharedRepo, never()).deleteById(any());
  }

  @Test
  void shouldSkipUnsharingIfAmountIsZero() {
    Long householdId = 50L;
    Long groupId = 6L;

    GroupHousehold groupHousehold = new GroupHousehold();
    groupHousehold.setId(120L);
    groupHousehold.setHouseholdId(householdId);
    groupHousehold.setGroupId(groupId);

    final SharedFood shared = new SharedFood(new SharedFoodKey(77L, 120L), 0f);

    Food food = new Food();
    food.setId(77L);
    food.setTypeId(3L);
    food.setHouseholdId(householdId);
    food.setExpirationDate(LocalDate.now());
    food.setAmount(10f);

    Household household = new Household();
    household.setId(householdId);

    when(householdRepo.findByUserId(any())).thenReturn(Optional.of(household));
    when(groupRepo.findByHouseholdIdAndGroupId(householdId, groupId)).thenReturn(groupHousehold);
    when(sharedRepo.findByGroupHouseholdId(120L)).thenReturn(List.of(shared));
    when(foodRepo.findById(77L)).thenReturn(Optional.of(food));
    when(foodRepo.findByTypeIdAndExpirationDateAndHouseholdId(
        3L, food.getExpirationDate(), householdId)).thenReturn(Optional.of(food));

    service.unshareAllFromGroup(householdId, groupId);

    verify(foodRepo, never()).update(any());
    verify(sharedRepo, never()).deleteById(any());
  }

  @Test
  void create_throwsIfFoodNotFound() {
    when(jwtService.extractUserId("t")).thenReturn(1L);
    when(householdRepo.findByUserId(1L))
        .thenReturn(Optional.of(new Household() {
          {
            setId(10L);
          }
        }));
    when(foodRepo.findById(5L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.create(new SharedFoodRequest(5L, 1.0f, 1L), "Bearer t"));
  }

  @Test
  void create_throwsIfFoodNotInYourHousehold() {
    Food f = new Food();
    f.setId(5L);
    f.setHouseholdId(999L);
    when(jwtService.extractUserId("t")).thenReturn(1L);
    when(householdRepo.findByUserId(1L))
        .thenReturn(Optional.of(new Household() {
          {
            setId(10L);
          }
        }));
    when(foodRepo.findById(5L)).thenReturn(Optional.of(f));

    assertThrows(IllegalStateException.class,
        () -> service.create(new SharedFoodRequest(5L, 1.0f, 1L), "Bearer t"));
  }

  @Test
  void create_throwsIfNotInGroup() {
    Food f = new Food();
    f.setId(5L);
    f.setHouseholdId(10L);
    when(jwtService.extractUserId("t")).thenReturn(1L);
    when(householdRepo.findByUserId(1L))
        .thenReturn(Optional.of(new Household() {
          {
            setId(10L);
          }
        }));
    when(foodRepo.findById(5L)).thenReturn(Optional.of(f));
    when(groupRepo.findByHouseholdIdAndGroupId(10L, 1L)).thenReturn(null);

    assertThrows(IllegalStateException.class,
        () -> service.create(new SharedFoodRequest(5L, 1.0f, 1L), "Bearer t"));
  }

  @Test
  void update_returnsFalseIfNotInGroup() {
    when(jwtService.extractUserId("x")).thenReturn(2L);
    when(householdRepo.findByUserId(2L))
        .thenReturn(Optional.of(new Household() {
          {
            setId(5L);
          }
        }));
    final SharedFoodRequest req = new SharedFoodRequest(1L, 1f, 42L);
    when(groupRepo.findByHouseholdIdAndGroupId(5L, 42L)).thenReturn(null);

    assertFalse(service.update(req, "Bearer x"));
  }

  @Test
  void delete_returnsFalseIfNotInGroup() {
    when(jwtService.extractUserId("y")).thenReturn(3L);
    when(householdRepo.findByUserId(3L))
        .thenReturn(Optional.of(new Household() {
          {
            setId(7L);
          }
        }));
    when(groupRepo.findByHouseholdIdAndGroupId(7L, 99L)).thenReturn(null);

    assertFalse(service.delete(8L, 99L, "Bearer y"));
  }

  @Test
  void moveFoodToSharedGroup_updatesWhenAlreadyShared() {
    when(jwtService.extractUserId("tok")).thenReturn(1L);
    Household household = new Household();
    household.setId(2L);
    when(householdRepo.findByUserId(1L)).thenReturn(Optional.of(household));
    Food food = new Food();
    food.setId(5L);
    food.setHouseholdId(2L);
    food.setAmount(20f);
    food.setExpirationDate(LocalDate.now());
    when(foodRepo.findById(5L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(2L, 3L))
        .thenReturn(new GroupHousehold(4L, 2L, 3L));
    SharedFoodKey key = new SharedFoodKey(5L, 4L);
    when(sharedRepo.findById(key)).thenReturn(Optional.of(new SharedFood(key, 2f)));

    assertTrue(service.moveFoodToSharedGroup(new SharedFoodRequest(5L, 10f, 3L), "Bearer tok"));
    verify(sharedRepo).update(argThat(sf -> sf.getAmount() == 12f));
    verify(foodRepo).update(argThat(f -> f.getAmount() == 10f));
  }

  @Test
  void moveFoodFromSharedGroup_deletesWhenExactAmount() {
    when(jwtService.extractUserId(anyString())).thenReturn(7L);
    Household household = new Household();
    household.setId(8L);
    when(householdRepo.findByUserId(7L)).thenReturn(Optional.of(household));
    Food food = new Food();
    food.setId(11L);
    food.setHouseholdId(8L);
    food.setTypeId(42L);
    food.setExpirationDate(LocalDate.now());
    when(foodRepo.findById(11L)).thenReturn(Optional.of(food));
    when(groupRepo.findByHouseholdIdAndGroupId(8L, 9L))
        .thenReturn(new GroupHousehold(10L, 8L, 9L));
    SharedFoodKey key = new SharedFoodKey(11L, 10L);
    when(sharedRepo.findById(key)).thenReturn(Optional.of(new SharedFood(key, 5f)));

    assertTrue(service.moveFoodFromSharedGroup(new SharedFoodRequest(11L, 5f, 9L), "Bearer x"));
    verify(sharedRepo).deleteById(key);
    verify(sharedRepo, never()).update(any());
  }

  @Test
  void getSharedFoodSummaryByGroup_skipsWhenTypeMissing() {
    SharedFoodKey key = new SharedFoodKey(22L, 21L);
    when(sharedRepo.findByGroupHouseholdId(21L))
        .thenReturn(List.of(new SharedFood(key, 3f)));
    Food food = new Food();
    food.setId(22L);
    food.setTypeId(99L);
    food.setExpirationDate(LocalDate.now());
    food.setHouseholdId(42L);
    when(foodRepo.findById(22L)).thenReturn(Optional.of(food));
    when(typeRepo.findById(99L)).thenReturn(Optional.empty());

    assertTrue(service.getSharedFoodSummaryByGroup(21L).isEmpty());
  }

}