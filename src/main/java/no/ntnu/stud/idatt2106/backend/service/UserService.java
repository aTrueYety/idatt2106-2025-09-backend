package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import no.ntnu.stud.idatt2106.backend.model.base.EmailConfirmationKey;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.UserMapper;
import no.ntnu.stud.idatt2106.backend.util.EmailTemplates;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepo;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private EmailConfirmationKeyService emailConfirmationKeyService;

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to be retrieved.
   * @return The user with the specified ID, or null if not found.
   */
  public User getUserById(Long id) {
    return userRepo.findById(id);
  }

  /**
   * Retrieves public user info of a user by their ID.
   *
   * @param id the ID of the user to be retrieved
   * @return the user info of the user with the specified ID
   * @throws NoSuchElementException if no user with the specified ID is found
   */
  public UserResponse getUserProfileById(Long id) {
    User user = userRepo.findById(id);

    if (user == null) {
      throw new NoSuchElementException("User with ID = " + id + " not found");
    }

    return UserMapper.toResponse(user);
  }

  /**
   * Adds a new user to the system.
   *
   * @param user The user to be added.
   */
  public void addUser(User user) {
    userRepo.addUser(user);
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username The username of the user to be retrieved.
   * @return The user with the specified username, or null if not found.
   */
  public User getUserByUsername(String username) {
    return userRepo.findUserByUsername(username);
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email The email of the user to be retrieved.
   * @return The user with the specified email, or null if not found.
   */
  public User getUserByEmail(String email) {
    return userRepo.findUserByEmail(email);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param user The user to be retrieved.
   */
  public void updateUserCredentials(User user) {
    userRepo.updateUser(user);
  }

  /**
   * Retrieves all users belonging to the household with the specified ID,
   * and maps them to UserResponses.
   *
   * @param householdId the ID of the household to retrieve Users from
   * @return a List of {@Link UserResponse} representing the users in the given
   *         household
   */
  public List<UserResponse> getUsersByHouseholdId(Long householdId) {
    return userRepo.findUsersByHouseholdId(householdId).stream()
        .map(UserMapper::toResponse)
        .toList();
  }

  /**
   * Checks if a user with the given ID exists.
   *
   * @param id the ID of the user to check
   * @return true if the user exists, false if it doesen't
   */
  public boolean userExists(Long id) {
    return userRepo.findById(id) != null;
  }

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  /**
   * Updates a users profile.
   *
   * @param id     the ID of the user to be updated
   * @param update the new user info
   * @return Repsonse object with the updated user
   * @throws NoSuchElementException if no user with the given ID exists
   */
  public UserResponse updateUserProfile(Long id, UserUpdate update, String token) {
    Long authId = jwtService.extractUserId(token.substring(7));
    Validate.that(id.equals(authId), Validate.isTrue(),
        "User id must be the your own");
    Validate.that(update.getFirstName(), Validate.isNotBlankOrNull());
    Validate.that(update.getLastName(), Validate.isNotBlankOrNull());
    Validate.that(update.getEmail(), Validate.isNotBlankOrNull());

    User existingUser = userRepo.findById(id);
    if (existingUser == null) {
      throw new NoSuchElementException("User with ID = " + id + " not found");
    }

    existingUser.setFirstName(update.getFirstName());
    existingUser.setLastName(update.getLastName());

    // Email set to not verified if changed
    if (!existingUser.getEmail().equals(update.getEmail())) {
      existingUser.setEmailConfirmed(false);
    }

    existingUser.setEmail(update.getEmail());

    userRepo.updateUser(existingUser);
    return UserMapper.toResponse(existingUser);
  }

  /**
   * Returns the user associated with the given JWT token.
   *
   * @param token the JWT token containing the user's ID
   * @return a DTO representing the user associated with the token'
   */
  public UserResponse getByToken(String token) {
    Long id = jwtService.extractUserId(token.substring(7));
    return UserMapper.toResponse(getUserById(id));
  }

  /**
   * Enables the sharing of the user's position for their household.
   *
   * @param userId The ID of the user for whom to enable position sharing.
   * @return true if the user's position sharing was successfully enabled, false
   *         otherwise.
   */
  public boolean enableSharePositionForHousehold(Long userId) {
    User user = userRepo.findById(userId);
    if (user == null) {
      return false;
    }
    userRepo.updateSharePositionHousehold(userId, true);
    return true;
  }

  /**
   * Updates the sharing of the user's position for their household and group.
   *
   * @param userId                  The ID of the user for whom to update position
   *                                sharing.
   * @param sharedPositionHousehold True to enable position sharing for the
   *                                household, false to disable it.
   * @param sharedPositionGroup     True to enable position sharing for the group,
   *                                false to disable it.
   * @return true if the user's position sharing was successfully updated, false
   *         if the user does not exist.
   */
  public boolean updateSharePositionHouseholdOrGroup(Long userId, boolean sharedPositionHousehold,
      boolean sharedPositionGroup) {
    User user = userRepo.findById(userId);
    if (user == null) {
      return false;
    }
    userRepo.updateSharePositionHousehold(userId, sharedPositionHousehold);
    userRepo.updateSharePositionGroup(userId, sharedPositionGroup);
    return true;
  }

  /**
   * Retrives all the admin users in the system.
   *
   * @param token the JWT token of the user logged in
   * @return A list of {@Link UserResponse} representing the admin users in the
   *         system
   */
  public List<UserResponse> getAllAdmins(String token) {
    Validate.isValid(jwtService.extractIsSuperAdmin(token.substring(7)),
        "You are not authorized to view all admins.");
    return userRepo.findAll().stream()
        .filter(User::isAdmin)
        .map(UserMapper::toResponse)
        .toList();
  }

  /**
   * Retrieves all the users in the system with pending admin registration keys.
   *
   * @param token the JWT token of the user logged in
   * @return A list of {@Link UserResponse} representing the users with pending
   *         admin registration keys
   */
  public List<UserResponse> getAllPendingAdmins(String token) {
    Validate.isValid(jwtService.extractIsSuperAdmin(token.substring(7)),
        "You are not authorized to view all users with pending admin registration keys.");
    return userRepo.findAllWithPendingAdminInvites().stream()
        .map(UserMapper::toResponse)
        .toList();
  }

  /**
   * Send an email to the user with a link to verify their email address.
   *
   * @param token the JWT token of the user logged in
   */
  public void sendEmailVerification(String token) {
    User user = getUserById(jwtService.extractUserId(token.substring(7)));
    Validate.that(user, Validate.isNotNull(), "User not found");

    Validate.that(user.getEmail(), Validate.isNotBlankOrNull(), "Email cannot be null or empty");
    
    if (emailConfirmationKeyService.emailConfirmationKeyExists(user.getId())) {
      emailConfirmationKeyService.deleteEmailConfirmationKey(user.getId());
    }
    String key = emailConfirmationKeyService.createEmailConfirmationKey(user.getId());
    
    try {
      emailService.sendHtmlEmail(
          user.getEmail(), 
          "Verify your email address", 
          EmailTemplates.getEmailConfirmationTemplate(key));
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email verification", e);
    }
  }

  /**
   * Confirms the user's email address using the provided key.
   *
   * @param key the confirmation key
   */
  public void confirmEmail(String key) {
    Validate.that(key, Validate.isNotBlankOrNull(), "Key cannot be null or empty");

    EmailConfirmationKey emailConfirmationKey = emailConfirmationKeyService
        .getEmailConfirmationKeyByKey(key);
    Validate.that(emailConfirmationKey, Validate.isNotNull(), "Invalid key");

    User user = getUserById(emailConfirmationKey.getUserId());
    Validate.that(user, Validate.isNotNull(), "User not found");

    user.setEmailConfirmed(true);
    userRepo.updateUser(user);
    emailConfirmationKeyService.deleteEmailConfirmationKey(user.getId());
  }
}