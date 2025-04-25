package no.ntnu.stud.idatt2106.backend.model.base;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraResident {
  private int id;
  private int householdid;
  private int typeId;
}
