package no.ntnu.stud.idatt2106.backend.model.request;

import java.sql.Blob;
import java.time.LocalTime;
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
public class MapObjectRequest {
  private int typeId;
  private float latitude;
  private float longitude;
  private LocalTime opening;
  private String contactPhone;
  private String contactEmail;
  private String contactName;
  private String description;
  private Blob image;
}
