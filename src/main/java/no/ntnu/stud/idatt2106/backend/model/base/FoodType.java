package no.ntnu.stud.idatt2106.backend.model.base;

/**
 * Represents a food type in the system.
 *
 * <p>This class contains the name, unit of measurement, caloric value per unit,
 * and an optional image representing the food type.
 * </p>
 */
public class FoodType {
  private int id;
  private String name;
  private String unit;
  private Float caloriesPerUnit;
  private byte[] picture;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Float getCaloriesPerUnit() {
    return caloriesPerUnit;
  }

  public void setCaloriesPerUnit(Float caloriesPerUnit) {
    this.caloriesPerUnit = caloriesPerUnit;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  @Override
  public String toString() {
    return "FoodType{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", unit='" + unit + '\''
        + ", caloriesPerUnit=" + caloriesPerUnit
        + '}';
  }
}
