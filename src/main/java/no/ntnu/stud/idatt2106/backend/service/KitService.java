package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.KitMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Kit;
import no.ntnu.stud.idatt2106.backend.model.request.KitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.KitResponse;
import no.ntnu.stud.idatt2106.backend.repository.KitRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing kits.
 */
@Service
public class KitService {
  
  private final KitRepository repository;

  public KitService(KitRepository repository) {
    this.repository = repository;
  }

  /**
   * Create a new kit.
   *
   * @param request the kit request containing kit details
   */
  public void create(KitRequest request) {
    Kit kit = KitMapper.toModel(request);
    repository.save(kit);
  }
  
  /**
   * Get all kits.
   *
   * @return List of KitResponse for all kits
   */
  public List<KitResponse> getAll() {
    return repository.findAll().stream()
        .map(KitMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Get Kit by ID.
   *
   * @param id the ID of the kit
   * @return Optional containing KitResponse if found, empty otherwise
   */
  public Optional<KitResponse> getById(Long id) {
    return repository.findById(id)
        .map(KitMapper::toResponse);
  }

  /**
   * Update an existing kit.
   *
   * @param id the ID of the kit to update
   * @param request the kit request containing updated kit details
   * @return true if the kit was updated successfully, false otherwise
   */
  public boolean update(Long id, KitRequest request) {
    return repository.findById(id)
        .map(existing -> {
          Kit updated = KitMapper.toModel(request);
          updated.setId(id);
          repository.update(updated);
          return true;
        })
        .orElse(false);
  }

  /**
   * Delete a kit by ID.
   *
   * @param id the ID of the kit to delete
   * @return true if the kit was deleted successfully, false otherwise
   */
  public boolean delete(Long id) {
    return repository.findById(id)
    .map(existing -> {
      repository.deleteById(id);
      return true;
    })
    .orElse(false);
  }

  /**
   * Find kits by name containing the given query, ignoring case.
   *
   * @param query the search query to match against kit names
   * @return a list of matching KitResponse entities
   */
  public List<KitResponse> searchByName(String query) {
    return repository.findByNameContainingIgnoreCase(query).stream()
        .map(KitMapper::toResponse)
        .collect(Collectors.toList());
  }
}
