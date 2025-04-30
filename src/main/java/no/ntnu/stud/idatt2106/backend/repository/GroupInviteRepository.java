package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.GroupInvite;

/**
 * Repository interface for managing group invitations.
 */
public interface GroupInviteRepository {
  /**
   * Saves a new GroupInvite to the database.
   *
   * @param groupInvite The GroupInvite to save.
   * @return The saved GroupInvite.
   */
  GroupInvite save(GroupInvite groupInvite);

  /**
   * Finds a GroupInvite by its household ID and group ID.
   *
   * @param householdId The ID of the household.
   * @param groupId The ID of the group.
   * @return The found GroupInvite, or null if not found.
   */
  GroupInvite findByHouseholdIdAndGroupId(Long householdId, Long groupId);

  /**
   * Retrieves all GroupInvites from the database.
   *
   * @return A list of all GroupInvites.
   */
  List<GroupInvite> findAll();

  /**
   * Deletes a GroupInvite by its household ID and group ID.
   *
   * @param householdId The ID of the household.
   * @param groupId The ID of the group.
   */
  void deleteByHouseholdIdAndGroupId(Long householdId, Long groupId);
}
