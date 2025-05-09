package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.EmailConfirmationKey;

/**
 * Repository interface for managing EmailConfirmationKey entities.
 */
public interface EmailConfirmationKeyRepository {
  /**
   * Saves a new EmailConfirmationKey to the database.
   *
   * @param key The EmailConfirmationKey to save.
   */
  void save(EmailConfirmationKey key);

  /**
   * Finds an EmailConfirmationKey by its key.
   *
   * @param key The key of the EmailConfirmationKey to find.
   * @return An Optional containing the EmailConfirmationKey if found, or empty if
   *         not found.
   */
  EmailConfirmationKey findByKey(String key);

  /**
   * Finds an EmailConfirmationKey by its user ID.
   *
   * @param userId The user ID of the EmailConfirmationKey to find.
   * @return An Optional containing the EmailConfirmationKey if found, or empty if
   */
  EmailConfirmationKey findByUserId(Long userId);

  /**
   * Updates an existing EmailConfirmationKey in the database.
   *
   * @param key The EmailConfirmationKey to update.
   */
  void update(EmailConfirmationKey key);

  /**
   * Deletes an EmailConfirmationKey by its key.
   *
   * @param key The key of the EmailConfirmationKey to delete.
   */
  void deleteByKey(String key);

  /**
   * Deletes an EmailConfirmationKey by its user ID.
   *
   * @param userId The user ID of the EmailConfirmationKey to delete.
   */
  void deleteByUserId(Long userId);
}
