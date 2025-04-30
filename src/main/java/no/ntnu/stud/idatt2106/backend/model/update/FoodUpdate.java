package no.ntnu.stud.idatt2106.backend.model.update;

import java.time.LocalDate;

/**
 * DTO for updating an existing Food.
 */
public class FoodUpdate {
  private int typeId;
  private int householdId;
  private LocalDate expirationDate;
  private float amount;

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public int getHouseholdId() {
    return householdId;
  }

  public void setHouseholdId(int householdId) {
    this.householdId = householdId;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }
}
