package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a group invitation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInvite {
  private Long groupId;
  private Long householdId;
}
