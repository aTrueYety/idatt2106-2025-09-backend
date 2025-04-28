package no.ntnu.stud.idatt2106.backend.model.base;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an extra resident in the system.
 * 
 * <p>Each extra resident is linked to a household and a resident type.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraResident {
  private int id;
  private int householdid;
  private int typeId;
}
