package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.AddUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;

import java.util.NoSuchElementException;

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

  @Autowired
  private UserService userService;

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
   *
   * @param addUserHouseholdRequest object containing the username of the
   *     user and the id of the household the user is to be added to.
   * @throws NoSuchElementException if no user exists the the specified username,
   *     or no household with the exists with the specified ID
   */
  public void addUserToHousehold(AddUserHouseholdRequest addUserHouseholdRequest) {
    String username = addUserHouseholdRequest.getUsername();
    Long householdId = addUserHouseholdRequest.getHouseholdId();
    
    User user = userService.getUserByUsername(username);

    if (user == null) {
      throw new NoSuchElementException("No user present with username = " + username);
    }
    if (!householdExists(householdId)) {
      throw new NoSuchElementException("No household present with id = " + householdId);
    }

    user.setHouseholdId(householdId);
    userService.updateUserCredentials(user);
  }

  /**
   * Checks if a household with the given ID exists.
   *
   * @param householdId the ID of the household
   * @return true if a household with the ID exists or false otherwise
   */
  public boolean householdExists(Long householdId) {
    return householdRepository.findById(householdId).isPresent();
  }
}
