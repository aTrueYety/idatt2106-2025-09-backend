package no.ntnu.stud.idatt2106.backend.model.request;

public class ExtraResidentTypeRequest {
  private String name;
  private float consumptionWater;
  private float consumptionFood;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getConsumptionWater() {
    return consumptionWater;
  }

  public void setConsumptionWater(float consumptionWater) {
    this.consumptionWater = consumptionWater;
  }

  public float getConsumptionFood() {
    return consumptionFood;
  }

  public void setConsumptionFood(float consumptionFood) {
    this.consumptionFood = consumptionFood;
  }
}
