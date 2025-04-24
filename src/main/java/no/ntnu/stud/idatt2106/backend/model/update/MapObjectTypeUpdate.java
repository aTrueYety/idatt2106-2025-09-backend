package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for updating a map object type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectTypeUpdate {
  private String name;
  private String icon;
}
