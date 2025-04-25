package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;

public class ExtraResidentMapper {


  public static ExtraResident toModel(ExtraResidentRequest request) {
    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(request.getHouseholdId());
    resident.setTypeId(request.getTypeId());
    return resident;
  }
  
  public static ExtraResident toModel(ExtraResidentUpdate update) {
    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(update.getHouseholdId());
    resident.setTypeId(update.getTypeId());
    return resident;
  }

  public static ExtraResidentResponse toResponse(ExtraResident resident) {
    ExtraResidentResponse response = new ExtraResidentResponse();
    response.setId(resident.getId());
    response.setHouseholdId(resident.getHouseholdid());
    response.setTypeId(resident.getTypeId());
    return response;
  }
}
