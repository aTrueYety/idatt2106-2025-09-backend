package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;

/**
 * Utility class for converting between ExtraResident entities and their corresponding DTOs.
 */
public class ExtraResidentMapper {

  /**
   * Converts an ExtraResidentRequest DTO to an ExtraResident entity.
   *
   * @param request the request DTO containing extra resident data
   * @return the mapped ExtraResident entity
   */
  public static ExtraResident toModel(ExtraResidentRequest request) {
    ExtraResident resident = new ExtraResident();
    resident.setHouseholdId(Long.valueOf(request.getHouseholdId()));
    resident.setTypeId(Long.valueOf(request.getTypeId()));
    resident.setName(request.getName());
    return resident;
  }

  /**
   * Converts an ExtraResidentUpdate DTO to an ExtraResident entity.
   *
   * @param update the update DTO containing extra resident data
   * @return the mapped ExtraResident entity
   */
  public static ExtraResident toModel(ExtraResidentUpdate update) {
    ExtraResident resident = new ExtraResident();
    resident.setHouseholdId(update.getHouseholdId());
    resident.setTypeId(update.getTypeId());
    resident.setName(update.getName());
    return resident;
  }

  /**
   * Converts an ExtraResident entity to an ExtraResidentResponse DTO.
   *
   * @param resident the ExtraResident entity
   * @return the corresponding ExtraResidentResponse DTO
   */
  public static ExtraResidentResponse toResponse(ExtraResident resident) {
    ExtraResidentResponse response = new ExtraResidentResponse();
    response.setId(resident.getId());
    response.setHouseholdId(resident.getHouseholdId());
    response.setTypeId(resident.getTypeId());
    response.setName(resident.getName());
    return response;
  }
}
