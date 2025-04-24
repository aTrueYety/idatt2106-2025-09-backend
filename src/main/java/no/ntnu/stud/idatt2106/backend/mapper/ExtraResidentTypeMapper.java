package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;

public class ExtraResidentTypeMapper {

  public static ExtraResidentType toModel(ExtraResidentTypeRequest request) {
    ExtraResidentType type = new ExtraResidentType();
    type.setName(request.getName());
    type.setConsumptionWater(request.getConsumptionWater());
    type.setConsumptionFood(request.getConsumptionFood());
    return type;
  }

  public static ExtraResidentTypeResponse toResponse(ExtraResidentType type) {
    ExtraResidentTypeResponse response = new ExtraResidentTypeResponse();
    response.setId(type.getId());
    response.setName(type.getName());
    response.setConsumptionWater(type.getConsumptionWater());
    response.setConsumptionFood(type.getConsumptionFood());
    return response;
  }
}
