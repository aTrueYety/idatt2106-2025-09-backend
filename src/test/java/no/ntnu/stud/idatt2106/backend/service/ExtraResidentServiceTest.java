package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for ExtraResidentService.
 */
@ExtendWith(MockitoExtension.class)
public class ExtraResidentServiceTest {

  @Mock
  private ExtraResidentRepository repository;

  @Mock
  private JwtService jwtService;

  @Mock
  private UserService userService;

  @InjectMocks
  private ExtraResidentService service;

  @Test
  void shouldCreateResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId((int) 1L);
    request.setTypeId((int) 2L);
    request.setName("Test");

    Long userId = 1L;
    Long householdId = 1L;
    User user = new User();
    user.setId(userId);
    user.setHouseholdId(householdId);

    String token = "Bearer token";
    when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
    when(userService.getUserById(userId)).thenReturn(user);

    service.create(request, token);

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

    Long householdId = 5L;
    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(5L);
    update.setTypeId(householdId);
    update.setName("Updated");

    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    user.setHouseholdId(householdId);

    String token = "Bearer token";
    when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
    when(userService.getUserById(userId)).thenReturn(user);
    boolean success = service.update(1L, update, token);

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

    Long userId = 1L;
    Long householdId = 1L;
    User user = new User();
    user.setId(userId);
    user.setHouseholdId(householdId);

    String token = "Bearer token";
    when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
    when(userService.getUserById(userId)).thenReturn(user);

    boolean success = service.update(999L, update, token);

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
