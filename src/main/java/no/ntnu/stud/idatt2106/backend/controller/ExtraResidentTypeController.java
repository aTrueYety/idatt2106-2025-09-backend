package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentTypeService;
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
 * Controller for managing extra resident types.
 */
@RestController
@RequestMapping("/api/extra-resident-types")
public class ExtraResidentTypeController {
  @Autowired
  private ExtraResidentTypeService service;

  @GetMapping
  public ResponseEntity<List<ExtraResidentTypeResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Get extra resident type by ID.
   *
   * @param id the ID of the extra resident type to retrieve
   * @return an Optional containing the ExtraResidentTypeResponse if found, empty otherwise
   */
  @GetMapping("/{id}")
  public ResponseEntity<ExtraResidentTypeResponse> getById(@PathVariable int id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Create a new extra resident type.
   *
   * @param request the request containing the details of the extra resident type to create
   */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody ExtraResidentTypeRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update existing extra resident type.
   *
   * @param id the ID of the extra resident type to update
   * @param request the request containing the updated details of the extra resident type
   * @return true if updated, false if not found
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable int id, @RequestBody ExtraResidentTypeRequest request) {
    boolean success = service.update(id, request);
    return success ? ResponseEntity.ok().build() : 
    ResponseEntity.notFound().build();
  }

  /**
   * Delete extra resident type by ID.
   *
   * @param id the ID of the extra resident type to delete
   * @return true if deleted, false if not found
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    return service.delete(id) ? ResponseEntity.noContent().build() :
    ResponseEntity.notFound().build();
  }
}
