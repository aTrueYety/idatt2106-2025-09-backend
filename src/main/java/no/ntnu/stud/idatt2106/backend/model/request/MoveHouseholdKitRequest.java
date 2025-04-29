package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for moving a kit from one household to another.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveHouseholdKitRequest {
  private Long oldHouseholdId;
  private Long kitId;
  private Long newHouseholdId;
}
