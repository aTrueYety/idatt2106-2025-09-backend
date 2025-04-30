package no.ntnu.stud.idatt2106.backend.mapper;


import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;

/**
 * Mapper for converting between SharedFood entities and DTOs.
 */
public class SharedFoodMapper {

  public static SharedFood toModel(SharedFoodRequest request) {
    SharedFoodKey key = new SharedFoodKey(request.getFoodId(), request.getGroupHouseholdId());
    return new SharedFood(key, request.getAmount());
  }

  /**
   * Converts a SharedFood entity to a SharedFoodResponse DTO.
   *
   * @param food the SharedFood entity to convert
   * @return the corresponding SharedFoodResponse DTO
   */
  public static SharedFoodResponse toResponse(SharedFood food) {
    int foodId = food.getId().getFoodId();
    int groupHouseholdId = food.getId().getGroupHouseholdId();
    float amount = food.getAmount();

    return new SharedFoodResponse(foodId, groupHouseholdId, amount);
  }
}


