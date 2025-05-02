package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for the UserService class.
 */
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository repository;

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
      update.setPicture(null);
      update.setSharePositionHousehold(true);
      update.setSharePositionGroup(false);

      Long userId = 1L;
      User existingUser = new User();
      existingUser.setId(userId);
      existingUser.setFirstName("Old");
      existingUser.setLastName("Name");
      existingUser.setEmail("old@example.com");
      existingUser.setEmailConfirmed(true);

      when(repository.findById(userId)).thenReturn(existingUser);

      UserResponse result = userService.updateUserProfile(userId, update);

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
      update.setSharePositionHousehold(true);
      update.setSharePositionGroup(false);

      assertThrows(NoSuchElementException.class, () -> {
        userService.updateUserProfile(userId, update);
      });
    }
  }
}
