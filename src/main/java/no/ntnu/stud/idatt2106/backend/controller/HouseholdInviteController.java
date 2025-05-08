package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import no.ntnu.stud.idatt2106.backend.service.HouseholdInviteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing household invites.
 */
@Tag(name = "Household Invites", 
    description = "Endpoints for operations related to household invites.")
@RestController
@RequestMapping("/api/household-invites")
public class HouseholdInviteController {
  private static final Logger logger = LoggerFactory.getLogger(HouseholdInviteController.class);

  @Autowired
  private HouseholdInviteService householdInviteService;

  /**
   * Retrieves all household invites for a user.
   *
   * @param token the JWT token of the user
   * @return a ResponseEntity containing the list of household invites
   */
  @Operation(summary = "Get household invites for a user", 
      description = "Retrieves all household invites for a user based on the user token.")
  @GetMapping("/user")//TODO remove token from param
  public ResponseEntity<List<HouseholdInvite>> getHouseholdInvitesForUser(
      @RequestHeader("Authorization") String token) {
    List<HouseholdInvite> householdInvites = 
        householdInviteService.findHouseholdInvitesForUser(token);
    logger.info("Retrieved household invites for user with token: {}", token);
    if (householdInvites.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(householdInvites);
    }
  }

  /**
   * Retrieves all household invites for a household.
   *
   * @param token the JWT token of the user
   * @return a ResponseEntity containing the list of household invites
   */
  @Operation(summary = "Get household invites for a household", 
      description = "Retrieves all household invites for a household based on the user token.")
  @GetMapping("/household")//TODO remove token from param
  public ResponseEntity<List<HouseholdInvite>> getHouseholdInvitesForHousehold(
      @RequestHeader("Authorization") String token) {
    List<HouseholdInvite> householdInvites = 
        householdInviteService.findHouseholdInvitesForHousehold(token);
    logger.info("Retrieved household invites for household with token: {}", token);
    if (householdInvites.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(householdInvites);
    }
  }
}
