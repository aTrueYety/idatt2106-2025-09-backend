package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.Data;

/**
 * Response object representing an extra resident type.
 *
 * <p>Each extra resident type has a unique ID, a name, and information about
 * how much water and food it consumes on a daily basis.</p>
 */
@Data
public class ExtraResidentTypeResponse {
  private int id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;

  
}
