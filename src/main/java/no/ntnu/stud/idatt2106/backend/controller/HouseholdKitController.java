package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.request.MoveHouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;
import no.ntnu.stud.idatt2106.backend.service.HouseholdKitService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "HouseholdKit", description = "API for managing relations between households and kits.")
@RestController
@RequestMapping("/api/household-kits")
public class HouseholdKitController {

  @Autowired
  private HouseholdKitService service;

  /**
   * Creates a new household-kit relation.
   *
   * @param request the request body containing household and kit IDs
   * @return HTTP 201 Created if successful
   */
  @Operation(summary = "Create a new household-kit relation", 
      description = "Creates a relation between a household and a kit.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Relation created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
  })
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
  @Operation(summary = "Get kits by household ID", 
      description = "Retrieves all kits belonging to a specific household.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", 
          description = "Kits retrieved successfully", 
          content = @Content(mediaType = "application/json", 
          schema = @Schema(implementation = HouseholdKitResponse.class))),
      @ApiResponse(responseCode = "404", 
          description = "Household not found", content = @Content)
  })
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
  @Operation(summary = "Get households by kit ID", 
      description = "Retrieves all households that have a specific kit.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", 
          description = "Households retrieved successfully", 
          content = @Content(mediaType = "application/json", 
          schema = @Schema(implementation = HouseholdKitResponse.class))),
      @ApiResponse(responseCode = "404", description = "Kit not found", content = @Content)
  })
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
  @Operation(summary = "Delete a household-kit relation", 
      description = "Deletes a relation between a household and a kit.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Relation deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Relation not found", content = @Content)
  })
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
  @Operation(summary = "Move a kit to another household", 
      description = "Moves a kit from one household to another.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Kit moved successfully"),
      @ApiResponse(responseCode = "404", description = "Relation not found", content = @Content)
  })
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
