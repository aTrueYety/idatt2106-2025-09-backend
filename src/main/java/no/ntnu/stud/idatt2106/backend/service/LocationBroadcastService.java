package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for broadcasting user location updates to their household over WebSocket.
 * This service is responsible for sending a {@link LocationUpdate} to all subscribers
 * of a specific household WebSocket topic, if the user has enabled position sharing
 * and belongs to a household.
 */
@Service
public class LocationBroadcastService {

  private static final Logger logger = LoggerFactory.getLogger(LocationBroadcastService.class);
  private final SimpMessagingTemplate messagingTemplate;
  private final UserRepository userRepository;

  /**
   * Constructs a new {@code LocationBroadcastService}.
   *
   * @param messagingTemplate the template for sending WebSocket messages
   * @param userRepository    the repository for retrieving user data
   */
  public LocationBroadcastService(SimpMessagingTemplate messagingTemplate,
                                  UserRepository userRepository) {
    this.messagingTemplate = messagingTemplate;
    this.userRepository = userRepository;
  }

  /**
   * Broadcasts a location update to the household topic if the user has enabled
   * position sharing and is associated with a household.
   *
   * @param locationUpdate the location update containing user ID, latitude, and longitude
   */
  public void broadcastToHousehold(LocationUpdate locationUpdate) {
    User user = userRepository.findById(locationUpdate.getUserId());

    if (user == null) {
      logger.warn("User with ID {} not found", locationUpdate.getUserId());
      return;
    }

    if (!user.isSharePositionHousehold()) {
      logger.info("User {} has disabled position sharing", user.getId());
      return;
    }

    if (user.getHouseholdId() == null) {
      logger.info("User {} has no household ID", user.getId());
      return;
    }

    logger.info("Broadcasting position for user {} to household {} at {}, {}",
        user.getId(), user.getHouseholdId(), locationUpdate.getLatitude(),
        locationUpdate.getLongitude());

    String topic = "/topic/household." + user.getHouseholdId();
    messagingTemplate.convertAndSend(topic, locationUpdate);
  }
}
