package no.ntnu.stud.idatt2106.backend.model.request;

/**
 * Represents a registration request in the system.
 * This class contains the username and password provided by the user during registration.
 */
public class RegisterRequest {
  private String username;
  private String password;
  private String email;

  /**
   * Constructor for RegisterRequest.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @param email the email of the user
   */
  public RegisterRequest(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}