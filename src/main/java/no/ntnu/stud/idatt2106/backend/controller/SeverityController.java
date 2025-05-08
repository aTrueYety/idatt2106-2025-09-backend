package no.ntnu.stud.idatt2106.backend.controller;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Severity;
import no.ntnu.stud.idatt2106.backend.model.request.SeverityRequest;
import no.ntnu.stud.idatt2106.backend.service.SeverityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for handling severity-related requests.
 */
@Tag(name = "Severity", description = "Endpoints for handling severity related operations.")
@RestController
@RequestMapping("/api/severity")
public class SeverityController {

  @Autowired
  private SeverityService severityService;

  /**
   * Adds a new severity level to the system.
   *
   * @param severity The severity level to be added.
   * @param token The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(summary = "Adds a new severity level")
  @PostMapping
  public ResponseEntity<Void> addSeverity(
      @RequestBody SeverityRequest severity,
      @RequestHeader("Authorization") String token) {
    severityService.saveSeverity(severity, token);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Retrieves a severity level by its ID.
   *
   * @param severityId The ID of the severity level to be retrieved.
   * @return a ResponseEntity containing the severity level details if found.
   */
  @Operation(summary = "Retrieves a severity level by its ID")
  @GetMapping("/{severityId}")
  public ResponseEntity<Severity> getSeverityById(@PathVariable Long severityId) {
    Severity severity = severityService.findSeverityById(severityId);
    if (severity != null) {
      return ResponseEntity.ok(severity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all severity levels in the system.
   *
   * @return a ResponseEntity containing a list of all severity levels.
   */
  @Operation(summary = "Retrieves all severity levels")
  @GetMapping
  public ResponseEntity<List<Severity>> getAllSeverities() {
    List<Severity> severities = severityService.findAllSeverities();
    if (severities != null && !severities.isEmpty()) {
      return ResponseEntity.ok(severities);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Updates an existing severity level.
   *
   * @param severity The updated severity level details.
   * @param token The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(summary = "Updates an existing severity level")
  @PostMapping("/update")
  public ResponseEntity<?> updateSeverity(
      @RequestBody Severity severity,
      @RequestHeader("Authorization") String token) {
    severityService.updateSeverity(severity, token);
    return ResponseEntity.ok("Severity updated successfully");
  }

  /**
   * Deletes a severity level by its ID.
   *
   * @param severityId The ID of the severity level to be deleted.
   * @param token The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(
      summary = "Deletes a severity level",
      description = "Deletes the severity level with the given ID. The user must be an admin."
  )
  @DeleteMapping("/{severityId}")
  public ResponseEntity<String> deleteSeverity(
      @PathVariable Long severityId,
      @RequestHeader("Authorization") String token) {
    severityService.deleteSeverity(severityId, token);
    return ResponseEntity.ok("Severity deleted successfully");
  }
}
