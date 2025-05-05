package no.ntnu.stud.idatt2106.backend.service;


import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
import no.ntnu.stud.idatt2106.backend.websocket.WebSocketEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for broadcasting event-related messages over WebSocket.
 * This service uses Spring's SimpMessagingTemplate to send messages to connected clients.
 */
@Service
public class EventWebSocketService {

  private final SimpMessagingTemplate messagingTemplate;

  public EventWebSocketService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void broadcastCreated(EventResponse event) {
    messagingTemplate.convertAndSend("/topic/events", new WebSocketEvent<>("created", event));
  }

  public void broadcastUpdated(EventResponse event) {
    messagingTemplate.convertAndSend("/topic/events", new WebSocketEvent<>("updated", event));
  }

  public void broadcastDeleted(Long eventId) {
    messagingTemplate.convertAndSend("/topic/events", new WebSocketEvent<>("deleted", eventId));
  }
}
