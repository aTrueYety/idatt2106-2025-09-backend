package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.model.base.GroupInvite;
import no.ntnu.stud.idatt2106.backend.repository.GroupInviteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing group invitations.
 */
@Service
public class GroupInviteService {
  @Autowired
  private GroupInviteRepositoryImpl groupInviteRepository;

  /**
   * Creates a new group invite for a household to a group.
   *
   * @param householdId The ID of the household to invite.
   * @param groupId The ID of the group to invite the household to.
   */
  public void createGroupInvite(Long householdId, Long groupId) {
    groupInviteRepository.save(new GroupInvite(groupId, householdId));
  }

  /**
   * Deletes a group invite for a household to a group.
   *
   * @param householdId The ID of the household whose invite to delete.
   * @param groupId The ID of the group whose invite to delete.
   */
  public void deleteGroupInvite(Long householdId, Long groupId) {
    groupInviteRepository.deleteByHouseholdIdAndGroupId(householdId, groupId);
  }

  /**
   * Checks if a household has an invite to a group.
   *
   * @param householdId The ID of the household to check.
   * @param groupId The ID of the group to check.
   * @return True if the household has an invite to the group, false otherwise.
   */
  public boolean hasGroupInvite(Long householdId, Long groupId) {
    return groupInviteRepository.findByHouseholdIdAndGroupId(householdId, groupId) != null;
  }

  /**
   * Retrieves all group invites for a household.
   *
   * @param householdId The ID of the household to retrieve invites for.
   * @return A list of group invites for the household.
   */
  public List<GroupInvite> getGroupInvitesForHousehold(Long householdId) {
    return groupInviteRepository.findAll().stream()
        .filter(invite -> invite.getHouseholdId().equals(householdId))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all group invites for a group.
   *
   * @param groupId The ID of the group to retrieve invites for.
   * @return A list of group invites for the group.
   */
  public List<GroupInvite> getGroupInvitesForGroup(Long groupId) {
    return groupInviteRepository.findAll().stream()
        .filter(invite -> invite.getGroupId().equals(groupId))
        .collect(Collectors.toList());
  }
}
