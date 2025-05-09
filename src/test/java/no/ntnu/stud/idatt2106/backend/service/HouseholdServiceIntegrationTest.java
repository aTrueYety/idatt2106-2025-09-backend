package no.ntnu.stud.idatt2106.backend.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
// import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepositoryImpl;
// import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Contains integration tests for the HouseholdService class.
 */
@JdbcTest
@ActiveProfiles("test")
// @Import({HouseholdService.class, UserService.class,
//   HouseholdRepositoryImpl.class, UserRepository.class, JwtService.class,
//   EmailService.class, HouseholdInviteService.class})
public class HouseholdServiceIntegrationTest {
  
  // @Autowired
  // private JdbcTemplate jdbcTemplate;

  // @Autowired
  // private HouseholdService householdService;

  // @BeforeEach
  // void setup() {
  //   jdbcTemplate.update("""
  //       INSERT INTO household (id, address, latitude, longitude, amount_water, last_water_change) 
  //       VALUES (1, 'Test Address', 10.0, 20.0, 100.0, '2025-04-01')
  //       """);

  //   jdbcTemplate.update("""
  //       INSERT INTO `user` (id, username, email, password, household_id)
  //       VALUES (1, 'Testuser', 'test@example.com', 'password', 1)
  //       """);
  // }

  // @Nested
  // class GetByIdTests {

  //   @Test
  //   void shouldReturnHousehold() {
  //     HouseholdResponse response = householdService.getById(1L);

  //     assertEquals(1L, response.getId());
  //     assertEquals("Test Address", response.getAddress());
  //   }
  // }
}
