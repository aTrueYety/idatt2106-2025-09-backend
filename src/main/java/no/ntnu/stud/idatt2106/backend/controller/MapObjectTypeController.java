package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import no.ntnu.stud.idatt2106.backend.service.MapObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing map object types.
 */
@RestController
@RequestMapping("/api/map-object-type")
public class MapObjectTypeController {
  @Autowired
  private MapObjectTypeService mapObjectTypeService;

  /**
   * Controller for managing map object types.
   * This class provides endpoints for creating, reading, updating, and deleting map object types.
   */
  @GetMapping
  public ResponseEntity<List<MapObjectType>> getAllMapObjectTypes() {
    List<MapObjectType> mapObjectTypes = mapObjectTypeService.getAllMapObjectTypes();
    if (mapObjectTypes.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mapObjectTypes);
  }

  /**
   * Retrieves a map object type by its ID.
   *
   * @param id the ID of the map object type
   * @return the map object type with the specified ID, or null if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<MapObjectType> getMapObjectTypeById(@PathVariable Long id) {
    MapObjectType mapObjectType = mapObjectTypeService.getMapObjectTypeById(id);
    if (mapObjectType == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mapObjectType);
  }

  /**
   * Creates a new map object type.
   *
   * @param mapObjectType the map object type to create
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @PostMapping
  public ResponseEntity<MapObjectType> createMapObjectType(
      @RequestBody MapObjectType mapObjectType,
      @RequestHeader("Authorization") String token) {
    mapObjectTypeService.createMapObjectType(mapObjectType, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing map object type.
   *
   * @param mapObjectType the map object type to update
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @PutMapping("/update")
  public ResponseEntity<MapObjectType> updateMapObjectType(
        @RequestBody MapObjectType mapObjectType,
        @RequestHeader("Authorization") String token) {
    mapObjectTypeService.updateMapObjectType(mapObjectType, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a map object type by its ID.
   *
   * @param id the ID of the map object type to delete
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMapObjectType(
        @PathVariable Long id,
        @RequestHeader("Authorization") String token) {
    mapObjectTypeService.deleteMapObjectType(id, token);
    return ResponseEntity.noContent().build();
  }
}
