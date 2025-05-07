package no.ntnu.stud.idatt2106.backend.service;

import java.util.UUID;
import no.ntnu.stud.idatt2106.backend.model.base.EmailConfirmationKey;
import no.ntnu.stud.idatt2106.backend.repository.EmailConfirmationKeyRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing email confirmation keys.
 */
@Service
public class EmailConfirmationKeyService {
  @Autowired
  private EmailConfirmationKeyRepositoryImpl repository;

  /**
   * Generates a new email confirmation key.
   *
   * @return the generated email confirmation key
   */
  public String generateEmailConfirmationKey() {
    return UUID.randomUUID().toString();
  }

  /**
   * Creates a new email confirmation key for a user.
   *
   * @param userId the ID of the user to create the key for
   * @return the generated email confirmation key
   */
  public String createEmailConfirmationKey(Long userId) {
    // Validate the user ID (not null, positive, etc.)
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(userId, Validate.isPositive(), "User ID must be positive");
    Validate.that(repository.findByUserId(userId), Validate.isNull(),
        "User already has an email confirmation key");

    // Generate a new email confirmation key
    String key = generateEmailConfirmationKey();

    // Create a new EmailConfirmationKey object and save it to the database
    repository.save(new EmailConfirmationKey(userId, null, key));

    return key;
  }

  /**
   * Validates an email confirmation key for a user.
   *
   * @param userId the ID of the user to validate the key for
   * @param key the email confirmation key to validate
   * @return true if the key is valid, false otherwise
   */
  public boolean validateEmailConfirmationKey(Long userId, String key) {
    // Validate the user ID and key (not null, positive, etc.)
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(userId, Validate.isPositive(), "User ID must be positive");
    Validate.that(key, Validate.isNotNull(), "Key cannot be null");

    // Retrieve the email confirmation key from the database
    EmailConfirmationKey emailConfirmationKey = repository.findByUserId(userId);

    // Check if the key matches and is not expired
    return emailConfirmationKey != null && emailConfirmationKey.getKey().equals(key);
  }

  /**
   * Deletes an email confirmation key for a user.
   *
   * @param userId the ID of the user to delete the key for
   */
  public void deleteEmailConfirmationKey(Long userId) {
    // Validate the user ID (not null, positive, etc.)
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(userId, Validate.isPositive(), "User ID must be positive");

    // Delete the email confirmation key from the database
    repository.deleteByUserId(userId);
  }

  /**
   * Checks if an email confirmation key exists for a user.
   *
   * @param userId the ID of the user to check for
   * @return true if the key exists, false otherwise
   */
  public boolean emailConfirmationKeyExists(Long userId) {
    // Validate the user ID (not null, positive, etc.)
    Validate.that(userId, Validate.isNotNull(), "User ID cannot be null");
    Validate.that(userId, Validate.isPositive(), "User ID must be positive");

    // Check if the email confirmation key exists in the database
    return repository.findByUserId(userId) != null;
  }

  /**
   * Retrieves the email confirmation key for a user by its key.
   *
   * @param key the email confirmation key to retrieve
   * @return the email confirmation key, or null if not found
   */
  public EmailConfirmationKey getEmailConfirmationKeyByKey(String key) {
    Validate.that(key, Validate.isNotBlankOrNull(), "Key cannot be null or blank");

    // Retrieve the email confirmation key from the database
    return repository.findByKey(key);
  }
}
