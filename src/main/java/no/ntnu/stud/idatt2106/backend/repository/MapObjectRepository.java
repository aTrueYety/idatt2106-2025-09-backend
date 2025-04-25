
package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;

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
  MapObject findById(Long id);

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
  List<MapObject> findAllInBounds(
      double minLat, double maxLat, double minLong, double maxLong);

}
