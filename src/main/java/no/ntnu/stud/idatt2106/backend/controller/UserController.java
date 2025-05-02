package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.stud.idatt2106.backend.model.base.User;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user-related operations.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Retrives a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    if (user != null) {
      return ResponseEntity.ok(user);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Updates the position sharing setting for a user.
   *
   * @param id the ID of the user whose position sharing setting is to be updated
   * @param request the request containing the new position sharing setting
   * @return a response indicating whether the update was successful or not
   */
  @PatchMapping("/{id}/position-sharing")
  public ResponseEntity<String> updatePositionSharing(@PathVariable Long id,
      @RequestBody UpdatePositionSharingRequest request) {
    boolean updated = userService.updateSharePositionHousehold(id,
        request.isSharePositionHousehold());
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
  @PutMapping("/{id}")
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
}
