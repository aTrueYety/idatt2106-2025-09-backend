package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.AddUserHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import no.ntnu.stud.idatt2106.backend.service.HouseholdService;
import no.ntnu.stud.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @Autowired
  private UserService userService;

  /**
   * Returns all of the registered households as HouseholdResponses.
   *
   * @return the registered households with a status code
   */
  @Operation(summary = "Retrieve all registered households", description = "Retrieves information about all of the registered households")
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
  @Operation(summary = "Creates a new household", description = "Creates a new household with the user creating it as a member")
  @PostMapping("/register")
  public ResponseEntity<?> registerHousehold(@RequestBody HouseholdRequest householdRequest) {
    householdService.registerHousehold(householdRequest);
    logger.info("Household created successfully");
    return ResponseEntity.ok().build();
  }

  /**
   * Handles request to add a user to a household.
   *
   * @param request object with the id of the household and the username of the
   *                user to be added
   * @return a ResponseEntity with the response to the operation or an error
   *         message
   */
  @Operation(summary = "Adds a user to a household", description = "Adds the user with the specified username to the "
      + "household with the specified ID")
  @PostMapping("add-user")
  public ResponseEntity<?> addUserToHousehold(@RequestBody AddUserHouseholdRequest request) {
    householdService.addUserToHousehold(request);
    logger.info("User added to household successfully");
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
    HouseholdResponse response = householdService.getById(id);
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
    List<UserResponse> response = userService.getUsersByHouseholdId(householdId);
    logger.info("Found {} members for household ID: {}", response.size(), householdId);
    return ResponseEntity.ok(response);
  }
}
