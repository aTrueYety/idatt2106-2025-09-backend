package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;

/**
 * Repository interface for managing food items.
 */
public interface FoodRepository {
  /**
   * Find a food item by its ID.
   *
   * @param id the ID of the food item
   * @return an Optional containing the Food if found, empty otherwise
   */
  Optional<Food> findById(int id);

  /**
   * Find all food items.
   *
   * @return a list of all Food objects
   */
  List<Food> findAll();

  /**
   * Save a new food item or update an existing one.
   *
   * @param food the Food object to save
   */
  void save(Food food);

  /**
   * Update an existing food item.
   *
   * @param food the Food object to update
   */
  void update(Food food);

  /**
   * Delete a food item by its ID.
   *
   * @param id the ID of the food item to delete
   */
  void deleteById(int id);

  /**
   * Find all food items associated with a specific household ID.
   *
   * @param householdId the ID of the household
   * @return a list of Food objects associated with the specified household ID
   */
  List<Food> findByHouseholdId(int householdId);
}
