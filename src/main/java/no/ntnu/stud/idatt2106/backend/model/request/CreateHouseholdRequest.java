package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating a new household.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHouseholdRequest {

  @NotBlank(message = "Address is required")
  @Size(max = 255, message = "Address must be less than 255 characters")
  private String address;

  @NotBlank(message = "Household name is required")
  @Size(max = 100, message = "Name must be less than 100 characters")
  private String name;

  @NotNull(message = "Longitude is required")
  @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
  @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
  private Double longitude;

  @NotNull(message = "Latitude is required")
  @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
  @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
  private Double latitude;

  @NotNull(message = "Water amount must be specified")
  @DecimalMin(value = "0.0", inclusive = false, message = "Water amount must be greater than 0")
  private Double waterAmountLiters;

  @NotNull(message = "Last water change date is required")
  private Date lastWaterChangeDate;
}

