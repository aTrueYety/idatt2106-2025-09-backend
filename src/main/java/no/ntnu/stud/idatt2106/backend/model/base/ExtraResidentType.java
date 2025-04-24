package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraResidentType {
  private int id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;
}
