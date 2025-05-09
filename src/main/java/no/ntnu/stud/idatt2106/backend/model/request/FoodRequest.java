package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * Request DTO for creating a new food item.
 *
 * <p>Contains the type ID, household ID, expiration date, and amount of the food.
 * </p>
 */
@Data
public class FoodRequest {

  @NotNull(message = "Food type ID is required")
  private Long typeId;

  @NotNull(message = "Household ID is required")
  private Long householdId;

  @NotNull(message = "Expiration date is required")
  private LocalDate expirationDate;

  @Min(value = 1, message = "Amount must be at least 1")
  private double amount;
}



