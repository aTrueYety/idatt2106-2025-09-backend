package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating a new map object type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectRequestType {
  private String name;
  private String icon;
}
