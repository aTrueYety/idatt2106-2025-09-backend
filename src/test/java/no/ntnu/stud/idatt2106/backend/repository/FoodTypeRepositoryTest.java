package no.ntnu.stud.idatt2106.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@JdbcTest
@ActiveProfiles("test")
@Import(FoodTypeRepositoryImpl.class)
public class FoodTypeRepositoryTest {

  @Autowired
  private FoodTypeRepository repository;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldSaveAndFindFoodType() {
    FoodType ft = new FoodType();
    ft.setName("Rice");
    ft.setUnit("kg");
    ft.setCaloriesPerUnit(3500.0f);
    ft.setPicture(null);

    repository.save(ft);

    List<FoodType> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getName()).isEqualTo("Rice");
  }

  @Test
  void shouldUpdateFoodType() {
    FoodType ft = new FoodType();
    ft.setName("White Rice");
    ft.setUnit("kg");
    ft.setCaloriesPerUnit(3500.0f);
    ft.setPicture(null);

    repository.save(ft);

    List<FoodType> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getName()).isEqualTo("White Rice");

    ft.setName("Brown Rice");
    repository.update(ft);

    all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getName()).isEqualTo("Brown Rice");
  }

  @Test
  void shouldDeleteFoodType() {

    FoodType ft = new FoodType();
    ft.setName("Rice");
    ft.setUnit("kg");
    ft.setCaloriesPerUnit(3500.0f);
    ft.setPicture(null);

    repository.save(ft);

    List<FoodType> all = repository.findAll();
    assertThat(all).hasSize(1);

    repository.deleteById(all.get(0).getId());

    all = repository.findAll();
    assertThat(all).isEmpty();

  }
}
