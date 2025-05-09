package no.ntnu.stud.idatt2106.backend.config;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Custom handshake handler for WebSocket connections.
 * This class is used to determine the user principal during the WebSocket
 * handshake.
 */
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

  @Override
  protected Principal determineUser(org.springframework.http.server.ServerHttpRequest request,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes) {

    // Extract the user ID from the attributes map
    Object user = attributes.get("user");
    if (user != null) {
      return new StompPrincipal(user.toString());
    }
    return request.getPrincipal();
  }
}