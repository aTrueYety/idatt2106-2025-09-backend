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
import org.springframework.web.bind.annotation.RequestHeader;
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

  /**
   * Invites a household to a group.
   *
   * @param request the request object containing the details of the group-household relation
   * @param token the JWT token of the user inviting the household
   * @return a ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Invite a household to a group")
  @PostMapping("/invite")
  public ResponseEntity<Void> create(
      @RequestBody GroupHouseholdRequest request,
      @RequestHeader("Authorization") String token) {
    service.invite(request, token);
    return ResponseEntity.status(201).build();
  }

  /**
   * Accepts an invitation for a household to join a group.
   *
   * @param groupId the ID of the group to join
   * @param token the JWT token of the user accepting the invitation
   * @return a ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Accept an invitation for a household to join a group")
  @PostMapping("/accept")
  public ResponseEntity<Void> accept(
      @RequestBody Long groupId,
      @RequestHeader("Authorization") String token) {
    service.acceptInvite(groupId, token);
    return ResponseEntity.status(201).build();
  }

  @Operation(summary = "Get all group-household relations")
  @GetMapping
  public ResponseEntity<List<GroupHouseholdResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Get all households in a specific group.
   *
   * @param groupId the ID of the group
   * @return a list of households in the group
   */
  @Operation(summary = "Get all households in a specific group")
  @GetMapping("/group/{groupId}")
  public ResponseEntity<List<GroupHouseholdResponse>> getByGroupId(@PathVariable Long groupId) {
    return ResponseEntity.ok(service.getByGroupId(groupId));
  }

  /**
   * Delete a group-household relation by ID.
   *
   * @param id the ID of the group-household relation to delete
   * @return a ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Delete a group-household relation by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id) ? ResponseEntity.noContent().build() 
      : ResponseEntity.notFound().build();
  }
}
