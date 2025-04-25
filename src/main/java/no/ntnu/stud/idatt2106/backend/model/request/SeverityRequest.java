package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating or updating a severity level.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeverityRequest {
  private String colour;
  private String name;
  private String description;
}
