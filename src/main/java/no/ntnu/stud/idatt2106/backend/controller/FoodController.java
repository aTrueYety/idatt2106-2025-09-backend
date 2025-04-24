package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

  private final FoodService service;

  public FoodController(FoodService service) {
    this.service = service;
  }

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
}
