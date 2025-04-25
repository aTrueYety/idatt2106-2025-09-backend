package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling operations related to extra residents.
 */
@RestController
@RequestMapping("/api/extra-residents")
public class ExtraResidentController {

  private final ExtraResidentService service;

  public ExtraResidentController(ExtraResidentService service) {
    this.service = service;
  }

  /** Get all extra residents. */
  @GetMapping
  public ResponseEntity<List<ExtraResidentResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /** Get a specific extra resident by ID. */
  @GetMapping("/{id}")
  public ResponseEntity<ExtraResidentResponse> getById(@PathVariable int id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /** Create a new extra resident. */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody ExtraResidentRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** Update an existing extra resident. */
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, @RequestBody ExtraResidentUpdate request) {
    boolean success = service.update(id, request);
    return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  /** Delete a resident by ID. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    return service.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
