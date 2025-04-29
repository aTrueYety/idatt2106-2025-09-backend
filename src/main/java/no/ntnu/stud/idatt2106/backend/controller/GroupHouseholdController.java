package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;
import no.ntnu.stud.idatt2106.backend.service.GroupHouseholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing group-household relations.
 */
@RestController
@RequestMapping("/api/group-households")
@RequiredArgsConstructor
public class GroupHouseholdController {

  private final GroupHouseholdService service;

  @Operation(summary = "Create a new group-household relation")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody GroupHouseholdRequest request) {
    service.create(request);
    return ResponseEntity.status(201).build();
  }

  @Operation(summary = "Get all group-household relations")
  @GetMapping
  public ResponseEntity<List<GroupHouseholdResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @Operation(summary = "Get all households in a specific group")
  @GetMapping("/group/{groupId}")
  public ResponseEntity<List<GroupHouseholdResponse>> getByGroupId(@PathVariable int groupId) {
    return ResponseEntity.ok(service.getByGroupId(groupId));
  }

  @Operation(summary = "Delete a group-household relation by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    return service.delete(id) ? ResponseEntity.noContent().build() 
      : ResponseEntity.notFound().build();
  }
}
