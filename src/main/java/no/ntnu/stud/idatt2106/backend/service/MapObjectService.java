package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.service.factory.MapObjectFactory;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing map objects.
 */
@Service
public class MapObjectService {
  @Autowired
  private MapObjectRepositoryImpl mapObjectRepository;
  @Autowired
  private JwtService jwtService;
  
  /**
   * Retrieves all map objects from the repository.
   *
   * @return a list of all map objects
   */
  public List<MapObject> getAllMapObjects() {
    return mapObjectRepository.findAll();
  }

  /**
   * Retrieves a map object by its ID.
   *
   * @param id the ID of the map object
   * @return the map object with the specified ID, or null if not found
   */
  public MapObjectResponse getMapObjectById(Long id) {
    return mapObjectRepository.findByIdWithDetail(id);
  }

  /**
   * Creates a new map object.
   *
   * @param mapObject the map object to create
   * @param token the JWT token for authentication
   */
  public void createMapObject(MapObjectRequest mapObject, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectRepository.save(MapObjectFactory.requestToMapObject(mapObject));
  }

  /**
   * Updates an existing map object.
   *
   * @param updatedMapObject the map object to update
   * @param token the JWT token for authentication
   */
  public void updateMapObject(MapObject updatedMapObject, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectRepository.update(updatedMapObject);
  }

  /**
   * Deletes a map object by its ID.
   *
   * @param id the ID of the map object to delete
   * @param token the JWT token for authentication
   */
  public void deleteMapObject(Long id, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectRepository.deleteById(id);
  }

  /**
   * Retrieves all map objects within specified geographical bounds.
   *
   * @param minLat  The minimum latitude of the bounding box.
   * @param maxLat  The maximum latitude of the bounding box.
   * @param minLong The minimum longitude of the bounding box.
   * @param maxLong The maximum longitude of the bounding box.
   * @return A list of map objects within the specified bounds.
   */
  public List<MapObjectResponse> getMapObjectsInBounds(
      double minLat, double maxLat, double minLong, double maxLong) {
    return mapObjectRepository.findAllInBoundsWithDetail(minLat, maxLat, minLong, maxLong);
  }
}
