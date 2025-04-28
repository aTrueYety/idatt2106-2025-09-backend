package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import({FoodService.class, FoodRepositoryImpl.class, FoodTypeRepositoryImpl.class})
public class FoodServiceIntegrationTest {

  @Autowired
  private FoodService service;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldCreateAndFetchFood() {
    jdbc.update(
        "INSERT INTO food_type (name, unit, calories_per_unit) VALUES (?, ?, ?)",
        "Bread", "stk", 100f);

    Integer typeId = jdbc.queryForObject(
        "SELECT id FROM food_type WHERE name = ?", Integer.class, "Bread");

    FoodRequest request = new FoodRequest();
    request.setTypeId(typeId);
    request.setHouseholdId(10);
    request.setExpirationDate(LocalDate.of(2025, 6, 1));
    request.setAmount(2);

    service.create(request);

    List<FoodResponse> all = service.getAll();

    assertThat(all).hasSize(1);
    assertThat(all.get(0).getHouseholdId()).isEqualTo(10);
  }
}
