package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing shared food between group households.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedFood {
  private SharedFoodKey id;
  private float amount;
}
