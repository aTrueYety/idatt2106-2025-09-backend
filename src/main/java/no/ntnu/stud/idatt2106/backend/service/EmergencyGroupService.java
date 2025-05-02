package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.EmergencyGroupMapper;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing emergency groups.
 */
@Service
@RequiredArgsConstructor
public class EmergencyGroupService {

  private final EmergencyGroupRepository repository;
  private final EmergencyGroupMapper mapper;

  /**
   * Creates a new emergency group from the provided request.
   *
   * @param request the request containing emergency group data
   */
  public void create(EmergencyGroupRequest request) {
    repository.save(mapper.toModel(request));
  }

  /**
   * Retrieves all emergency groups and maps them to response objects.
   *
   * @return a list of EmergencyGroupResponse objects
   */
  public List<EmergencyGroupResponse> getAll() {
    return repository.findAll().stream()
        .map(mapper::toResponse)
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
    EmergencyGroup group = new EmergencyGroup(id, request.getName(), request.getDescription());
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
        .map(mapper::toResponse)
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
}
