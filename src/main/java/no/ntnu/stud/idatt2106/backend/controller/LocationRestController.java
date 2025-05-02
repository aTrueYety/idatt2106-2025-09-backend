package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/location")
public class LocationRestController {

  private static final Logger logger = LoggerFactory.getLogger(LocationRestController.class);
  private final LocationService locationService;

  /**
   * Constructs the controller with the required service.
   *
   * @param locationService the service handling location logic
   */
  public LocationRestController(LocationService locationService) {
    this.locationService = locationService;
  }

  /**
   * Toggles the "sharePositionHousehold" flag for all users in a household.
   *
   * @param householdId ID of the household to update
   * @param share       true to enable sharing, false to disable
   * @return HTTP 200 with number of updated users, or 404 if none were affected
   */
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
