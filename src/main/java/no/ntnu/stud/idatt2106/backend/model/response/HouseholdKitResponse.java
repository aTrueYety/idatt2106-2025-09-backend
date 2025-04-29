package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for returning HouseholdKit relations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdKitResponse {
  private Long householdId;
  private Long kitId;
}
