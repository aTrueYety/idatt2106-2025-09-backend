package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.EmergencyGroupMapper;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing emergency groups.
 */
@Service
@RequiredArgsConstructor
public class EmergencyGroupService {

  private final EmergencyGroupRepository repository;
  private final GroupHouseholdRepository groupHouseholdRepository;
  private final UserService userService;
  private final ExtraResidentService extraResidentService;

  /**
   * Creates a new emergency group from the provided request.
   *
   * @param request the request containing emergency group data
   */
  public void create(EmergencyGroupRequest request) {
    repository.save(EmergencyGroupMapper.toModel(request));
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
  public boolean delete(int id) {
    return repository.deleteById(id);
  }

  /**
   * Updates an emergency group by ID.
   *
   * @param id      the group ID
   * @param request the updated values
   * @return true if updated successfully, false otherwise
   */
  public boolean update(int id, EmergencyGroupRequest request) {
    EmergencyGroup group = new EmergencyGroup((long) id, request.getName(),
        request.getDescription());
    return repository.update(id, group);
  }

  /**
   * Retrieves an emergency group by its ID.
   *
   * @param id the ID of the emergency group
   * @return the EmergencyGroupResponse object if found, or null if not found
   */
  public EmergencyGroupResponse getById(int id) {
    return repository.findById(id)
        .map(EmergencyGroupMapper::toResponse)
        .orElse(null);
  }

  /**
   * Retrieves summaries of emergency groups that a household is part of.
   *
   * @param householdId the ID of the household
   * @return a list of EmergencyGroupSummaryResponse objects
   */
  public List<EmergencyGroupSummaryResponse> getGroupSummariesByHouseholdId(int householdId) {
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
    EmergencyGroup group = repository.findById(groupId.intValue())
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

}
