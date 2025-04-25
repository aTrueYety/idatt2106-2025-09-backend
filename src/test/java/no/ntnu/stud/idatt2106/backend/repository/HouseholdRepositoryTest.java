package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for HouseholdRepository.
 */
@JdbcTest
@ActiveProfiles("Test")
@Import(HouseholdRepositoryImpl.class)
public class HouseholdRepositoryTest {
  
  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldSaveAndReturnWithId() {
    Household household = new Household();
    household.setAdress("Test street 2");
    household.setLatitude(32.43);
    household.setLongitude(453.4);
    household.setWaterAmountLiters(43.24);
    household.setLastWaterChangeDate(new Date());

    Household savedHousehold = householdRepository.save(household);

    assertNotNull(savedHousehold.getId());

    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM household WHERE id = ?",
        Integer.class,
        savedHousehold.getId());

    assertEquals(count, 1);
  }

  @Test
  void savedHouseholdsCanBeRetrievedById() {
    jdbcTemplate.update("""
        INSERT INTO household
        (id, adress, latitude, longitude, amount_water, last_water_change)
        VALUES (?, ?, ?, ?, ?, ?)
        """,
        1L, "Test adress", 64.34, 34.45, 23, new Date()
    );

    var result = householdRepository.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    assertEquals("Test adress", result.get().getAdress());
    assertEquals(64.34, result.get().getLatitude());
    assertEquals(34.45, result.get().getLongitude());
    assertEquals(23, result.get().getWaterAmountLiters());
  }
}
