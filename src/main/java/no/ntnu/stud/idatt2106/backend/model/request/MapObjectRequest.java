package no.ntnu.stud.idatt2106.backend.model.request;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating a new map object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectRequest {
  private int typeId;
  private float latitude;
  private float longitude;
  private Timestamp opening;
  private Timestamp closing;
  private String contactPhone;
  private String contactEmail;
  private String contactName;
  private String description;
}
