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

  public EmergencyGroup toModel(EmergencyGroupRequest request) {
    return new EmergencyGroup(null, request.getName(), request.getDescription());
  }

  public EmergencyGroupResponse toResponse(EmergencyGroup group) {
    return new EmergencyGroupResponse(group.getId(), group.getName(), group.getDescription());
  }
}
