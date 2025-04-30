package no.ntnu.stud.idatt2106.backend.model.response;

import java.time.LocalDate;
import lombok.Data;

/**
 * Represents a batch of food with the same expiration date and amount.
 */
@Data
public class FoodBatchResponse {
  private long id;
  private float amount;
  private LocalDate expirationDate;
  private int householdId;
}
