package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating a group-household relation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupHouseholdRequest {

  @NotNull(message = "Household ID is required")
  private Long householdId;

  @NotNull(message = "Group ID is required")
  private Long groupId;
}
