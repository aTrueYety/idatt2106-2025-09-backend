package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object representing an extra resident type.
 *
 * <p>
 * Each extra resident type has a unique ID, a name, and information about
 * how much water and food it consumes on a daily basis.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraResidentTypeResponse {
  private long id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;

}
