package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;

/**
 * Mapper for converting between FoodType model and its corresponding request/response DTOs.
 */
public class FoodTypeMapper {

    /**
     * Converts a FoodTypeRequest DTO to a FoodType model object.
     *
     * @param request The request DTO.
     * @return The corresponding FoodType model.
     */
    public static FoodType toModel(FoodTypeRequest request) {
      FoodType foodType = new FoodType();
      foodType.setName(request.getName());
      foodType.setUnit(request.getUnit());
      foodType.setCaloriesPerUnit(request.getCaloriesPerUnit());
      foodType.setPicture(request.getPicture());
      return foodType;
    }

    /**
     * Converts a FoodType model object to a FoodTypeResponse DTO.
     *
     * @param foodType The model object.
     * @return The corresponding response DTO.
     */
    public static FoodTypeResponse toResponse(FoodType foodType) {
      FoodTypeResponse response = new FoodTypeResponse();
      response.setId(foodType.getId());
      response.setName(foodType.getName());
      response.setUnit(foodType.getUnit());
      response.setCaloriesPerUnit(foodType.getCaloriesPerUnit());
      response.setPicture(foodType.getPicture()); 
      return response;
    }
}
