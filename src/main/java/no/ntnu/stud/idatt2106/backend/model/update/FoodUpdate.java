package no.ntnu.stud.idatt2106.backend.model.update;

import java.time.LocalDate;
import lombok.Data;

/**
 * DTO for updating an existing Food.
 */
@Data
public class FoodUpdate {
  private Long typeId;
  private Long householdId;
  private LocalDate expirationDate;
  private double amount;
}
