package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for location-related operations such as broadcasting
 * or storing user positions, and toggling position sharing for households.
 */
@Service
public class LocationService {

  private final UserRepository userRepository;
  private final LocationBroadcastService locationBroadcastService;

  public LocationService(UserRepository userRepository, LocationBroadcastService locationBroadcastService) {
    this.userRepository = userRepository;
    this.locationBroadcastService = locationBroadcastService;
  }

  /**
   * Retrieves last known positions for all users in a household
   * who have enabled position sharing.
   *
   * @param householdId the household ID
   * @return list of {@link LocationUpdate} containing user IDs and positions
   */
  public List<LocationUpdate> getLastKnownPositionsByHousehold(Long householdId) {
    List<User> users = userRepository.findUsersByHouseholdId(householdId);
    return users.stream()
        .filter(User::isSharePositionHousehold)
        .map(user -> {
          LocationUpdate update = new LocationUpdate();
          update.setUserId(user.getId());
          update.setLatitude((double) user.getLastLatitude());
          update.setLongitude((double) user.getLastLongitude());
          return update;
        })
        .collect(Collectors.toList());
  }

  /**
   * Toggles the position sharing setting for all users in a household.
   *
   * @param householdId the household ID
   * @param share       true to enable sharing, false to disable
   * @return number of users updated
   */
  public int toggleShareLocationForHousehold(Long householdId, boolean share) {
    return userRepository.updateSharePositionHouseholdForHousehold(householdId, share);
  }

  /**
   * Updates the last known position of a user in the database.
   *
   * @param userId    the ID of the user
   * @param latitude  new latitude
   * @param longitude new longitude
   */
  public void updateLastKnownPosition(Long userId, float latitude, float longitude) {
    userRepository.updateLastKnownPosition(userId, latitude, longitude);
    locationBroadcastService.broadcastToHousehold(new LocationUpdate(userId, (double) latitude, (double) longitude));
  }
}
