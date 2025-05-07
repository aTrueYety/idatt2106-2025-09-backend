package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Severity;
import no.ntnu.stud.idatt2106.backend.model.request.SeverityRequest;

/**
 * Mapper class for creating Severity objects and converting between different
 * representations of severity levels.
 */
public class SeverityMapper {
  /**
   * Converts a SeverityRequest object to a Severity object.
   *
   * @param severityRequest the SeverityRequest object to convert
   * @return the converted Severity object
   */
  public static Severity requestToSeverity(SeverityRequest severityRequest) {
    return new Severity(
        null,
        severityRequest.getColour(),
        severityRequest.getName(),
        severityRequest.getDescription());
  }

  /**
   * Converts a Severity object to a SeverityRequest object.
   *
   * @param severity the Severity object to convert
   * @return the converted SeverityRequest object
   */
  public static SeverityRequest severityToRequest(Severity severity) {
    return new SeverityRequest(
        severity.getColour(),
        severity.getName(),
        severity.getDescription());
  }
}
