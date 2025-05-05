package no.ntnu.stud.idatt2106.backend.service;


import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.websocket.WebSocketEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for broadcasting map object events over WebSocket.
 * This service uses Spring's SimpMessagingTemplate to send messages to connected clients.
 */
@Service
public class MapObjectWebSocketService {

  private final SimpMessagingTemplate messagingTemplate;

  public MapObjectWebSocketService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void broadcastCreated(MapObjectRequest obj) {
    messagingTemplate.convertAndSend("/topic/map-object/all", new WebSocketEvent<>("created", obj));
  }

  public void broadcastUpdated(MapObjectResponse obj) {
    messagingTemplate.convertAndSend("/topic/map-object/all", new WebSocketEvent<>("updated", obj));
  }

  public void broadcastDeleted(Long id) {
    messagingTemplate.convertAndSend("/topic/map-object/all", new WebSocketEvent<>("deleted", id));
  }
}
