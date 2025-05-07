package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodSummaryResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.FoodMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

  @InjectMocks
  private FoodService service;

  @Mock
  private FoodRepository repository;
  
  @Mock
  private FoodTypeRepository foodTypeRepository;

  @Mock
  private FoodTypeService foodTypeService;

  @Test
  void shouldCreateFood() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1L);
    request.setHouseholdId(1L);
    request.setExpirationDate(LocalDate.now());
    request.setAmount(2);

    service.create(request);

    verify(repository).save(any(Food.class));
  }

  @Test
  void shouldThrowIfCreateAmountNotPositive() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1L);
    request.setAmount(-1);

    assertThrows(IllegalArgumentException.class, () -> {
      service.create(request);
    });
  }

  @Test
  void shouldGetAllFoods() {
    Food food = new Food();
    food.setId(1L);
    food.setHouseholdId(1L);
    food.setAmount(5);

    when(repository.findAll()).thenReturn(List.of(food));

    List<FoodResponse> all = service.getAll();

    assertThat(all).hasSize(1);
    assertThat(all.get(0).getId()).isEqualTo(1);
  }

  @Test
  void shouldGetByIdIfExists() {
    Food food = new Food();
    food.setId(1L);
    food.setAmount(3);

    when(repository.findById(1L)).thenReturn(Optional.of(food));

    Optional<FoodResponse> response = service.getById(1L);

    assertThat(response).isPresent();
    assertThat(response.get().getAmount()).isEqualTo(3);
  }

  @Test
  void shouldReturnEmptyIfNotFound() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    Optional<FoodResponse> response = service.getById(99L);

    assertThat(response).isEmpty();
  }

  @Test
  void shouldUpdateIfExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(new Food()));

    FoodUpdate update = new FoodUpdate();
    update.setAmount(10);
    update.setExpirationDate(LocalDate.now());
    update.setHouseholdId(1L);
    update.setTypeId(1L);

    boolean result = service.update(1L, update);

    assertThat(result).isTrue();
    verify(repository).update(any(Food.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    FoodUpdate update = new FoodUpdate();
    update.setAmount(1);

    boolean result = service.update(1L, update);

    assertThat(result).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldThrowIfUpdateAmountNotPositive() {
    FoodUpdate update = new FoodUpdate();
    update.setAmount(0);

    assertThrows(IllegalArgumentException.class, () -> {
      service.update(1L, update);
    });
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(new Food()));

    boolean result = service.delete(1L);

    assertThat(result).isTrue();
    verify(repository).deleteById(1L);
  }

  @Test
  void shouldNotDeleteIfMissing() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    boolean result = service.delete(1L);

    assertThat(result).isFalse();
    verify(repository, never()).deleteById(any());
  }

  @Test
  void shouldGetFoodDetailedByHouseholdId() {
    Food food1 = new Food();
    food1.setId(20L);
    food1.setTypeId(1L);
    food1.setAmount(2.2);
    food1.setExpirationDate(LocalDate.now().plusDays(4));

    Food food2 = new Food();
    food2.setId(30L);
    food2.setTypeId(2L);
    food2.setAmount(9.4);
    food2.setExpirationDate(LocalDate.now().plusDays(10));

    Food food3 = new Food();
    food3.setId(11L);
    food3.setTypeId(2L);
    food3.setAmount(4.4);
    food3.setExpirationDate(LocalDate.now().plusDays(2));

    List<Food> foods = List.of(food1, food2, food3);

    Long householdId = 1L;
    when(repository.findByHouseholdId(householdId)).thenReturn(foods);

    FoodType type1 = new FoodType();
    type1.setId(1L);
    type1.setName("Rice");
    type1.setUnit("kg");
    type1.setCaloriesPerUnit(120f);

    FoodType type2 = new FoodType();
    type2.setId(2L);
    type2.setName("Beans");
    type2.setUnit("kg");
    type2.setCaloriesPerUnit(160f);

    when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(type1));
    when(foodTypeRepository.findById(2L)).thenReturn(Optional.of(type2));

    List<FoodDetailedResponse> result = service.getFoodDetailedByHousehold(householdId);

    assertEquals(2, result.size());

    FoodDetailedResponse riceSummary = result.stream()
        .filter(r -> r.getTypeId().equals(1L))
        .findFirst()
        .orElseThrow();
    
    assertEquals("Rice", riceSummary.getTypeName());
    assertEquals("kg", riceSummary.getUnit());
    assertEquals(2.2, riceSummary.getTotalAmount(), 0.000001);
    assertEquals(2.2 * 120, riceSummary.getTotalCalories());
    assertEquals(1, riceSummary.getBatches().size());

    FoodDetailedResponse beanSummary = result.stream()
        .filter(r -> r.getTypeId().equals(2L))
        .findFirst()
        .orElseThrow();
    
    assertEquals("Beans", beanSummary.getTypeName());
    assertEquals("kg", beanSummary.getUnit());
    assertEquals(9.4 + 4.4, beanSummary.getTotalAmount(), 0.000001);
    assertEquals((9.4 + 4.4) * 160, beanSummary.getTotalCalories());
    assertEquals(2, beanSummary.getBatches().size());
  }

  @Test
  void shouldThrowIfFoodTypeNotFound() {
    Food food1 = new Food();
    food1.setId(20L);
    food1.setTypeId(1L);
    food1.setAmount(2.2);
    food1.setExpirationDate(LocalDate.now().plusDays(4));

    Food food2 = new Food();
    food2.setId(30L);
    food2.setTypeId(1L);
    food2.setAmount(9.4);
    food2.setExpirationDate(LocalDate.now().plusDays(10));

    Food food3 = new Food();
    food3.setId(11L);
    food3.setTypeId(1L);
    food3.setAmount(4.4);
    food3.setExpirationDate(LocalDate.now().plusDays(2));

    List<Food> foods = List.of(food1, food2, food3);

    when(repository.findByHouseholdId(1L)).thenReturn(foods);
    when(foodTypeRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(NoSuchElementException.class, () -> {
      service.getFoodDetailedByHousehold(1L);
    });

    assertTrue(exception.getMessage().contains("FoodType with ID = 1 not found"));
  }

  @Test
  void shouldReturnFoodSummaryByHousehold() {
    Food food1 = new Food();
    food1.setTypeId(1L);
    food1.setAmount(10);

    Food food2 = new Food();
    food2.setTypeId(1L);
    food2.setAmount(5);

    Food food3 = new Food();
    food3.setTypeId(2L);
    food3.setAmount(1);

    List<Food> foods = List.of(food1, food2, food3);

    Long householdId = 1L;
    when(repository.findByHouseholdId(householdId)).thenReturn(foods);

    List<FoodSummaryResponse> result = service.getFoodSummaryByHousehold(householdId);

    assertEquals(2, result.size());
    
    FoodSummaryResponse response1 = result.stream()
        .filter(r -> r.getTypeId().equals(1L))
        .findFirst()
        .orElseThrow();
    
    assertEquals(15, response1.getTotalAmount());

    FoodSummaryResponse response2 = result.stream()
        .filter(r -> r.getTypeId().equals(2L))
        .findFirst()
        .orElseThrow();
    
    assertEquals(1, response2.getTotalAmount());
  }

  @Test
  void shouldGetCaloriesByHouseholdId() {
    FoodType foodType = new FoodType();
    foodType.setId(1L);
    foodType.setCaloriesPerUnit(10f);

    Food food1 = new Food();
    food1.setTypeId(1L);
    food1.setAmount(10);

    Food food2 = new Food();
    food2.setTypeId(1L);
    food2.setAmount(15);

    List<Food> foods = List.of(food1, food2);

    Long householdId = 1L;
    when(repository.findByHouseholdId(householdId)).thenReturn(foods);
    when(foodTypeService.getCaloriesById(foodType.getId()))
        .thenReturn(foodType.getCaloriesPerUnit());

    double result = service.getCaloriesByHouseholdId(householdId);

    assertEquals(250, result);
  }

  @Test
  void shouldGetFoodByHouseholdId() {
    Food food1 = new Food();
    Food food2 = new Food();
    List<Food> foods = List.of(food1, food2);

    FoodResponse response1 = new FoodResponse();
    FoodResponse response2 = new FoodResponse();
    List<FoodResponse> responses = List.of(response1, response2);

    Long householdId = 1L;
    when(repository.findByHouseholdId(householdId)).thenReturn(foods);

    try (MockedStatic<FoodMapper> mapper = Mockito.mockStatic(FoodMapper.class)) {
      mapper.when(() -> FoodMapper.toResponse(food1)).thenReturn(response1);
      mapper.when(() -> FoodMapper.toResponse(food2)).thenReturn(response2);

      List<FoodResponse> result = service.getByHouseholdId(householdId);

      assertEquals(responses, result);
      assertEquals(2, result.size());
    }
  }
}
