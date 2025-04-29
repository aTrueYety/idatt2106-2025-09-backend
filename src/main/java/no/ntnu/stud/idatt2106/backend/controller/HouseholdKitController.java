package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.request.MoveHouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;
import no.ntnu.stud.idatt2106.backend.service.HouseholdKitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing HouseholdKit relations.
 */
@RestController
@RequestMapping("/api/household-kits")
public class HouseholdKitController {

  private final HouseholdKitService service;

  /**
   * Constructs a HouseholdKitController with the given service.
   *
   * @param service the household kit service
   */
  public HouseholdKitController(HouseholdKitService service) {
    this.service = service;
  }

  /**
   * Creates a new household-kit relation.
   *
   * @param request the request body containing household and kit IDs
   * @return HTTP 201 Created if successful
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody HouseholdKitRequest request) {
    service.create(request);
    return ResponseEntity.status(201).build();
  }

  /**
   * Gets all kits belonging to a specific household.
   *
   * @param householdId the household ID
   * @return list of HouseholdKitResponse
   */
  @GetMapping("/household/{householdId}")
  public ResponseEntity<List<HouseholdKitResponse>> getByHouseholdId(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getByHouseholdId(householdId));
  }

  /**
   * Gets all households that have a specific kit.
   *
   * @param kitId the kit ID
   * @return list of HouseholdKitResponse
   */
  @GetMapping("/kit/{kitId}")
  public ResponseEntity<List<HouseholdKitResponse>> getByKitId(
      @PathVariable Long kitId) {
    return ResponseEntity.ok(service.getByKitId(kitId));
  }

  /**
   * Deletes a household-kit relation.
   *
   * @param request the request body containing household and kit IDs
   * @return HTTP 204 No Content if deleted, 404 Not Found if relation not found
   */
  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestBody HouseholdKitRequest request) {
    boolean deleted = service.delete(request);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Moves a kit from one household to another.
   *
   * @param request the request body containing old household ID, kit ID, and new household ID
   * @return HTTP 200 OK if moved, 404 Not Found if relation not found
   */
  @PutMapping("/move")
  public ResponseEntity<Void> moveKit(@RequestBody MoveHouseholdKitRequest request) {
    boolean moved = service.moveKitToAnotherHousehold(request);
    if (moved) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
