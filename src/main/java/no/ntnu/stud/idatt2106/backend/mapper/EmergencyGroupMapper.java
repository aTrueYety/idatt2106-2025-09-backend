package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;

/**
 * Mapper for converting between EmergencyGroup entities and DTOs.
 */
public class EmergencyGroupMapper {

  public static EmergencyGroup toModel(EmergencyGroupRequest request) {
    return new EmergencyGroup(0, request.getName(), request.getDescription());
  }

  public static EmergencyGroupResponse toResponse(EmergencyGroup group) {
    return new EmergencyGroupResponse(group.getId(), group.getName(), group.getDescription());
  }
}
