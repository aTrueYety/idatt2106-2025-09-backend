package no.ntnu.stud.idatt2106.backend.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request object for creating or updating an extra resident type.
 *
 * <p>This class contains the necessary data to define a resident type
 * with specific water and food consumption values.
 * </p>
 */

@Data
public class ExtraResidentTypeRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @Positive(message = "Water consumption must be a positive number")
  private float consumptionWater;

  @Positive(message = "Food consumption must be a positive number")
  private float consumptionFood;
}
