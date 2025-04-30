package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.service.SharedFoodService;
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
 * REST controller for managing shared food entries between group households.
 */
@RestController
@RequestMapping("/api/shared-food")
@RequiredArgsConstructor
public class SharedFoodController {

  private final SharedFoodService service;

  /**
   * Creates a new shared food entry.
   *
   * @param request the shared food request containing food ID, group household
   *                ID, and amount
   * @return ResponseEntity indicating the result of the create operation
   */
  @Operation(summary = "Create a new shared food entry")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody SharedFoodRequest request) {
    service.create(request);
    return ResponseEntity.status(201).build();
  }

  /**
   * Retrieves all shared food entries.
   *
   * @return ResponseEntity containing a list of shared food responses
   */
  @Operation(summary = "Get all shared food entries")
  @GetMapping
  public ResponseEntity<List<SharedFoodResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Updates the amount for a shared food entry.
   *
   * @param request the shared food request containing updated information
   * @return ResponseEntity indicating the result of the update operation
   */
  @Operation(summary = "Update amount for shared food entry")
  @PutMapping
  public ResponseEntity<Void> update(@RequestBody SharedFoodRequest request) {
    return service.update(request)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Deletes a shared food entry based on the provided food ID and group household
   * ID.
   *
   * @param foodId           the ID of the food item
   * @param groupHouseholdId the ID of the group household associated with the
   *                         food
   * @return ResponseEntity indicating the result of the delete operation
   */
  @Operation(summary = "Delete a shared food entry")
  @DeleteMapping("/{foodId}/{groupHouseholdId}")
  public ResponseEntity<Void> delete(@PathVariable int foodId, @PathVariable int groupHouseholdId) {
    return service.delete(foodId, groupHouseholdId)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Moves a portion of food to a shared group.
   *
   * @param request the request with food ID, group household ID and amount
   * @return HTTP 200 OK if moved, 400 Bad Request if not enough food or food not
   *         found
   */
  @Operation(summary = "Move food to a shared group (reduces original amount)")
  @PostMapping("/move")
  public ResponseEntity<Void> moveFood(@RequestBody SharedFoodRequest request) {
    return service.moveFoodToSharedGroup(request)
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }

  /**
   * Retrieves a detailed summary of shared food for a specific group household.
   *
   * @param groupHouseholdId the ID of the group household
   * @return ResponseEntity containing a list of detailed food responses
   */
  @Operation(summary = "Get detailed summary of shared food for a group")
  @GetMapping("/summary/detailed/{groupHouseholdId}")
  public ResponseEntity<List<FoodDetailedResponse>> getSharedFoodSummary(
      @PathVariable int groupHouseholdId) {
    return ResponseEntity.ok(service.getSharedFoodSummaryByGroup(groupHouseholdId));
  }

}
