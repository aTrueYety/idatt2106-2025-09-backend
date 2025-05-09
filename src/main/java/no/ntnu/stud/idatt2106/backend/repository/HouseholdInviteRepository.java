package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;

/**
 * Repository interface for managing HouseholdInvite entities.
 */
public interface HouseholdInviteRepository {
  /**
   * Saves a HouseholdInvite entity to the database.
   *
   * @param invite the HouseholdInvite entity to save
   */
  void save(HouseholdInvite invite);

  /**
   * Finds a HouseholdInvite entity by its user ID and household ID.
   *
   * @param userId the user ID of the HouseholdInvite entity to find
   * @return the found HouseholdInvite entity, or null if not found
   */
  List<HouseholdInvite> findByUserId(Long userId);

  /**
   * Finds all HouseholdInvite entities associated with a specific household ID.
   *
   * @param householdId the household ID to search for
   * @return a list of HouseholdInvite entities associated with the specified household ID
   */
  List<HouseholdInvite> findByHouseholdId(Long householdId);

  /**
   * Finds a HouseholdInvite entity by its user ID and household ID.
   *
   * @param userId      the user ID of the HouseholdInvite entity to find
   * @param householdId the household ID of the HouseholdInvite entity to find
   * @return a list of HouseholdInvite entities matching the specified user ID and household ID
   */
  HouseholdInvite findByUserIdAndHouseholdId(Long userId, Long householdId);

  /**
   * Deletes a HouseholdInvite entity by its user ID and household ID.
   *
   * @param userId      the user ID of the HouseholdInvite entity to delete
   * @param householdId the household ID of the HouseholdInvite entity to delete
   */
  void deleteByUserIdAndHouseholdId(Long userId, Long householdId);
}
