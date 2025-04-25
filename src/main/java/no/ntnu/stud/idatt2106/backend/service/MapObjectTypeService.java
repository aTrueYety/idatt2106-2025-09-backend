package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectTypeRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing map object types.
 */
@Service
public class MapObjectTypeService {
  @Autowired
  private MapObjectTypeRepositoryImpl mapObjectTypeRepository;
  @Autowired
  private JwtService jwtService;

  /**
   * Service class for managing map object types.
   * This class provides methods to create, read, update, and delete map object types.
   */
  public List<MapObjectType> getAllMapObjectTypes() {
    return mapObjectTypeRepository.findAll();
  }

  /**
   * Retrieves a map object type by its ID.
   *
   * @param id the ID of the map object type
   * @return the map object type with the specified ID, or null if not found
   */
  public MapObjectType getMapObjectTypeById(Long id) {
    return mapObjectTypeRepository.findById(id);
  }

  /**
   * Creates a new map object type.
   *
   * @param mapObjectType the map object type to create
   * @param token the JWT token for authentication
   */
  public void createMapObjectType(MapObjectType mapObjectType, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectTypeRepository.save(mapObjectType);
  }

  /**
   * Updates an existing map object type.
   *
   * @param mapObjectType the map object type to update
   * @param token the JWT token for authentication
   */
  public void updateMapObjectType(MapObjectType mapObjectType, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectTypeRepository.save(mapObjectType);
  }

  /**
   * Deletes a map object type by its ID.
   *
   * @param id the ID of the map object type to delete
   * @param token the JWT token for authentication
   */
  public void deleteMapObjectType(Long id, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), "User is not admin");
    mapObjectTypeRepository.deleteById(id);
  }
}
