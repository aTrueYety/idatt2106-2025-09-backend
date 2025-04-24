package no.ntnu.stud.idatt2106.backend.model.response;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for an event.
 * This class is used to represent the response data for an event in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
  private long id;
  private long infoPageId;
  private double latitude;
  private double longitude;
  private double radius;
  private Timestamp startTime;
  private Timestamp endTime;
  private long severityId;
  private String recomendation;
  private String colour;
  private String severityName;
  private String severityDescription;
}
