package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for sharing food.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedFoodRequest {
  private Long foodId;
  private float amount;
  private Long groupId;
}
