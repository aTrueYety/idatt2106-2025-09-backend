package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import no.ntnu.stud.idatt2106.backend.service.EmergencyGroupService;
import no.ntnu.stud.idatt2106.backend.service.mapper.EmergencyGroupMapper;
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
 * REST controller for managing emergency groups.
 */
@RestController
@RequestMapping("/api/emergency-groups")
@RequiredArgsConstructor
public class EmergencyGroupController {

  private final EmergencyGroupService service;
  private final EmergencyGroupRepository repository;

  @Operation(summary = "Create a new emergency group")
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody EmergencyGroupRequest request,
      @RequestHeader("Authorization") String token) {
    service.create(request, token);
    return ResponseEntity.status(201).build();
  }

  @Operation(summary = "Get all emergency groups")
  @GetMapping
  public ResponseEntity<List<EmergencyGroupResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @Operation(summary = "Delete an emergency group by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id) ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Updates an existing emergency group by its ID.
   *
   * @param id      the ID of the emergency group to update
   * @param request the request object containing updated emergency group details
   * @return a ResponseEntity indicating the result of the update operation.
   */
  @Operation(summary = "Update an emergency group by ID")
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id,
      @RequestBody EmergencyGroupRequest request) {
    return service.update(id, request)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Retrieves an emergency group by its ID.
   *
   * @param id the ID of the emergency group to retrieve
   * @return the emergency group with the specified ID
   */
  @Operation(summary = "Get an emergency group by ID")
  @GetMapping("/{id}")
  public ResponseEntity<EmergencyGroupResponse> getById(@PathVariable Long id) {
    return repository.findById(id)
        .map(EmergencyGroupMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Get emergency group summaries for a household")
  @GetMapping("/summary/{householdId}")
  public ResponseEntity<List<EmergencyGroupSummaryResponse>> getGroupSummaries(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getGroupSummariesByHouseholdId(householdId));
  }

  @Operation(summary = "Get summary for all emergency groups")
  @GetMapping("/summary")
  public ResponseEntity<List<EmergencyGroupSummaryResponse>> getAllGroupSummaries() {
    return ResponseEntity.ok(service.getAllSummaries());
  }

  @Operation(summary = "Get summary for a specific emergency group")
  @GetMapping("/summary/group/{groupId}")
  public ResponseEntity<EmergencyGroupSummaryResponse> getGroupSummaryById(
      @PathVariable Long groupId) {
    return ResponseEntity.ok(service.getSummaryByGroupId(groupId));
  }

}
