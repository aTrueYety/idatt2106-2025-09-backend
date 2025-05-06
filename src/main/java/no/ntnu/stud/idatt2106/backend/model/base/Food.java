package no.ntnu.stud.idatt2106.backend.model.base;

import java.time.LocalDate;
import lombok.Data;

/**
 * Response object representing a food item.
 * 
 * <p>This DTO is used for transferring information about food entries
 * between backend and frontend, such as when listing or fetching food data.
 * </p>
 *
 * <p>Each food item is associated with a type, belongs to a household,
 * has an expiration date, and a specified amount.
 * </p>
 */
@Data
public class Food {
  private Long id;
  private Long typeId;
  private Long householdId;
  private LocalDate expirationDate;
  private double amount;
}
