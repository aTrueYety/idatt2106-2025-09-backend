package no.ntnu.stud.idatt2106.backend.model.request;

/**
 * Represents a login request in the system.
 * This class contains the username and password provided by the user during login.
 */
public class LoginRequest {
  private String username;
  private String password;

  /**
   * Constructor for LoginRequest.
   *
   * @param username the username provided by the user
   * @param password the password provided by the user
   */
  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

