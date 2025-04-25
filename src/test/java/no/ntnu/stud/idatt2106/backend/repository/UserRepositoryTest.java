package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import no.ntnu.stud.idatt2106.backend.model.base.User;

@JdbcTest
@ActiveProfiles("Test")
@Import(UserRepository.class)
public class UserRepositoryTest {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserRepository userRepository;

  @Nested
  class GetUsersByHouseholdIdTests {

    @Test
    void shouldReturnUsersInGivenHousehold() {
      jdbcTemplate.update("""
          INSERT INTO household (id, adress, latitude, longitude, amount_water, last_water_change) 
          VALUES
          (10, 'Address 1', 12.34, 56.78, 100.0, '2025-04-01'),
          (20, 'Address 2', 23.45, 67.89, 150.0, '2025-04-01')
          """);

      jdbcTemplate.update(""" 
          INSERT INTO \"user\" (username, email, password, household_id) VALUES
          ('user1', 'user1@example.com', 'pass1', 10),
          ('user2', 'user2@example.com', 'pass2', 10),
          ('user3', 'user3@example.com', 'pass3', 20)
          """);

      List<User> users = userRepository.findUsersByHouseholdId(10L);
      
      assertEquals(2, users.size());
      assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user1")));
      assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user2")));
    }
  }
}
