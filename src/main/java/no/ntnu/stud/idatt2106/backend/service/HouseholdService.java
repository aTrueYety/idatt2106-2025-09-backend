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
   * @param createHouseholdDTO DTO with information about the new household
   */
  public void registerHousehold(HouseholdRequest createHouseholdDTO) {
    Household household = new Household();
    household.setAdress(createHouseholdDTO.getAdress());
    household.setLatitude(createHouseholdDTO.getLatitude());
    household.setLongitude(createHouseholdDTO.getLongitude());
    household.setWaterAmountLiters(createHouseholdDTO.getWaterAmountLiters());
    household.setLastWaterChangeDate(createHouseholdDTO.getLastWaterChangeDate());
    household.setHasFirstAidKit(createHouseholdDTO.isHasFirstAidKit());

    householdRepository.save(household);
  }
}
