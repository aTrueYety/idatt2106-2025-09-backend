package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Household;

/**
 * Defines methods for storing and retrieving households.
 *
 * @version 1.0
 * @since 23.04.2025
 */
public interface HouseholdRepository {
  /**
   * Saves a household to the repository.
   *
   * @param household the household to be saved
   * @return the saved household
   */
  Household save(Household household);

  /**
   * Retrieves a List with all registered households.
   *
   * @return List of all registered households
   */
  List<Household> findAll();

  /**
   * Retrieves a household by its ID.
   *
   * @param id the ID of the household to be retrieved
   * @return an optional containing the retrieved household, or empty if no
   *         household with the given ID was found
   */
  Optional<Household> findById(Long id);

  /**
   * Updates a registered household.
   *
   * @param household household with the new information
   */
  void update(Household household);

  /**
   * Deletes the household with the given ID.
   *
   * @param id the ID of the household to be deleted
   */
  void deleteById(Long id);
  
  Optional<Household> findByUserId(Long userId);

}
