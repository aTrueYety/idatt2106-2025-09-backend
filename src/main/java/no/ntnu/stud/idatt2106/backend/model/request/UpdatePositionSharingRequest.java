package no.ntnu.stud.idatt2106.backend.model.request;

/**
 * Request DTO for updating position sharing settings for a user.
 */
public class UpdatePositionSharingRequest {
  private boolean sharePositionHousehold;

  public boolean isSharePositionHousehold() {
    return sharePositionHousehold;
  }

  public void setSharePositionHousehold(boolean sharePositionHousehold) {
    this.sharePositionHousehold = sharePositionHousehold;
  }
}
