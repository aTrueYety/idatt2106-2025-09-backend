package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import java.util.List;
import java.util.Optional;

public interface FoodRepository {
  Optional<Food> findById(int id);
  List<Food> findAll();
  void save(Food food);
  void update(Food food);
  void deleteById(int id);

  
  List<Food> findByHouseholdId(int householdId);
}
