package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.mapper.HouseholdMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.AddUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides methods related to Households.
 */
@Service
public class HouseholdService {

  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private UserService userService;

  /**
   * Returns all registered households as a list of HouseholdResponse objects.
   *
   * @return list of all households as HouseholdResponse objects
   */
  public List<HouseholdResponse> getAll() {
    return householdRepository.findAll().stream()
      .map(HouseholdMapper::toResponse)
      .toList();
  }

  /**
   * Returns a HuseholdResponse of the Household with the specified ID.
   *
   * @param id the ID of the household to be retrieved
   * @return HouseholdResponse with the household with the specified ID
   * @throws NoSuchElementException if there is no registered Household with the specified id
   */
  public HouseholdResponse getById(Long id) {   
    return householdRepository.findById(id).map(HouseholdMapper::toResponse)
        .orElseThrow(() -> new NoSuchElementException("Household with ID = " + id + " not found"));
  }

  /**
   * Returns a HouseholdResponse of the Household the user is a part of.
   *
   * @param id the ID of the user to get the household of
   * @return HouseholdResponse with the household the user with the id is a part of
   */
  public HouseholdResponse getByUserId(Long id) {
    User user = userService.getUserById(id);
    
    if (user == null) {
      throw new NoSuchElementException("User with ID = " + id + " not found");
    }

    Long householdId = user.getHouseholdId();

    if (householdId == null) {
      throw new IllegalArgumentException("User with ID = " + id + " is not in a household");
    }

    return householdRepository.findById(householdId).map(HouseholdMapper::toResponse).get();
  }

  /**
   * Creates and saves a new Household.
   *
   * @param householdReqeust DTO with information about the new household
   */
  public void registerHousehold(HouseholdRequest householdReqeust) {
    Validate.that(householdReqeust.getLongitude(),
        Validate.isNotNull(), "Longitude cannot be null");
    Validate.that(householdReqeust.getLatitude(),
        Validate.isNotNull(), "Latitude cannot be null");

    Household household = new Household();
    household.setAdress(householdReqeust.getAdress());
    household.setLatitude(householdReqeust.getLatitude());
    household.setLongitude(householdReqeust.getLongitude());
    household.setWaterAmountLiters(householdReqeust.getWaterAmountLiters());
    household.setLastWaterChangeDate(householdReqeust.getLastWaterChangeDate());

    Household registeredHousehold = householdRepository.save(household);

    //Adds the user creating the household to the household.
    addUserToHousehold(new AddUserHouseholdRequest(
        householdReqeust.getUsername(),
        registeredHousehold.getId())
    );
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
      throw new NoSuchElementException("User with username = " + username + " not found");
    }
    if (!householdExists(householdId)) {
      throw new NoSuchElementException("Household with ID = " + householdId + " not found");
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

  /**
   * Updates the household with the given ID.
   *
   * @param id the ID of the household to be updated
   * @param request the new household values, request null values are not changed
   * @return response object with the updated values
   */
  public HouseholdResponse updateHousehold(Long id, HouseholdRequest request) {
    Optional<Household> existingHousehold = householdRepository.findById(id);

    Validate.that(existingHousehold.isPresent(),
        Validate.isTrue(), "Household with ID = " + id + " not found");

    Household validatedHousehold = existingHousehold.get();

    if (request.getAdress() != null) {
      validatedHousehold.setAdress(request.getAdress());
    }

    if (request.getLatitude() != null) {
      validatedHousehold.setLatitude(request.getLatitude());
    }

    if (request.getLongitude() != null) {
      validatedHousehold.setLongitude(request.getLongitude());
    }

    if (request.getWaterAmountLiters() != null) {
      validatedHousehold.setWaterAmountLiters(request.getWaterAmountLiters());
    }

    if (request.getLastWaterChangeDate() != null) {
      validatedHousehold.setLastWaterChangeDate(request.getLastWaterChangeDate());
    }

    householdRepository.update(validatedHousehold);
    return HouseholdMapper.toResponse(validatedHousehold);
  }

  /**
   * Retrieves all the user members of the household with the given ID.
   *
   * @param id the ID of the household to get members from
   * @return the members of the household mapped to response objects
   */
  public List<UserResponse> getMembers(Long id) {
    return userService.getUsersByHouseholdId(id);
  }
}
