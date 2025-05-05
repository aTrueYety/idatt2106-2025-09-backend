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
import no.ntnu.stud.idatt2106.backend.util.Validate;
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
   * @param repository         the food repository
   * @param foodTypeRepository the food type repository
   */
  public FoodService(FoodRepository repository, FoodTypeRepository foodTypeRepository) {
    this.repository = repository;
    this.foodTypeRepository = foodTypeRepository;
  }

  /**
   * Creates a new food item.
   *
   * @param request the food request containing food details
   */
  public void create(FoodRequest request) {
    Validate.that(request.getAmount(), Validate.isPositive());

    Food food = FoodMapper.toModel(request);
    repository.save(food);
  }

  /**
   * Gets a food item by its ID.
   *
   * @param id the ID of the food item
   * @return an Optional containing FoodResponse if found, otherwise empty
   */
  public Optional<FoodResponse> getById(Long id) {
    return repository.findById(id).map(FoodMapper::toResponse);
  }

  /**
   * Gets all food items.
   *
   * @return a list of all food items
   */
  public List<FoodResponse> getAll() {
    return repository.findAll().stream()
        .map(FoodMapper::toResponse)
        .toList();
  }

  /**
   * Updates an existing food item.
   *
   * @param id     the ID of the food item to update
   * @param update the update request
   * @return true if updated, false if not found
   */
  public boolean update(Long id, FoodUpdate update) {
    Validate.that(update.getAmount(), Validate.isPositive());
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    Food food = FoodMapper.toModel(update);
    food.setId(id);
    repository.update(food);
    return true;
  }

  /**
   * Deletes a food item by its ID.
   *
   * @param id the ID of the food item to delete
   * @return true if deleted, false if not found
   */
  public boolean delete(Long id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }

  /**
   * Gets food items by household ID.
   *
   * @param householdId the ID of the household
   * @return a list of food items for the given household
   */
  public List<FoodResponse> getByHouseholdId(Long householdId) {
    return repository.findByHouseholdId(householdId).stream()
        .map(FoodMapper::toResponse)
        .toList();
  }

  /**
   * Get the total amount of calories in a household by its ID.
   *
   * @param householdId the ID of the household
   * @return the total calories in the household
   */
  public double getCaloriesByHouseholdId(Long householdId) {
    FoodTypeService foodTypeService = new FoodTypeService(foodTypeRepository);

    return repository.findByHouseholdId(householdId).stream()
        .mapToDouble(food -> food.getAmount() * foodTypeService.getCaloriesById(food.getTypeId()))
        .sum();
  }

  /**
   * Gets food summary by household ID.
   * Groups food items by type and sums their amount.
   *
   * @param householdId the ID of the household
   * @return a list of FoodSummaryResponse grouped by type
   */
  public List<FoodSummaryResponse> getFoodSummaryByHousehold(Long householdId) {
    List<Food> foodList = repository.findByHouseholdId(householdId);

    return foodList.stream()
        .collect(Collectors.groupingBy(Food::getTypeId,
            Collectors.summingDouble(f -> f.getAmount())))
        .entrySet().stream()
        .map(entry -> {
          FoodSummaryResponse response = new FoodSummaryResponse();
          response.setTypeId(entry.getKey());
          response.setTotalAmount(((Double) entry.getValue()).floatValue()
          );
          return response;
        })

        .toList();
  }

  /**
   * Gets detailed food information by household ID.
   * Includes food batches, type details, and total calories.
   *
   * @param householdId the ID of the household
   * @return a list of FoodDetailedResponse with detailed info
   */
  public List<FoodDetailedResponse> getFoodDetailedByHousehold(Long householdId) {
    List<Food> foods = repository.findByHouseholdId(householdId);

    Map<Long, List<Food>> grouped = foods.stream()
        .collect(Collectors.groupingBy(Food::getTypeId));

    return grouped.entrySet().stream()
        .map(entry -> {
          Long typeId = entry.getKey();
          Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
          if (typeOpt.isEmpty()) {
            return null;
          }
          FoodType type = typeOpt.get();
          List<Food> foodList = entry.getValue();
          FoodDetailedResponse summary = new FoodDetailedResponse();
          summary.setTypeId(typeId);
          summary.setTypeName(type.getName());
          summary.setUnit(type.getUnit());

          float totalAmount = (float) foodList.stream()
              .mapToDouble(Food::getAmount)
              .sum();
          summary.setTotalAmount(totalAmount);

          float totalCalories = totalAmount * type.getCaloriesPerUnit();
          summary.setTotalCalories(totalCalories);

          List<FoodBatchResponse> batches = foodList.stream()
              .map(f -> {
                FoodBatchResponse batch = new FoodBatchResponse();
                batch.setId(f.getId());
                batch.setAmount(f.getAmount());
                batch.setExpirationDate(f.getExpirationDate());
                return batch;
              })
              .toList();

          summary.setBatches(batches);
          return summary;
        })
        .filter(Objects::nonNull)
        .toList();
  }
}