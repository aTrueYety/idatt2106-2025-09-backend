package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;

/**
 * Mapper for converting between GroupHousehold entities and DTOs.
 */
public class GroupHouseholdMapper {
  /**
   * Converts a GroupHouseholdRequest DTO to a GroupHousehold entity.
   *
   * @param request the GroupHouseholdRequest DTO to convert
   * @return the corresponding GroupHousehold entity
   */
  public static GroupHousehold toModel(GroupHouseholdRequest request) {
    return new GroupHousehold(null, request.getHouseholdId(), request.getGroupId());
  }

  /**
   * Converts a GroupHousehold entity to a GroupHouseholdResponse DTO.
   *
   * @param groupHousehold the GroupHousehold entity to convert
   * @return the corresponding GroupHouseholdResponse DTO
   */
  public static GroupHouseholdResponse toResponse(
      GroupHousehold groupHousehold) {
    return new GroupHouseholdResponse(
        groupHousehold.getId(),
        groupHousehold.getHouseholdId(),
        groupHousehold.getGroupId());
  }
}
