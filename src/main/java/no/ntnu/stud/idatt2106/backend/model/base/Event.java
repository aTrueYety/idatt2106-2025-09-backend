package no.ntnu.stud.idatt2106.backend.model.base;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an event.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  private Long id;
  private Long infoPageId;
  private double latitude;
  private double longitude;
  private double radius;
  private Timestamp startTime;
  private Timestamp endTime;
  private Long severityId;
  private String recomendation;
}