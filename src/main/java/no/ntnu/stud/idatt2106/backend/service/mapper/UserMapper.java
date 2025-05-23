package no.ntnu.stud.idatt2106.backend.service.mapper;

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
    response.setHouseholdId(user.getHouseholdId());
    response.setEmail(user.getEmail());
    response.setEmailConfirmed(user.isEmailConfirmed());
    response.setUsername(user.getUsername());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setSharePositionHousehold(user.isSharePositionHousehold());
    response.setSharePositionGroup(user.isSharePositionGroup());
    response.setAdmin(user.isAdmin());
    response.setSuperAdmin(user.isSuperAdmin());
    return response;
  }
}
