package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;

/**
 * Utility class for mapping between ExtraResidentType entities and DTOs.
 */
public class ExtraResidentTypeMapper {

  /**
   * Converts an ExtraResidentTypeRequest DTO to an ExtraResidentType entity.
   *
   * @param request the request DTO containing data for a new extra resident type
   * @return the mapped ExtraResidentType entity
   */
  public static ExtraResidentType toModel(ExtraResidentTypeRequest request) {
    ExtraResidentType type = new ExtraResidentType();
    type.setName(request.getName());
    type.setConsumptionWater(request.getConsumptionWater());
    type.setConsumptionFood(request.getConsumptionFood());
    return type;
  }

  /**
   * Converts an ExtraResidentType entity to an ExtraResidentTypeResponse DTO.
   *
   * @param type the ExtraResidentType entity
   * @return the corresponding ExtraResidentTypeResponse DTO
   */
  public static ExtraResidentTypeResponse toResponse(ExtraResidentType type) {
    ExtraResidentTypeResponse response = new ExtraResidentTypeResponse();
    response.setId(type.getId());
    response.setName(type.getName());
    response.setConsumptionWater(type.getConsumptionWater());
    response.setConsumptionFood(type.getConsumptionFood());
    return response;
  }
}
