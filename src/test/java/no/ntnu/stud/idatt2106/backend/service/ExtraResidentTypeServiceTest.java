package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepository;
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

  public ExtraResidentTypeServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateType() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Adult");
    request.setConsumptionWater(1.5f);
    request.setConsumptionFood(1.2f);

    service.create(request);

    verify(repository, times(1)).save(any(ExtraResidentType.class));
  }

  @Test
  void shouldReturnAllTypes() {
    ExtraResidentType type = new ExtraResidentType();
    type.setId(1);
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
    type.setId(1);
    type.setName("child");
    type.setConsumptionWater(2.0f);
    type.setConsumptionFood(1.8f);

    when(repository.findById(1)).thenReturn(Optional.of(type));

    Optional<ExtraResidentTypeResponse> result = service.getById(1);
    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo("child");
  }

  @Test
  void shouldUpdateExistingType() {
    when(repository.findById(1)).thenReturn(Optional.of(new ExtraResidentType()));

    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("child");
    request.setConsumptionWater(1.0f);
    request.setConsumptionFood(1.0f);

    boolean result = service.update(1, request);

    assertThat(result).isTrue();
    verify(repository).update(any(ExtraResidentType.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(999)).thenReturn(Optional.empty());

    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    boolean result = service.update(999, request);

    assertThat(result).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1)).thenReturn(Optional.of(new ExtraResidentType()));

    boolean result = service.delete(1);

    assertThat(result).isTrue();
    verify(repository).deleteById(1);
  }

  @Test
  void shouldNotDeleteIfNotFound() {
    when(repository.findById(1)).thenReturn(Optional.empty());

    boolean result = service.delete(1);

    assertThat(result).isFalse();
    verify(repository, never()).deleteById(anyInt());
  }
}
