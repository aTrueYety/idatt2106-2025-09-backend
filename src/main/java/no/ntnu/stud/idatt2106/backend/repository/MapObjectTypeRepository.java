package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;

/**
 * Repository interface for managing map object types.
 * This interface defines the methods for CRUD operations on map object types.
 */
public interface MapObjectTypeRepository {
  /**
   * Finds all map object types in the database.
   *
   * @return a list of all map object types
   */
  List<MapObjectType> findAll();

  /**
   * Finds a map object type by its ID.
   *
   * @param id the ID of the map object type
   * @return the map object type with the specified ID, or null if not found
   */
  MapObjectType findById(Long id);

  /**
   * Saves a new map object type to the database.
   *
   * @param mapObjectType the map object type to save
   */
  void save(MapObjectType mapObjectType);

  /**
   * Updates an existing map object type in the database.
   *
   * @param mapObjectType the map object type to update
   */
  void update(MapObjectType mapObjectType);

  /**
   * Deletes a map object type by its ID.
   *
   * @param id the ID of the map object type to delete
   */
  void deleteById(Long id);
}
