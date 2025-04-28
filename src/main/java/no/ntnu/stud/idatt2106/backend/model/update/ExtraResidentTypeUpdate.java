package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.Data;

/**
 * Class representing an update for an extra resident type.
 */
@Data
public class ExtraResidentTypeUpdate {
  
  private int id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;


}
