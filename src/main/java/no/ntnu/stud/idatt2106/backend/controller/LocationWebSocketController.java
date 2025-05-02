package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.service.LocationBroadcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller for handling real-time location updates.
 * Listens to STOMP messages sent to "/app/location" and delegates processing
 * to the {@link LocationBroadcastService}. Validates incoming data before broadcasting.
 */
@Controller
public class LocationWebSocketController {

  private static final Logger logger = LoggerFactory.getLogger(LocationWebSocketController.class);
  private final LocationBroadcastService locationBroadcastService;

  /**
   * Constructs a LocationWebSocketController with required services.
   *
   * @param locationBroadcastService the service used to broadcast location updates
   */
  public LocationWebSocketController(LocationBroadcastService locationBroadcastService) {
    this.locationBroadcastService = locationBroadcastService;
  }

  /**
   * Handles incoming location updates sent via WebSocket.
   * Expects a {@link LocationUpdate} payload with non-null userId, latitude and longitude.
   *
   * @param locationUpdate DTO containing location data
   */
  @MessageMapping("/location")
  public void handleLocation(@Payload LocationUpdate locationUpdate) {
    if (locationUpdate.getUserId() == null 
        || locationUpdate.getLatitude() == null 
        || locationUpdate.getLongitude() == null) {
      logger.warn("Rejected incomplete location update: {}", locationUpdate);
      return;
    }

    logger.debug("Received valid location update: userId={}, lat={}, lng={}",
        locationUpdate.getUserId(),
        locationUpdate.getLatitude(),
        locationUpdate.getLongitude());

    locationBroadcastService.broadcastToHousehold(locationUpdate);
  }
}
