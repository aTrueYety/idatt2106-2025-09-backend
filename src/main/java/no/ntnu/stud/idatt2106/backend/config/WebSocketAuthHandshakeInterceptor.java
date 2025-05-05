package no.ntnu.stud.idatt2106.backend.config;

import java.util.Map;
import no.ntnu.stud.idatt2106.backend.service.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Interceptor for WebSocket handshake requests.
 * This class is used to extract the JWT token from the request and validate it.
 * If the token is valid, the user ID is added to the attributes map.
 */
@Configuration
public class WebSocketAuthHandshakeInterceptor implements HandshakeInterceptor {

  private final JwtService jwtService;

  public WebSocketAuthHandshakeInterceptor(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    String query = request.getURI().getQuery();
    String token = null;

    // Extract the token from the query parameters
    if (StringUtils.hasText(query)) {
      for (String param : query.split("&")) {
        String[] pair = param.split("=");
        if (pair.length == 2 && "token".equals(pair[0])) {
          token = pair[1];
          break;
        }
      }
    }

    // If the token is not found in the query parameters, return false
    if (!StringUtils.hasText(token)) {
      return true;
    }

    // Extract the user ID from the token and add it to the attributes map
    try {
      Long userId = jwtService.extractUserId(token);
      attributes.put("user", userId);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
  }
}