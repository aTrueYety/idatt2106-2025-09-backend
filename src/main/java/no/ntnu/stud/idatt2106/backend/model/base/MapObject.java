package no.ntnu.stud.idatt2106.backend.model.base;

import java.sql.Blob;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a map object with various attributes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapObject {
  private int id;
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