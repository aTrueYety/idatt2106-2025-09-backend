package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.Data;

/**
 * DTO for updating and existing user.
 */
@Data
public class UserUpdate {
  private String firstName;
  private String lastName;
  private String email;
}
