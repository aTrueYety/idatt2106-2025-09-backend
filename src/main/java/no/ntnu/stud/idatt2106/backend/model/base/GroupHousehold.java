package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the relation between a household and an emergency group.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupHousehold {
  private Long id;
  private Long householdId;
  private Long groupId;
}
