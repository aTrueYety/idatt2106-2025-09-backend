package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to add a user to a household.
 * This class contains the username of the user to be added and the ID of the household.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserHouseholdRequest {
  private String username;
  private Long householdId;
}
