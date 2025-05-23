package no.ntnu.stud.idatt2106.backend.model.update;

/**
 * Represents the request payload for changing a user's credentials.
 */
public class CredentialsUpdate {
  private String username;
  private String currentPassword;
  private String newPassword;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}