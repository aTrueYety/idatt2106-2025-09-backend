package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodSummaryResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
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
@RestController
@RequestMapping("/api/food")
public class FoodController {

  @Autowired
  private FoodService service;

  /**
   * Get all food items.
   */
  @GetMapping
  public ResponseEntity<List<FoodResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Get a food item by its ID.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FoodResponse> getById(@PathVariable int id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Create a new food item.
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FoodRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update an existing food item.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, @RequestBody FoodUpdate update) {
    boolean success = service.update(id, update);
    if (!success) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Delete a food item by its ID.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
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
  public ResponseEntity<List<FoodResponse>> getByHouseholdId(@PathVariable int householdId) {
    return ResponseEntity.ok(service.getByHouseholdId(householdId));
  }

  @GetMapping("/household/{id}/summary")
  public ResponseEntity<List<FoodSummaryResponse>> getSummaryByHousehold(@PathVariable int id) {
    return ResponseEntity.ok(service.getFoodSummaryByHousehold(id));
  }
  
  @GetMapping("/summary/detailed/{householdId}")
  public ResponseEntity<List<FoodDetailedResponse>> getSummaryDetailed(@PathVariable int householdId) {
    return ResponseEntity.ok(service.getFoodDetailedByHousehold(householdId));
  }

}
