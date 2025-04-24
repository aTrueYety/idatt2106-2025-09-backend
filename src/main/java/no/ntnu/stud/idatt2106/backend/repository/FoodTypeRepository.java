package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import java.util.List;
import java.util.Optional;

public interface FoodTypeRepository {
  Optional<FoodType> findById(int id);
  List<FoodType> findAll();
  void save(FoodType foodType);
  void update(FoodType foodType);
  void deleteById(int id);
}