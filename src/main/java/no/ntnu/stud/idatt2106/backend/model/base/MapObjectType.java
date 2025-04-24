package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a MapObjectType.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectType {
  private int id;
  private String name;
  private String icon;
}
