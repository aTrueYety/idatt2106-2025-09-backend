package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;

/**
 * Interface for group-household persistence operations.
 */
public interface GroupHouseholdRepository {
  /**
   * Saves a new GroupHousehold to the database.
   *
   * @param groupHousehold The GroupHousehold to save.
-   */
  void save(GroupHousehold groupHousehold);

  /**
   * Finds a GroupHousehold by its ID.
   *
   * @param id The ID of the GroupHousehold.
   * @return An Optional containing the found GroupHousehold, or empty if not found.
   */
  Optional<GroupHousehold> findById(Long id);

  /**
   * Retrieves all GroupHouseholds from the database.
   *
   * @return A list of all GroupHouseholds.
   */
  List<GroupHousehold> findAll();

  /**
   * Finds all GroupHouseholds by group ID.
   *
   * @param groupId The ID of the group.
   * @return A list of GroupHouseholds associated with the given group ID.
   */
  List<GroupHousehold> findByGroupId(Long groupId);

  /**
   * Finds a GroupHousehold by household ID and group ID.
   *
   * @param householdId The ID of the household.
   * @param groupId The ID of the group.
   * @return The found GroupHousehold, or null if not found.
   */
  GroupHousehold findByHouseholdIdAndGroupId(Long householdId, Long groupId);

  /**
   * Finds all GroupHouseholds by household ID.
   *
   * @param id The ID of the household.
   * @return A list of GroupHouseholds associated with the given household ID.
   */
  boolean deleteById(Long id);

  List<GroupHousehold> findByHouseholdId(Long householdId);

}
