package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a composite key for the HouseholdInvite entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdInvite {
  private Long userId;
  private Long householdId;
}
