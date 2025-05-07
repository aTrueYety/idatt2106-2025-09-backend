package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.GroupHouseholdMapper;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing group-household relations.
 */
@Service
@RequiredArgsConstructor
public class GroupHouseholdService {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserService userService;

  @Autowired
  private GroupInviteService groupInviteService;

  @Autowired
  private GroupHouseholdRepository repository;

  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private EmergencyGroupService emergencyGroupService;



  /**
   * Creates a new group-household relation.
   *
   * @param request the request object containing the details of the relation
   */
  public void create(GroupHouseholdRequest request) {
    repository.save(GroupHouseholdMapper.toModel(request));
  }

  /**
   * Retrieves all group-household relations.
   *
   * @return list of group-household responses
   */
  public List<GroupHouseholdResponse> getAll() {
    return repository.findAll().stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all group-household relations for a specific group.
   *
   * @param groupId the group ID
   * @return list of group-household responses
   */
  public List<GroupHouseholdResponse> getByGroupId(Long groupId) {
    return repository.findByGroupId(groupId).stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Deletes a group-household relation.
   *
   * @param id the relation ID
   * @param token the JWT token
   * @return true if deleted, false otherwise
   */
  public boolean delete(Long id, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Validate.that(user.getHouseholdId(), Validate.isNotNull(), "User does not have a household.");
    GroupHousehold groupHousehold = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("GroupHousehold not found with ID: " + id));
    Validate.that(
        repository.findByHouseholdIdAndGroupId(user.getHouseholdId(), groupHousehold.getGroupId()),
        Validate.isNotNull(),
        "User's household is not a member of this group.");
    Validate.isValid(
        groupHousehold.getHouseholdId().equals(user.getHouseholdId()),
        "User's household does not match the group-household relation.");
    boolean success = repository.deleteById(id);
    List<GroupHousehold> groupHouseholds = repository.findByGroupId(groupHousehold.getGroupId());
    if (groupHouseholds.isEmpty()) {
      emergencyGroupService.delete(groupHousehold.getGroupId());
    }
    return success;
  }

  /**
   * Invites a household to a group.
   *
   * @param request the group-household request
   * @param token   the JWT token
   */
  public void invite(GroupHouseholdRequest request, String token) {
    Validate.that(
        repository.findByHouseholdIdAndGroupId(
            request.getHouseholdId(), request.getGroupId()),
        Validate.isNull(),
        "Household is already in this group.");
    Validate.isValid(!groupInviteService.hasGroupInvite(request.getHouseholdId(), 
        request.getGroupId()),
        "Household already has an invite to this group.");

    // Validate that the user is a member of the group
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Validate.that(user.getHouseholdId(), Validate.isNotNull(), "User does not have a household.");
    Validate.that(
        repository.findByHouseholdIdAndGroupId(user.getHouseholdId(), request.getGroupId()),
        Validate.isNotNull(),
        "User's household is not a member of this group.");
    groupInviteService.createGroupInvite(request.getHouseholdId(), request.getGroupId());
  }

  /**
   * Accepts a group invitation.
   *
   * @param groupId the group ID
   * @param token   the JWT token
   */
  public void acceptInvite(Long groupId, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Validate.that(user.getHouseholdId(), Validate.isNotNull(), "User does not have a household.");
    Validate.that(repository.findByHouseholdIdAndGroupId(user.getHouseholdId(), groupId),
        Validate.isNull(),
        "Household is already in this group.");
    Validate.isValid(
        groupInviteService.hasGroupInvite(user.getHouseholdId(), groupId),
        "User's household does not have an invite to this group.");
    create(new GroupHouseholdRequest(user.getHouseholdId(), groupId));
    groupInviteService.deleteGroupInvite(user.getHouseholdId(), groupId);
  }

  /**
   * Rejects a group invitation.
   *
   * @param groupId the group ID
   * @param token   the JWT token
   */
  public void rejectInvite(Long groupId, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Validate.that(user.getHouseholdId(), Validate.isNotNull(), "User does not have a household.");
    Validate.isValid(
        groupInviteService.hasGroupInvite(user.getHouseholdId(), groupId),
        "User's household does not have an invite to this group.");
    groupInviteService.deleteGroupInvite(user.getHouseholdId(), groupId);
  }

  /**
   * Retrieves all group memberships for a user's household.
   *
   * @param userId the user ID
   * @return list of group-household responses
   */
  public List<GroupHouseholdResponse> getByUserId(Long userId) {
    Long householdId = householdRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("Household not found for user ID: " + userId))
        .getId();

    List<GroupHousehold> groupRelations = repository.findByHouseholdId(householdId);

    return groupRelations.stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

}
