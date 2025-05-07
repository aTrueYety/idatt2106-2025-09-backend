package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.CreateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.HouseHoldInviteAcceptRequest;
import no.ntnu.stud.idatt2106.backend.model.request.InviteUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.UpdateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LevelOfPreparednessResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.HouseholdMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains tests for the HouseholdService class.
 */
@ExtendWith(MockitoExtension.class)
public class HouseholdServiceTest {

  @InjectMocks
  private HouseholdService householdService;

  @Mock
  private HouseholdRepository householdRepository;

  @Mock
  private UserService userService;

  @Mock
  private HouseholdInviteService householdInviteService;

  @Mock
  private EmailService emailService;

  @Mock
  private LevelOfPreparednessService levelOfPreparednessService;

  @Mock
  private JwtService jwtService;

  @Mock
  private Household existingHousehold;

  @BeforeEach
  void setup() {
    User mockUser = new User();
    mockUser.setUsername("Testuser");
    existingHousehold = new Household();
    existingHousehold.setId(1L);
    existingHousehold.setAddress("Test address");
    existingHousehold.setLatitude(10.0);
    existingHousehold.setLongitude(20.0);
    existingHousehold.setWaterAmountLiters(50.0);
    existingHousehold.setLastWaterChangeDate(new Date());
  }

  @Test
  void getAllShouldReturnAllHouseholds() {
    Household household1 = new Household();
    household1.setId(1L);
    household1.setAddress("Test Street 1");

    Household household2 = new Household();
    household2.setId(2L);
    household2.setAddress("Test Street 2");

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
    Date waterChange = new Date();
    CreateHouseholdRequest request = new CreateHouseholdRequest();
    request.setAddress("Test");
    request.setLatitude(32.3);
    request.setLongitude(34.23);
    request.setWaterAmountLiters(32.23);
    request.setLastWaterChangeDate(waterChange);

    User user = new User();
    user.setUsername("Testuser");
    when(userService.getUserByUsername("Testuser")).thenReturn(user);

    String token = "Bearer jwt.token";
    when(jwtService.extractUserName(token.substring(7))).thenReturn("Testuser");

    Household household = new Household();
    household.setAddress("Test");
    household.setLatitude(32.3);
    household.setLongitude(34.23);
    household.setWaterAmountLiters(32.23);
    household.setLastWaterChangeDate(waterChange);

    Household registeredHousehold = new Household();
    registeredHousehold.setId(1L);
    registeredHousehold.setAddress("Test");
    registeredHousehold.setLatitude(32.3);
    registeredHousehold.setLongitude(34.23);
    registeredHousehold.setWaterAmountLiters(32.23);
    registeredHousehold.setLastWaterChangeDate(waterChange);

    try (MockedStatic<HouseholdMapper> mapper = Mockito.mockStatic(HouseholdMapper.class)) {
      mapper.when(() -> HouseholdMapper.toEntity(request)).thenReturn(household);
      when(householdRepository.save(household)).thenReturn(registeredHousehold);
      when(householdRepository.findById(1L)).thenReturn(Optional.of(registeredHousehold));

      householdService.registerHousehold(request, token);
      verify(householdRepository).save(any(Household.class));
      verify(userService).getUserByUsername("Testuser");
      verify(userService).updateUserCredentials(any(User.class));
    }
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
    household.setAddress("Test street");
    household.setLatitude(10.0);
    household.setLongitude(20.0);
    household.setWaterAmountLiters(100.0);

    LevelOfPreparednessResponse preparedness = new LevelOfPreparednessResponse();
    preparedness.setLevelOfPreparedness(0.8);

    when(householdRepository.findById(id)).thenReturn(Optional.of(household));
    HouseholdResponse householdResponse = new HouseholdResponse();
    householdResponse.setId(household.getId());
    householdResponse.setAddress(household.getAddress());
    householdResponse.setLatitude(household.getLatitude());
    householdResponse.setLongitude(household.getLongitude());
    householdResponse.setWaterAmountLiters(household.getWaterAmountLiters());

    when(levelOfPreparednessService.getPreparednessForHousehold(any(HouseholdResponse.class)))
        .thenReturn(preparedness);

    HouseholdResponse response = householdService.getByIdWithPreparedness(id);

    assertNotNull(response);
    assertEquals(id, response.getId());
    assertEquals("Test street", response.getAddress());
    assertNotNull(response.getLevelOfPreparedness());
    assertEquals(0.8, response.getLevelOfPreparedness().getLevelOfPreparedness());
  }

