package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodSummaryResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Food", description = "Endpoints for operations related to food.")
@RestController
@RequestMapping("/api/food")
public class FoodController {

  @Autowired
  private FoodService service;

  /**
   * Get all food items.
   *
   * @return list of all food items
   */
  @Operation(
      summary = "Retrieves all registered food items"
  )
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
  @Operation(
      summary = "Retrieves a food item by its ID"
  )
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
  @Operation(
      summary = "Registers a new food item"
  )
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
  @Operation(
      summary = "Updates an existing food item",
      description = "Updates the food item with the given ID."
  )
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
  @Operation(
      summary = "Deletes an existing food item",
      description = "Deletes the food item with the given ID."
  )
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
  @Operation(
      summary = "Retrieves all food items belonging to a household"
  )
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
  @Operation(
      summary = "Retrieves a summary of all food items in a household grouped by type"
  )
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
  @Operation(
      summary = "Retrieves a detailed summary of the food in a household"
  )
  @GetMapping("/household/summary/detailed/{householdId}")
  public ResponseEntity<List<FoodDetailedResponse>> getSummaryDetailed(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getFoodDetailedByHousehold(householdId));
  }
}
