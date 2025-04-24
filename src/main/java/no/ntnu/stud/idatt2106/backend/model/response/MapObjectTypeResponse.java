package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response object for map type-related operations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectTypeResponse {
  private int id;
  private String name;
  private String icon;
}
