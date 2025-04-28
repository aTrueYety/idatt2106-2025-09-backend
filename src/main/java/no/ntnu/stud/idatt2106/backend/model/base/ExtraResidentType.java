package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity representing an extra resident type.
 * 
 * <p>Each type specifies the resource consumption (water and food) per resident.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraResidentType {
  private int id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;
}
