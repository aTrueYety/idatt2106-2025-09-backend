package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ExtraResidentServiceTest {

  @Mock
  private ExtraResidentRepository repository;

  @InjectMocks
  private ExtraResidentService service;

  public ExtraResidentServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId(1);
    request.setTypeId(2);

    service.create(request);

    verify(repository).save(any(ExtraResident.class));
  }

  @Test
  void shouldReturnAllResidents() {
    ExtraResident resident = new ExtraResident(1, 1, 2);
    when(repository.findAll()).thenReturn(List.of(resident));

    List<ExtraResidentResponse> result = service.getAll();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getHouseholdId()).isEqualTo(1);
    assertThat(result.get(0).getTypeId()).isEqualTo(2);
  }

  @Test
  void shouldGetResidentById() {
    ExtraResident resident = new ExtraResident(1, 3, 4);
    when(repository.findById(1)).thenReturn(Optional.of(resident));

    Optional<ExtraResidentResponse> result = service.getById(1);

    assertThat(result).isPresent();
    assertThat(result.get().getTypeId()).isEqualTo(4);
  }

  @Test
  void shouldUpdateIfExists() {
    when(repository.findById(1)).thenReturn(Optional.of(new ExtraResident()));

    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(5);
    update.setTypeId(6);

    boolean success = service.update(1, update);

    assertThat(success).isTrue();
    verify(repository).update(any(ExtraResident.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(999)).thenReturn(Optional.empty());

    ExtraResidentUpdate update = new ExtraResidentUpdate();
    boolean success = service.update(999, update);

    assertThat(success).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1)).thenReturn(Optional.of(new ExtraResident()));

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
