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
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodBatchResponse;
import no.ntnu.stud.idatt2106.backend.model.response.FoodDetailedResponse;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.SharedFoodRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to shared food in group households.
 */
@Service
@RequiredArgsConstructor
public class SharedFoodService {

  private final SharedFoodRepository repository;
  private final FoodRepository foodRepository;
  private final FoodTypeRepository foodTypeRepository;

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
  public boolean delete(int foodId, int groupHouseholdId) {
    return repository.deleteById(new SharedFoodKey(foodId, groupHouseholdId));
  }

  /**
   * Moves a specified amount of food from a household to a shared group.
   * <p>This operation will:</p>
   * <ul>
   *   <li>Reduce the amount in the original food item</li>
   *   <li>Create a new food entry with the shared amount and same household</li>
   *   <li>Link the new food item to the group household via SharedFood</li>
   * </ul>
   *
   * @param request shared food request with food ID, group household ID, and amount
   * @return true if move was successful, false if not enough amount or food not found
   */
  public boolean moveFoodToSharedGroup(SharedFoodRequest request) {
    Optional<Food> originalOpt = foodRepository.findById(request.getFoodId());
    if (originalOpt.isEmpty()) {
      return false;
    }

    Food original = originalOpt.get();
    if (original.getAmount() < request.getAmount()) {
      return false;
    }

    original.setAmount(original.getAmount() - request.getAmount());
    foodRepository.update(original);

    Food cloned = new Food();
    cloned.setTypeId(original.getTypeId());
    cloned.setExpirationDate(original.getExpirationDate());
    cloned.setAmount(request.getAmount());
    cloned.setHouseholdId(original.getHouseholdId());

    int newFoodId = foodRepository.save(cloned);

    SharedFoodKey key = new SharedFoodKey(newFoodId, request.getGroupHouseholdId());
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
  public List<FoodDetailedResponse> getSharedFoodSummaryByGroup(int groupHouseholdId) {
    List<SharedFood> sharedFoods = repository.findByGroupHouseholdId(groupHouseholdId);

    Map<Integer, Food> foodMap = sharedFoods.stream()
        .map(sf -> foodRepository.findById(sf.getId().getFoodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Food::getId, food -> food));

    Map<Integer, List<SharedFood>> groupedByType = sharedFoods.stream()
        .filter(sf -> foodMap.containsKey(sf.getId().getFoodId()))
        .collect(Collectors.groupingBy(
            sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

    return groupedByType.entrySet().stream()
        .map(entry -> {
          int typeId = entry.getKey();
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
}
