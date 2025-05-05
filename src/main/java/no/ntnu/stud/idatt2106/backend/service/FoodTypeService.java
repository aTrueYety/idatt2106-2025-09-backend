package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.FoodTypeMapper;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing food types.
 */
@Service
public class FoodTypeService {

  private final FoodTypeRepository repository;

  /**
   * Constructs a FoodTypeService with the given repository.
   *
   * @param repository the food type repository
   */
  public FoodTypeService(FoodTypeRepository repository) {
    this.repository = repository;
  }

  /**
   * Create a new food type.
   *
   * @param request the food type request containing type details
   */
  public void create(FoodTypeRequest request) {
    FoodType foodType = FoodTypeMapper.toModel(request);
    repository.save(foodType);
  }

  /**
   * Get all food types.
   *
   * @return List of FoodTypeResponse for all food types
   */
  public List<FoodTypeResponse> getAll() {
    return repository.findAll().stream()
        .map(FoodTypeMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Get food type by ID.
   *
   * @param id the ID of the food type
   * @return Optional containing FoodTypeResponse if found, empty otherwise
   */
  public Optional<FoodTypeResponse> getById(Long id) {
    return repository.findById(id)
        .map(FoodTypeMapper::toResponse);
  }

  /**
   * Update an existing food type.
   *
   * @param id      the ID of the food type to update
   * @param request the update request
   * @return true if updated, false if not found
   */
  public boolean update(Long id, FoodTypeRequest request) {
    return repository.findById(id)
        .map(existing -> {
          FoodType updated = FoodTypeMapper.toModel(request);
          updated.setId(id);
          repository.update(updated);
          return true;
        })
        .orElse(false);
  }

  /**
   * Delete a food type by ID.
   *
   * @param id the ID of the food type to delete
   * @return true if deleted, false if not found
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
   * Search for food types by name.
   *
   * @param query the search query string
   * @return List of matching FoodTypeResponse objects
   */
  public List<FoodTypeResponse> searchByName(String query) {
    return repository.findByNameContainingIgnoreCase(query).stream()
        .map(FoodTypeMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Get calories per unit for a given food type ID.
   *
   * @param id the ID of the food type
   * @return calories per unit
   * @throws NoSuchElementException if the food type is not found
   */
  public float getCaloriesById(Long id) {
    return repository.findById(id)
        .map(FoodType::getCaloriesPerUnit)
        .orElseThrow(() -> new NoSuchElementException("Food type not found with id = " + id));
  }
}