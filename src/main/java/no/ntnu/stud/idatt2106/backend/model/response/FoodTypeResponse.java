package no.ntnu.stud.idatt2106.backend.model.response;

/**
 * Represents a food type response in the system.
 */
public class FoodTypeResponse {
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
}
