package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.request.CreateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;

/**
 * Utility class for mapping different household objects to eachother.
 */
public class HouseholdMapper {

  /**
   * Maps a base Household to a response Household.
   *
   * @param household the reponse Household
   */
  public static HouseholdResponse toResponse(Household household) {
    HouseholdResponse response = new HouseholdResponse();
    response.setId(household.getId());
    response.setName(household.getName());
    response.setAddress(household.getAddress());
    response.setLatitude(household.getLatitude());
    response.setLongitude(household.getLongitude());
    response.setWaterAmountLiters(household.getWaterAmountLiters());
    response.setLastWaterChangeDate(household.getLastWaterChangeDate());
    response.setNextWaterChangeDate(null);
    return response;
  }

  /**
   * Maps a CreateHouseholdRequest to a base Household.
   *
   * @param request the CreateHouseholdRequest
   */
  public static Household toEntity(CreateHouseholdRequest request) {
    Household household = new Household();
    household.setName(request.getName());
    household.setAddress(request.getAddress());
    household.setLatitude(request.getLatitude());
    household.setLongitude(request.getLongitude());
    household.setWaterAmountLiters(request.getWaterAmountLiters());
    household.setLastWaterChangeDate(request.getLastWaterChangeDate());
    return household;
  }
}
