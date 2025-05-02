package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ExtraResidentService {

  private final ExtraResidentRepository repository;

  public void create(ExtraResidentRequest request) {
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    repository.save(resident);
  }

  /**
   * Retrieves all extra residents and maps them to response objects.
   *
   * @return a list of ExtraResidentResponse objects
   */
  public List<ExtraResidentResponse> getAll() {
    return repository.findAll().stream()
        .map(ExtraResidentMapper::toResponse)
        .collect(Collectors.toList());
  }

  public List<ExtraResident> getAllEntities() {
    return repository.findAll();
  }

  public Optional<ExtraResidentResponse> getById(Long id) {
    return repository.findById(id)
        .map(ExtraResidentMapper::toResponse);
  }

  /**
   * Updates an existing extra resident with the provided ID and request data.
   *
   * @param id      the ID of the extra resident to update
   * @param request the request containing updated data
   * @return true if the resident was updated, false if the resident was not found
   */
  public boolean update(Long id, ExtraResidentUpdate request) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    resident.setId(id);
    repository.update(resident);
    return true;
  }

  /**
   * Deletes an extra resident by their ID.
   *
   * @param id the ID of the extra resident to delete
   * @return true if the resident was deleted, false if the resident was not found
   */
  public boolean delete(Long id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }
}
