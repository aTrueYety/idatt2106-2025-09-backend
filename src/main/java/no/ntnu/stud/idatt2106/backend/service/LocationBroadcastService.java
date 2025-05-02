package no.ntnu.stud.idatt2106.backend.service;

import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.update.LocationUpdate;
import no.ntnu.stud.idatt2106.backend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


/**
 * Service for broadcasting location updates to household topic.
 */
@Service
public class LocationBroadcastService {

  private final SimpMessagingTemplate messagingTemplate;
  private final UserRepository userRepository;

  public LocationBroadcastService(SimpMessagingTemplate messagingTemplate, 
      UserRepository userRepository) {
    this.messagingTemplate = messagingTemplate;
    this.userRepository = userRepository;
  }

  /**
   * Broadcasts location update to the household topic if sharing is enabled.
   *
   * @param locationUpdate location update DTO
   */
  public void broadcastToHousehold(LocationUpdate locationUpdate) {
    User user = userRepository.findById(locationUpdate.getUserId());
  
    if (user == null || !user.isSharePositionHousehold() || user.getHouseholdId() == null) {
      return;
    }
  
    String topic = "/topic/household." + user.getHouseholdId();
    
    messagingTemplate.convertAndSend(topic, locationUpdate);
  }
  
}
