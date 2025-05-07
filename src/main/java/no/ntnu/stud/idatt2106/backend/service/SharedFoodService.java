package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.SharedFoodMapper;
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
import no.ntnu.stud.idatt2106.backend.repository.SharedFoodRepository;
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
  private final FoodTypeRepository foodTypeRepository;
  private final GroupHouseholdRepository groupHouseholdRepository;

  /**
   * Creates a new shared food entry.
   *
   * @param request the shared food request to be persisted
   */
  public void create(SharedFoodRequest request) {
    repository.save(SharedFoodMapper.toModel(request));
  }

  /**
   * Retrieves all shared food entries in the system.
   *
   * @return list of shared food responses
   */
  public List<SharedFoodResponse> getAll() {
    return repository.findAll().stream()
        .map(SharedFoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Updates an existing shared food amount.
   *
   * @param request shared food request with updated amount
   * @return true if update was successful, false if not found
   */
  public boolean update(SharedFoodRequest request) {
    return repository.update(SharedFoodMapper.toModel(request));
  }

  /**
   * Deletes a shared food entry based on composite key.
   *
   * @param foodId           the food ID
   * @param groupHouseholdId the group household ID
   * @return true if deleted, false if not found
   */
  public boolean delete(Long foodId, Long groupHouseholdId) {
    return repository.deleteById(new SharedFoodKey(foodId, groupHouseholdId));
  }

  /**
   * Moves a specified amount of food from a household to a shared group.
   * <p>
   * This operation will:
   * </p>
   * <ul>
   * <li>Reduce the amount in the original food item</li>
   * <li>Create a new food entry with the shared amount and same household</li>
   * <li>Link the new food item to the group household via SharedFood</li>
   * </ul>
   *
   * @param request shared food request with food ID, group household ID, and
   *                amount
   * @return true if move was successful, false if not enough amount or food not
   *         found
   */
  public boolean moveFoodToSharedGroup(SharedFoodRequest request) {
    Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
    if (foodOpt.isEmpty()) {
      return false;
    }
    
    Food food = foodOpt.get();
    if (food.getAmount() < request.getAmount()) {
      return false;
    }

    GroupHousehold groupHousehold = groupHouseholdRepository
        .findByHouseholdIdAndGroupId(food.getHouseholdId(), request.getGroupId());
    if (groupHousehold == null) {
      return false;
    }

    SharedFoodKey key = new SharedFoodKey(food.getId(), groupHousehold.getId());
    if (repository.findById(key).isPresent()) {

      return false;
    }

    food.setAmount(food.getAmount() - request.getAmount());
    foodRepository.update(food);

    SharedFood shared = new SharedFood(key, request.getAmount());
    repository.save(shared);

    return true;
  }

  /**
   * Retrieves a detailed summary of shared food grouped by food type.
   * Includes total amount, calories, and individual batch info.
   *
   * @param groupHouseholdId the ID of the group household
   * @return list of detailed food summaries for the group
   */
  public List<FoodDetailedResponse> getSharedFoodSummaryByGroup(Long groupHouseholdId) {
    List<SharedFood> sharedFoods = repository.findByGroupHouseholdId(groupHouseholdId);

    Map<Long, Food> foodMap = sharedFoods.stream()
        .map(sf -> foodRepository.findById(sf.getId().getFoodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Food::getId, food -> food));

    Map<Long, List<SharedFood>> groupedByType = sharedFoods.stream()
        .filter(sf -> foodMap.containsKey(sf.getId().getFoodId()))
        .collect(Collectors.groupingBy(
            sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

    return groupedByType.entrySet().stream()
        .map(entry -> {
          Long typeId = entry.getKey();
          Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
          if (typeOpt.isEmpty()) {
            return null;
          }

          FoodType type = typeOpt.get();

          List<FoodBatchResponse> batches = entry.getValue().stream()
              .map(sf -> {
                Food food = foodMap.get(sf.getId().getFoodId());
                FoodBatchResponse batch = new FoodBatchResponse();
                batch.setId(food.getId());
                batch.setExpirationDate(food.getExpirationDate());
                batch.setAmount(sf.getAmount());
                batch.setHouseholdId(food.getHouseholdId());
                batch.setGroupHouseholdId(sf.getId().getGroupHouseholdId());
                return batch;
              })
              .filter(Objects::nonNull)
              .toList();

          float totalAmount = (float) batches.stream()
              .mapToDouble(FoodBatchResponse::getAmount)
              .sum();

          float totalCalories = totalAmount * type.getCaloriesPerUnit();

          FoodDetailedResponse response = new FoodDetailedResponse();
          response.setTypeId(type.getId());
          response.setTypeName(type.getName());
          response.setUnit(type.getUnit());
          response.setTotalAmount(totalAmount);
          response.setTotalCalories(totalCalories);
          response.setBatches(batches);

          return response;
        })
        .filter(Objects::nonNull)
        .toList();
  }

  /**
   * Moves a specified amount of food from a shared group back to the household.
   *
   * @param request shared food request with food ID, group household ID, and
   *                amount
   * @return true if move was successful, false if not enough amount or not found
   */
  public boolean moveFoodFromSharedGroup(SharedFoodRequest request) {
    SharedFoodKey key = new SharedFoodKey(request.getFoodId(), request.getGroupHouseholdId());
    Optional<SharedFood> sharedOpt = repository.findById(key);
    if (sharedOpt.isEmpty()) {
      return false;
    }

    SharedFood shared = sharedOpt.get();
    if (shared.getAmount() < request.getAmount()) {
      return false;
    }

    Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
    if (foodOpt.isEmpty()) {
      return false;
    }

    Food sharedFood = foodOpt.get();

    shared.setAmount(shared.getAmount() - request.getAmount());
    if (shared.getAmount() == 0) {
      repository.deleteById(key);
    } else {
      repository.update(shared);
    }

    Optional<Food> existingFoodOpt = foodRepository
        .findByTypeIdAndExpirationDateAndHouseholdId(
            sharedFood.getTypeId(),
            sharedFood.getExpirationDate(),
            sharedFood.getHouseholdId());

    if (existingFoodOpt.isPresent()) {
      Food existing = existingFoodOpt.get();
      existing.setAmount(existing.getAmount() + request.getAmount());
      foodRepository.update(existing);
    } else {
      Food foodToReturn = new Food();
      foodToReturn.setTypeId(sharedFood.getTypeId());
      foodToReturn.setExpirationDate(sharedFood.getExpirationDate());
      foodToReturn.setAmount(request.getAmount());
      foodToReturn.setHouseholdId(sharedFood.getHouseholdId());
      foodRepository.save(foodToReturn);
    }

    return true;
  }

  /**
   * Retrieves a detailed summary of shared food for the entire group.
   * <p>
   * This method aggregates the shared food across all group households in the
   * specified group.
   * </p>
   *
   * @param groupId the ID of the group
   * @return list of detailed food summaries for the entire group
   */
  public List<FoodDetailedResponse> getSharedFoodSummaryByGroupId(Long groupId) {
    List<GroupHousehold> memberships = groupHouseholdRepository.findByGroupId(groupId);

    List<SharedFood> sharedFoods = memberships.stream()
        .flatMap(gh -> repository.findByGroupHouseholdId(gh.getId()).stream())
        .toList();

    Map<Long, Food> foodMap = sharedFoods.stream()
        .map(sf -> foodRepository.findById(sf.getId().getFoodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Food::getId, food -> food));

    Map<Long, List<SharedFood>> groupedByType = sharedFoods.stream()
        .filter(sf -> foodMap.containsKey(sf.getId().getFoodId()))
        .collect(Collectors.groupingBy(sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

    return groupedByType.entrySet().stream()
        .map(entry -> {
          Long typeId = entry.getKey();
          Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
          if (typeOpt.isEmpty()) {
            return null;
          }
          FoodType type = typeOpt.get();

          List<FoodBatchResponse> batches = entry.getValue().stream()
              .map(sf -> {
                Food food = foodMap.get(sf.getId().getFoodId());
                FoodBatchResponse batch = new FoodBatchResponse();
                batch.setId(food.getId());
                batch.setAmount(sf.getAmount());
                batch.setExpirationDate(food.getExpirationDate());
                batch.setHouseholdId(food.getHouseholdId());
                batch.setGroupHouseholdId(sf.getId().getGroupHouseholdId());
                return batch;
              })
              .toList();

          float totalAmount = (float) batches.stream().mapToDouble(FoodBatchResponse::getAmount)
              .sum();
          float totalCalories = totalAmount * type.getCaloriesPerUnit();

          FoodDetailedResponse response = new FoodDetailedResponse();
          response.setTypeId(type.getId());
          response.setTypeName(type.getName());
          response.setUnit(type.getUnit());
          response.setTotalAmount(totalAmount);
          response.setTotalCalories(totalCalories);
          response.setBatches(batches);
          return response;
        })
        .filter(Objects::nonNull)
        .toList();
  }

}
