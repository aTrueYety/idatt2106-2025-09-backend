package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import org.springframework.stereotype.Component;

@Component
public class ExtraResidentMapper {

  public ExtraResident toEntity(ExtraResidentRequest request) {
    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(request.getHouseholdId());
    resident.setTypeId(request.getTypeId());
    return resident;
  }

  public ExtraResidentResponse toResponse(ExtraResident resident) {
    ExtraResidentResponse response = new ExtraResidentResponse();
    response.setId(resident.getId());
    response.setHouseholdId(resident.getHouseholdid());
    response.setTypeId(resident.getTypeId());
    return response;
  }
}
