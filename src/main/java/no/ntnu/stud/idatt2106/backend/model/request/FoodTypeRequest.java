package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.Data;

/**
 * Request object for creating or updating a food type.
 * <p>
 * This class includes the name, unit of measurement, caloric value per unit,
 * and an optional image representing the food type.
 * </p>
 */
@Data
public class FoodTypeRequest {
  private String name;
  private String unit;
  private Float caloriesPerUnit;
  private byte[] picture;

}
