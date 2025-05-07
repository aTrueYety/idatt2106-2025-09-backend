package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;


/**
 * Mapper class for converting between HouseholdKit entities and DTOs.
 */
public class HouseholdKitMapper {

  private HouseholdKitMapper() {
    // Private constructor to prevent instantiation
  }

  /**
   * Maps a HouseholdKitRequest to a HouseholdKit entity.
   *
   * @param request the HouseholdKitRequest object
   * @return the corresponding HouseholdKit entity
   */
  public static HouseholdKit toEntity(HouseholdKitRequest request) {
    return new HouseholdKit(request.getHouseholdId(), request.getKitId());
  }

  /**
   * Maps a HouseholdKit entity to a HouseholdKitResponse.
   *
   * @param householdKit the HouseholdKit entity
   * @return the corresponding HouseholdKitResponse object
   */
  public static HouseholdKitResponse toResponse(HouseholdKit householdKit) {
    return new HouseholdKitResponse(householdKit.getHouseholdId(), householdKit.getKitId());
  }
}
