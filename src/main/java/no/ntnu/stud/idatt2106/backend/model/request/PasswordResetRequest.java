package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;

/**
 * Model class representing a password change key request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
  private String key;
  private String newPassword;
}