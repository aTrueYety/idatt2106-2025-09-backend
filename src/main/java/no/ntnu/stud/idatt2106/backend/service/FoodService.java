package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.FoodMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing food items.
 */
@Service
public class FoodService {
  @Autowired
  private FoodRepository repository;

  /**
   * Create a new food item.
   */
  public void create(FoodRequest request) {
    Food food = FoodMapper.toModel(request);
    repository.save(food);
  }

  /**
   * Get food item by ID.
   *
   * @return Optional containing FoodResponse if found, empty otherwise
   */
  public Optional<FoodResponse> getById(int id) {
    return repository.findById(id).map(FoodMapper::toResponse);
  }

  public List<FoodResponse> getAll() {
    return repository.findAll().stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Update existing food item.
   *
   * @return true if updated, false if not found
   */
  public boolean update(int id, FoodUpdate update) {
    if (repository.findById(id).isEmpty()) {
      return false; 
    }
    Food food = FoodMapper.toModel(update);
    food.setId(id);
    repository.update(food);
    return true;
  }

  /**
   * Delete food item by ID.
   *
   * @return true if deleted, false if not found
   */
  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }

  /**
   * Get food items by household ID.
   *
   * @return List of FoodResponse for the given household ID
   */
  public List<FoodResponse> getByHouseholdId(int householdId) {
    return repository.findByHouseholdId(householdId).stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

}
