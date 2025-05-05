package no.ntnu.stud.idatt2106.backend.config;

import no.ntnu.stud.idatt2106.backend.service.HouseholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import no.ntnu.stud.idatt2106.backend.service.HouseholdService;

class WebSocketInboundInterceptor implements ChannelInterceptor {

  private final HouseholdService householdService;

  public WebSocketInboundInterceptor(HouseholdService householdService) {
    this.householdService = householdService;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (!StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
      return message;
    }

    String dest = accessor.getDestination();

    if (dest.startsWith("/topic/location/")) {
      if (!canSubscribeToHousehold(dest, Long.parseLong(accessor.getUser().getName()))) {
        throw new IllegalArgumentException("Not allowed to subscribe to this location topic");
      }
    }

    return message;
  }

  /**
   * Checks if the user can subscribe to the household topic.
   * 
   * @param destination the destination topic
   * @param userId      the user ID
   * @return true if the user can subscribe, false otherwise
   */
  private boolean canSubscribeToHousehold(String destination, Long userId) {
    String householdId = destination.substring(destination.lastIndexOf("/") + 1);
    return householdService.getMembers(Long.parseLong(householdId)).stream()
        .anyMatch(member -> member.getId().equals(userId));
  }
}