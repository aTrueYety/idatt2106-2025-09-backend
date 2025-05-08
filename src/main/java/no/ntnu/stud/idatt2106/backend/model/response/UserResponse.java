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
  private Long id;
  private Long householdId;
  private String email;
  private boolean emailConfirmed;
  private String username;
  private String firstName;
  private String lastName;
  private boolean sharePositionHousehold;
  private boolean sharePositionGroup;
  private float lastLatitude;
  private float lastLongitude;
  private Boolean admin;
  private Boolean superAdmin;
}
