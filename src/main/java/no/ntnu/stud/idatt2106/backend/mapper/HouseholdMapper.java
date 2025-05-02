package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Household;
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
}
