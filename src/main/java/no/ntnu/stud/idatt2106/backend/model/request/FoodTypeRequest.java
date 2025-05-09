package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


/**
 * Request object for creating or updating a food type.
 *
 * <p>This class includes the name, unit of measurement, caloric value per unit,
 * and an optional image representing the food type.
 * </p>
 */
@Data
public class FoodTypeRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Unit is required")
  private String unit;

  @NotNull(message = "Calories per unit is required")
  @Positive(message = "Calories per unit must be a positive number")
  private Float caloriesPerUnit;

  private byte[] picture; // optional, no validation
}
