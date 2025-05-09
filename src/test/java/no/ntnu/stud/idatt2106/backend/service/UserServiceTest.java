package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.UserMapper;
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
}
