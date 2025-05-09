package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodBatchResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;

/**
 * Utility class for mapping between Food entities and their corresponding DTOs.
 */
public class FoodMapper {

  /**
   * Converts a FoodRequest DTO to a Food entity.
   *
   * @param request the request DTO containing data for creating a food item
   * @return the mapped Food entity
   */
  public static Food toModel(FoodRequest request) {
    Food food = new Food();
    food.setTypeId(request.getTypeId());
    food.setHouseholdId(request.getHouseholdId());
    food.setExpirationDate(request.getExpirationDate());
    food.setAmount(request.getAmount());
    return food;
  }

  /**
   * Converts a FoodUpdate DTO to a Food entity.
   *
   * @param update the update DTO containing updated food data
   * @return the mapped Food entity
   */
  public static Food toModel(FoodUpdate update) {
    Food food = new Food();
    food.setTypeId(update.getTypeId());
    food.setHouseholdId(update.getHouseholdId());
    food.setExpirationDate(update.getExpirationDate());
    food.setAmount(update.getAmount());
    return food;
  }

  /**
   * Converts a Food entity to a FoodResponse DTO.
   *
   * @param food the Food entity
   * @return the corresponding FoodResponse DTO
   */
  public static FoodResponse toResponse(Food food) {
    FoodResponse response = new FoodResponse();
    response.setId(food.getId());
    response.setTypeId(food.getTypeId());
    response.setHouseholdId(food.getHouseholdId());
    response.setExpirationDate(food.getExpirationDate());
    response.setAmount(food.getAmount());
    return response;
  }

  /**
   * Converts a Food entity to a BatchResponse DTO.
   * <p>This method is used for creating batch summaries with ID, amount, and expiration date.</p>
   *
   * @param food the Food entity
   * @return the corresponding BatchResponse DTO
   */
  public static FoodBatchResponse toBatchResponse(Food food) {
    FoodBatchResponse foodBatchResponse = new FoodBatchResponse();
    foodBatchResponse.setId(food.getId());
    foodBatchResponse.setAmount(food.getAmount());
    foodBatchResponse.setExpirationDate(food.getExpirationDate());
    return foodBatchResponse;
  }
}
