package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.service.EmergencyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing emergency groups.
 */
@RestController
@RequestMapping("/api/emergency-groups")
@RequiredArgsConstructor
public class EmergencyGroupController {

  private final EmergencyGroupService service;

  @Operation(summary = "Create a new emergency group")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody EmergencyGroupRequest request) {
    service.create(request);
    return ResponseEntity.status(201).build();
  }

  @Operation(summary = "Get all emergency groups")
  @GetMapping
  public ResponseEntity<List<EmergencyGroupResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @Operation(summary = "Delete an emergency group by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    return service.delete(id) ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Updates an existing emergency group by its ID.
   *
   * @param id      the ID of the emergency group to update
   * @param request the request object containing updated emergency group details
   * @return a ResponseEntity indicating the result of the update operation
   */
  @Operation(summary = "Update an emergency group by ID")
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, 
      @RequestBody EmergencyGroupRequest request) {
    return service.update(id, request)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

}
