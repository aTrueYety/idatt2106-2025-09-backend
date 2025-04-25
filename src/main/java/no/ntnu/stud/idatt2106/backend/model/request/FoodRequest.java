package no.ntnu.stud.idatt2106.backend.model.request;

import java.time.LocalDate;

/**
 * Request DTO for creating a new food item.
 * <p>
 * Contains the type ID, household ID, expiration date, and amount of the food.
 * </p>
 */
public class FoodRequest {
  private int typeId;
  public int householdId;
  private LocalDate expirationDate;
  private int amount;

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

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
