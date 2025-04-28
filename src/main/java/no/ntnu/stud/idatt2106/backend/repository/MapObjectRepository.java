
package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;

/**
 * Repository interface for managing map objects.
 */
public interface MapObjectRepository {
  /**
   * Finds all map objects in the database.
   *
   * @return a list of all map objects
   */
  List<MapObject> findAll();

  /**
   * Finds a map object by its ID.
   *
   * @param id the ID of the map object
   * @return the map object with the specified ID, or null if not found
   */
  MapObjectResponse findByIdWithDetail(Long id);

  /**
   * Saves a new map object to the database.
   *
   * @param mapObject the map object to save
   */
  void save(MapObject mapObject);

  /**
   * Updates an existing map object in the database.
   *
   * @param mapObject the map object to update
   */
  void update(MapObject mapObject);

  /**
   * Deletes a map object by its ID.
   *
   * @param id the ID of the map object to delete
   */
  void deleteById(Long id);

  /**
   * Finds all map objects within specified latitude and longitude bounds.
   *
   * @param minLat  The minimum latitude of the bounding box.
   * @param maxLat  The maximum latitude of the bounding box.
   * @param minLong The minimum longitude of the bounding box.
   * @param maxLong The maximum longitude of the bounding box.
   */
  List<MapObjectResponse> findAllInBoundsWithDetail(
      double minLat, double maxLat, double minLong, double maxLong);

  /**
   * Finds the closest map object to the specified latitude and longitude of a given type.
   *
   * @param latitude  The latitude of the location.
   * @param longitude The longitude of the location.
   * @param typeId    The ID of the map object type.
   * @return The closest map object of the specified type, or null if not found.
   */
  MapObjectResponse findClosestWithDetail(
      double latitude, double longitude, long typeId);

}
