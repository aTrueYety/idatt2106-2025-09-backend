package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.KitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.KitResponse;
import no.ntnu.stud.idatt2106.backend.service.KitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling kit-related requests.
 *
 * <p>
 * This class is currently empty and serves as a placeholder for future
 * development.
 * </p>
 */
@RestController
@RequestMapping("/api/kits")
public class KitController {

  @Autowired
  private KitService service;

  /**
   * Retrieves all kits.
   *
   * @return a ResponseEntity containing a list of KitResponse objects
   */
  @GetMapping()
  public ResponseEntity<List<KitResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Retrieves a kit by its ID.
   *
   * @param id the ID of the kit to retrieve
   * @return a ResponseEntity containing the KitResponse if found, or 404 Not
   *         Found if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<KitResponse> getById(@RequestParam Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Create a new kit.
   *
   * @param request the kit request containing kit details
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody KitRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update an existing kit.
   *
   * @param id the ID of the kit to update
   */
  @PutMapping("path/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody KitRequest request) {
    boolean updated = service.update(id, request);
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Delete a kit by its ID.
   *
   * @param id the ID of the kit to delete
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
  public ResponseEntity<List<KitResponse>> search(@RequestParam String query) {
    return ResponseEntity.ok(service.searchByName(query));
  }
}
