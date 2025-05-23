package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;

/**
 * Repository interface for managing food types.
 */
public interface FoodTypeRepository {
  /**
   * Find a food type by its ID.
   *
   * @param id the ID of the food type
   * @return an Optional containing the FoodType if found, empty otherwise
   */
  Optional<FoodType> findById(Long id);

  /**
   * Find all food types.
   *
   * @return a list of all FoodType objects
   */
  List<FoodType> findAll();

  /**
   * Save a new food type or update an existing one.
   *
   * @param foodType the FoodType object to save
   */
  void save(FoodType foodType);

  /**
   * Update an existing food type.
   *
   * @param foodType the FoodType object to update
   */
  void update(FoodType foodType);

  /**
   * Delete a food type by its ID.
   *
   * @param id the ID of the food type to delete
   */
  void deleteById(Long id);

  /**
   * Finds all food types where the name contains the given query, ignoring case.
   *
   * @param query the search query to match against food type names
   * @return a list of matching FoodType entities
   */
  List<FoodType> findByNameContainingIgnoreCase(String query);

}