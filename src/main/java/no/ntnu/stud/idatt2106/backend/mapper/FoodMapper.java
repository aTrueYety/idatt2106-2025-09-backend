package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;

public class FoodMapper {

  public static Food toModel(FoodRequest request) {
    Food food = new Food();
    food.setTypeId(request.getTypeId());
    food.setHouseholdId(request.getHouseholdId());
    food.setExpirationDate(request.getExpirationDate());
    food.setAmount(request.getAmount());
    return food;
  }

  public static Food toModel(FoodUpdate update) {
    Food food = new Food();
    food.setTypeId(update.getTypeId());
    food.setHouseholdId(update.getHouseholdId());
    food.setExpirationDate(update.getExpirationDate());
    food.setAmount(update.getAmount());
    return food;
  }

  public static FoodResponse toResponse(Food food) {
    FoodResponse response = new FoodResponse();
    response.setId(food.getId());
    response.setTypeId(food.getTypeId());
    response.setHouseholdId(food.getHouseholdId());
    response.setExpirationDate(food.getExpirationDate());
    response.setAmount(food.getAmount());
    return response;
  }
}
