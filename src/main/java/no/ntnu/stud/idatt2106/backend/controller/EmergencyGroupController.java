package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.service.EmergencyGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmergencyGroupController {
  private static final Logger logger = LoggerFactory.getLogger(EmergencyGroupController.class);

  @Autowired
  private EmergencyGroupService service;

  /**
   * Handles requests to create new EmergencyGroup.
   *
   * @param request the request for a new group
   * @param token the users valid jwt token
   * @return ResponseEntity with status 201 if successfull
   */
  @Operation(
      summary = "Create a new emergency group",
      description = """
          Creates a new emergency group that households can be a part of
          in order to share resources.
          """)
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody EmergencyGroupRequest request,
      @RequestHeader("Authorization") String token) {
    logger.info("Creating group with name = {}", request.getName());
    service.create(request, token);
    logger.info("Group created successfully");
    return ResponseEntity.status(201).build();
  }


  /**
   * Handles requests to get all EmergencyGroups.
   *
   * @return ResponseEntity with status OK containging a list of the EmergencyGroups.
   */
  @Operation(
      summary = "Get all emergency groups",
      description = """
          Retrieves all registered emergency groups.
          """)
  @GetMapping
  public ResponseEntity<List<EmergencyGroupResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }


  /**
   * Handles requests to delete emergency groups.
   *
   * @param id the ID of the emergency group to be deleted
   * @return ResponseEntity with status code No Content on success, or
   *         status code Not Found if no emergency group with the given ID is registered
   */
  @Operation(
      summary = "Delete an emergency group by ID",
      description = """
          Deletes the emergency group with the given ID.
          """)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id) ? ResponseEntity.noContent().build() //TODO Should require auth
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
      @RequestBody EmergencyGroupRequest request) { //TODO Should require auth
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
  @Operation(
      summary = "Get an emergency group by ID",
      description = """
          Retrieves the emergency group with the given ID.
          """)
  @GetMapping("/{id}")
  public ResponseEntity<EmergencyGroupResponse> getById(@PathVariable Long id) {
    logger.info("Retrieving emergency group with ID = {}", id);
    EmergencyGroupResponse response = service.getById(id);
    logger.info("Emergency group retrieved successfully");
    return ResponseEntity.ok(response);
  }

  /**
   * Handles requests to get emergency group summaries for a household.
   *
   * @param householdId the ID of the household to get the summaries for
   * @return the emergency group summaries for the household
   */
  @Operation(summary = "Get emergency group summaries for a household")
  @GetMapping("/summary/{householdId}")
  public ResponseEntity<List<EmergencyGroupSummaryResponse>> getGroupSummaries(
      @PathVariable Long householdId) {
    return ResponseEntity.ok(service.getGroupSummariesByHouseholdId(householdId));
  }

  /**
   * Handles requests to get all emergency group summaries.
   *
   * @return all emergency group summaries for all groups
   */
  @Operation(summary = "Get summary for all emergency groups")
  @GetMapping("/summary")
  public ResponseEntity<List<EmergencyGroupSummaryResponse>> getAllGroupSummaries() {
    return ResponseEntity.ok(service.getAllSummaries());
  }

  /**
   * Handles requests to get the emergency group summary for a specified group.
   *
   * @param groupId the ID of the group to get a summary for
   * @return a summary for the group with the given ID
   */
  @Operation(summary = "Get summary for a specific emergency group")
  @GetMapping("/summary/group/{groupId}")
  public ResponseEntity<EmergencyGroupSummaryResponse> getGroupSummaryById(
      @PathVariable Long groupId) {
    return ResponseEntity.ok(service.getSummaryByGroupId(groupId));
  }

}
