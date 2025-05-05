package no.ntnu.stud.idatt2106.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket using STOMP and SockJS.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final WebSocketAuthHandshakeInterceptor authInterceptor;
  private final WebSocketHandshakeHandler handshakeHandler;

  public WebSocketConfig(WebSocketAuthHandshakeInterceptor authInterceptor,
                         WebSocketHandshakeHandler handshakeHandler) {
    this.authInterceptor = authInterceptor;
    this.handshakeHandler = handshakeHandler;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .setHandshakeHandler(handshakeHandler)
        .addInterceptors(authInterceptor);

    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .setHandshakeHandler(handshakeHandler)
        .addInterceptors(authInterceptor)
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
  }
}

