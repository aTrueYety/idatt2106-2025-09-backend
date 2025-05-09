package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.AdminRegistrationKey;

/**
 * Repository interface for performing CRUD operations on AdminRegistrationKey
 * entities.
 */
public interface AdminRegistrationKeyRepository {
  /**
   * Saves a new AdminRegistrationKey to the database.
   *
   * @param key The AdminRegistrationKey to save.
   */
  void save(AdminRegistrationKey key);

  /**
   * Finds an AdminRegistrationKey by its key.
   *
   * @param key The Key of the AdminRegistrationKey to find.
   * @return The found AdminRegistrationKey, or null if not found.
   */
  AdminRegistrationKey findByKey(String key);

  /**
   * Finds an AdminRegistrationKey by its user ID.
   *
   * @param userId The user ID of the AdminRegistrationKey to find.
   * @return The found AdminRegistrationKey, or null if not found.
   */
  AdminRegistrationKey findByUserId(Long userId);

  /**
   * Retrieves all AdminRegistrationKeys from the database.
   *
   * @return A list of all AdminRegistrationKeys.
   */
  List<AdminRegistrationKey> findAll();

  /**
   * Updates an existing AdminRegistrationKey in the database.
   *
   * @param key The AdminRegistrationKey with updated information.
   */
  void update(AdminRegistrationKey key);

  /**
   * Deletes an AdminRegistrationKey by its key.
   *
   * @param key The Key of the AdminRegistrationKey to delete.
   */
  void deleteByKey(String key);

  /**
   * Deletes an AdminRegistrationKey by its user ID.
   *
   * @param userId The user ID of the AdminRegistrationKey to delete.
   */
  void deleteByUserId(Long userId);
}
