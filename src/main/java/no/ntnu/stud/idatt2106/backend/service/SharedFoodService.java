package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodBatchResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepository;
import no.ntnu.stud.idatt2106.backend.repository.SharedFoodRepository;
import no.ntnu.stud.idatt2106.backend.service.mapper.SharedFoodMapper;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to shared food in group
 * households.
 */
@Service
@RequiredArgsConstructor
public class SharedFoodService {

  private final SharedFoodRepository repository;
  private final FoodRepository foodRepository;
  private final HouseholdRepository householdRepository;
  private final FoodTypeRepository foodTypeRepository;
  private final GroupHouseholdRepository groupHouseholdRepository;
  private final JwtService jwtService;

  /**
   * Creates a new shared food entry in the repository.
   *
   * @param request The request containing food ID, group household ID, and
   *                amount.
   * @param token   The JWT token of the user performing the operation.
   */
  public void create(SharedFoodRequest request, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
    if (foodOpt.isEmpty()) {
      throw new IllegalArgumentException("Food not found");
    }

    Food food = foodOpt.get();
    if (!Objects.equals(food.getHouseholdId(), householdId)) {
      throw new IllegalStateException("Food does not belong to your household");
    }

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, request.getGroupId());
    if (groupHousehold == null) {
      throw new IllegalStateException("Group household membership not found");
    }

