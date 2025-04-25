package no.ntnu.stud.idatt2106.backend.service.factory;

import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectTypeRequest;

/**
 * Factory class for creating MapObjectType objects and converting between different
 * representations of map object types.
 */
public class MapObjectTypeFactory {

  /**
   * Converts a MapObjectTypeRequest object to a MapObjectType object.
   *
   * @param mapObjectTypeRequest the MapObjectTypeRequest object to convert
   * @return the converted MapObjectType object
   */
  public static MapObjectType requestToMapObjectType(MapObjectTypeRequest mapObjectTypeRequest) {
    return new MapObjectType(null, mapObjectTypeRequest.getName());
  }

  /**
   * Converts a MapObjectType object to a MapObjectTypeRequest object.
   *
   * @param mapObjectType the MapObjectType object to convert
   * @return the converted MapObjectTypeRequest object
   */
  public static MapObjectTypeRequest mapObjectTypeToRequest(MapObjectType mapObjectType) {
    return new MapObjectTypeRequest(mapObjectType.getName());
  }
}
