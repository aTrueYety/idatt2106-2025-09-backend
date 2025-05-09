package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling operations related to extra residents.
 */
@Tag(name = "Extra Residents", 
    description = "Endpoints for operations related to the extra residents in a household.")
@RestController
@RequestMapping("/api/extra-residents")
public class ExtraResidentController {

  @Autowired
  private ExtraResidentService service;

  /** Get all extra residents. */
  @Operation(summary = "Retrieves all extra residents")
  @GetMapping
  public ResponseEntity<List<ExtraResidentResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /** Get a specific extra resident by ID. */
  @Operation(summary = "Retreives a extra resident by its ID")
  @GetMapping("/{id}")
  public ResponseEntity<ExtraResidentResponse> getById(@PathVariable long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /** Create a new extra resident. */
  @Operation(summary = "Creates a new extra resident")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody ExtraResidentRequest request,
      @RequestHeader("Authorization") String token) {
    service.create(request, token);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** Update an existing extra resident. */
  @Operation(summary = "Updates an existing extra resident")
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable Long id, @RequestBody ExtraResidentUpdate request,
      @RequestHeader("Authorization") String token) {
    boolean success = service.update(id, request, token);
    return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  /** Delete a resident by ID. */
  @Operation(summary = "Deletes the extra resident with the given ID")
  @DeleteMapping("/{id}") //TODO add check if the resident is in same household as user.
  public ResponseEntity<Void> delete(@PathVariable long id) {
    return service.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
