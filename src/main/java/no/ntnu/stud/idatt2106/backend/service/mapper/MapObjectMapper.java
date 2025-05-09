package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;

/**
 * Factory class for creating MapObject objects and converting between different
 * representations of map objects.
 */
public class MapObjectMapper {
  /**
   * Converts a MapObjectRequest object to a MapObject object.
   *
   * @param mapObjectRequest the MapObjectRequest object to convert
   * @return the converted MapObject object
   */
  public static MapObject requestToMapObject(MapObjectRequest mapObjectRequest) {
    return new MapObject(
        null,
        mapObjectRequest.getTypeId(),
        mapObjectRequest.getLatitude(),
        mapObjectRequest.getLongitude(),
        mapObjectRequest.getOpening(),
        mapObjectRequest.getClosing(),
        mapObjectRequest.getContactPhone(),
        mapObjectRequest.getContactEmail(),
        mapObjectRequest.getContactName(),
        mapObjectRequest.getDescription(),
        mapObjectRequest.getImage());
  }

  /**
   * Converts a MapObject object to a MapObjectRequest object.
   *
   * @param mapObject the MapObject object to convert
   * @return the converted MapObjectRequest object
   */
  public static MapObjectRequest mapObjectToRequest(MapObject mapObject) {
    return new MapObjectRequest(
        mapObject.getTypeId(),
        mapObject.getLatitude(),
        mapObject.getLongitude(),
        mapObject.getOpening(),
        mapObject.getClosing(),
        mapObject.getContactPhone(),
        mapObject.getContactEmail(),
        mapObject.getContactName(),
        mapObject.getDescription(),
        mapObject.getImage());
  }
}
