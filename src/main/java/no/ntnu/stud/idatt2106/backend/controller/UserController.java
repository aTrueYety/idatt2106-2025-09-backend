package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.UpdatePositionSharingRequest;
import no.ntnu.stud.idatt2106.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user-related operations.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
  
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

}
