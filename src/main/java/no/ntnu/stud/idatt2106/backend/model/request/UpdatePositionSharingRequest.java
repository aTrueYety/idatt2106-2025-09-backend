package no.ntnu.stud.idatt2106.backend.model.request;

/**
 * Request DTO for updating position sharing settings for a user.
 */
public class UpdatePositionSharingRequest {
  private boolean sharePositionHousehold;
  private boolean sharePositionGroup;

  public boolean isSharePositionHousehold() {
    return sharePositionHousehold;
  }

  public boolean isSharePositionGroup() {
    return sharePositionGroup;
  }

  public void setSharePositionHousehold(boolean sharePositionHousehold) {
    this.sharePositionHousehold = sharePositionHousehold;
  }

  public void setSharePositionGroup(boolean sharePositionGroup) {
    this.sharePositionGroup = sharePositionGroup;
  }
}
