package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for transmitting real-time location updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdate {
  private Long userId;
  private Double latitude;
  private Double longitude;
}


