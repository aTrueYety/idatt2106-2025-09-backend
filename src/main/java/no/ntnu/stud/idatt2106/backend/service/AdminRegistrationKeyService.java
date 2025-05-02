package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.AdminRegistrationKey;
import no.ntnu.stud.idatt2106.backend.repository.AdminRegistrationKeyRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing admin registration keys.
 */
@Service
public class AdminRegistrationKeyService {
  @Autowired
  private AdminRegistrationKeyRepositoryImpl repository;

  /**
   * Generates a unique registration key for an admin user.
   *
   * @return a unique registration key as a string
   */
  private String generateRegistrationKey() {
    return java.util.UUID.randomUUID().toString();
  }

  /**
   * Creates a new admin registration key.
   *
   * @param userId the ID of the user to register as admin
   * @return the generated registration key
   */
  public String createAdminRegistrationKey(Long userId) {
    // Validate the user ID (not null, positive, etc.)
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(userId, Validate.isPositive(), "User ID must be positive");
    Validate.that(repository.findByUserId(userId), Validate.isNull(), 
        "User already has a registration key");

    // Generate a new registration key
    String registrationKey = generateRegistrationKey();

    // Create a new AdminRegistrationKey object and save it to the database
    repository.save(new AdminRegistrationKey(userId, registrationKey, null));

    return registrationKey;
  }

  /**
   * Finds an admin registration key by its Key.
   *
   * @param key the registration key to search for
   * @return the found AdminRegistrationKey object, or null if not found
   */
  public AdminRegistrationKey findByKey(String key) {
    Validate.that(key, Validate.isNotNull(), "Registration key cannot be null");
    return repository.findByKey(key);
  }

  /**
   * Finds an admin registration key by its user ID.
   *
   * @param userId the user ID to search for
   * @return the found AdminRegistrationKey object, or null if not found
   */
  public AdminRegistrationKey findByUserId(Long userId) {
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    return repository.findByUserId(userId);
  }

  /**
   * Deletes an admin registration key by its Key.
   *
   * @param key the registration key to delete
   */
  public void deleteByKey(String key) {
    Validate.that(key, Validate.isNotNull(), "Registration key cannot be null");
    repository.deleteByKey(key);
  }
}
