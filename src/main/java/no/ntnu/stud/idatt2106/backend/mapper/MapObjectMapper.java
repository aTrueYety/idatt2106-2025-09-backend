package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.model.update.MapObjectUpdate;

public class MapObjectMapper {

  /**
   * Converts a MapObjectRequest to a MapObject model.
   */
  public static MapObject toModel(MapObjectRequest mapObjectRequest) {
    MapObject newMapObject = new MapObject();
    newMapObject.setTypeId(mapObjectRequest.getTypeId());
    newMapObject.setLatitude(mapObjectRequest.getLatitude());
    newMapObject.setLongitude(mapObjectRequest.getLongitude());
    newMapObject.setOpening(mapObjectRequest.getOpening());
    newMapObject.setClosing(mapObjectRequest.getClosing());
    newMapObject.setContactPhone(mapObjectRequest.getContactPhone());
    newMapObject.setContactEmail(mapObjectRequest.getContactEmail());
    newMapObject.setContactName(mapObjectRequest.getContactName());
    newMapObject.setDescription(mapObjectRequest.getDescription());
    return newMapObject;
  }

  /**
   * Converts a MapObjectUpdate to a MapObject model.
   */

  public static MapObject toModel(MapObjectUpdate update) {
    MapObject newMapObject = new MapObject();
    newMapObject.setTypeId(update.getTypeId());
    newMapObject.setLatitude(update.getLatitude());
    newMapObject.setLongitude(update.getLongitude());
    newMapObject.setOpening(update.getOpening());
    newMapObject.setClosing(update.getClosing());
    newMapObject.setContactPhone(update.getContactPhone());
    newMapObject.setContactEmail(update.getContactEmail());
    newMapObject.setContactName(update.getContactName());
    newMapObject.setDescription(update.getDescription());
    return newMapObject;
  }

  /**
   * Converts a MapObject to a MapObjectResponse.
   */

  public static MapObjectResponse toRespone(MapObject mapObject) {
    MapObjectResponse response = new MapObjectResponse();
    response.setId(mapObject.getId());
    response.setTypeId(mapObject.getTypeId());
    response.setLatitude(mapObject.getLatitude());
    response.setLongitude(mapObject.getLongitude());
    response.setOpening(mapObject.getOpening());
    response.setClosing(mapObject.getClosing());
    response.setContactPhone(mapObject.getContactPhone());
    response.setContactEmail(mapObject.getContactEmail());
    response.setContactName(mapObject.getContactName());
    response.setDescription(mapObject.getDescription());
    return response;
  }
}
