package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.ExtraResidentTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing extra resident types.
 */
@Service
public class ExtraResidentTypeService {
  @Autowired
  private ExtraResidentTypeRepository repository;

  /**
   * Create a new extra resident type.
   *
   * @param request the request containing the details of the extra resident type
   *                to create
   */
  public void create(ExtraResidentTypeRequest request) {
    ExtraResidentType type = ExtraResidentTypeMapper.toModel(request);
    repository.save(type);
  }

  /**
   * Retrieves all extra resident types.
   *
   * @return a list of ExtraResidentTypeResponse representing all extra resident
   *         types
   */
  public List<ExtraResidentTypeResponse> getAll() {
    return repository.findAll().stream()
        .map(ExtraResidentTypeMapper::toResponse)
        .toList();
  }

  /**
   * Get extra resident type by ID.
   *
   * @param id the ID of the extra resident type to retrieve
   * @return an Optional containing the ExtraResidentTypeResponse if found, empty
   *         otherwise
   */
  public Optional<ExtraResidentTypeResponse> getById(long id) {
    return repository.findById(id).map(ExtraResidentTypeMapper::toResponse);
  }

  /**
   * Update existing extra resident type.
   *
   * @param id      the ID of the extra resident type to update
   * @param request the request containing the updated details of the extra
   *                resident type
   * @return true if updated, false if not found
   */
  public boolean update(long id, ExtraResidentTypeRequest request) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    ExtraResidentType type = ExtraResidentTypeMapper.toModel(request);
    type.setId(id);
    repository.update(type);
    return true;
  }

  /**
   * Delete extra resident type by ID.
   *
   * @param id the ID of the extra resident type to delete
   * @return true if deleted, false if not found
   */
  public boolean delete(long id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }
}
