package no.ntnu.stud.idatt2106.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Contains tests for ExtraResidentRepository.
 */
@JdbcTest
@ActiveProfiles("test")
@Import(ExtraResidentRepositoryImpl.class)
public class ExtraResidentRepositoryTest {

  @Autowired
  private JdbcTemplate jdbc;

  @BeforeEach
  void setUp() {
    insertRequiredForeignKeys();
  }

  private void insertRequiredForeignKeys() {
    jdbc.update("""
        INSERT INTO extra_resident_type (id, name, consumption_water, consumption_food) 
        VALUES (?, ?, ?, ?)
        """,
        1L, "Visitor", 1.0f, 1.5f);

    jdbc.update("""
        INSERT INTO household (id, address, latitude, longitude, amount_water, last_water_change) 
        VALUES (?, ?, ?, ?, ?, CURRENT_DATE),
        1L, "Somewhere"
        """, 1.1f, 1.2f, 50.0f);
  }
}
