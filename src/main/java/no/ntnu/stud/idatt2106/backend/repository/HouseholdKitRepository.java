package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;

/**
 * Repository interface for managing HouseholdKit entities.
 */
public interface HouseholdKitRepository {

  /**
   * Saves a new HouseholdKit relation.
   *
   * @param householdKit the household-kit relation to save
   */
  void save(HouseholdKit householdKit);

  /**
   * Deletes an existing HouseholdKit relation.
   *
   * @param householdKit the household-kit relation to delete
   */
  void delete(HouseholdKit householdKit);

  /**
   * Finds all kits related to a specific household ID.
   *
   * @param householdId the household ID
   * @return a list of HouseholdKit relations
   */
  List<HouseholdKit> findByHouseholdId(Long householdId);

  /**
   * Finds all households related to a specific kit ID.
   *
   * @param kitId the kit ID
   * @return a list of HouseholdKit relations
   */
  List<HouseholdKit> findByKitId(Long kitId);

  /**
   * Updates a HouseholdKit relation by changing the household assignment for a kit.
   *
   * @param oldHouseholdId the current household ID
   * @param kitId the kit ID to move
   * @param newHouseholdId the new household ID to assign
   */
  void updateHouseholdForKit(Long oldHouseholdId, Long kitId, Long newHouseholdId);

}
