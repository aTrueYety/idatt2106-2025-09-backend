package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;
import no.ntnu.stud.idatt2106.backend.model.request.InfoPageRequest;
import no.ntnu.stud.idatt2106.backend.model.update.InfoPageUpdate;
import no.ntnu.stud.idatt2106.backend.service.InfoPageService;
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
 * Controller class for managing information pages.
 */
@Tag(name = "Info Page", description = "Endpoints for operation related to information pages.")
@RestController
@RequestMapping("/api/info-page")
public class InfoPageController {

  @Autowired
  private InfoPageService infoPageService;

  /**
   * Retrieve all information pages.
   *
   * @return a list of all information pages
   */
  @Operation(summary = "Retrieve all information pages", 
      description = "Fetches a list of all information pages.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", 
          description = "Successfully retrieved list of information pages"),
      @ApiResponse(responseCode = "404", 
          description = "No information pages found", content = @Content)
  })
  @GetMapping("/all")
  public ResponseEntity<List<InfoPage>> getAllInfoPages() {
    List<InfoPage> infoPages = infoPageService.getAllInfoPages();
    if (infoPages.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(infoPages);
  }

  /**
   * Retrieve an information page by its ID.
   *
   * @param id the ID of the information page
   * @return the information page with the specified ID or 404 if not found
   */
  @Operation(summary = "Retrieve an information page by ID", 
      description = "Fetches an information page by its unique ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", 
          description = "Successfully retrieved the information page"),
      @ApiResponse(responseCode = "404", 
          description = "Information page not found", content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<InfoPage> getInfoPageById(@PathVariable Long id) {
    InfoPage infoPage = infoPageService.getInfoPageById(id);
    if (infoPage == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(infoPage);
  }

  /**
   * Create a new information page.
   *
   * @param infoPage the information page to create
   * @param token    the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Create a new information page", 
      description = "Creates a new information page with the provided data.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created the information page"),
      @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content)
  })
  @PostMapping() //TODO remove token from param
  public ResponseEntity<Void> createInfoPage(
      @RequestBody InfoPageRequest infoPage,
      @RequestHeader("Authorization") String token) {
    infoPageService.createInfoPage(infoPage, token);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Update an existing information page.
   *
   * @param infoPage the updated information page data
   * @param token    the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Update an existing information page", 
      description = "Updates an existing information page with the provided data.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully updated the information page"),
      @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content)
  })
  @PutMapping
  public ResponseEntity<Void> updateInfoPage(
      @RequestBody InfoPageUpdate infoPage,
      @RequestHeader("Authorization") String token) {
    infoPageService.updateInfoPage(infoPage, token);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete an information page by its ID.
   *
   * @param id    the ID of the information page to delete
   * @param token the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
  @Operation(summary = "Delete an information page by ID", 
      description = "Deletes an information page by its unique ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted the information page"),
      @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInfoPage(
      @PathVariable Long id,
      @RequestHeader("Authorization") String token) {
    infoPageService.deleteInfoPage(id, token);
    return ResponseEntity.noContent().build();
  }
}
