package no.ntnu.stud.idatt2106.backend.model.response;

/**
 * Represents a login response in the system.
 * This class contains the message and token returned to the user after a successful login.
 */
public class LoginResponse {
  private String message;
  private String token;

  /**
   * Constructor for LoginResponse.
   *
   * @param message the message to be returned to the user
   * @param token the token to be returned to the user
   */
  public LoginResponse(String message, String token) {
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