package no.ntnu.stud.idatt2106.backend.model.response;

import java.time.LocalDate;

public class FoodResponse {
    private int id;
    private int typeId;
    private int householdId;
    private LocalDate expirationDate;
    private int amount;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public int getHouseholdId() { return householdId; }
    public void setHouseholdId(int householdId) { this.householdId = householdId; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
