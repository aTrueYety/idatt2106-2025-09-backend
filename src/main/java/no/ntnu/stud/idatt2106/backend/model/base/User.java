package no.ntnu.stud.idatt2106.backend.model.base;

import java.util.List;

/**
 * Represents a user in the system.
 */
public class User {
  private Long id;
  private Long householdId;
  private String email;
  private String username;
  private String password;
  private boolean emailConfirmed = false;
  private boolean isAdmin = false;
  private boolean isSuperAdmin = false;
  private String firstName;
  private String lastName;
  private boolean sharePositionHousehold = false;
  private boolean sharePositionGroup = false;
  private float lastLatidude;
  private float lastLongitude;
  private byte[] picture;

  /**
   * Constructor for User.
   */
  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getHouseholdId() {
    return householdId;
  }

  public void setHouseholdId(Long householdId) {
    this.householdId = householdId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
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

  public boolean isEmailConfirmed() {
    return emailConfirmed;
  }

  public void setEmailConfirmed(boolean emailConfirmed) {
    this.emailConfirmed = emailConfirmed;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public boolean isSuperAdmin() {
    return isSuperAdmin;
  }

  public void setSuperAdmin(boolean superAdmin) {
    isSuperAdmin = superAdmin;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isSharePositionHousehold() {
    return sharePositionHousehold;
  }

  public void setSharePositionHousehold(boolean sharePositionHousehold) {
    this.sharePositionHousehold = sharePositionHousehold;
  }

  public boolean isSharePositionGroup() {
    return sharePositionGroup;
  }

  public void setSharePositionGroup(boolean sharePositionGroup) {
    this.sharePositionGroup = sharePositionGroup;
  }

  public float getLastLatidude() {
    return lastLatidude;
  }

  public void setLastLatidude(float lastLatidude) {
    this.lastLatidude = lastLatidude;
  }

  public float getLastLongitude() {
    return lastLongitude;
  }

  public void setLastLongitude(float lastLongitude) {
    this.lastLongitude = lastLongitude;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  
  
}
