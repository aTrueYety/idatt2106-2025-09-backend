package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test class for the UserRepository class.
 */
@JdbcTest
@ActiveProfiles("Test")
@Import(UserRepository.class)
public class UserRepositoryTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    jdbcTemplate.update("""
        INSERT INTO household (id, adress, latitude, longitude, amount_water, last_water_change)
        VALUES
        (10, 'Address 1', 12.34, 56.78, 100.0, '2025-04-01'),
        (20, 'Address 2', 23.45, 67.89, 150.0, '2025-04-01')
        """);
  }

  @Test
  void shouldReturnUsersInGivenHousehold() {
    jdbcTemplate.update("""
        INSERT INTO `user` (username, email, password, household_id) VALUES
        ('user1', 'user1@example.com', 'pass1', 10),
        ('user2', 'user2@example.com', 'pass2', 10),
        ('user3', 'user3@example.com', 'pass3', 20)
        """);

    List<User> users = userRepository.findUsersByHouseholdId(10L);

    assertEquals(2, users.size());
    assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user1")));
    assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user2")));
  }

  @Test
  void shouldFindUserByEmail() {
    jdbcTemplate.update("""
        INSERT INTO `user` (username, email, password, household_id) VALUES
        ('user1', 'user1@example.com', 'pass1', 10),
        ('user2', 'user2@example.com', 'pass2', 10),
        ('user3', 'user3@example.com', 'pass3', 20)
        """);
    User users = userRepository.findUserByEmail("user1@example.com");
    assertEquals("user1", users.getUsername());
    User users2 = userRepository.findUserByEmail("not@example.com");
    assertEquals(null, users2);
  }

  @Test
  void shouldFindUserByUsername() {
    jdbcTemplate.update("""
        INSERT INTO `user` (username, email, password, household_id) VALUES
        ('user1', 'user1@example.com', 'pass1', 10),
        ('user2', 'user2@example.com', 'pass2', 10),
        ('user3', 'user3@example.com', 'pass3', 20)
        """);
    User users = userRepository.findUserByUsername("user1");
    assertEquals("user1", users.getUsername());
    User users2 = userRepository.findUserByUsername("notuser");
    assertEquals(null, users2);
  }

  @Test
  void shouldFindUserById() {
    jdbcTemplate.update("""
        INSERT INTO `user` (id, username, email, password, household_id) VALUES
        (1, 'user1', 'user1@example.com', 'pass1', 10),
        (2, 'user2', 'user2@example.com', 'pass2', 10),
        (3, 'user3', 'user3@example.com', 'pass3', 20)
        """);
    User users = userRepository.findById(1L);
    assertEquals("user1", users.getUsername());
    User users2 = userRepository.findById(100L);
    assertEquals(null, users2);
  }

  @Test
  void shouldAddUser() {
    User user = new User();
    user.setUsername("newuser");
    user.setEmail("user1@example.com");
    user.setPassword("pass1");
    user.setHouseholdId(10L);
    user.setEmailConfirmed(false);
    user.setAdmin(false);
    user.setSuperAdmin(false);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setSharePositionHousehold(false);
    user.setSharePositionGroup(false);

    User userBefore = userRepository.findUserByUsername("newuser");
    assertEquals(null, userBefore);
    int rowsAffected = userRepository.addUser(user);
    assertEquals(1, rowsAffected);
    User userAfter = userRepository.findUserByUsername("newuser");
    assertEquals("newuser", userAfter.getUsername());
  }

  @Test
  void shouldUpdateUser() {
    jdbcTemplate.update("""
        INSERT INTO `user` (id, username, email, password, household_id) VALUES
        (1, 'user1', 'user1@example.com', 'pass1', 10)"""
    );
    User user = userRepository.findById(1L);
    user.setUsername("updateduser");
    userRepository.updateUser(user);
    User updatedUser = userRepository.findById(1L);
    assertEquals("updateduser", updatedUser.getUsername());
  }
}
