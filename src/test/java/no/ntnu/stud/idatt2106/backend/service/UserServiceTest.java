package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.repository.UserRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Unit test for the UserService class.
 */
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository repository;

  @Nested
  class UpdateUserTests {

    @Test
    void shouldUpdateExistingUser() {
      
    }
  }
}
