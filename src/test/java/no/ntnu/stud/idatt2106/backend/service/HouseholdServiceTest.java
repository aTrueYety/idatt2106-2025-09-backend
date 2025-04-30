package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Contains tests for the HouseholdService class.
 */
public class HouseholdServiceTest {
  
  @Mock
  private HouseholdRepository householdRepository;

  @Mock
  private UserService userService;

  @Mock
  private HouseholdInviteService householdInviteService;

  @InjectMocks
  private HouseholdService householdService;

  private Household existingHousehold;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    User mockUser = new User();
    mockUser.setUsername("Testuser");
    when(userService.getUserByUsername("Testuser")).thenReturn(mockUser);
    when(householdRepository.save(any(Household.class))).thenAnswer(i -> {
      Household h = i.getArgument(0);
      h.setId(1L);
      return h;
    });
    when(householdRepository.findById(1L)).thenReturn(Optional.of(new Household()));

    existingHousehold = new Household();
    existingHousehold.setId(1L);
    existingHousehold.setAdress("Test address");
    existingHousehold.setLatitude(10.0);
    existingHousehold.setLongitude(20.0);
    existingHousehold.setWaterAmountLiters(50.0);
    existingHousehold.setLastWaterChangeDate(new Date());
  }

  @Test
  void getAllShouldReturnAllHouseholds() {
    Household household1 = new Household();
    household1.setId(1L);
    household1.setAdress("Test Street 1");

    Household household2 = new Household();
    household2.setId(2L);
    household2.setAdress("Test Street 2");

    List<Household> households = new ArrayList<>();
    households.add(household1);
    households.add(household2);

    when(householdRepository.findAll()).thenReturn(households);

    HouseholdResponse response1 = new HouseholdResponse();
    HouseholdResponse response2 = new HouseholdResponse();
    response1.setId(1L);
    response1.setAddress("Test Street 1");
    response2.setId(2L);
    response2.setAddress("Test Street 2");

    List<HouseholdResponse> result = householdService.getAll();

    assertEquals(result.size(), 2);
    assertEquals("Test Street 1", result.get(0).getAddress());
    assertEquals("Test Street 2", result.get(1).getAddress());
  }

  @Test
  void shouldRegisterHousehold() {
    HouseholdRequest request = new HouseholdRequest();
    request.setAdress("Test");
    request.setLatitude(32.3);
    request.setLongitude(34.23);
    request.setWaterAmountLiters(32.23);
    request.setLastWaterChangeDate(new Date());
    request.setUsername("Testuser");

    householdService.registerHousehold(request);
    verify(householdRepository).save(any(Household.class));
    verify(userService).getUserByUsername("Testuser");
    verify(userService).updateUserCredentials(any(User.class));
  }

  @Test
  void shouldAddUserToHousehold() {
    String username = "Testuser";
    Long householdId = 1L;
    User user = new User();
    user.setUsername("Testusername");
    
    when(userService.getUserByUsername(username)).thenReturn(user);
    when(householdRepository.findById(householdId)).thenReturn(Optional.of(new Household()));

    householdService.addUserToHousehold(username, householdId);

    assertEquals(householdId, user.getHouseholdId());
    verify(userService).updateUserCredentials(user);
  }

  @Test
  void shouldThrowIfUserDoesNotExist() {
    String username = "NonExistentUser";
    Long householdId = 1L;

    when(userService.getUserByUsername(username)).thenReturn(null);

    assertThrows(NoSuchElementException.class, () -> {
      householdService.addUserToHousehold(username, householdId);
    });
  }

  @Test
  void shouldThrowIfHouseholdDoesNotExist() {
    String username = "Testuser";
    Long householdId = 999L;

    User user = new User();
    user.setUsername(username);

    when(userService.getUserByUsername(username)).thenReturn(user);
    when(householdRepository.findById(householdId)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> {
      householdService.addUserToHousehold(username, householdId);
    });
  }

  @Test
  void getByIdShouldGetHouseholdByid() {
    Long id = 1L;
    Household household = new Household();
    household.setId(id);
    household.setAdress("Test street");

    when(householdRepository.findById(id)).thenReturn(Optional.of(household));

    HouseholdResponse response = householdService.getById(id);

    assertNotNull(response);
    assertEquals(id, response.getId());
    assertEquals("Test street", response.getAddress());
  }

  @Test
  void getByIdShouldThrowExceptionWhenHouseholdDoesNotExist() {
    Long id = 999L;
    when(householdRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> {
      householdService.getById(id);
    });
  }

  @Nested
  class GetByUserIdTests {

    @Test
    void shouldGetHouseholdResponseIfUserIsInHousehold() {
      User mockUser = new User();
      Household mockHousehold = new Household();
      mockHousehold.setId(100L);
      mockHousehold.setAdress("Test Address");
      mockUser.setId(1L);
      mockUser.setHouseholdId(100L);

      Long userId = 1L;
      when(userService.userExists(userId)).thenReturn(true);
      when(userService.getUserById(userId)).thenReturn(mockUser);
      when(householdRepository.findById(mockUser.getHouseholdId()))
          .thenReturn(Optional.of(mockHousehold));
      
      HouseholdResponse response = householdService.getByUserId(userId);

      assertNotNull(response);
      assertEquals("Test Address", response.getAddress());
    }

    @Test
    void shouldThrowExceptionIfUserDoesentExist() {
      Long userId = 1L;
      when(userService.userExists(userId)).thenReturn(false);

      NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
        householdService.getByUserId(userId);
      });

      assertEquals("User with ID = " + userId + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUserHasNoHousehold() {
      User mockUser = new User();
      mockUser.setId(1L);
      Long userId = 1L;
      when(userService.userExists(userId)).thenReturn(true);
      when(userService.getUserById(userId)).thenReturn(mockUser);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        householdService.getByUserId(userId);
      });

      assertEquals("User with ID = " + userId + " is not in a household", exception.getMessage());
    }
  }

  @Nested
  class UpdateHouseholdTests {

    @Test
    void shouldUpdateHouseholdWhenValid() {
      HouseholdRequest request = new HouseholdRequest();
      request.setAdress("New Address");
      request.setLatitude(30.0);
      request.setLongitude(40.0);
      request.setWaterAmountLiters(100.0);
      request.setLastWaterChangeDate(new Date());

      when(householdRepository.findById(1L)).thenReturn(Optional.of(existingHousehold));

      HouseholdResponse response = householdService.updateHousehold(1L, request);

      assertEquals("New Address", response.getAddress());
      assertEquals(30.0, response.getLatitude());
      assertEquals(40.0, response.getLongitude());
      assertEquals(100.0, response.getWaterAmountLiters());

      verify(householdRepository).update(existingHousehold);;
    }

    @Test
    void shouldThrowExceptionWhenHouseholdNotFound() {
      when(householdRepository.findById(2L)).thenReturn(Optional.empty());

      HouseholdRequest request = new HouseholdRequest();

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        householdService.updateHousehold(2L, request);
      });

      assertTrue(exception.getMessage().contains("Household with ID = 2 not found"));
    }

    @Test
    void shouldNotUpdateWhenFieldsAreNull() {
      HouseholdRequest request = new HouseholdRequest();

      when(householdRepository.findById(1L)).thenReturn(Optional.of(existingHousehold));
  
      HouseholdResponse response = householdService.updateHousehold(1L, request);
  
      assertEquals("Test address", response.getAddress());
      assertEquals(10.0, response.getLatitude());
      assertEquals(20.0, response.getLongitude());
      assertEquals(50.0, response.getWaterAmountLiters());
  
      verify(householdRepository).update(existingHousehold);
    }
  }

  @Nested
  class AcceptHouseholdInviteTests {

    @Test
    void shouldAcceptAndDeleteOldHouseholdIfEmpty() {
      String inviteKey = "abd123";
      HouseholdInvite invite = new HouseholdInvite();
      invite.setUserId(1L);
      invite.setHouseholdId(2L);
      invite.setInviteKey(inviteKey);

      Long oldHouseholdId = 10L;
      User user = new User();
      user.setId(1L);
      user.setHouseholdId(oldHouseholdId);

      when(householdInviteService.findByKey(inviteKey)).thenReturn(invite);
      when(userService.getUserById(1L)).thenReturn(user);
      when(householdService.getMembers(10L)).thenReturn(Collections.emptyList());

      householdService.acceptHouseholdInvite(inviteKey);

      verify(userService).updateUserCredentials(user);
      verify(householdInviteService).deleteInvite(inviteKey);
      verify(householdRepository).deleteById(oldHouseholdId);
    }

    @Test
    void shouldAcceptAndNotDeleteIfOldHouseholdNotEmptry() {
      String inviteKey = "abc123";
      Long newHouseholdId = 2L;
      Long userId = 100L;

      HouseholdInvite invite = new HouseholdInvite();
      invite.setInviteKey(inviteKey);
      invite.setHouseholdId(newHouseholdId);
      invite.setUserId(userId);
      when(householdInviteService.findByKey(inviteKey)).thenReturn(invite);

      Long oldHouseholdId = 1L;
      User user = new User();
      user.setHouseholdId(oldHouseholdId);
      when(userService.getUserById(userId)).thenReturn(user);

      when(householdService.getMembers(oldHouseholdId))
          .thenReturn(List.of(new UserResponse(), new UserResponse()));

      householdService.acceptHouseholdInvite(inviteKey);

      verify(userService).updateUserCredentials(user);
      verify(householdInviteService).deleteInvite(inviteKey);
      verify(householdRepository, never()).deleteById(oldHouseholdId);
    }

    @Test
    void shouldThrowIfInviteKeyIsNull() {
      assertThrows(IllegalArgumentException.class, () -> {
        householdService.acceptHouseholdInvite(null);
      });
    }

    @Test
    void shouldThrowIfInviteKeyIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> {
        householdService.acceptHouseholdInvite("");
      });
    }

    @Test
    void shouldThrowIfInviteNotFound() {
      String inviteKey = "notAKey";
      when(householdInviteService.findByKey(inviteKey)).thenReturn(null);

      assertThrows(IllegalArgumentException.class, () -> {
        householdService.acceptHouseholdInvite(inviteKey);
      });
    }
  }
}
