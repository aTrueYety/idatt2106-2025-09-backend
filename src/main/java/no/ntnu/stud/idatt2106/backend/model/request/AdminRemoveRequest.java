package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to upgrade a user to admin status using a registration key.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRemoveRequest {
  private String username;
}
