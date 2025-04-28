package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;

/**
 * Contains integration tests for the HouseholdService class.
 */
@JdbcTest
@ActiveProfiles("test")
public class HouseholdServiceIntegrationTest {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private HouseholdService householdService;

  @BeforeEach
  void setup() {
    jdbcTemplate.update("""
        INSERT INTO household (id, adress, latitude, longitude, amount_water, last_water_change) 
        VALUES (1, 'Test Address', 10.0, 20.0, 100.0, '2025-04-01')
        """);

    jdbcTemplate.update("""
        INSERT INTO "user" (id, username, email, password, household_id)
        VALUES (1, 'Testuser', 'test@example.com', 'password', 1)
        """);
  }

  @Nested
  class GetByIdTests {

    @Test
    void shouldReturnHousehold() {
      HouseholdResponse response = householdService.getById(1L);

      assertEquals(1L, response.getId());
      assertEquals("Test Adress", response.getAddress());
    }
  }
}
