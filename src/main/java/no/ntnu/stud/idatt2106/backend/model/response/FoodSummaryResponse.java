package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.Data;

/**
 * Represents a summary of food for a specific type within a household.
 */
@Data
public class FoodSummaryResponse {
  private int typeId;
  private float totalAmount;
}
