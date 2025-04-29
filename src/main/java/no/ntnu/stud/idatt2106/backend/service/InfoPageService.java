package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.mapper.InfoPageMapper;
import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;
import no.ntnu.stud.idatt2106.backend.model.request.InfoPageRequest;
import no.ntnu.stud.idatt2106.backend.model.update.InfoPageUpdate;
import no.ntnu.stud.idatt2106.backend.repository.InfoPageRepository;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing information pages.
 */
@Service
public class InfoPageService {
  @Autowired
  private InfoPageRepository infoPageRepository;
  @Autowired
  private JwtService jwtService;

  public List<InfoPage> getAllInfoPages() {
    return infoPageRepository.findAll();
  }

  /**
   * Retrieve an information page by its ID.
   *
   * @param id the ID of the information page
   * @return the information page with the specified ID
   */
  public InfoPage getInfoPageById(Long id) {
    return infoPageRepository.findById(id);
  }

  /**
   * Create a new information page.
   *
   * @param infoPageRequest the information page to create
   * @param token the JWT token for authorization
   */
  public void createInfoPage(InfoPageRequest infoPageRequest, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), 
        "User is not admin");
    Validate.that(infoPageRequest.getTitle(), Validate.isNotBlankOrNull(), "Title is required");
    Validate.that(infoPageRequest.getContent(), Validate.isNotBlankOrNull(), "Content is required");
    infoPageRepository.save(InfoPageMapper.requestToInfoPage(infoPageRequest));
  }

  /**
   * Update an existing information page.
   *
   * @param infoPageUpdate the information page update to apply
   * @param token the JWT token for authorization
   */
  public void updateInfoPage(InfoPageUpdate infoPageUpdate, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), 
        "User is not admin");
    Validate.that(infoPageUpdate.getId(), Validate.isNotNull(), "ID is required");
    Validate.that(infoPageUpdate.getTitle(), Validate.isNotBlankOrNull(), "Title is required");
    Validate.that(infoPageUpdate.getContent(), Validate.isNotBlankOrNull(), "Content is required");
    infoPageRepository.update(InfoPageMapper.infoPageToUpdate(infoPageUpdate));
  }

  /**
   * Delete an information page by its ID.
   *
   * @param id the ID of the information page to delete
   * @param token the JWT token for authorization
   */
  public void deleteInfoPage(Long id, String token) {
    Validate.isValid(jwtService.extractIsAdmin(token.substring(7)), 
        "User is not admin");
    infoPageRepository.deleteById(id);
  }
}