  @Nested
  class GetByUserIdTests {

    @Test
    void shouldGetHouseholdResponseIfUserIsInHousehold() {
      User mockUser = new User();
      Household mockHousehold = new Household();
      mockHousehold.setId(100L);
      mockHousehold.setAddress("Test Address");
      mockUser.setId(1L);
      mockUser.setHouseholdId(100L);

      Long userId = 1L;
      when(userService.getUserById(userId)).thenReturn(mockUser);
      when(householdRepository.findById(mockUser.getHouseholdId()))
          .thenReturn(Optional.of(mockHousehold));
      when(jwtService.extractUserId(anyString())).thenReturn(userId);

      HouseholdResponse response = householdService.getByUserId("token  ");

      assertNotNull(response);
      assertEquals("Test Address", response.getAddress());
    }
    
    @Test
    void shouldThrowExceptionIfUserDoesentExist() {
      Long userId = 1L;
      when(jwtService.extractUserId(anyString())).thenReturn(userId);

      NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
        householdService.getByUserId("token  ");
      });

      assertEquals("User with ID = " + userId + " not found", exception.getMessage());
    }
  }

  @Nested
  class UpdateHouseholdTests {

    @Test
    void shouldUpdateHouseholdWhenValid() {
      UpdateHouseholdRequest request = new UpdateHouseholdRequest();
      request.setAddress("New Address");
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

      verify(householdRepository).update(existingHousehold);
      ;
    }

    @Test
    void shouldThrowExceptionWhenHouseholdNotFound() {
      when(householdRepository.findById(2L)).thenReturn(Optional.empty());

      UpdateHouseholdRequest request = new UpdateHouseholdRequest();

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        householdService.updateHousehold(2L, request);
      });

      assertTrue(exception.getMessage().contains("Household with ID = 2 not found"));
    }

    @Test
    void shouldNotUpdateWhenFieldsAreNull() {
      UpdateHouseholdRequest request = new UpdateHouseholdRequest();

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
      HouseholdInvite invite = new HouseholdInvite();
      invite.setUserId(1L);
      invite.setHouseholdId(2L);

      Long oldHouseholdId = 10L;
      User user = new User();
      user.setId(1L);
      user.setHouseholdId(oldHouseholdId);

      when(householdInviteService.findByUserIdAndHouseholdId(1L, 2L)).thenReturn(invite);
      when(userService.getUserById(1L)).thenReturn(user);
      when(householdService.getMembers(10L)).thenReturn(Collections.emptyList());
      when(jwtService.extractUserId(anyString())).thenReturn(1L);

      householdService.acceptHouseholdInvite(
        new HouseHoldInviteAcceptRequest(2L), "abc1234");

      verify(userService).updateUserCredentials(user);
      verify(householdInviteService).deleteHouseholdInvite(1L, 2L);
      verify(householdRepository).deleteById(oldHouseholdId);
    }

    @Test
    void shouldAcceptAndNotDeleteIfOldHouseholdNotEmptry() {
      Long newHouseholdId = 2L;
      Long userId = 100L;

      HouseholdInvite invite = new HouseholdInvite();
      invite.setHouseholdId(newHouseholdId);
      invite.setUserId(userId);

      Long oldHouseholdId = 1L;
      User user = new User();
      user.setId(userId);
      user.setHouseholdId(oldHouseholdId);
      when(userService.getUserById(userId)).thenReturn(user);

      when(householdService.getMembers(oldHouseholdId))
          .thenReturn(List.of(new UserResponse(), new UserResponse()));
      when(jwtService.extractUserId(anyString())).thenReturn(userId);
      when(userService.getUserById(userId)).thenReturn(user);
      when(householdInviteService.findByUserIdAndHouseholdId(anyLong(), anyLong()))
          .thenReturn(invite);

      householdService.acceptHouseholdInvite(
          new HouseHoldInviteAcceptRequest(newHouseholdId), "abc1234");

      verify(userService).updateUserCredentials(user);
      verify(householdInviteService).deleteHouseholdInvite(userId, newHouseholdId);
      verify(householdRepository, never()).deleteById(oldHouseholdId);
    }

    @Test
    void shouldThrowIfInviteNotFound() {
      when(jwtService.extractUserId(anyString())).thenReturn(0L);

      assertThrows(IllegalArgumentException.class, () -> {
        householdService.acceptHouseholdInvite(
            new HouseHoldInviteAcceptRequest(0L), "abc1234");
      });
    }
  }

  @Nested
  class InviteUserToHouseholdTests {

    @Test
    void shouldSendEmailIfValid() {
      String receiverName = "testuser";
      InviteUserHouseholdRequest inviteRequest = new InviteUserHouseholdRequest(receiverName);
      inviteRequest.setUsername(receiverName);
      ;

      Long senderId = 1L;
      User sender = new User();
      sender.setId(senderId);
      sender.setHouseholdId(10L);

      String email = "user@example.com";
      User receiver = new User();
      receiver.setId(senderId);
      receiver.setEmail(email);

      Long householdId = 10L;
      Household household = new Household();
      household.setId(householdId);
      household.setName("Test Navn");

      String token = "Bearer validtoken";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(senderId);
      when(householdRepository.findById(householdId)).thenReturn(Optional.of(household));

      when(userService.getUserByUsername(receiverName)).thenReturn(receiver);
      when(userService.getUserById(senderId)).thenReturn(sender);
      householdService.inviteUserToHousehold(inviteRequest, token);
      try {
        verify(emailService).sendHtmlEmail(eq(email),
            contains("Du har blitt invitert"), contains("Test Navn"));
      } catch (Exception e) {
        fail("Expected emailService.sendHTMLEmail to be called, but exception was thrown");
      }
    }

    @Test
    void shouldThrowRuntimeExceptionWhenFailedToSendMail() throws MessagingException {
      Household household = new Household();
      Long houseHoldId = 10L;
      household.setId(houseHoldId);

      User sender = new User();
      Long senderId = 1L;
      sender.setId(senderId);
      sender.setHouseholdId(houseHoldId);

      User receiver = new User();
      Long receiverId = 2L;
      receiver.setId(receiverId);
      receiver.setEmail("user@example.com");

      InviteUserHouseholdRequest request = new InviteUserHouseholdRequest();
      request.setUsername("Testuser");

      String token = "Bearer token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(senderId);
      when(userService.getUserById(senderId)).thenReturn(sender);
      when(householdRepository.findById(houseHoldId)).thenReturn(Optional.of(household));
      when(userService.getUserByUsername("Testuser")).thenReturn(receiver);


      doThrow(new MessagingException())
          .when(emailService)
          .sendHtmlEmail(anyString(), anyString(), anyString());

      RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        householdService.inviteUserToHousehold(request, token);
      });

      assertTrue(exception.getMessage().contains("Failed to send email"));
    }
  }

  @Test
  void loneUserLeavesAndHouseholdGetsDeleted() {
    Long householdId = 1L;
    User user = new User();
    user.setHouseholdId(householdId);
    user.setId(1L);

    when(jwtService.extractUserId(anyString())).thenReturn(user.getId());
    when(userService.getUserById(user.getId())).thenReturn(user);
    when(householdService.getMembers(householdId)).thenReturn(Collections.emptyList());
    householdService.leaveHousehold("Bearer token");

    assertNull(user.getHouseholdId());
    verify(userService).updateUserCredentials(user);
    verify(householdRepository).deleteById(householdId);
  }

  @Test
  void userLeavesHouseholdAndHouseholdStillExists() {
    Long householdId = 1L;
    User user = new User();
    User user2 = new User();
    user2.setId(2L);
    user2.setHouseholdId(householdId);    
    user.setHouseholdId(householdId);
    user.setId(1L);

    when(jwtService.extractUserId(anyString())).thenReturn(user.getId());
    when(userService.getUserById(user.getId())).thenReturn(user, user2);
    when(householdService.getMembers(householdId)).thenReturn(List.of(new UserResponse()));

    householdService.leaveHousehold("Bearer token");
    verify(userService).updateUserCredentials(user);
    verify(householdRepository, never()).deleteById(householdId);
  }

  @Test
  void shouldThrowExceptionWhenUserNotInHousehold() {
    
    User user = new User();
    user.setId(1L);
    when(jwtService.extractUserId(anyString())).thenReturn(user.getId());
    when(userService.getUserById(user.getId())).thenReturn(user);

    assertThrows(NoSuchElementException.class, () -> {
      householdService.leaveHousehold("Bearer token");
    });
  }
}
