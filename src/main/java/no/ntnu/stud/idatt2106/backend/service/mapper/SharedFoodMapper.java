package no.ntnu.stud.idatt2106.backend.service.mapper;


import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;

/**
 * Mapper for converting between SharedFood entities and DTOs.
 */
public class SharedFoodMapper {

  /**
   * Creates a SharedFood model from foodId, groupHouseholdId and amount.
   * Used in controller/service after resolving groupHouseholdId.
   */
  public static SharedFood toModel(Long foodId, Long groupHouseholdId, float amount) {
    return new SharedFood(new SharedFoodKey(foodId, groupHouseholdId), amount);
  }


  

  /**
   * Converts a SharedFood entity to a SharedFoodResponse DTO.
   *
   * @param food the SharedFood entity to convert
   * @return the corresponding SharedFoodResponse DTO
   */
  public static SharedFoodResponse toResponse(SharedFood food) {
    return new SharedFoodResponse(
        food.getId().getFoodId(),
        food.getId().getGroupHouseholdId(),
        food.getAmount());
  }
}
