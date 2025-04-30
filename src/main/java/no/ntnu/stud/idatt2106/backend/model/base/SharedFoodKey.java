package no.ntnu.stud.idatt2106.backend.model.base;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Composite key for SharedFood entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedFoodKey implements Serializable {
  private int foodId;
  private int groupHouseholdId;
}
