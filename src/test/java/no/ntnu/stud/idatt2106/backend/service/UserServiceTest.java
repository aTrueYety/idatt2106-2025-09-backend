package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.NoSuchElementException;
import jakarta.mail.MessagingException;
import no.ntnu.stud.idatt2106.backend.model.base.EmailConfirmationKey;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.UserMapper;
import no.ntnu.stud.idatt2106.backend.util.EmailTemplates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for the UserService class.
 */
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository repository;

  @Mock
  private JwtService jwtService;

  @Mock
  private EmailService emailService;
  @Mock
  private EmailConfirmationKeyService emailConfirmationKeyService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  class UpdateUserTests {

    @Test
    void shouldUpdateExistingUser() {
      UserUpdate update = new UserUpdate();
      update.setFirstName("New");
      update.setLastName("User");
      update.setEmail("new@example.com");

      Long userId = 1L;
      User existingUser = new User();
      existingUser.setId(userId);
      existingUser.setFirstName("Old");
      existingUser.setLastName("Name");
      existingUser.setEmail("old@example.com");
      existingUser.setEmailConfirmed(false);
      existingUser.setSharePositionHousehold(true);
      existingUser.setSharePositionGroup(false);

      String token = "Bearer token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);

      when(repository.findById(userId)).thenReturn(existingUser);
      when(repository.findById(userId)).thenReturn(existingUser);

      UserResponse result = userService.updateUserProfile(userId, update, token);

      assertEquals(update.getFirstName(), result.getFirstName());
      assertEquals(update.getEmail(), existingUser.getEmail());
      assertFalse(existingUser.isEmailConfirmed());
      verify(repository).updateUser(existingUser);
    }

    @Test
    void shouldThrowIsUserNotFound() {
      Long userId = 5L;
      when(repository.findById(userId)).thenReturn(null);

      UserUpdate update = new UserUpdate();
      update.setFirstName("First");
      update.setLastName("Last");
      update.setEmail("email@example.com");

      String token = "Bearer token";
      User user = new User();

      when(jwtService.extractUserId(token)).thenReturn(userId);
      when(repository.findById(userId)).thenReturn(user);

      assertThrows(IllegalArgumentException.class, () -> {
        userService.updateUserProfile(userId, update, token);
      });
    }
  }

  @Nested
  class GetByTokenTests {

    @Test
    void shouldReturnUserResponseWhenTokenIsValid() {
      Long userId = 1L;
      String username = "testuser";

      User user = new User();
      user.setId(userId);
      user.setUsername(username);

      UserResponse expectedResponse = new UserResponse();
      expectedResponse.setId(userId);
      expectedResponse.setUsername(username);

      String token = "valid.jwt.token";
      when(jwtService.extractUserId(token.substring(7))).thenReturn(userId);
      when(repository.findById(userId)).thenReturn(user);

      try (MockedStatic<UserMapper> mocked = Mockito.mockStatic(UserMapper.class)) {
        mocked.when(() -> UserMapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.getByToken(token);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        mocked.verify(() -> UserMapper.toResponse(user));
      }
      verify(jwtService).extractUserId(token.substring(7));
      verify(repository).findById(userId);
    }
  }

  @Nested
  class GetUserProfileByIdTests {

    @Test
    void shouldReturnUserProfileIfUserExists() {
      User user = new User();
      UserResponse expected = new UserResponse();
      Long userId = 1L;
      String username = "Test";
      expected.setId(userId);
      expected.setUsername(username);

      when(repository.findById(userId)).thenReturn(user);
      try (MockedStatic<UserMapper> mocked = Mockito.mockStatic(UserMapper.class)) {
        mocked.when(() -> UserMapper.toResponse(user)).thenReturn(expected);

        UserResponse response = userService.getUserProfileById(userId);

        assertEquals(expected, response);
        mocked.verify(() -> UserMapper.toResponse(user));
      }

      verify(repository).findById(userId);
    }

    @Test
    void shouldThrowIfUserNotFound() {
      Long id = 2L;
      when(repository.findById(id)).thenReturn(null);

      assertThrows(NoSuchElementException.class, () -> {
        userService.getUserProfileById(id);
      });
    }

    @Test
    void shouldUpdateSharePositionHouseholdOrGroup_callsRepoAndReturnsTrue() {
      Long userId = 1L;
      User user = new User();
      user.setId(userId);
      user.setSharePositionHousehold(false);
      user.setSharePositionGroup(false);
      when(repository.findById(userId)).thenReturn(user);

      boolean result = userService.updateSharePositionHouseholdOrGroup(
          userId,
          true,
          true);

      assertTrue(result, "Service should return true when user exists");
      verify(repository).updateSharePositionHousehold(userId, true);
      verify(repository).updateSharePositionGroup(userId, true);
    }

    @Test
    void shouldReturnFalseAndNotCallRepo_whenUserNotFound() {
      Long userId = 1L;
      when(repository.findById(userId)).thenReturn(null);

      boolean result = userService.updateSharePositionHouseholdOrGroup(
          userId,
          true,
          true);

      assertFalse(result, "Service should return false when user does not exist");
      verify(repository, never()).updateSharePositionHousehold(anyLong(), anyBoolean());
      verify(repository, never()).updateSharePositionGroup(anyLong(), anyBoolean());
    }

    @Test
    void shouldReturnTrueAndCallRepo_whenUserFoundAndUpdateIsTrue() {
      Long userId = 1L;
      User user = new User();
      user.setId(userId);
      user.setSharePositionHousehold(false);
      user.setSharePositionGroup(false);
      when(repository.findById(userId)).thenReturn(user);

      boolean result = userService.updateSharePositionHouseholdOrGroup(
          userId,
          true,
          false);

      assertTrue(result, "Service should return true when user exists");
      verify(repository).updateSharePositionGroup(userId, false);
      verify(repository).updateSharePositionHousehold(userId, true);
    }

    @Test
    void shouldReturnFalseAndNotCallRepo_whenUserFoundAndUpdateIsFalse() {
      Long userId = 1L;
      User user = new User();
      user.setId(userId);
      user.setSharePositionHousehold(false);
      user.setSharePositionGroup(false);
      when(repository.findById(userId)).thenReturn(user);

      boolean result = userService.updateSharePositionHouseholdOrGroup(
          userId,
          false,
          false);

      assertTrue(result, "Service should return true when user exists");
      verify(repository).updateSharePositionGroup(userId, false);
      verify(repository).updateSharePositionHousehold(userId, false);
    }
  }

  @Nested
  class CrudAndMappingTests {
    @Test
    void addUser_callsRepo() {
      User u = new User();
      userService.addUser(u);
      verify(repository).addUser(u);
    }

    @Test
    void getUserByUsername_callsRepo() {
      User u = new User();
      when(repository.findUserByUsername("bob")).thenReturn(u);
      assertSame(u, userService.getUserByUsername("bob"));
    }

    @Test
    void getUserByEmail_callsRepo() {
      User u = new User();
      when(repository.findUserByEmail("x@x")).thenReturn(u);
      assertSame(u, userService.getUserByEmail("x@x"));
    }

    @Test
    void getUsersByHouseholdId_mapsResponses() {
      User a = new User(), b = new User();
      when(repository.findUsersByHouseholdId(7L)).thenReturn(List.of(a, b));
      try (MockedStatic<UserMapper> ms = Mockito.mockStatic(UserMapper.class)) {
        UserResponse ra = new UserResponse(), rb = new UserResponse();
        ms.when(() -> UserMapper.toResponse(a)).thenReturn(ra);
        ms.when(() -> UserMapper.toResponse(b)).thenReturn(rb);

        List<UserResponse> out = userService.getUsersByHouseholdId(7L);
        assertEquals(List.of(ra, rb), out);
        ms.verify(() -> UserMapper.toResponse(a));
        ms.verify(() -> UserMapper.toResponse(b));
      }
    }

    @Test
    void userExists_trueAndFalse() {
      when(repository.findById(1L)).thenReturn(new User());
      when(repository.findById(2L)).thenReturn(null);
      assertTrue(userService.userExists(1L));
      assertFalse(userService.userExists(2L));
    }

    @Test
    void getAllUsers_returnsAll() {
      List<User> all = List.of(new User(), new User());
      when(repository.findAll()).thenReturn(all);
      assertEquals(all, userService.getAllUsers());
    }
  }

  @Nested
  class SharePositionTests {
    @Test
    void enableSharePositionForHousehold_falseIfMissing() {
      when(repository.findById(5L)).thenReturn(null);
      assertFalse(userService.enableSharePositionForHousehold(5L));
      verify(repository, never()).updateSharePositionHousehold(anyLong(), anyBoolean());
    }

    @Test
    void enableSharePositionForHousehold_trueIfExists() {
      User u = new User();
      u.setId(8L);
      when(repository.findById(8L)).thenReturn(u);
      assertTrue(userService.enableSharePositionForHousehold(8L));
      verify(repository).updateSharePositionHousehold(8L, true);
    }
  }

  @Nested
  class AdminTests {
    @Test
    void getAllAdmins_throwsIfNotSuperAdmin() {
      when(jwtService.extractIsSuperAdmin("t")).thenReturn(false);
      assertThrows(IllegalArgumentException.class, () -> userService.getAllAdmins("Bearer t"));
    }

    @Test
    void getAllAdmins_filtersAndMaps() {
      when(jwtService.extractIsSuperAdmin("t")).thenReturn(true);
      User u1 = new User();
      u1.setAdmin(true);
      User u2 = new User();
      u2.setAdmin(false);
      when(repository.findAll()).thenReturn(List.of(u1, u2));
      try (MockedStatic<UserMapper> ms = Mockito.mockStatic(UserMapper.class)) {
        UserResponse r1 = new UserResponse();
        ms.when(() -> UserMapper.toResponse(u1)).thenReturn(r1);

        List<UserResponse> out = userService.getAllAdmins("Bearer t");
        assertEquals(List.of(r1), out);
        ms.verify(() -> UserMapper.toResponse(u1));
      }
    }

    @Test
    void getAllPendingAdmins_throwsIfNotSuperAdmin() {
      when(jwtService.extractIsSuperAdmin("x")).thenReturn(false);
      assertThrows(IllegalArgumentException.class, () -> userService.getAllPendingAdmins("Bearer x"));
    }

    @Test
    void getAllPendingAdmins_mapsAll() {
      when(jwtService.extractIsSuperAdmin("x")).thenReturn(true);
      User u = new User();
      when(repository.findAllWithPendingAdminInvites()).thenReturn(List.of(u));
      try (MockedStatic<UserMapper> ms = Mockito.mockStatic(UserMapper.class)) {
        UserResponse rr = new UserResponse();
        ms.when(() -> UserMapper.toResponse(u)).thenReturn(rr);

        List<UserResponse> out = userService.getAllPendingAdmins("Bearer x");
        assertEquals(List.of(rr), out);
        ms.verify(() -> UserMapper.toResponse(u));
      }
    }
  }

  @Nested
  class EmailVerificationTests {
    @Test
    void sendEmailVerification_createsKeyAndSends_whenNoExisting() throws Exception {
      when(jwtService.extractUserId("tok")).thenReturn(1L);
      User u = new User();
      u.setId(1L);
      u.setEmail("a@b.com");
      when(repository.findById(1L)).thenReturn(u);
      when(emailConfirmationKeyService.emailConfirmationKeyExists(1L)).thenReturn(false);
      when(emailConfirmationKeyService.createEmailConfirmationKey(1L)).thenReturn("KEY123");

      userService.sendEmailVerification("Bearer tok");

      verify(emailConfirmationKeyService, never()).deleteEmailConfirmationKey(1L);
      verify(emailConfirmationKeyService).createEmailConfirmationKey(1L);
      verify(emailService).sendHtmlEmail(
          eq("a@b.com"),
          eq("Verify your email address"),
          eq(EmailTemplates.getEmailConfirmationTemplate("KEY123")));
    }

    @Test
    void sendEmailVerification_deletesOldKeyFirst() throws Exception {
      when(jwtService.extractUserId("tk")).thenReturn(2L);
      User u = new User();
      u.setId(2L);
      u.setEmail("x@y");
      when(repository.findById(2L)).thenReturn(u);
      when(emailConfirmationKeyService.emailConfirmationKeyExists(2L)).thenReturn(true);
      when(emailConfirmationKeyService.createEmailConfirmationKey(2L)).thenReturn("NEW");

      userService.sendEmailVerification("Bearer tk");

      verify(emailConfirmationKeyService).deleteEmailConfirmationKey(2L);
      verify(emailConfirmationKeyService).createEmailConfirmationKey(2L);
      verify(emailService).sendHtmlEmail(any(), any(), any());
    }

    @Test
    void sendEmailVerification_wrapsException() throws MessagingException {
      when(jwtService.extractUserId("tok2")).thenReturn(3L);
      User u = new User();
      u.setId(3L);
      u.setEmail("u@v");
      when(repository.findById(3L)).thenReturn(u);
      when(emailConfirmationKeyService.emailConfirmationKeyExists(3L)).thenReturn(false);
      when(emailConfirmationKeyService.createEmailConfirmationKey(3L)).thenReturn("K");

      doThrow(new RuntimeException("SMTP send failure",
          new MessagingException("SMTP error")))
          .when(emailService)
          .sendHtmlEmail(
              anyString(),
              anyString(),
              anyString());
      assertThrows(RuntimeException.class,
          () -> userService.sendEmailVerification("Bearer tok2"));
    }

    @Nested
    class ConfirmEmailTests {
      @Test
      void confirmEmail_successfulFlow() {
        EmailConfirmationKey k = new EmailConfirmationKey();
        k.setUserId(9L);
        when(emailConfirmationKeyService.getEmailConfirmationKeyByKey("KK"))
            .thenReturn(k);
        User u = new User();
        u.setId(9L);
        u.setEmailConfirmed(false);
        when(repository.findById(9L)).thenReturn(u);

        userService.confirmEmail("KK");

        assertTrue(u.isEmailConfirmed());
        verify(repository).updateUser(u);
        verify(emailConfirmationKeyService).deleteEmailConfirmationKey(9L);
      }

      @Test
      void confirmEmail_throwsOnBlankKey() {
        assertThrows(IllegalArgumentException.class,
            () -> userService.confirmEmail(""));
      }
    }
  }
}
