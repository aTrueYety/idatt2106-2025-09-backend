package no.ntnu.stud.idatt2106.backend.model.response;

import java.time.LocalDate;
import lombok.Data;

/**
 * DTO for a Food response.
 * This class is used to represent the response of a food item in the system.
 */
@Data
public class FoodResponse {
  private Long id;
  private Long typeId;
  private Long householdId;
  private LocalDate expirationDate;
  private double amount;
}
