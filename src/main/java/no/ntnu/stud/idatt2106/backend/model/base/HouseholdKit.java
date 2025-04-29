package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a household kit containing various items or utilities.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdKit {
  private Long householdId;
  private Long kitId;
}
