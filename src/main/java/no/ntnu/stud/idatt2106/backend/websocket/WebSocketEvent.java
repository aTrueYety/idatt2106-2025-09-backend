package no.ntnu.stud.idatt2106.backend.websocket;

/**
 * WebSocketEvent class representing an event in the WebSocket communication.
 * This class is used to encapsulate the event type and payload.
 *
 * @param <T> The type of the payload associated with the event.
 */
@lombok.Data
public class WebSocketEvent<T> {
  private String eventType;
  private T payload;

  public WebSocketEvent(String eventType, T payload) {
    this.eventType = eventType;
    this.payload = payload;
  }
}
