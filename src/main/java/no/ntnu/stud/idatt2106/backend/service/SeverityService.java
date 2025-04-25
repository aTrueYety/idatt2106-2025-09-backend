package no.ntnu.stud.idatt2106.backend.service;


import no.ntnu.stud.idatt2106.backend.model.base.Severity;
import no.ntnu.stud.idatt2106.backend.model.request.SeverityRequest;
import no.ntnu.stud.idatt2106.backend.repository.SeverityRepository;
import no.ntnu.stud.idatt2106.backend.service.factory.SeverityFactory;
import no.ntnu.stud.idatt2106.backend.util.Validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing severity levels.
 */
@Service
public class SeverityService {
  @Autowired
  private SeverityRepository severityRepository;
  @Autowired
  private JwtService jwtService;

  /**
   * Saves a severity level to the repository.
   *
   * @param severity the severity level to be saved
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int saveSeverity(SeverityRequest severity, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    return severityRepository.save(SeverityFactory.requestToSeverity(severity));
  }

  /**
   * Finds a severity level by its ID.
   *
   * @param id the ID of the severity level to be found
   * @return the severity level with the specified ID, or null if not found
   */
  public Severity findSeverityById(long id) {
    return severityRepository.findSeverityById(id);
  }

  /**
   * Updates a severity level in the repository.
   *
   * @param severity the severity level to be updated
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int updateSeverity(Severity severity, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    return severityRepository.update(severity);
  }

  /**
   * Deletes a severity level from the repository.
   *
   * @param id the ID of the severity level to be deleted
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int deleteSeverity(long id, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    return severityRepository.delete(id);
  }

  /**
   * Finds all severity levels in the repository.
   *
   * @return a list of all severity levels
   */
  public List<Severity> findAllSeverities() {
    return severityRepository.findAll();
  }
}
