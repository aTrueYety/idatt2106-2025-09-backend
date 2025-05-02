package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepository;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId((int) 1L);
    request.setTypeId((int) 2L);
    request.setName("Test");

    service.create(request);

    verify(repository, times(1)).save(any(ExtraResident.class));
  }

  @Test
  void shouldReturnAllResidents() {
    ExtraResident resident = new ExtraResident(1L, 1L, 2L, "John Doe");
    when(repository.findAll()).thenReturn(List.of(resident));

    List<ExtraResidentResponse> result = service.getAll();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getHouseholdId()).isEqualTo(1L);
    assertThat(result.get(0).getTypeId()).isEqualTo(2L);
    assertThat(result.get(0).getName()).isEqualTo("John Doe");
  }

  @Test
  void shouldGetResidentById() {
    ExtraResident resident = new ExtraResident(1L, 3L, 4L, "Jane Doe");
    when(repository.findById(1L)).thenReturn(Optional.of(resident));

    Optional<ExtraResidentResponse> result = service.getById(1L);

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(1L);
    assertThat(result.get().getTypeId()).isEqualTo(4L);
    assertThat(result.get().getName()).isEqualTo("Jane Doe");
  }

  @Test
  void shouldUpdateIfExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(new ExtraResident()));

    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(5L);
    update.setTypeId(6L);
    update.setName("Updated");

    boolean success = service.update(1L, update);

    assertThat(success).isTrue();
    verify(repository).update(any(ExtraResident.class));
  }

  @Test
  void shouldNotUpdateIfNotFound() {
    when(repository.findById(999L)).thenReturn(Optional.empty());

    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(1L);
    update.setTypeId(1L);
    update.setName("Missing");

    boolean success = service.update(999L, update);

    assertThat(success).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteIfExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(new ExtraResident()));

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
