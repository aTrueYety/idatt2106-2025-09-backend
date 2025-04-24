package no.ntnu.stud.idatt2106_2025_9.backend.model.response;

/**
 * Represents a registration response in the system.
 * This class contains the message and token returned to the user after a successful registration.
 */
public class RegisterResponse {
  private String message;

  private String token;

  public RegisterResponse(String message, String token) {
    this.message = message;
    this.token = token;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
