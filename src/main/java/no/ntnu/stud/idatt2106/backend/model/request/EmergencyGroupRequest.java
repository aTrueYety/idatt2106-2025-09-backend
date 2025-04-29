package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating or updating an emergency group.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyGroupRequest {
  private String name;
  private String description;
}
