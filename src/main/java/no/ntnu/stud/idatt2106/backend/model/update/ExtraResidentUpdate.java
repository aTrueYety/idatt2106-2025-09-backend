package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.Data;

/**
 * Class representing an update for an extra resident type.
 */
@Data
public class ExtraResidentUpdate {
  private long householdId;
  private long typeId;
  private String name;
}
