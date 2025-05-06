package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.SharedFoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SharedFoodServiceTest {

  @InjectMocks
  private SharedFoodService service;

  @Mock
  private SharedFoodRepository repository;
  @Mock
  private FoodRepository foodRepository;
  @Mock
  private FoodTypeRepository foodTypeRepository;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreate_shouldSaveSharedFood() {
    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setGroupHouseholdId(2L);
    request.setAmount(5.0f);

    ArgumentCaptor<SharedFood> captor = ArgumentCaptor.forClass(SharedFood.class);

    service.create(request);

    verify(repository).save(captor.capture());

    SharedFood saved = captor.getValue();
    assertEquals(1, saved.getId().getFoodId());
    assertEquals(2, saved.getId().getGroupHouseholdId());
    assertEquals(5.0f, saved.getAmount());
  }

  @Test
  void testGetAll_shouldReturnMappedList() {
    SharedFood shared1 = new SharedFood(new SharedFoodKey(1L, 2L), 5.0f);
    SharedFood shared2 = new SharedFood(new SharedFoodKey(2L, 3L), 10.0f);
    when(repository.findAll()).thenReturn(List.of(shared1, shared2));

    List<SharedFoodResponse> result = service.getAll();

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void testUpdate_shouldReturnTrueIfUpdated() {
    SharedFoodRequest request = new SharedFoodRequest();
    when(repository.update(any())).thenReturn(true);

    boolean result = service.update(request);

    assertTrue(result);
  }

  @Test
  void testDelete_shouldReturnTrueIfDeleted() {
    when(repository.deleteById(any())).thenReturn(true);

    boolean result = service.delete(1L, 2L);

    assertTrue(result);
  }


  @Test
  void testMoveFoodToSharedGroup_shouldSucceed() {
    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setGroupHouseholdId(2L);
    request.setAmount(5.0f);
  
    Food food = new Food();
    food.setId(1L);
    food.setAmount(10.0f);
    food.setTypeId(1L);
    food.setExpirationDate(LocalDate.now());
    food.setHouseholdId(3L);
  
    SharedFoodKey expectedKey = new SharedFoodKey(1L, 2L);
  
    when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
    when(repository.findById(expectedKey)).thenReturn(Optional.empty());
  
    boolean result = service.moveFoodToSharedGroup(request);
  
    assertTrue(result);
    verify(foodRepository).findById(1L);
    verify(repository).findById(expectedKey);
    verify(repository).save(any(SharedFood.class));
  }
  

  @Test
  void testGetSharedFoodSummaryByGroup_shouldAggregateCorrectly() {
    SharedFood sf = new SharedFood(new SharedFoodKey(1L, 1L), 5.0f);
    when(repository.findByGroupHouseholdId(1L)).thenReturn(List.of(sf));

    Food food = new Food();
    food.setId(1L);
    food.setTypeId(10L);
    food.setAmount(5.0f);
    food.setExpirationDate(LocalDate.now());
    food.setHouseholdId(2L);
    when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

    FoodType type = new FoodType();
    type.setId(10L);
    type.setName("Rice");
    type.setUnit("kg");
    type.setCaloriesPerUnit(3.5f);
    when(foodTypeRepository.findById(10L)).thenReturn(Optional.of(type));

    List<FoodDetailedResponse> result = service.getSharedFoodSummaryByGroup(1L);

    assertEquals(1, result.size());
    assertEquals("Rice", result.get(0).getTypeName());
  }

  @Test
  void testMoveFoodToSharedGroup_shouldReturnFalseWhenFoodNotFound() {
    SharedFoodRequest request = new SharedFoodRequest();
    request.setFoodId(1L);
    request.setAmount(5.0f);

    when(foodRepository.findById(1L)).thenReturn(Optional.empty());

    boolean result = service.moveFoodToSharedGroup(request);

    assertFalse(result);
  }

  @Test
  void testGetSharedFoodSummaryByGroup_shouldSkipEntryWhenFoodTypeNotFound() {
    Long groupHouseholdId = 1L;
    Long foodId = 10L;
    Long missingTypeId = 99L;

    // SharedFood links foodId to groupHouseholdId
    SharedFood shared = new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), 2.0f);
    when(repository.findByGroupHouseholdId(groupHouseholdId))
        .thenReturn(List.of(shared));

    Food food = new Food();
    food.setId(foodId);
    food.setTypeId(missingTypeId);
    food.setExpirationDate(LocalDate.now());
    food.setAmount(2.0f);
    food.setHouseholdId(1L);

    when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

    when(foodTypeRepository.findById(missingTypeId)).thenReturn(Optional.empty());

    List<FoodDetailedResponse> result = service.getSharedFoodSummaryByGroup(groupHouseholdId);

    assertNotNull(result);
    assertTrue(result.isEmpty(), "Should skip entries with missing food type");
  }

}
