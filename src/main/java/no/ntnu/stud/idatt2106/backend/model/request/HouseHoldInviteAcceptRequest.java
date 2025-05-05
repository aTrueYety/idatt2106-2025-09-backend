package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for accepting a household invite.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseHoldInviteAcceptRequest {
  private Long householdId;
}
