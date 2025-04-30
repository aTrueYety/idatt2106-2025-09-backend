package no.ntnu.stud.idatt2106.backend.model.update;

/**
 * Data Transfer Object for transmitting real-time location updates.
 */
public class LocationUpdate {

  private Long userId;
  private Double latitude;
  private Double longitude;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
}
