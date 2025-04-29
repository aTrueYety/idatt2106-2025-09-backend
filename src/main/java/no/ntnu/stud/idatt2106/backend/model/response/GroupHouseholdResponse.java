package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for a group-household relation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupHouseholdResponse {
  private int id;
  private int householdId;
  private int groupId;
}
