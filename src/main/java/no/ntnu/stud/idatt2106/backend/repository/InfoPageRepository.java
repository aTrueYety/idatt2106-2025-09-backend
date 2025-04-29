package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;

/**
 * Repository interface for managing information pages.
 */
public interface InfoPageRepository {

  /**
   * Save an information page to the database.
   *
   * @param infoPage the information page to save
   */
  void save(InfoPage infoPage);

  /**
   * Find an information page by its ID.
   *
   * @param id the ID of the information page
   * @return the found information page, or null if not found
   */
  InfoPage findById(Long id);

  /**
   * Find all information pages in the database.
   *
   * @return a list of all information pages
   */
  List<InfoPage> findAll();

  /**
   * Update an information page in the database.
   *
   * @param infoPageUpdate the information page update to apply
   */
  void update(InfoPage infoPageUpdate);

  /**
   * Delete an information page by its ID.
   *
   * @param id the ID of the information page to delete
   */
  void deleteById(Long id);
}