    repository.save(SharedFoodMapper.toModel(
        food.getId(),
        groupHousehold.getId(),
        request.getAmount()));
  }

  /**
   * Retrieves all shared food items from the repository.
   *
   * @return A list of shared food responses.
   */
  public List<SharedFoodResponse> getAll() {
    return repository.findAll().stream()
        .map(SharedFoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Updates the amount of a shared food item in the repository.
   *
   * @param request The request containing updated information.
   * @param token   The JWT token of the user performing the operation.
   * @return true if the update was successful, false otherwise.
   */
  public boolean update(SharedFoodRequest request, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, request.getGroupId());

    if (groupHousehold == null) {
      return false;
    }

    SharedFood food = SharedFoodMapper.toModel(
        request.getFoodId(),
        groupHousehold.getId(),
        request.getAmount());

    return repository.update(food);
  }

  /**
   * Deletes a shared food item from the repository.
   *
   * @param foodId  The ID of the food item to delete.
   * @param groupId The ID of the group household.
   * @param token   The JWT token of the user performing the operation.
   * @return true if the deletion was successful, false otherwise.
   */
  public boolean delete(Long foodId, Long groupId, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, groupId);

    if (groupHousehold == null) {
      return false;
    }

    return repository.deleteById(new SharedFoodKey(foodId, groupHousehold.getId()));
  }

  /**
   * Moves food from the household to a shared group.
   *
   * @param request The request containing food details and amount to move.
   * @param token   The JWT token of the user performing the operation.
   * @return true if the operation was successful, false otherwise.
   */
  public boolean moveFoodToSharedGroup(SharedFoodRequest request, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
    if (foodOpt.isEmpty()) {
      return false;
    }

    Food food = foodOpt.get();
    if (!Objects.equals(food.getHouseholdId(), householdId)) {
      return false;
    }
    if (food.getAmount() < request.getAmount()) {
      return false;
    }

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, request.getGroupId());
    if (groupHousehold == null) {
      return false;
    }

    SharedFoodKey key = new SharedFoodKey(food.getId(), groupHousehold.getId());

    Optional<SharedFood> existingSharedOpt = repository.findById(key);
    if (existingSharedOpt.isPresent()) {
      SharedFood existing = existingSharedOpt.get();
      existing.setAmount(existing.getAmount() + request.getAmount());
      repository.update(existing);
    } else {
      repository.save(new SharedFood(key, request.getAmount()));
    }

    food.setAmount(food.getAmount() - request.getAmount());
    foodRepository.update(food);

    return true;
  }

  /**
   * Moves food from a shared group back to the household.
   *
   * @param request The request containing food details and amount to move.
   * @param token   The JWT token of the user performing the operation.
   * @return true if the operation was successful, false otherwise.
   */
  public boolean moveFoodFromSharedGroup(SharedFoodRequest request, String token) {
    Long userId = jwtService.extractUserId(token.substring(7));
    Long householdId = getHouseholdIdByUser(userId);

    Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
    if (foodOpt.isEmpty()) {
      return false;
    }
    Food food = foodOpt.get();

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, request.getGroupId());
    if (groupHousehold == null) {
      return false;
    }

    SharedFoodKey key = new SharedFoodKey(food.getId(), groupHousehold.getId());
    Optional<SharedFood> sharedOpt = repository.findById(key);
    if (sharedOpt.isEmpty()) {
      return false;
    }

    SharedFood shared = sharedOpt.get();
    if (shared.getAmount() < request.getAmount()) {
      return false;
    }

    shared.setAmount(shared.getAmount() - request.getAmount());
    if (shared.getAmount() == 0) {
      repository.deleteById(key);
    } else {
      repository.update(shared);
    }

    Optional<Food> existingOpt = foodRepository.findByTypeIdAndExpirationDateAndHouseholdId(
        food.getTypeId(), food.getExpirationDate(), food.getHouseholdId());

    if (existingOpt.isPresent()) {
      Food existing = existingOpt.get();
      existing.setAmount(existing.getAmount() + request.getAmount());
      foodRepository.update(existing);
    } else {
      Food newFood = new Food();
      newFood.setTypeId(food.getTypeId());
      newFood.setExpirationDate(food.getExpirationDate());
      newFood.setAmount(request.getAmount());
      newFood.setHouseholdId(food.getHouseholdId());
      foodRepository.save(newFood);
    }

    return true;
  }

  /**
   * Retrieves a summary of shared food items for a specific group household.
   *
   * @param groupHouseholdId The ID of the group household.
   * @return A list of detailed responses containing shared food information.
   */
  public List<FoodDetailedResponse> getSharedFoodSummaryByGroup(Long groupHouseholdId) {
    List<SharedFood> sharedFoods = repository.findByGroupHouseholdId(groupHouseholdId);
    Map<Long, Food> foodMap = sharedFoods.stream()
        .map(sf -> foodRepository.findById(sf.getId().getFoodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Food::getId, f -> f));

    Map<Long, List<SharedFood>> grouped = sharedFoods.stream()
        .filter(sf -> foodMap.containsKey(sf.getId().getFoodId()))
        .collect(Collectors.groupingBy(sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

    return grouped.entrySet().stream().map(entry -> {
      Long typeId = entry.getKey();
      Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
      if (typeOpt.isEmpty()) {
        return null;
      }

      FoodType type = typeOpt.get();
      List<FoodBatchResponse> batches = entry.getValue().stream().map(sf -> {
        Food food = foodMap.get(sf.getId().getFoodId());
        FoodBatchResponse b = new FoodBatchResponse();
        b.setId(food.getId());
        b.setAmount(sf.getAmount());
        b.setExpirationDate(food.getExpirationDate());
        b.setHouseholdId(food.getHouseholdId());
        b.setGroupHouseholdId(sf.getId().getGroupHouseholdId());
        return b;
      }).toList();

      float totalAmount = (float) batches.stream().mapToDouble(FoodBatchResponse::getAmount).sum();
      float totalCalories = totalAmount * type.getCaloriesPerUnit();

      FoodDetailedResponse response = new FoodDetailedResponse();
      response.setTypeId(type.getId());
      response.setTypeName(type.getName());
      response.setUnit(type.getUnit());
      response.setTotalAmount(totalAmount);
      response.setTotalCalories(totalCalories);
      response.setBatches(batches);
      return response;
    }).filter(Objects::nonNull).toList();
  }

  /**
   * Retrieves a summary of shared food items for all households in a specific
   * group.
   *
   * @param groupId The ID of the group.
   * @return A list of detailed responses containing shared food information.
   */
  public List<FoodDetailedResponse> getSharedFoodSummaryByGroupId(Long groupId) {
    List<GroupHousehold> memberships = groupHouseholdRepository.findByGroupId(groupId);
    List<SharedFood> sharedFoods = memberships.stream()
        .flatMap(m -> repository.findByGroupHouseholdId(m.getId()).stream())
        .toList();

    Map<Long, Food> foodMap = sharedFoods.stream()
        .map(sf -> foodRepository.findById(sf.getId().getFoodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Food::getId, f -> f));

    Map<Long, List<SharedFood>> grouped = sharedFoods.stream()
        .filter(sf -> foodMap.containsKey(sf.getId().getFoodId()))
        .collect(Collectors.groupingBy(sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

    return grouped.entrySet().stream().map(entry -> {
      Long typeId = entry.getKey();
      Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
      if (typeOpt.isEmpty()) {
        return null;
      }

      FoodType type = typeOpt.get();
      List<FoodBatchResponse> batches = entry.getValue().stream().map(sf -> {
        Food food = foodMap.get(sf.getId().getFoodId());
        FoodBatchResponse b = new FoodBatchResponse();
        b.setId(food.getId());
        b.setAmount(sf.getAmount());
        b.setExpirationDate(food.getExpirationDate());
        b.setHouseholdId(food.getHouseholdId());
        b.setGroupHouseholdId(sf.getId().getGroupHouseholdId());
        return b;
      }).toList();

      float totalAmount = (float) batches.stream().mapToDouble(FoodBatchResponse::getAmount).sum();
      float totalCalories = totalAmount * type.getCaloriesPerUnit();

      FoodDetailedResponse response = new FoodDetailedResponse();
      response.setTypeId(type.getId());
      response.setTypeName(type.getName());
      response.setUnit(type.getUnit());
      response.setTotalAmount(totalAmount);
      response.setTotalCalories(totalCalories);
      response.setBatches(batches);
      return response;
    }).filter(Objects::nonNull).toList();
  }

  private Long getHouseholdIdByUser(Long userId) {
    return householdRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Household not found for user"))
        .getId();
  }

  /**
   * Unshares all food items from a specific group for a user.
   *
   * @param userId  The ID of the user.
   * @param groupId The ID of the group.
   */
  public void unshareAllFromGroup(Long userId, Long groupId) {
    Long householdId = getHouseholdIdByUser(userId);
  
    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(householdId, groupId);
    if (groupHousehold == null) {
      return; 
    }
  
    List<SharedFood> sharedFoods = repository.findByGroupHouseholdId(groupHousehold.getId());
    for (SharedFood shared : sharedFoods) {
      Long foodId = shared.getId().getFoodId();
  
      Optional<Food> foodOpt = foodRepository.findById(foodId);
      if (foodOpt.isEmpty()) continue;
  
      Food food = foodOpt.get();
      if (!Objects.equals(food.getHouseholdId(), householdId)) continue;
  
      float amount = shared.getAmount();
  
      Optional<Food> existingOpt = foodRepository.findByTypeIdAndExpirationDateAndHouseholdId(
          food.getTypeId(), food.getExpirationDate(), householdId);
  
      if (existingOpt.isPresent()) {
        Food existing = existingOpt.get();
        existing.setAmount(existing.getAmount() + amount);
        foodRepository.update(existing);
      } else {
        Food newFood = new Food();
        newFood.setTypeId(food.getTypeId());
        newFood.setExpirationDate(food.getExpirationDate());
        newFood.setAmount(amount);
        newFood.setHouseholdId(householdId);
        foodRepository.save(newFood);
      }
  
      repository.deleteById(shared.getId());
    }
  }
  
}
