package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.Data;

/**
 * Request object for creating or updating an extra resident type.
 *
 * <p>This class contains the necessary data to define a resident type
 * with specific water and food consumption values.
 * </p>
 */
@Data
public class ExtraResidentTypeRequest {
  private String name;
  private float consumptionWater;
  private float consumptionFood;


}
