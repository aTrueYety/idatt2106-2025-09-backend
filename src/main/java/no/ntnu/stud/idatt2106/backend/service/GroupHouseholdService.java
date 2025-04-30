package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.GroupHouseholdMapper;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
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

  /**
   * Creates a new group-household relation.
   *
   * @param request the request object containing the details of the
   *                group-household relation to be created.
   */
  public void create(GroupHouseholdRequest request) {
    repository.save(GroupHouseholdMapper.toModel(request));
  }

  /**
   * Retrieves all group-household relations.
   *
   * @return a list of GroupHouseholdResponse objects representing all
   *         group-household relations.
   */
  public List<GroupHouseholdResponse> getAll() {
    return repository.findAll().stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all group-household relations for a specific group ID.
   *
   * @param groupId the ID of the group whose household relations are to be
   *                retrieved.
   * @return a list of GroupHouseholdResponse objects representing the
   *         group-household relations.
   */
  public List<GroupHouseholdResponse> getByGroupId(Long groupId) {
    return repository.findByGroupId(groupId).stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all group-household relations for a specific user ID.
   *
   * @param id the ID of the user whose household relations are to be retrieved.
   * @return a list of GroupHouseholdResponse objects representing the
   *         group-household relations.
   */
  public boolean delete(Long id) {
    return repository.deleteById(id);
  }

  /**
   * Invites a household to a group.
   *
   * @param request the request object containing the details of the group-household relation
   * @param token the JWT token of the user inviting the household
   */
  public void invite(GroupHouseholdRequest request, String token) {
    Long userId = jwtService.extractUserId(token);
    Validate.that(
        repository.findByHouseholdIdAndGroupId(
          request.getHouseholdId(), request.getGroupId()),
        Validate.isNull(),
        "Household is already in this group.");
    Validate.isValid(!groupInviteService.hasGroupInvite(userId, request.getGroupId()),
        "User already has an invite to this group.");
    groupInviteService.createGroupInvite(userId, request.getGroupId());
  }

  /**
   * Accepts a group invite for a user to a group.
   *
   * @param groupId The id of the group to accept the invite for.
   * @param token The users JWT token.
   */
  public void acceptInvite(Long groupId, String token) {
    Long userId = jwtService.extractUserId(token);
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
}
