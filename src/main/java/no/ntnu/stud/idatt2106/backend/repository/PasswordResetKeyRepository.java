
package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.PasswordResetKey;

/**
 * Repository interface for managing PasswordChangeKey entities.
 */
public interface PasswordResetKeyRepository {
  /**
   * Saves a new PasswordChangeKey to the database.
   *
   * @param key The PasswordChangeKey to save.
   * @return The saved PasswordChangeKey.
   */
  PasswordResetKey save(PasswordResetKey key);

  /**
   * Finds a PasswordChangeKey by its userId.
   *
   * @param userId The userId of the PasswordChangeKey.
   * @return An Optional containing the PasswordChangeKey if found, or empty if
   *         not.
   */
  PasswordResetKey findByUserId(Long userId);

  /**
   * Finds a PasswordChangeKey by its key.
   *
   * @param key The key of the PasswordChangeKey.
   * @return An Optional containing the PasswordChangeKey if found, or empty if
   *         not.
   */
  PasswordResetKey findByKey(String key);

  /**
   * Deletes a PasswordChangeKey by its key.
   *
   * @param key The key of the PasswordChangeKey to delete.
   */
  void deleteByKey(String key);

  /**
   * Updates an existing PasswordChangeKey in the database.
   *
   * @param key The PasswordChangeKey with updated information.
   */
  void update(PasswordResetKey key);
}
