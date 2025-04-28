package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.Data;

/**
 * Request object for creating or updating an extra resident.
 */
@Data
public class ExtraResidentRequest {
  private int householdId;
  private int typeId;
  private String name;
}
