package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
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
@Import(FoodRepositoryImpl.class)
public class FoodRepositoryTest {

  @Autowired
  private FoodRepository repository;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldSaveAndRetrieveFood() {
    jdbc.update("""
        INSERT INTO food_type (name, unit, calories_per_unit, picture) VALUES (?, ?, ?, ?)
        """, "Rice", "kg", 350.0f, null);

    Food food = new Food();
    food.setTypeId(1L);
    food.setHouseholdId(42L);
    food.setExpirationDate(LocalDate.of(2025, 5, 20));
    food.setAmount(3);

    repository.save(food);

    List<Food> all = repository.findAll();
    assertThat(all).hasSize(1);
    Food result = all.get(0);
    assertThat(result.getHouseholdId()).isEqualTo(42);
    assertThat(result.getAmount()).isEqualTo(3);
  }
}
