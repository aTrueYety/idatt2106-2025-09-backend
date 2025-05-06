package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodSummaryResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.service.FoodService;
import org.springframework.http.HttpStatus;
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
 * Controller for handling food-related requests.
 */
@RestController
@RequestMapping("/api/food")
public class FoodController {

  private final FoodService service;

  /**
   * Constructs a FoodController with the given FoodService.
   *
   * @param service the food service
   */
  public FoodController(FoodService service) {
    this.service = service;
  }

  /**
   * Get all food items.
   *
   * @return list of all food items
   */
  @GetMapping
  public ResponseEntity<List<FoodResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Get a food item by its ID.
   *
   * @param id the ID of the food item
   * @return the food item if found, otherwise 404
   */
  @GetMapping("/{id}")
  public ResponseEntity<FoodResponse> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Create a new food item.
   *
   * @param request the food request containing item details
   * @return 201 Created if successful
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FoodRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update an existing food item.
   *
   * @param id the ID of the food item
   * @param update the update request
   * @return 200 OK if updated, 404 Not Found if item does not exist
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody FoodUpdate update) {
    boolean success = service.update(id, update);
    if (!success) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Delete a food item by its ID.
   *
   * @param id the ID of the food item
   * @return 204 No Content if deleted, 404 Not Found if item does not exist
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boolean success = service.delete(id);
    if (!success) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Get all food items belonging to a specific household.
   *
   * @param householdId the ID of the household
   * @return list of food items
   */
  @GetMapping("/household/{householdId}")
  public ResponseEntity<List<FoodResponse>> getByHouseholdId(@PathVariable Long householdId) {
    return ResponseEntity.ok(service.getByHouseholdId(householdId));
  }

  /**
   * Get a summary of food items grouped by type for a specific household.
   *
   * @param householdId the ID of the household
   * @return list of food summary responses
   */
  @GetMapping("/household/summary/{householdId}")
  public ResponseEntity<List<FoodSummaryResponse>> getSummaryByHousehold(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getFoodSummaryByHousehold(householdId));
  }

  /**
   * Get detailed food information for a specific household with
   * batches for expired sorting.
   *
   * @param householdId the ID of the household
   * @return list of detailed food responses
   */
  @GetMapping("/household/summary/detailed/{householdId}")
  public ResponseEntity<List<FoodDetailedResponse>> getSummaryDetailed(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getFoodDetailedByHousehold(householdId));
  }
}
