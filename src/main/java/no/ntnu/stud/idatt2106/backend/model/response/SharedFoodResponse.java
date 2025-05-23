package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for shared food.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedFoodResponse {
  private Long foodId;
  private Long groupHouseholdId;
  private float amount;
 
}
