package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.Data;

/**
 * Represents a food type response in the system.
 */
@Data
public class FoodTypeResponse {
  private Long id;
  private String name;
  private String unit;
  private double caloriesPerUnit;
  private byte[] picture;
}
