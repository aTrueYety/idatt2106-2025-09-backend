package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.service.MapObjectService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing map objects.
 */
@Tag(name = "MapObject", description = "API for managing map objects")
@RestController
@RequestMapping("/api/map-object")
public class MapObjectController {
  @Autowired
  private MapObjectService mapObjectService;

  /**
   * Retrieves a map object by its ID.
   *
   * @param id the ID of the map object
   * @return the map object with the specified ID, or null if not found
   */
  @Operation(summary = "Get map object by ID", 
      description = "Retrieve a specific map object by its ID.")
  @GetMapping("/{id}")
  public ResponseEntity<MapObject> getMapObjectById(
      @Parameter(description = "ID of the map object to retrieve") @PathVariable Long id) {
    MapObject mapObject = mapObjectService.getMapObjectById(id);
    if (mapObject == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mapObject);
  }

  /**
   * Creates a new map object.
   *
   * @param mapObject the map object to create
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Create a new map object", description = "Create a new map object.")
  @PostMapping
  public ResponseEntity<MapObject> createMapObject(
      @RequestBody MapObjectRequest mapObject,
      @Parameter(description = "JWT token for authentication") 
        @RequestHeader("Authorization") String token) {
    mapObjectService.createMapObject(mapObject, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing map object.
   *
   * @param mapObject the map object to update
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Update an existing map object", 
      description = "Update an existing map object.")
  @PutMapping("/update")
  public ResponseEntity<MapObject> updateMapObject(
      @RequestBody MapObject mapObject,
      @Parameter(description = "JWT token for authentication") 
        @RequestHeader("Authorization") String token) {
    mapObjectService.updateMapObject(mapObject, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a map object by its ID.
   *
   * @param id the ID of the map object to delete
   * @param token the JWT token for authentication
   * @return a ResponseEntity indicating success or failure
   */
  @Operation(summary = "Delete a map object by ID", 
      description = "Delete a specific map object by its ID.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMapObject(
      @Parameter(description = "ID of the map object to delete") @PathVariable Long id,
      @Parameter(description = "JWT token for authentication") 
        @RequestHeader("Authorization") String token) {
    mapObjectService.deleteMapObject(id, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrieves all map objects within specified geographical bounds.
   *
   * @param minLat  The minimum latitude of the bounding box.
   * @param maxLat  The maximum latitude of the bounding box.
   * @param minLong The minimum longitude of the bounding box.
   * @param maxLong The maximum longitude of the bounding box.
   * @return A ResponseEntity containing a list of map objects within the specified
   *         bounds.
   */
  @Operation(summary = "Get map objects in bounds", 
      description = "Retrieve map objects within specified geographical bounds.")
  @GetMapping("/bounds")
  public ResponseEntity<List<MapObject>> getMapObjectsInBounds(
      @Parameter(description = "Minimum latitude of the bounding box") @RequestParam double minLat, 
      @Parameter(description = "Maximum latitude of the bounding box") @RequestParam double maxLat, 
      @Parameter(description = "Minimum longitude of the bounding box") 
        @RequestParam double minLong, 
      @Parameter(description = "Maximum longitude of the bounding box") 
        @RequestParam double maxLong) {
    List<MapObject> mapObjects = mapObjectService.getMapObjectsInBounds(
        minLat, maxLat, minLong, maxLong);
    if (mapObjects.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mapObjects);
  }
}
