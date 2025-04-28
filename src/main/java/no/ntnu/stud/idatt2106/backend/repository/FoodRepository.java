package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;

/**
 * Repository interface for accessing and managing food items.
 */
public interface FoodRepository {

  /**
   * Find a food item by its ID.
   *
   * @param id the ID of the food item
   * @return an Optional containing the found food item, or empty if not found
   */
  Optional<Food> findById(int id);

  /**
   * Find all food items.
   *
   * @return a list of all food items
   */
  List<Food> findAll();

  /**
   * Save a new food item.
   *
   * @param food the food item to save
   */
  void save(Food food);

  /**
   * Update an existing food item.
   *
   * @param food the food item with updated information
   */
  void update(Food food);

  /**
   * Delete a food item by its ID.
   *
   * @param id the ID of the food item to delete
   */
  void deleteById(int id);

  /**
   * Find all food items belonging to a specific household.
   *
   * @param householdId the household ID
   * @return a list of food items belonging to the given household
   */
  List<Food> findByHouseholdId(int householdId);
}
