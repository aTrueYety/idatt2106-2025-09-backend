package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.GroupHouseholdMapper;
import no.ntnu.stud.idatt2106.backend.model.request.GroupHouseholdRequest;
import no.ntnu.stud.idatt2106.backend.model.response.GroupHouseholdResponse;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing group-household relations.
 */
@Service
@RequiredArgsConstructor
public class GroupHouseholdService {

  private final GroupHouseholdRepository repository;

  public void create(GroupHouseholdRequest request) {
    repository.save(GroupHouseholdMapper.toModel(request));
  }

  /**
   * Retrieves all group-household relations.
   *
   * @return a list of GroupHouseholdResponse objects representing all group-household relations.
   */
  public List<GroupHouseholdResponse> getAll() {
    return repository.findAll().stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all group-household relations for a specific group ID.
   *
   * @param groupId the ID of the group whose household relations are to be retrieved.
   * @return a list of GroupHouseholdResponse objects representing the group-household relations.
   */
  public List<GroupHouseholdResponse> getByGroupId(int groupId) {
    return repository.findByGroupId(groupId).stream()
        .map(GroupHouseholdMapper::toResponse)
        .collect(Collectors.toList());
  }

  public boolean delete(int id) {
    return repository.deleteById(id);
  }
}
