package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.EmergencyGroupMapper;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing emergency groups.
 */
@Service
@RequiredArgsConstructor
public class EmergencyGroupService {

  private final EmergencyGroupRepository repository;

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

}
