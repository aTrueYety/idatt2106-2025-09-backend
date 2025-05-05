package no.ntnu.stud.idatt2106.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import no.ntnu.stud.idatt2106.backend.service.HouseholdService;
import no.ntnu.stud.idatt2106.backend.service.JwtService;

class WebSocketInboundInterceptor implements ChannelInterceptor {

  @Autowired
  HouseholdService householdService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    System.out.println("Command: " + accessor.getCommand());

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

  private boolean canSubscribeToHousehold(String destination, Long userId) {
    String householdId = destination.substring(destination.lastIndexOf("/") + 1);
    System.out.println("HouseholdId: " + householdId);
    System.out.println("UserId: " + userId);
    return householdService.getMembers(Long.parseLong(householdId)).stream()
        .anyMatch(member -> member.getId().equals(userId));
  }
}