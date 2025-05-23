package no.ntnu.stud.idatt2106.backend.model.response;

import java.util.List;
import lombok.Data;

/**
 * Represents a summary of all food of a given type in a household,
 * including total amount and expiration-based batches.
 */
@Data
public class FoodDetailedResponse {
  private Long typeId;
  private String typeName;
  private String unit;
  private double totalAmount;
  private double totalCalories;
  private List<FoodBatchResponse> batches;
}
