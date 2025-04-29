package no.ntnu.stud.idatt2106.backend.controller;

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
   * @param token the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
  @PostMapping()
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
   * @param token the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
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
   * @param id the ID of the information page to delete
   * @param token the authorization token
   * @return ResponseEntity indicating the result of the operation
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInfoPage(
      @PathVariable Long id,
      @RequestHeader("Authorization") String token) {
    infoPageService.deleteInfoPage(id, token);
    return ResponseEntity.noContent().build();
  }
}
