package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.Data;

/**
 * Response object representing an extra resident.
 *
 * <p>
 * Contains information about the resident's ID, the household they belong to,
 * and the type of extra resident.
 * </p>
 */
@Data
public class ExtraResidentResponse {
  private long id;
  private long householdId;
  private long typeId;
  private String name;
}
