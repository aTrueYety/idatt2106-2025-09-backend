package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;

/**
 * Utility class for mapping between User entities and DTOs.
 */
public class UserMapper {
  
  /**
   * Converts a User entity to a response DTO.
   *
   * @param user the model object
   * @return the corresponding response DTO
   */
  public static UserResponse toResponse(User user) {
    UserResponse response = new UserResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setEmail(user.getEmail());
    response.setPicture(user.getPicture());
    response.setHouseholdId(user.getHouseholdId());

    return response;
  }
}
