package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for an emergency group.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyGroupResponse {
  private int id;
  private String name;
  private String description;
}
