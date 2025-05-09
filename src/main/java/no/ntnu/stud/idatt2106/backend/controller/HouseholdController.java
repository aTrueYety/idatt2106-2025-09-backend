package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.CreateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.HouseHoldInviteAcceptRequest;
import no.ntnu.stud.idatt2106.backend.model.request.InviteUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.UpdateHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.service.HouseholdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for operations related to Households,
 * like registering a new household or adding users to an existing household.
 */
@Tag(name = "Households", description = "Endpoints for household operations")
@RestController
@RequestMapping("api/households")
public class HouseholdController {
  private static final Logger logger = LoggerFactory.getLogger(HouseholdController.class);

  @Autowired
  private HouseholdService householdService;

  /**
   * Returns all of the registered households as HouseholdResponses.
   *
   * @return the registered households with a status code
   */
  @Operation(summary = "Retrieve all registered households",
       description = "Retrieves information about all of the registered households")
  @GetMapping
  public ResponseEntity<List<HouseholdResponse>> getAll() {
    List<HouseholdResponse> households = householdService.getAll();
    logger.info("Retrieved all households successfully");
    return ResponseEntity.ok().body(households);
  }

  /**
   * Handles request to register a new household.
   *
   * @param householdRequest the request containing the details of the household
   * @return a ResponseEntity containing the registration response or an error
   *         message.
   */
  @Operation(summary = "Creates a new household",
         description = "Creates a new household with the user creating it as a member")
  @PostMapping("/register")
  public ResponseEntity<Void> 
      registerHousehold(@RequestBody CreateHouseholdRequest householdRequest,
      @RequestHeader("Authorization") String token) {
    householdService.registerHousehold(householdRequest, token);
    logger.info("Household created successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to update existing household.
   *
   * @param id      the id of the household to be updated
   * @param request the new household info
   * @return response object with the updated household
   */
  @Operation(summary = "Updates an existing household", description = """
      Updates the household with the given ID. If no household is registered
      with the given ID a BAD_REQUEST response code is returned. If values in the request
      body are null they will not be updated.
      """)
  @PutMapping("/{id}") //TODO check if user is in household
  public ResponseEntity<HouseholdResponse> updateHousehold(@PathVariable Long id,
      @RequestBody UpdateHouseholdRequest request) {
    logger.info("Updating household with ID = {}", id);
    HouseholdResponse response = householdService.updateHousehold(id, request);
    logger.info("Household with ID = {} updated", id);
    return ResponseEntity.ok(response);
  }

  /**
   * Handles request to invite a user to a household.
   *
   * @param request object with the id of the household and of the user to be
   *                added
   * @return a ResponseEntity with the response to the operation or an error
   *         message
   */
  @Operation(summary = "Invites a user to a household", description = """
      Invites a user to a household. The user will be added to the household
      if they accept the invitation, sent by email.
      """)
  @PostMapping("invite-user")
  public ResponseEntity<Void> inviteUserToHousehold(
      @RequestBody InviteUserHouseholdRequest request,
      @RequestHeader("Authorization") String token) {
    householdService.inviteUserToHousehold(request, token);
    logger.info("User invited to household successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to accept a household invite.
   *
   * @param request object with the id of the household to be joined
   * @param token   the jwt token of the user
   * @return a ResponseEntity with the response to the operation or an error
   *         message
   */
  @Operation(
      summary = "Accepts a invite to a household",
      description = "Updates the household of the user accepting the invite."
  )
  @PostMapping("accept")
  public ResponseEntity<Void> acceptHouseholdInvite(
      @RequestBody HouseHoldInviteAcceptRequest request,
      @RequestHeader("Authorization") String token) {
    householdService.acceptHouseholdInvite(request, token);
    logger.info("User accepted household invite successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to reject a household invite.
   *
   * @param token the jwt token of the user
   * @param request object with the id of the household to be rejected
   * @return a ResponseEntity with the response to the operation or an error
   *         message
   */
  @Operation(summary = "Rejects a household invite", description = """
      Rejects a household invite. The user will not be added to the household.
      """)
  @PostMapping("reject")
  public ResponseEntity<Void> rejectHouseholdInvite(
      @RequestBody HouseHoldInviteAcceptRequest request,
      @RequestHeader("Authorization") String token) {
    householdService.rejectHouseholdInvite(request, token);
    logger.info("User rejected household invite successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to leave a household.
   *
   * @return a ResponseEntity with the response to the operation or an error
   */
  @Operation(summary = "Leaves the household of the currently logged in user",
      description = """
          Removes the currently logged in user from their household.
          """)
  @PostMapping("leave")
  public ResponseEntity<Void> leaveHousehold(
      @RequestHeader("Authorization") String token) {
    householdService.leaveHousehold(token);
    logger.info("User left household successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to get the Household with a given ID.
   *
   * @param id the ID off the household to be retrieved
   * @return a ResponseEntity with the retrieved Household
   */
  @Operation(summary = "Retrieves the household with the given ID", description = """
      Retrieves information about the household with the specified ID
      if it exists.
      """)
  @GetMapping("/{id}")
  public ResponseEntity<HouseholdResponse> getById(@PathVariable Long id) {
    HouseholdResponse response = householdService.getByIdWithPreparedness(id);
    logger.info("Retrieved household successfully");
    return ResponseEntity.ok().body(response);
  }

  /**
   * Handles request to get all of the users in a Household.
   *
   * @param householdId the ID of the household to retrieve users from
   * @return a ResponseEntity with the retrieved users
   */
  @Operation(summary = "Retrieves all users from the household with the given ID", description = """
      Retrieves public user information like username, email, name, and profile picture
      of all of the users in the household with the specified ID.
      """)
  @GetMapping("/{householdId}/users")
  public ResponseEntity<List<UserResponse>> getHouseholdMembers(@PathVariable Long householdId) {
    logger.info("Fetching user members for household with ID: {}", householdId);
    List<UserResponse> response = householdService.getMembers(householdId);
    logger.info("Found {} members for household ID: {}", response.size(), householdId);
    return ResponseEntity.ok(response);
  }

  /**
   * Handles request to get the household of the current user.
   *
   * @param token the jwt token of the user
   * @return a ResponseEntity with the retrieved household
   */
  @Operation(
      summary = "Retrieves the household of the currently logged in user",
      description = """
          Retrieves information about the household the current user is a part of.
          """
  )
  @GetMapping("/my-household")
  public ResponseEntity<HouseholdResponse> getCurrentUserHousehold(
      @RequestHeader("Authorization") String token) {
    logger.info("Fetching household of authenticated user");
    HouseholdResponse response = householdService.getByUserId(token);
    if (response == null) {
      logger.warn("No household found for user with token: {}", token);
      return ResponseEntity.notFound().build();
    }
    logger.info("Found household of user with token: {}", token);
    return ResponseEntity.ok(response);
  }
}
