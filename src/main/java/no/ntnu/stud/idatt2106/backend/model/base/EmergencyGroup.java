package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an emergency group entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyGroup {
  private int id;
  private String name;
  private String description;
}
