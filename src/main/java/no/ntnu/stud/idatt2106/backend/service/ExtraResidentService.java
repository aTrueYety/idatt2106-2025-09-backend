package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.ExtraResidentMapper;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing extra residents.
 */
@Service
public class ExtraResidentService {

  private final ExtraResidentRepository repository;

  /**
   * Constructs an ExtraResidentService with the given repository.
   *
   * @param repository the extra resident repository
   */
  public ExtraResidentService(ExtraResidentRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a new extra resident.
   *
   * @param request the request containing the details of the extra resident to create
   */
  public void create(ExtraResidentRequest request) {
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    repository.save(resident);
  }

  /**
   * Gets all extra residents.
   *
   * @return a list of ExtraResidentResponse for all extra residents
   */
  public List<ExtraResidentResponse> getAll() {
    return repository.findAll().stream()
        .map(ExtraResidentMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Gets an extra resident by ID.
   *
   * @param id the ID of the extra resident to retrieve
   * @return an Optional containing the ExtraResidentResponse if found, empty otherwise
   */
  public Optional<ExtraResidentResponse> getById(int id) {
    return repository.findById(id)
        .map(ExtraResidentMapper::toResponse);
  }

  /**
   * Updates an existing extra resident.
   *
   * @param id the ID of the extra resident to update
   * @param request the request containing the updated details of the extra resident
   * @return true if updated, false if not found
   */
  public boolean update(int id, ExtraResidentUpdate request) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    resident.setId(id);
    repository.update(resident);
    return true;
  }

  /**
   * Deletes an extra resident by ID.
   *
   * @param id the ID of the extra resident to delete
   * @return true if deleted, false if not found
   */
  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }
}
