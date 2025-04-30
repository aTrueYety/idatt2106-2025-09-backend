package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.PasswordResetKey;
import no.ntnu.stud.idatt2106.backend.repository.PasswordResetKeyRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
  @Autowired
  private PasswordResetKeyRepositoryImpl passwordResetKeyRepository;

  /**
   * Generate a new password reset key for the user.
   *
   * @return the generated password reset key
   */
  public String generatePasswordResetKey() {
    return java.util.UUID.randomUUID().toString();
  }

  /**
   * Creates a new password reset key for the user.
   *
   * @param userId the ID of the user to create the password reset key for
   * @return the generated password reset key
   */
  public String createPasswordResetKey(Long userId) {
    String passwordResetKey = generatePasswordResetKey();
    passwordResetKeyRepository.save(new PasswordResetKey(
        userId, passwordResetKey, null));
    return passwordResetKey;
  }

  /**
   * Finds a password reset key by its key.
   *
   * @param key the password reset key to search for
   * @return the found PasswordResetKey object, or null if not found
   */
  public PasswordResetKey findByKey(String key) {
    return passwordResetKeyRepository.findByKey(key);
  }

  /**
   * Deletes the password reset key.
   *
   * @param key the password reset key to delete
   */
  public void deletePasswordResetKey(String key) {
    passwordResetKeyRepository.deleteByKey(key);
  }
}
