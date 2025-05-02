package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to invite an admin user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInviteRequest {
  private String username;
}
