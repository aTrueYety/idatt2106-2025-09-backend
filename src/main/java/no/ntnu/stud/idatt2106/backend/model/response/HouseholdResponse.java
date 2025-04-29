package no.ntnu.stud.idatt2106.backend.model.response;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for sending Household data to the frontend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdResponse {
  private Long id;
  private String address;
  private double longitude;
  private double latitude;
  private double waterAmountLiters;
  private Date lastWaterChangeDate;
  private List<UserResponse> users;
}
