package no.ntnu.stud.idatt2106.backend.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.base.Kit;
import no.ntnu.stud.idatt2106.backend.model.response.KitResponse;

/**
 * Repository interface for performing CRUD operations on {@link Kit} entities.
 *
 * <p>This interface defines methods for retrieving, saving, updating, and deleting kits
 * from the underlying data source.
 */
public interface KitRepository {

  /**
   * Finds a kit by its unique identifier.
   *
   * @param id the ID of the kit to retrieve
   * @return an {@link Optional} containing the found kit, or an empty Optional if not found
   */
  Optional<Kit> findById(Long id);

  /**
   * Retrieves all kits.
   *
   * @return a list of all kits
   */
  List<Kit> findAll();

  /**
   * Saves a new kit to the repository.
   *
   * @param kit the kit to save
   */
  void save(Kit kit);

  /**
   * Updates an existing kit in the repository.
   *
   * @param kit the kit with updated information
   */
  void update(Kit kit);

  /**
   * Deletes a kit by its unique identifier.
   *
   * @param id the ID of the kit to delete
   */
  void deleteById(Long id);

    /**
   * Finds all food types where the name contains the given query, ignoring case.
   *
   * @param query the search query to match against food type names
   * @return a list of matching Kit entities
   */
  List<Kit> findByNameContainingIgnoreCase(String query);
}
