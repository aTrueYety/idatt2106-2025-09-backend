package no.ntnu.stud.idatt2106.backend.model.request;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating or updating an event.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
  private Long infoPageId;
  private String name;
  private double latitude;
  private double longitude;
  private double radius;
  private Timestamp startTime;
  private Timestamp endTime;
  private long severityId;
  private String recomendation;
}
