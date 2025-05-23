package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.service.mapper.MapObjectMapper;
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

  @Autowired
  private MapObjectWebSocketService webSocketService;

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
   * Creates a new map object and broadcasts it over WebSocket.
   *
   * @param request the map object to create
   * @param token the JWT token for authentication
   */
  public void createMapObject(MapObjectRequest request, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");

    MapObject object = MapObjectMapper.requestToMapObject(request);
    mapObjectRepository.save(object);


    webSocketService.broadcastCreated(request);
  }

  /**
   * Updates an existing map object and broadcasts the change.
   *
   * @param updatedMapObject the map object to update
   * @param token the JWT token for authentication
   */
  public void updateMapObject(MapObject updatedMapObject, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");

    mapObjectRepository.update(updatedMapObject);
    MapObjectResponse response = mapObjectRepository.findByIdWithDetail(updatedMapObject.getId());
    webSocketService.broadcastUpdated(response);
  }

  /**
   * Deletes a map object by its ID and broadcasts the deletion.
   *
   * @param id the ID of the map object to delete
   * @param token the JWT token for authentication
   */
  public void deleteMapObject(Long id, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");

    mapObjectRepository.deleteById(id);
    webSocketService.broadcastDeleted(id);
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

  /**
   * Retrieves the closest map object to a given location of a specific type.
   *
   * @param latitude  The latitude of the location.
   * @param longitude The longitude of the location.
   * @param type      The type of the map object to search for.
   * @return The closest map object to the specified location.
   */
  public MapObjectResponse getClosestMapObject(double latitude, double longitude, long type) {
    return mapObjectRepository.findClosestWithDetail(latitude, longitude, type);
  }
}
