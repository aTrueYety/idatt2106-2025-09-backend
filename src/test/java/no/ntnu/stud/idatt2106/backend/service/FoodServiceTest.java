package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for FoodService with JWT-auth handling.
 */
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

  @Mock
  private JwtService jwtService;

  @Mock
  private HouseholdRepository householdRepository;

  private final String token = "Bearer dummy.jwt.token";

  @Test
  void shouldCreateFoodIfValidTokenAndOwnership() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1L);
    request.setHouseholdId(100L);
    request.setExpirationDate(LocalDate.now());
    request.setAmount(2);

    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(5L);
    Household household = new Household();
    household.setId(100L);
    when(householdRepository.findByUserId(5L)).thenReturn(Optional.of(household));

    service.create(request, token);

    verify(repository).save(any(Food.class));
  }

  @Test
  void shouldRejectCreateIfWrongHousehold() {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1L);
    request.setHouseholdId(200L); // Feil ID i forhold til brukerens husholdning
    request.setExpirationDate(LocalDate.now());
    request.setAmount(2);

    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(5L);
    Household household = new Household();
    household.setId(100L);
    when(householdRepository.findByUserId(5L)).thenReturn(Optional.of(household));

    assertThrows(IllegalStateException.class, () -> service.create(request, token));
  }

  @Test
  void shouldUpdateFoodIfOwnershipCorrect() {
    FoodUpdate update = new FoodUpdate();
    update.setAmount(10);
    update.setExpirationDate(LocalDate.now());
    update.setHouseholdId(100L);
    update.setTypeId(1L);

    Food existing = new Food();
    existing.setId(5L);
    existing.setHouseholdId(100L);

    when(repository.findById(5L)).thenReturn(Optional.of(existing));
    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(5L);
    Household household = new Household();
    household.setId(100L);
    when(householdRepository.findByUserId(5L)).thenReturn(Optional.of(household));

    boolean result = service.update(5L, update, token);
    assertThat(result).isTrue();
    verify(repository).update(any(Food.class));
  }

  @Test
  void shouldNotUpdateFoodIfWrongOwner() {
    FoodUpdate update = new FoodUpdate();
    update.setAmount(10);
    update.setHouseholdId(100L);
    update.setTypeId(1L);

    Food existing = new Food();
    existing.setId(5L);
    existing.setHouseholdId(200L); // ikke brukerens husholdning

    when(repository.findById(5L)).thenReturn(Optional.of(existing));
    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(5L);
    Household household = new Household();
    household.setId(100L);
    when(householdRepository.findByUserId(5L)).thenReturn(Optional.of(household));

    boolean result = service.update(5L, update, token);
    assertThat(result).isFalse();
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteFoodIfOwner() {
    Food existing = new Food();
    existing.setId(7L);
    existing.setHouseholdId(300L);

    when(repository.findById(7L)).thenReturn(Optional.of(existing));
    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(99L);
    Household household = new Household();
    household.setId(300L);
    when(householdRepository.findByUserId(99L)).thenReturn(Optional.of(household));

    boolean result = service.delete(7L, token);
    assertThat(result).isTrue();
    verify(repository).deleteById(7L);
  }

  @Test
  void shouldNotDeleteIfWrongUser() {
    Food existing = new Food();
    existing.setId(7L);
    existing.setHouseholdId(999L);

    when(repository.findById(7L)).thenReturn(Optional.of(existing));
    when(jwtService.extractUserId("dummy.jwt.token")).thenReturn(5L);
    Household household = new Household();
    household.setId(100L);
    when(householdRepository.findByUserId(5L)).thenReturn(Optional.of(household));

    boolean result = service.delete(7L, token);
    assertThat(result).isFalse();
    verify(repository, never()).deleteById(any());
  }
}
