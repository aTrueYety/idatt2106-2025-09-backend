package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a map object type.
 * This class is used to define the types of map objects in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectType {
  private Long id;
  private String name;
  private String icon;
}
