package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for FoodTypeService.
 */
@ExtendWith(MockitoExtension.class)
class FoodTypeServiceTest {

  @Mock
  private FoodTypeRepository repository;

  @InjectMocks
  private FoodTypeService service;

  @Test
  void shouldCreateFoodType() {
    FoodTypeRequest request = new FoodTypeRequest();
    request.setName("Bread");
    request.setUnit("stk");
    request.setCaloriesPerUnit(220f);
    request.setPicture(null);

    service.create(request);

    verify(repository).save(any(FoodType.class));
  }

  @Test
  void shouldGetAllFoodTypes() {
    FoodType food = new FoodType();
    food.setId(1L);
    food.setName("Apple");
    food.setCaloriesPerUnit(12.0f);

    when(repository.findAll()).thenReturn(List.of(food));

    List<FoodTypeResponse> result = service.getAll();
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Apple");
  }

  @Test
  void shouldUpdateFoodTypeIfExists() {
    FoodTypeRequest request = new FoodTypeRequest();
    request.setName("Updated");
    request.setUnit("unit");
    request.setCaloriesPerUnit(10f);
    request.setPicture(null);

    when(repository.findById(1L)).thenReturn(Optional.of(new FoodType()));

    boolean updated = service.update(1L, request);
    assertThat(updated).isTrue();
    verify(repository).update(any(FoodType.class));
  }

  @Test
  void shouldNotUpdateIfFoodTypeNotExists() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    boolean updated = service.update(1L, new FoodTypeRequest());
    assertThat(updated).isFalse();
    verify(repository, never()).update(any());
  }
}
