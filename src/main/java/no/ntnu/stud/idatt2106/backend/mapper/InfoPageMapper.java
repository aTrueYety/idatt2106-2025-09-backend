package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;
import no.ntnu.stud.idatt2106.backend.model.request.InfoPageRequest;
import no.ntnu.stud.idatt2106.backend.model.update.InfoPageUpdate;

/**
 * Mapper class for converting between InfoPage and InfoPageDTO objects.
 */
public class InfoPageMapper {
  /**
   * Converts an InfoPageRequest object to an InfoPage object.
   *
   * @param infoPageRequest the InfoPageRequest object to convert
   * @return the converted InfoPage object
   */
  public static InfoPage requestToInfoPage(InfoPageRequest infoPageRequest) {
    return new InfoPage(
      null, 
      infoPageRequest.getTitle(),
      infoPageRequest.getShortDescription(),
      infoPageRequest.getContent(),
      null,
      null
    );
  }

  /**
   * Converts an InfoPageUpdate object to an InfoPage object.
   *
   * @param infoPage the InfoPage object to convert
   * @return the converted InfoPageUpdate object
   */
  public static InfoPage infoPageToUpdate(InfoPageUpdate infoPage) {
    return new InfoPage(
      infoPage.getId(), 
      infoPage.getTitle(), 
      infoPage.getShortDescription(),
      infoPage.getContent(),
      null, 
      null
    );
  }
}
