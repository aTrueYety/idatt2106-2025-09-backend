package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a response in the system.
 * Contains publicly availible user info, such as username, email and the profile picture.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String username;
  private String email; 
  private String firstName;
  private String lastName;
  private Long householdId;
  private byte[] picture;
}
