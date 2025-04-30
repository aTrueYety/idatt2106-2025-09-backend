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
 * Service for managing shared food.
 */
@Service
@RequiredArgsConstructor
public class SharedFoodService {

  private final SharedFoodRepository repository;
  private final FoodRepository foodRepository;
  private final FoodTypeRepository foodTypeRepository;

  public void create(SharedFoodRequest request) {
    repository.save(SharedFoodMapper.toModel(request));
  }

  /**
   * Retrieves all shared food entries.
   *
   * @return a list of shared food responses
   */
  public List<SharedFoodResponse> getAll() {
    return repository.findAll().stream()
        .map(SharedFoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  public boolean update(SharedFoodRequest request) {
    return repository.update(SharedFoodMapper.toModel(request));
  }

  public boolean delete(int foodId, int groupHouseholdId) {
    return repository.deleteById(new SharedFoodKey(foodId, groupHouseholdId));
  }

  /**
   * Moves a specified amount of food to a shared group.
   *
   * @param request the request containing food ID, group household ID, and amount
   * @return true if the operation was successful, false otherwise
   */
  public boolean moveFoodToSharedGroup(SharedFoodRequest request) {
    Optional<Food> optional = foodRepository.findById(request.getFoodId());
    if (optional.isEmpty()) {
      return false;
    }

    Food food = optional.get();
    if (food.getAmount() < request.getAmount()) {
      return false;
    }

    food.setAmount(food.getAmount() - request.getAmount());
    foodRepository.update(food);

    repository.save(SharedFoodMapper.toModel(request));
    return true;
  }

  /**
   * Retrieves a detailed summary of shared food grouped by food type,
   * for a specific group household.
   *
   * @param groupHouseholdId the ID of the group household
   * @return a list of detailed food responses
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
        .collect(Collectors.groupingBy(sf -> foodMap.get(sf.getId().getFoodId()).getTypeId()));

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
                return batch;
              })
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
