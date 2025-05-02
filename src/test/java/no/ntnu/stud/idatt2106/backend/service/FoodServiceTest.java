package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FoodServiceTest {

  @Mock
  private FoodRepository repository;

  @InjectMocks
  private FoodService service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateFood() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1);
    request.setHouseholdId(1);
    request.setExpirationDate(LocalDate.now());
    request.setAmount(2);

    service.create(request);

    verify(repository).save(any(Food.class));
  }

  @Test
  void shouldThrowIfCreateAmountNotPositive() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1);
    request.setAmount(-1);

    assertThrows(IllegalArgumentException.class, () -> {
      service.create(request);
    });
  }

  @Test
  void shouldGetAllFoods() {
    Food food = new Food();
    food.setId(1);
    food.setHouseholdId(1);
    food.setAmount(5);

    when(repository.findAll()).thenReturn(List.of(food));

    List<FoodResponse> all = service.getAll();

    assertThat(all).hasSize(1);
    assertThat(all.get(0).getId()).isEqualTo(1);
  }

  @Test
  void shouldGetByIdIfExists() {
    Food food = new Food();
    food.setId(1);
    food.setAmount(3);

    when(repository.findById(1)).thenReturn(Optional.of(food));

    Optional<FoodResponse> response = service.getById(1);

    assertThat(response).isPresent();
    assertThat(response.get().getAmount()).isEqualTo(3);
  }

  @Test
  void shouldReturnEmptyIfNotFound() {
    when(repository.findById(99)).thenReturn(Optional.empty());

    Optional<FoodResponse> response = service.getById(99);

    assertThat(response).isEmpty();
  }

  @Test
  void shouldUpdateIfExists() {
    when(repository.findById(1)).thenReturn(Optional.of(new Food()));

    FoodUpdate update = new FoodUpdate();
    update.setAmount(10);
    update.setExpirationDate(LocalDate.now());
    update.setHouseholdId(1);
    update.setTypeId(1);

    boolean result = service.update(1, update);

    assertThat(result).isTrue();
    verify(repository).update(any(Food.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(1)).thenReturn(Optional.empty());
    FoodUpdate update = new FoodUpdate();
    update.setAmount(1);

    boolean result = service.update(1, update);

    assertThat(result).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldThrowIfUpdateAmountNotPositive() {
    when(repository.findById(1)).thenReturn(Optional.of(new Food()));

    FoodUpdate update = new FoodUpdate();
    update.setAmount(0);

    assertThrows(IllegalArgumentException.class, () -> {
      service.update(1, update);
    });
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1)).thenReturn(Optional.of(new Food()));

    boolean result = service.delete(1);

    assertThat(result).isTrue();
    verify(repository).deleteById(1);
  }

  @Test
  void shouldNotDeleteIfMissing() {
    when(repository.findById(1)).thenReturn(Optional.empty());

    boolean result = service.delete(1);

    assertThat(result).isFalse();
    verify(repository, never()).deleteById(anyInt());
  }
}
