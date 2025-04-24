package no.ntnu.stud.idatt2106.backend.model.update;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request payload for updating a map object.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectUpdate {
  private int typeId;
  private float latitude;
  private float longitude;
  private Timestamp opening;
  private Timestamp closing;
  private String contactPhone;
  private String contactEmail;
  private String contactName;
  private String description;
  private String name;
}
