package no.ntnu.stud.idatt2106.backend.model.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for updating an existing household.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHouseholdRequest {
  private String address;
  private Double longitude;
  private Double latitude;
  private Double waterAmountLiters;
  private Date lastWaterChangeDate;
}
