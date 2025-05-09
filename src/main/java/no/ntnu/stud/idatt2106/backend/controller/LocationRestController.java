package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing location-related operations such as
 * toggling position sharing or retrieving last known positions.
 */
@Tag(name = "Location", description = "Endpoints for operations related to locations.")
@RestController
@RequestMapping("/api/location")
public class LocationRestController {
  private static final Logger logger = LoggerFactory.getLogger(LocationRestController.class);

  @Autowired
  private LocationService locationService;

  /**
   * Toggles the "sharePositionHousehold" flag for all users in a household.
   *
   * @param householdId ID of the household to update
   * @param share       true to enable sharing, false to disable
   * @return HTTP 200 with number of updated users, or 404 if none were affected
   */
  @Operation(
      summary = "Toggles shared position between household members"
  ) //TODO user should be a part of the household
  @PutMapping("/toggle-household/{householdId}")
  public ResponseEntity<String> toggleHouseholdShare(
      @PathVariable Long householdId,
      @RequestParam boolean share) {

    logger.info("Toggle sharing for household {} to {}", householdId, share);

    int affected = locationService.toggleShareLocationForHousehold(householdId, share);

    if (affected > 0) {
      logger.info("Updated {} users in household {}", affected, householdId);
      return ResponseEntity.ok("Updated " + affected + " users in household " + householdId);
    } else {
      logger.warn("No users found to update for household {}", householdId);
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves last known positions for all users in a given household
   * who have enabled position sharing.
   *
   * @param householdId the ID of the household
   * @return list of {@link LocationUpdate} for visible users in the household
   */
  @Operation(
      summary = "Retrieves last known position of the users in a household"
  ) //TODO user should be a part of the household
  @GetMapping("/last-known/{householdId}")
  public ResponseEntity<List<LocationUpdate>> getLastKnownPositions(
      @PathVariable Long householdId) {

    logger.debug("Fetching last known positions for household {}", householdId);

    List<LocationUpdate> updates = locationService.getLastKnownPositionsByHousehold(householdId);

    logger.info("Returned {} position(s) for household {}", updates.size(), householdId);
    return ResponseEntity.ok(updates);
  }

  /**
   * Updates a user's last known position in the database.
   *
   * @param update location update DTO
   * @return 200 OK if updated, 400 if invalid
   */
  @Operation(
      summary = "Updates the last known position for a user"
  )
  @PutMapping("/update")
  public ResponseEntity<String> updateLastKnownPosition(@RequestBody LocationUpdate update) {

    logger.debug("Received position update: {}", update);

    if (update.getUserId() == null || update.getLatitude() == null 
        || update.getLongitude() == null) {
      logger.warn("Missing fields in location update: {}", update);
      return ResponseEntity.badRequest().body("Missing fields in location update.");
    }

    locationService.updateLastKnownPosition(
        update.getUserId(),
        update.getLatitude().floatValue(),
        update.getLongitude().floatValue());

    logger.info("Updated position for user {} to {}, {}", update.getUserId(),
        update.getLatitude(), update.getLongitude());

    return ResponseEntity.ok("Position updated.");
  }
}
