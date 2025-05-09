package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;
import no.ntnu.stud.idatt2106.backend.service.GroupHouseholdService;
import no.ntnu.stud.idatt2106.backend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "GroupHouseholds", description = "Endpoints for group-household operations")
public class GroupHouseholdController {
  private static final Logger logger = LoggerFactory.getLogger(GroupHouseholdController.class);

  @Autowired
  private GroupHouseholdService service;

  @Autowired
  private JwtService jwtService;

  /**
   * Invites a household to a group.
   *
   * @param request the request object containing the details of the group-household relation
   * @param token   the JWT token of the user inviting the household
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
   * @param token   the JWT token of the user accepting the invitation
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

  /**
   * Rejects an invitation for a household to join a group.
   *
   * @param groupId the ID of the group to reject
   * @param token   the JWT token of the user rejecting the invitation
   * @return a ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Reject an invitation for a household to join a group")
  @PostMapping("/reject")
  public ResponseEntity<Void> reject(
      @RequestBody Long groupId,
      @RequestHeader("Authorization") String token) {
    service.rejectInvite(groupId, token);
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
  public ResponseEntity<Void> delete(
      @PathVariable Long id, //TODO check if user is in group
      @RequestHeader("Authorization") String token) {
    return service.delete(id, token) ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  /**
   * Retrieves all group memberships for the household of the current user.
   *
   * @param token the JWT token of the authenticated user
   * @return a list of group-household relations for the user's household
   */
  @Operation(
      summary = "Get all group memberships for the household of the current user",
      description = """
          Retrieves all group-household relations associated with the household that
          the currently authenticated user belongs to.
          """
  )
  @GetMapping("/my-groups")
  public ResponseEntity<List<GroupHouseholdResponse>> getGroupsForCurrentUserHousehold(
      @RequestHeader("Authorization") String token) {
    logger.info("Fetching group-households for authenticated user's household");
    Long userId = jwtService.extractUserId(token.substring(7));
    List<GroupHouseholdResponse> responses = service.getByUserId(userId);
    logger.info("Found {} group-households for user ID: {}", responses.size(), userId);
    return ResponseEntity.ok(responses);
  }
}
