package no.ntnu.stud.idatt2106.backend.model.request;

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
    private int householdId;
    private int groupId;
}
