package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between EmergencyGroupRequest, 
 * EmergencyGroup, and EmergencyGroupResponse.
 */
@Component
public class EmergencyGroupMapper {

  /**
   * Converts a EmergencyGroupRequest DTO to an EmergencyGroup entity.
   *
   * @param request the request DTO containing emergency group data
   * @return the mapped EmergencyGroup entity
   */
  public static EmergencyGroup toModel(EmergencyGroupRequest request) {
    return new EmergencyGroup(null, request.getName(), request.getDescription());
  }

  /**
   * Converts a EmergencyGroup entity to a response DTO.
   *
   * @param group the EmergencyGroup entity
   * @return the mapped EmergencyGroup response DTO
   */
  public static EmergencyGroupResponse toResponse(EmergencyGroup group) {
    return new EmergencyGroupResponse(group.getId(), group.getName(), group.getDescription());
  }
}
