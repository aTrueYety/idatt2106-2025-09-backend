package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or managing a HouseholdKit relation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdKitRequest {
  private Long householdId;
  private Long kitId;
}
