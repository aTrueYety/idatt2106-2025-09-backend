package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides methods related to Households.
 *
 * @version 1.0
 * @Since 23.04.2025
 */
@Service
public class HouseholdService {

  @Autowired
  private HouseholdRepository householdRepository;

  /**
   * Creates and saves a new Household.
   *
   * @param householdReqeust DTO with information about the new household
   */
  public void registerHousehold(HouseholdRequest householdReqeust) {
    Household household = new Household();
    household.setAdress(householdReqeust.getAdress());
    household.setLatitude(householdReqeust.getLatitude());
    household.setLongitude(householdReqeust.getLongitude());
    household.setWaterAmountLiters(householdReqeust.getWaterAmountLiters());
    household.setLastWaterChangeDate(householdReqeust.getLastWaterChangeDate());
    household.setHasFirstAidKit(householdReqeust.isHasFirstAidKit());

    householdRepository.save(household);
  }

  /**
   * Adds a user to an existing household.
   */
  public void addUserToHousehold(Long userId) {

  }
}
