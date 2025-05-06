package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;


/**
 * Interface for emergency group persistence operations.
 */
public interface EmergencyGroupRepository {

  /**
   * Saves a new EmergencyGroup.
   *
   * @param group the EmergencyGroup to be saved
   */
  EmergencyGroup save(EmergencyGroup group);

  /**
   * Retrieves an emergency group by its ID.
   *
   * @param id the ID of the group to be retrieved
   * @return Optional with the group with the given id, 
   *         or with null if there is no group with the given ID
   */
  Optional<EmergencyGroup> findById(Long id);

  /**
   * Retrieves all existing emergency groups.
   *
   * @return a List of all registered emergency groups
   */
  List<EmergencyGroup> findAll();

  /**
   * Updates an existing emergency group.
   *
   * @param id the ID of the emergency group to be updated
   * @param group the new version of the emergency group
   * @return true if the group was updated, false otherwise
   */
  boolean update(Long id, EmergencyGroup group);

  /**
   * Deletes an existing emergency group.
   *
   * @param id the id of the emergency group to be deleted
   * @return true if a group was deleted, false otherwise
   */
  boolean deleteById(Long id);

  /**
   * Retrives a summary of an emergency group.
   *
   * @param householdId the ID of a household in the group
   * @return a list of EmergencyGroupSummaryResponses
   */
  List<EmergencyGroupSummaryResponse> findGroupSummariesByHouseholdId(Long householdId);
}
