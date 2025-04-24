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
public class HouseholdRequest {
  private String adress;
  private double longitude;
  private double latitude;
  private double waterAmountLiters;
  private Date lastWaterChangeDate;
  private boolean hasFirstAidKit;
  private Long userId;
}
