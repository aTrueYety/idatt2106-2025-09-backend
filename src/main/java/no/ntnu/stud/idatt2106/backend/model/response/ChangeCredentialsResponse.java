package no.ntnu.stud.idatt2106.backend.model.response;

/**
 * Represents a change credentials response in the system.
 * This class contains the message and token returned to the user after a
 * successful change of credentials.
 */
public class ChangeCredentialsResponse {
  private String message;
  private String token;

  /**
   * Constructor for ChangeCredentialsResponse.
   *
   * @param message the message to be returned to the user
   * @param token   the token to be returned to the user
   */
  public ChangeCredentialsResponse(String message, String token) {
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
