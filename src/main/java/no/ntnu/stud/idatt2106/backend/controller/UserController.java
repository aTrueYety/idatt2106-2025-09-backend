package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.model.update.UserUpdate;
import no.ntnu.stud.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id, UserUpdate update) {
    logger.info("Updating user with ID = {}", id);
    UserResponse response = userService.updateUserProfile(id, update);
    logger.info("Updated user successfully");
    return ResponseEntity.ok(response);
  }

}
