package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a severity level.
 * This class is used to define the severity levels of events in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Severity {
  private Long id;
  private String colour;
  private String name;
  private String description;
}
