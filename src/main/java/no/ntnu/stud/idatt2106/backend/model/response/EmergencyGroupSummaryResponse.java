package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for summarizing emergency group information.
 */
@Data
@AllArgsConstructor
public class EmergencyGroupSummaryResponse {
  private Long groupId;
  private String groupName;
  private String groupDescription;
  private int totalHouseholds;
  private int totalResidents;
  private int totalExtraResidents;
}
