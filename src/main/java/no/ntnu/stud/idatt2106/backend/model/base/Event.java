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
  long id;
  long infoPageId;
  double latitude;
  double longitude;
  double radius;
  Timestamp startTime;
  Timestamp endTime;
  long severityId;
  String recomendation;
}