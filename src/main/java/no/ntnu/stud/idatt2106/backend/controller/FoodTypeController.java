package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.FoodTypeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling food type-related requests.
 */
@RestController
@RequestMapping("/api/food-types")
public class FoodTypeController {

  @Autowired
  private FoodTypeService service;

  /**
   * Get all food types.
   */
  @GetMapping
  public ResponseEntity<List<FoodTypeResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Get a single food type by its ID.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FoodTypeResponse> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Create a new food type.
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FoodTypeRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update an existing food type.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody FoodTypeRequest request) {
    boolean updated = service.update(id, request);
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Delete a food type by ID.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boolean deleted = service.delete(id);
    if (!deleted) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Search for food types by name.
   */
  @GetMapping("/search")
  public ResponseEntity<List<FoodTypeResponse>> search(@RequestParam String query) {
    return ResponseEntity.ok(service.searchByName(query));
  }
  
}
