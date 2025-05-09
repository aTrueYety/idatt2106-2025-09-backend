package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.FoodMapper;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing food items and related operations.
 */
@Service
public class FoodService {

  @Autowired
  private FoodRepository repository;

  @Autowired
  private FoodTypeRepository foodTypeRepository;

  @Autowired
  private FoodTypeService foodTypeService;

  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private JwtService jwtService;

  /**
   * Creates a new food item, only for the user's household.
   *
   * @param request the food request containing food details
   * @param token   the JWT token from the logged-in user
   */
  public void create(FoodRequest request, String token) {
    Validate.that(request.getAmount(), Validate.isPositive());

    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    Food food = FoodMapper.toModel(request);
    if (!Objects.equals(food.getHouseholdId(), householdId)) {
      throw new IllegalStateException("Cannot create food for another household.");
    }

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
   * Updates an existing food item, only if it belongs to user's household.
   *
   * @param id     the ID of the food item to update
   * @param update the update request
   * @param token  the JWT token from the logged-in user
   * @return true if updated, false if not found or not allowed
   */
  public boolean update(Long id, FoodUpdate update, String token) {
    Validate.that(update.getAmount(), Validate.isPositive());
    Optional<Food> existing = repository.findById(id);
    if (existing.isEmpty()) {
      return false;
    }

    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    if (!Objects.equals(existing.get().getHouseholdId(), householdId)) {
      return false;
    }

    Food food = FoodMapper.toModel(update);
    food.setId(id);
    food.setHouseholdId(householdId); // ensure correct household
    repository.update(food);
    return true;
  }

  /**
   * Deletes a food item by its ID, only if it belongs to the user.
   *
   * @param id    the ID of the food item to delete
   * @param token the JWT token from the logged-in user
   * @return true if deleted, false if not found or not allowed
   */
  public boolean delete(Long id, String token) {
    Optional<Food> existing = repository.findById(id);
    if (existing.isEmpty()) {
      return false;
    }

    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    if (!Objects.equals(existing.get().getHouseholdId(), householdId)) {
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
            Collectors.summingDouble(Food::getAmount)))
        .entrySet().stream()
        .map(entry -> {
          FoodSummaryResponse response = new FoodSummaryResponse();
          response.setTypeId(entry.getKey());
          response.setTotalAmount(((Double) entry.getValue()).floatValue());
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
   * @throws NoSuchElementException if no foodtype with a given ID is found
   */
  public List<FoodDetailedResponse> getFoodDetailedByHousehold(Long householdId) {
    List<Food> foods = repository.findByHouseholdId(householdId);

    Map<Long, List<Food>> grouped = foods.stream()
        .collect(Collectors.groupingBy(Food::getTypeId));

    return grouped.entrySet().stream()
        .map(entry -> {
          Long typeId = entry.getKey();
          FoodType type = foodTypeRepository.findById(typeId)
              .orElseThrow(() -> new NoSuchElementException("FoodType with ID = " 
              + typeId + " not found"));

          List<Food> foodList = entry.getValue();
          float totalAmount = (float) foodList.stream().mapToDouble(Food::getAmount).sum();
          float totalCalories = totalAmount * type.getCaloriesPerUnit();

          List<FoodBatchResponse> batches = foodList.stream().map(f -> {
            FoodBatchResponse b = new FoodBatchResponse();
            b.setId(f.getId());
            b.setAmount(f.getAmount());
            b.setExpirationDate(f.getExpirationDate());
            b.setHouseholdId(f.getHouseholdId());
            return b;
          }).toList();

          FoodDetailedResponse summary = new FoodDetailedResponse();
          summary.setTypeId(typeId);
          summary.setTypeName(type.getName());
          summary.setUnit(type.getUnit());
          summary.setTotalAmount(totalAmount);
          summary.setTotalCalories(totalCalories);
          summary.setBatches(batches);
          return summary;
        })
        .filter(Objects::nonNull)
        .toList();
  }

  private Long getHouseholdIdByUser(Long userId) {
    return householdRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Household not found for user"))
        .getId();
  }
}
