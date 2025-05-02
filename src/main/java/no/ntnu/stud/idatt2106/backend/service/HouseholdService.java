package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.mapper.HouseholdMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.CreateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.InviteUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.UpdateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.util.EmailTemplates;
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
  @Autowired
  private JwtService jwtService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private HouseholdInviteService householdInviteService;
  @Autowired
  private LevelOfPreparednessService levelOfPreparednessService;

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
   * @throws NoSuchElementException if there is no registered Household with the
   *                                specified id
   */
  public HouseholdResponse getByIdWithPreparedness(Long id) {
    HouseholdResponse response = householdRepository.findById(id).map(HouseholdMapper::toResponse)
        .orElseThrow(() -> new NoSuchElementException("Household with ID = " + id + " not found"));
    response.setLevelOfPreparedness(levelOfPreparednessService
        .getPreparednessForHousehold(getById(id)));
    return response;
  }

  /**
   * Retrieves a Household by its ID.
   *
   * @param id the ID of the household to be retrieved
   * @return the Household object with the specified ID
   */
  public HouseholdResponse getById(Long id) {
    return householdRepository.findById(id)
        .map(HouseholdMapper::toResponse)
        .orElseThrow(() -> new NoSuchElementException("Household with ID = " + id + " not found"));
  }

  /**
   * Returns a HouseholdResponse of the Household the user is a part of.
   *
   * @param id the ID of the user to get the household of
   * @return HouseholdResponse with the household the user with the id is a part
   *         of
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

    HouseholdResponse householdResponse = householdRepository.findById(householdId)
        .map(HouseholdMapper::toResponse).get();
    householdResponse.setLevelOfPreparedness(levelOfPreparednessService
        .getPreparednessForHousehold(getById(householdId)));
    return householdResponse;
  }

  /**
   * Creates and saves a new Household.
   *
   * @param householdReqeust DTO with information about the new household
   */
  public void registerHousehold(CreateHouseholdRequest householdReqeust) {
    Validate.that(householdReqeust.getLongitude(),
        Validate.isNotNull(), "Longitude cannot be null");
    Validate.that(householdReqeust.getLatitude(),
        Validate.isNotNull(), "Latitude cannot be null");

    Household household = new Household();
    household.setAddress(householdReqeust.getAddress());
    household.setLatitude(householdReqeust.getLatitude());
    household.setLongitude(householdReqeust.getLongitude());
    household.setWaterAmountLiters(householdReqeust.getWaterAmountLiters());
    household.setLastWaterChangeDate(householdReqeust.getLastWaterChangeDate());

    Household registeredHousehold = householdRepository.save(household);

    // Adds the user creating the household to the household.
    addUserToHousehold(
        householdReqeust.getUsername(),
        registeredHousehold.getId());
  }

  /**
   * Adds a user to an existing household.
   *
   * @param username    the username of the user to be added
   * @param householdId the ID of the household to add the user to
   * @throws NoSuchElementException if no user exists the the specified username,
   *                                or no household with the exists with the
   *                                specified ID
   */
  public void addUserToHousehold(String username, Long householdId) {
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
   * Invites a user to a household by sending an email and generating a invite
   * link.
   *
   * @param inviteRequest the request containing the user ID and household ID
   * @param token         the JWT token for authorization
   */
  public void inviteUserToHousehold(InviteUserHouseholdRequest inviteRequest, String token) {
    Long senderHouseholdId = userService.getUserById(
        jwtService.extractUserId(token.substring(7)))
        .getHouseholdId();
    Validate.that(senderHouseholdId,
        Validate.isNotNull(), "You are not in a household");
    Household household = householdRepository.findById(senderHouseholdId).get();
    Validate.that(household,
        Validate.isNotNull(), "Household with id = " + senderHouseholdId + " not found");
    User user = userService.getUserByUsername(inviteRequest.getUsername());
    Validate.that(user,
        Validate.isNotNull(), "User with username = " + inviteRequest.getUsername() + " not found");

    String inviteKey = householdInviteService.createHouseholdInvite(
        household.getId(), user.getId());

    try {
      emailService.sendHtmlEmail(
          user.getEmail(),
          "Du har blitt invitert til å bli med i en husstand",
          EmailTemplates.getHouseholdInviteTemplate(household.getAddress(), inviteKey));
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }

  /**
   * Accepts a household invite by updating the user's household ID and
   * deleting the invite key.
   *
   * @param inviteKey the key of the household invite to be accepted
   */
  public void acceptHouseholdInvite(String inviteKey) {
    Validate.that(inviteKey,
        Validate.isNotBlankOrNull(), "Invite key cannot be null");

    HouseholdInvite invite = householdInviteService.findByKey(inviteKey);
    Validate.that(invite,
        Validate.isNotNull(), "No invite with key = " + inviteKey + " found");

    User user = userService.getUserById(invite.getUserId());
    Long oldHouseholdId = user.getHouseholdId();
    user.setHouseholdId(invite.getHouseholdId());
    userService.updateUserCredentials(user);

    if (getMembers(oldHouseholdId).size() == 0) {
      householdRepository.deleteById(oldHouseholdId);
    }

    householdInviteService.deleteInvite(inviteKey);
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
   * @param id      the ID of the household to be updated
   * @param request the new household values, request null values are not changed
   * @return response object with the updated values
   */
  public HouseholdResponse updateHousehold(Long id, UpdateHouseholdRequest request) {
    Optional<Household> existingHousehold = householdRepository.findById(id);

    Validate.that(existingHousehold.isPresent(),
        Validate.isTrue(), "Household with ID = " + id + " not found");

    Household validatedHousehold = existingHousehold.get();

    if (request.getAddress() != null) {
      validatedHousehold.setAddress(request.getAddress());
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

  /**
   * Gets the amount of water in liters for a given household.
   *
   * @param householdId the ID of the household to retrieve the water amount for
   * @return the amount of water in liters for the specified household
   */
  public double getWaterAmount(Long householdId) {
    return householdRepository.findById(householdId)
        .orElseThrow(() -> new NoSuchElementException("No household with id = " + householdId))
        .getWaterAmountLiters();
  }
}
