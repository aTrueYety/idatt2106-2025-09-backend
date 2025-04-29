package no.ntnu.stud.idatt2106.backend.model.response;

import java.sql.Blob;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response object for map objects.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObjectResponse {
  private Long id;
  private Long typeId;
  private String typeName;
  private String typeIcon;
  private float latitude;
  private float longitude;
  private Timestamp opening;
  private Timestamp closing;
  private String contactPhone;
  private String contactEmail;
  private String contactName;
  private String description;
  private Blob image;
}
