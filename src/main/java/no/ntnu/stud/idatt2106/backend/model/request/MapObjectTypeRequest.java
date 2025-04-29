package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating or updating a map object type.
 * This class is used to define the types of map objects in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectTypeRequest {
  private String name;
  private String icon;
}
