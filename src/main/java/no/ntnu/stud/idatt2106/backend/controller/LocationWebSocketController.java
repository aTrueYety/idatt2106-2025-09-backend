package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.service.LocationBroadcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller for receiving location updates and forwarding them.
 */
@Controller
public class LocationWebSocketController {

  private static final Logger logger = LoggerFactory.getLogger(LocationWebSocketController.class);
  private final LocationBroadcastService locationBroadcastService;


  public LocationWebSocketController(LocationBroadcastService locationBroadcastService) {
    this.locationBroadcastService = locationBroadcastService;
  }

  /**
   * Handles incoming location update from client.
   *
   * @param locationUpdate DTO containing location data
   */
  @MessageMapping("/location")
  public void handleLocation(@Payload LocationUpdate locationUpdate) {
    if (locationUpdate.getUserId() == null || locationUpdate.getLatitude() == null 
        || locationUpdate.getLongitude() == null) {
      logger.warn("Incomplete location update received: {}", locationUpdate);
      return;
    }
    locationBroadcastService.broadcastToHousehold(locationUpdate);
  }
  
}
