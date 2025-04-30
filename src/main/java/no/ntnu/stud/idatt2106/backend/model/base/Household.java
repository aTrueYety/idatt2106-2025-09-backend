package no.ntnu.stud.idatt2106.backend.model.base;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a HouseHold.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Household {
  private Long id;
  private String address;
  private double longitude;
  private double latitude;
  private double waterAmountLiters;
  private Date lastWaterChangeDate;
}