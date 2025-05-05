package no.ntnu.stud.idatt2106.backend.config;

import java.security.Principal;

/**
 * Custom Principal implementation for STOMP messages.
 * This class is used to represent the user principal in STOMP messages.
 */
public class StompPrincipal implements Principal {
  private final String name;

  public StompPrincipal(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}