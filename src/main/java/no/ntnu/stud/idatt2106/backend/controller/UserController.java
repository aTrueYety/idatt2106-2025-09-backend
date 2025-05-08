package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.UpdatePositionSharingRequest;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user-related operations.
 */
@Tag(name = "User", description = "Endpoints for operations related to users.")
@RestController
@RequestMapping("/api/user")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Retrives a users info by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID
   */
  @Operation(
      summary = "Retrieves a user's public info",
      description = """
          Retrieves the publicly available info about the user. Info such as
          username, first- and last name, email, etc. Doesen't include info 
          such as password.
          """
  )
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    logger.info("Retrieving user profile of user with ID = " + id);
    UserResponse response = userService.getUserProfileById(id);
    logger.info("User info retrieved successfully");
    return ResponseEntity.ok(response);
  }

  /**
   * Updates the position sharing setting for a user.
   *
   * @param id the ID of the user whose position sharing setting is to be updated
   * @param request the request containing the new position sharing setting
   * @return a response indicating whether the update was successful or not
   */
  @PatchMapping("/{id}/position-sharing") //TODO auth
  public ResponseEntity<String> updatePositionSharing(@PathVariable Long id,
      @RequestBody UpdatePositionSharingRequest request) {
    boolean updated = userService.updateSharePositionHouseholdOrGroup(id,
        request.isSharePositionHousehold(), request.isSharePositionGroup());
    if (updated) {
      return ResponseEntity.ok("Position sharing updated for user " + id);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  
  /**
   * Endpoint for updating a users profile information.
   *
   * @param id the id of the user
   * @param update the new profile info
   * @return a ResponseEntity with the updated user
   */
  @Operation(
      summary = "Updates a user's profile information.",
      description = """
          Updates a user's profile info, such as the user's user username, name, email, etc.
          """
  )
  @PutMapping("/{id}") //TODO auth
  public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id,
      @RequestBody UserUpdate update) {
    logger.info("Updating user with ID = {}", id);
    UserResponse response = userService.updateUserProfile(id, update);
    logger.info("Updated user successfully");
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint for retrieving the profile of the logged in user.
   *
   * @param token the JWT token of the user logged in
   * @return A ResponseEntity with a DTO representing the logged in user
   */
  @Operation(
      summary = "Retrieves the current user",
      description = """
          Retrieves the profile info of the user currently logged in using their JWT token.
          """
  )
  @GetMapping("/my-user")
  public ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String token) {
    logger.info("Fetching profile of current user");
    UserResponse response = userService.getByToken(token);
    logger.info("User retrieved successfully");
    return ResponseEntity.ok(response);
  }

  /**
   * Retrives all admin users in the system.
   *
   * @param token the JWT token of the user logged in
   * @return A ResponseEntity with a list of all admin users in the system
   */
  @Operation(
      summary = "Retrieves all admin users",
      description = """
          Retrieves all admin users in the system. Only available for super admins.
          """
  )
  @GetMapping("/admins")
  public ResponseEntity<List<UserResponse>> getAllAdmins(
        @RequestHeader("Authorization") String token) {
    logger.info("Fetching all admin users");
    List<UserResponse> responses = userService.getAllAdmins(token);
    logger.info("Admin users retrieved successfully");
    return ResponseEntity.ok(responses);
  }

  /**
   * Retrives all users with a pending admin registration key.
   *
   * @param token the JWT token of the user logged in
   * @return A ResponseEntity with a list of all users with a pending admin registration key
   */
  @Operation(
      summary = "Retrieves all users with a pending admin registration key",
      description = """
          Retrieves all users with a pending admin registration key. Available for super admins.
          """
  )
  @GetMapping("/pending-admins")
  public ResponseEntity<List<UserResponse>> getAllPendingAdmins(
        @RequestHeader("Authorization") String token) {
    logger.info("Fetching all users with a pending admin registration key");
    List<UserResponse> responses = userService.getAllPendingAdmins(token);
    logger.info("Users with pending admin registration keys retrieved successfully");
    return ResponseEntity.ok(responses);
  }

  /**
   * Handles the request to send an email verification link to the user.
   *
   * @param token the JWT token of the user logged in
   * @return A ResponseEntity with a message indicating the result of the operation
   */
  @Operation(
      summary = "Sends an email verification link to the user",
      description = """
          Sends an email verification link to the user currently logged in using their JWT token.
          """
  )
  @PostMapping("/send-email-verification")
  public ResponseEntity<Void> sendEmailVerification(
        @RequestHeader("Authorization") String token) {
    logger.info("Sending email verification link to user");
    userService.sendEmailVerification(token);
    logger.info("Email verification link sent successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles the request to confirm the user's email address.
   *
   * @param key the confirmation key
   * @return A ResponseEntity with a message indicating the result of the operation
   */
  @Operation(
      summary = "Confirms the user's email address",
      description = """
          Confirms the user's email address using the provided confirmation key.
          """
  )
  @PostMapping("/confirm-email/{key}")
  public ResponseEntity<Void> confirmEmail(@PathVariable String key) {
    logger.info("Confirming email address with key = {}", key);
    userService.confirmEmail(key);
    logger.info("Email address confirmed successfully");
    return ResponseEntity.ok().build();
  }
}
