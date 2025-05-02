package no.ntnu.stud.idatt2106.backend.model.response;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lombok annotation to generate getters, setters, toString, equals, and
 * hashCode methods.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelOfPreparednessResponse {
  /**
   * Class for sending LevelOfPreparedness data to the frontend.
   */
  private double levelOfPreparedness;
  private double levelOfPreparednessWater;
  private double levelOfPreparednessFood;
  private double levelOfPreparednessKit;
  private long timePrepared;
}