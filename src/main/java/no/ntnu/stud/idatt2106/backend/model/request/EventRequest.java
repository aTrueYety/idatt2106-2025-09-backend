package no.ntnu.stud.idatt2106.backend.model.request;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

  @NotNull(message = "InfoPage ID is required")
  private Long infoPageId;

  @NotBlank(message = "Event name is required")
  @Size(max = 255, message = "Name must be under 255 characters")
  private String name;

  @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
  @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
  private double latitude;

  @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
  @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
  private double longitude;

  @Positive(message = "Radius must be greater than 0")
  private double radius;

  @NotNull(message = "Start time is required")
  private Timestamp startTime;

  @NotNull(message = "End time is required")
  private Timestamp endTime;

  @Positive(message = "Severity ID must be a positive number")
  private long severityId;

  @Size(max = 1000, message = "Recommendation must be under 1000 characters")
  private String recomendation;
}

