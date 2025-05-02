package no.ntnu.stud.idatt2106.backend.model.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating a new household.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHouseholdRequest {
  private String address;
  private String name;
  private Double longitude;
  private Double latitude;
  private Double waterAmountLiters;
  private Date lastWaterChangeDate;
  private String username;
}
