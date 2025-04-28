package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.FoodMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodBatchResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodSummaryResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing food items and related operations.
 */
@Service
public class FoodService {

  private final FoodRepository repository;
  private final FoodTypeRepository foodTypeRepository;

  /**
   * Constructs a FoodService with given repositories.
   *
   * @param repository the food repository
   * @param foodTypeRepository the food type repository
   */
  public FoodService(FoodRepository repository, FoodTypeRepository foodTypeRepository) {
    this.repository = repository;
    this.foodTypeRepository = foodTypeRepository;
  }

  /**
   * Create a new food item.
   *
   * @param request the food request containing food details
   */
  public void create(FoodRequest request) {
    Food food = FoodMapper.toModel(request);
    repository.save(food);
  }

  /**
   * Get a food item by its ID.
   *
   * @param id the ID of the food item
   * @return Optional containing FoodResponse if found, empty otherwise
   */
  public Optional<FoodResponse> getById(int id) {
    return repository.findById(id).map(FoodMapper::toResponse);
  }

  /**
   * Get all food items.
   *
   * @return List of FoodResponse for all food items
   */
  public List<FoodResponse> getAll() {
    return repository.findAll().stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Update an existing food item.
   *
   * @param id the ID of the food item to update
   * @param update the update request
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
   * Delete a food item by its ID.
   *
   * @param id the ID of the food item to delete
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
   * @param householdId the household ID
   * @return List of FoodResponse for the given household
   */
  public List<FoodResponse> getByHouseholdId(int householdId) {
    return repository.findByHouseholdId(householdId).stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Get food summary by household ID.
   * Groups food by type and sums their amount.
   *
   * @param householdId the household ID
   * @return List of FoodSummaryResponse grouped by type
   */
  public List<FoodSummaryResponse> getFoodSummaryByHousehold(int householdId) {
    List<Food> foodList = repository.findByHouseholdId(householdId);

    return foodList.stream()
        .collect(Collectors.groupingBy(Food::getTypeId, Collectors.summingInt(Food::getAmount)))
        .entrySet().stream()
        .map(entry -> {
          FoodSummaryResponse response = new FoodSummaryResponse();
          response.setTypeId(entry.getKey());
          response.setTotalAmount(entry.getValue());
          return response;
        })
        .collect(Collectors.toList());
  }

  /**
   * Get detailed food information by household ID.
   * Includes food batches and type details.
   *
   * @param householdId the household ID
   * @return List of FoodDetailedResponse with detailed info
   */
  public List<FoodDetailedResponse> getFoodDetailedByHousehold(int householdId) {
    List<Food> foods = repository.findByHouseholdId(householdId);

    Map<Integer, List<Food>> grouped = foods.stream()
        .collect(Collectors.groupingBy(Food::getTypeId));

    return grouped.entrySet().stream()
        .map(entry -> {
          int typeId = entry.getKey();
          List<Food> foodList = entry.getValue();

          Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
          if (typeOpt.isEmpty()) {
            return null;
          }

          FoodType type = typeOpt.get();
          FoodDetailedResponse summary = new FoodDetailedResponse();
          summary.setTypeId(typeId);
          summary.setTypeName(type.getName());
          summary.setUnit(type.getUnit());
          summary.setTotalAmount(foodList.stream()
              .mapToInt(Food::getAmount)
              .sum());

          List<FoodBatchResponse> batches = foodList.stream()
              .map(f -> {
                FoodBatchResponse batch = new FoodBatchResponse();
                batch.setAmount(f.getAmount());
                batch.setExpirationDate(f.getExpirationDate());
                return batch;
              })
              .collect(Collectors.toList());

          summary.setBatches(batches);
          return summary;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
