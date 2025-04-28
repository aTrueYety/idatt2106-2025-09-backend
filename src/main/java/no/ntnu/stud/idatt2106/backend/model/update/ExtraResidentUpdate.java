package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.Data;

/**
 * Class representing an update for an extra resident type.
 */
@Data
public class ExtraResidentUpdate {
  private int householdId;
  private int typeId;
}
