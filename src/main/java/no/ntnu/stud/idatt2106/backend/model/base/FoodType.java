package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.Data;

/**
 * Represents a food type in the system.
 *
 * <p>This class contains the name, unit of measurement, caloric value per unit,
 * and an optional image representing the food type.
 * </p>
 */
@Data
public class FoodType {
  private Long id;
  private String name;
  private String unit;
  private Float caloriesPerUnit;
  private byte[] picture;
}
