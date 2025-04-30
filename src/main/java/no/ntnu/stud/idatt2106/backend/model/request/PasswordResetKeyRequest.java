package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a password reset key request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetKeyRequest {
  private String email;
}
