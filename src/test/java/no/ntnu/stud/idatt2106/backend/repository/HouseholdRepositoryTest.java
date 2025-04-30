package no.ntnu.stud.idatt2106.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

  @BeforeEach
  void setup() {
    jdbcTemplate.update("DELETE FROM household");
    jdbcTemplate.execute("ALTER TABLE household ALTER COLUMN id RESTART WITH 1");

    jdbcTemplate.update("""
        INSERT INTO household
        (adress, latitude, longitude, amount_water, last_water_change)
        VALUES (?, ?, ?, ?, ?)
        """,
        "Test adress", 64.34, 34.45, 23, new Date()
    );

    jdbcTemplate.update("""
        INSERT INTO household
        (adress, latitude, longitude, amount_water, last_water_change)
        VALUES (?, ?, ?, ?, ?)
        """,
        "Test adress 2", 64.33, 31.45, 24, new Date()
    );
  }

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


    var result = householdRepository.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    assertEquals("Test adress", result.get().getAdress());
    assertEquals(64.34, result.get().getLatitude());
    assertEquals(34.45, result.get().getLongitude());
    assertEquals(23, result.get().getWaterAmountLiters());
  }

  @Test
  void findAllShouldReturnAllHouseholds() {
    List<Household> result = householdRepository.findAll();

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(h -> h.getAdress().equals("Test adress")));
    assertTrue(result.stream().anyMatch(h -> h.getAdress().equals("Test adress")));
  }

  @Nested
  class UpdateTests {

    @Test
    void shouldUpdateExistingHousehold() {
      Long id = jdbcTemplate
          .queryForObject("SELECT id FROM household LIMIT 1", Long.class);

      Household updatedHousehold = new Household();
      updatedHousehold.setId(id);
      updatedHousehold.setAdress("New Address");
      updatedHousehold.setLatitude(55.5);
      updatedHousehold.setLongitude(66.6);
      updatedHousehold.setWaterAmountLiters(200.0);
      updatedHousehold.setLastWaterChangeDate(new Date());

      // Act
      householdRepository.update(updatedHousehold);

      // Assert
      Map<String, Object> result = 
          jdbcTemplate.queryForMap("SELECT * FROM household WHERE id = ?", id);
      assertEquals("New Address", result.get("adress"));
      assertEquals(55.5, (Double) result.get("latitude"), 0.001);
      assertEquals(66.6, (Double) result.get("longitude"), 0.001);
      assertEquals(200.0, (Double) result.get("amount_water"), 0.001);
    }
  }

  @Nested
  class DeleteTests {

    @Test
    void shouldDeleteExistingHousehold() {
      jdbcTemplate.update("""
          INSERT INTO household (adress, latitude, longitude, amount_water, last_water_change)
          VALUES (?, ?, ?, ?, ?)
          """, "Test address", 10.0, 20.0, 100.0, new Date());


      Integer countBefore = jdbcTemplate.queryForObject(
          "SELECT COUNT(*) FROM household WHERE id = ?",
          Integer.class,
          1L);
      assertEquals(1, countBefore);

      householdRepository.deleteById(1L);

      Integer countAfter = jdbcTemplate.queryForObject(
          "SELECT COUNT(*) FROM household WHERE id = ?",
          Integer.class,
          1L);
      assertEquals(0, countAfter);
    }
  }
}
