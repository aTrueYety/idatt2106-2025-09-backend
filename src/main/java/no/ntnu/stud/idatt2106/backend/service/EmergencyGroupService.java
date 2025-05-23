package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.EmergencyGroupMapper;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing emergency groups.
 */
@Service
public class EmergencyGroupService {

  @Autowired
  private EmergencyGroupRepository repository;

  @Autowired
  private GroupHouseholdRepository groupHouseholdRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private ExtraResidentService extraResidentService;

  @Autowired
  private JwtService jwtService;

  /**
   * Creates a new emergency group from the provided request.
   *
   * @param request the request containing emergency group data
   * @param token   the authorization token (not used in this method)
   */
  public void create(EmergencyGroupRequest request, String token) {
    User user = userService.getUserById(jwtService.extractUserId(token.substring(7)));
    Validate.that(user.getHouseholdId(), Validate.isNotNull(), "User must be in a household");
    Validate.that(request.getName(), Validate.isNotNull(), "Group name cannot be null");
    
    EmergencyGroup group = repository.save(EmergencyGroupMapper.toModel(request));
    groupHouseholdRepository.save(new GroupHousehold(null, user.getHouseholdId(), group.getId()));
  }

  /**
   * Retrieves all emergency groups and maps them to response objects.
   *
   * @return a list of EmergencyGroupResponse objects
   */
  public List<EmergencyGroupResponse> getAll() {
    return repository.findAll().stream()
        .map(EmergencyGroupMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Deletes an emergency group by ID.
   *
   * @param id the ID of the emergency group
   * @return true if deleted successfully, false otherwise
   */
  public boolean delete(Long id, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Long householdId = user.getHouseholdId();
    Validate.that(isHouseholdInGroup(householdId, id), Validate.isTrue(),
        "Household with ID = " + householdId + " is not in group with ID = " + id);
    return repository.deleteById(id);
  }

  /**
   * Updates an emergency group by ID.
   *
   * @param id      the group ID
   * @param request the updated values
   * @return true if updated successfully, false otherwise
   */
  public boolean update(Long id, EmergencyGroupRequest request, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    User user = userService.getUserById(userId);
    Long householdId = user.getHouseholdId();
    Validate.that(isHouseholdInGroup(householdId, id), Validate.isTrue(),
        "Household with ID = " + householdId + " is not in group with ID = " + id);
    EmergencyGroup group = new EmergencyGroup(id, request.getName(),
        request.getDescription());
    return repository.update(id, group);
  }

  /**
   * Retrieves an emergency group by its ID.
   *
   * @param id the ID of the emergency group
   * @return the EmergencyGroupResponse object if found, or null if not found
   */
  public EmergencyGroupResponse getById(Long id) {
    return repository.findById(id)
        .map(EmergencyGroupMapper::toResponse)
        .orElseThrow(() -> 
          new NoSuchElementException("Emergency group with ID = " + id + " not found")
        );
  }

  /**
   * Retrieves summaries of emergency groups that a household is part of.
   *
   * @param householdId the ID of the household
   * @return a list of EmergencyGroupSummaryResponse objects
   */
  public List<EmergencyGroupSummaryResponse> getGroupSummariesByHouseholdId(Long householdId) {
    return repository.findGroupSummariesByHouseholdId(householdId);
  }

  /**
   * Retrieves summary data for all emergency groups.
   *
   * @return a list of EmergencyGroupSummaryResponse with member and household
   *         counts
   */
  public List<EmergencyGroupSummaryResponse> getAllSummaries() {
    List<EmergencyGroup> groups = repository.findAll();
    List<GroupHousehold> groupHouseholds = groupHouseholdRepository.findAll();
    List<User> allUsers = userService.getAllUsers();
    List<ExtraResident> allExtras = extraResidentService.getAllEntities();

    return groups.stream().map(group -> {
      Long groupId = group.getId();

      List<GroupHousehold> householdsInGroup = groupHouseholds.stream()
          .filter(gh -> Objects.equals(gh.getGroupId(), groupId))
          .toList();

      Set<Long> householdIds = householdsInGroup.stream()
          .map(GroupHousehold::getHouseholdId)
          .collect(Collectors.toSet());

      int totalHouseholds = householdIds.size();

      int totalResidents = (int) allUsers.stream()
          .filter(user -> user.getHouseholdId() != null && householdIds.contains(
              user.getHouseholdId()))
          .count();

      int totalExtraResidents = (int) allExtras.stream()
          .filter(extra -> extra.getHouseholdId() != null && householdIds.contains(
              extra.getHouseholdId()))
          .count();

      return new EmergencyGroupSummaryResponse(
          groupId,
          group.getName(),
          group.getDescription(),
          totalHouseholds,
          totalResidents,
          totalExtraResidents);
    }).toList();
  }

  /**
   * Retrieves summary data for a specific emergency group by its ID.
   *
   * @param groupId the ID of the emergency group
   * @return an EmergencyGroupSummaryResponse object with member and household
   *         counts
   */
  public EmergencyGroupSummaryResponse getSummaryByGroupId(Long groupId) {
    EmergencyGroup group = repository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

    List<GroupHousehold> householdsInGroup = groupHouseholdRepository.findAll().stream()
        .filter(gh -> Objects.equals(gh.getGroupId(), groupId))
        .toList();

    Set<Long> householdIds = householdsInGroup.stream()
        .map(GroupHousehold::getHouseholdId)
        .collect(Collectors.toSet());

    List<User> allUsers = userService.getAllUsers();
    List<ExtraResident> allExtras = extraResidentService.getAllEntities();

    int totalHouseholds = householdIds.size();

    int totalResidents = (int) allUsers.stream()
        .filter(user -> user.getHouseholdId() != null && householdIds.contains(
          user.getHouseholdId()))
        .count();

    int totalExtraResidents = (int) allExtras.stream()
        .filter(extra -> extra.getHouseholdId() != null && householdIds.contains(
          extra.getHouseholdId()))
        .count();

    return new EmergencyGroupSummaryResponse(
        group.getId(),
        group.getName(),
        group.getDescription(),
        totalHouseholds,
        totalResidents,
        totalExtraResidents);
  }

  /**
   * Checks if a household is in the specified group.
   *
   * @param householdId the id of the household that might be in the group
   * @param groupId the group to check if the household is in
   * @return true if the household is in the group, otherwise false
   */
  public boolean isHouseholdInGroup(Long householdId, Long groupId) {
    return groupHouseholdRepository.findByHouseholdIdAndGroupId(householdId, groupId) != null;
  }
}
