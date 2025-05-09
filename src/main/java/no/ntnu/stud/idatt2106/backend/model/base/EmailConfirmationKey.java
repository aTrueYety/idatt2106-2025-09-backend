package no.ntnu.stud.idatt2106.backend.model.base;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an email confirmation key for a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfirmationKey {
  private Long userId;
  private Timestamp createdAt;
  private String key;
}
