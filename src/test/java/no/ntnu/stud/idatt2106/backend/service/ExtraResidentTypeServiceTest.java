package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ExtraResidentTypeServiceTest {

  @Mock
  private ExtraResidentTypeRepository repository;

  @InjectMocks
  private ExtraResidentTypeService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateType() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Adult");
    request.setConsumptionWater(1.5f);
    request.setConsumptionFood(1.2f);

    service.create(request);

    verify(repository).save(any(ExtraResidentType.class));
  }

  @Test
  void shouldReturnAllTypes() {
    ExtraResidentType type = new ExtraResidentType();
    type.setId(1L);
    type.setName("Adult");
    type.setConsumptionWater(1.5f);
    type.setConsumptionFood(1.2f);

    when(repository.findAll()).thenReturn(List.of(type));

    List<ExtraResidentTypeResponse> result = service.getAll();
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Adult");
  }

  @Test
  void shouldFindById() {
    ExtraResidentType type = new ExtraResidentType();
    type.setId(1L);
    type.setName("Child");
    type.setConsumptionWater(2.0f);
    type.setConsumptionFood(1.8f);

    when(repository.findById(1L)).thenReturn(Optional.of(type));

    Optional<ExtraResidentTypeResponse> result = service.getById(1L);
    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo("Child");
  }

  @Test
  void shouldUpdateExistingType() {
    when(repository.findById(1L)).thenReturn(Optional.of(new ExtraResidentType()));

    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Updated");
    request.setConsumptionWater(1.0f);
    request.setConsumptionFood(1.0f);

    boolean result = service.update(1L, request);

    assertThat(result).isTrue();
    verify(repository).update(any(ExtraResidentType.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(999L)).thenReturn(Optional.empty());

    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("None");
    request.setConsumptionWater(0.5f);
    request.setConsumptionFood(0.5f);

    boolean result = service.update(999L, request);

    assertThat(result).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(new ExtraResidentType()));

    boolean result = service.delete(1L);

    assertThat(result).isTrue();
    verify(repository).deleteById(1L);
  }

  @Test
  void shouldNotDeleteIfNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    boolean result = service.delete(1L);

    assertThat(result).isFalse();
    verify(repository, never()).deleteById(anyLong());
  }
}
