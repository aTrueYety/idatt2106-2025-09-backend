package no.ntnu.stud.idatt2106.backend.model.request;

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
  private Long typeId;
  private Long householdId;
  private LocalDate expirationDate;
  private double amount;
}



