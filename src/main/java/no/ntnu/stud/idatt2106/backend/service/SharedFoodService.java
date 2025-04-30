package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.mapper.SharedFoodMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import no.ntnu.stud.idatt2106.backend.model.request.SharedFoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.SharedFoodResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
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

  /**
   * Creates a shared food entry without affecting the original food record.
   *
   * @param request the shared food request
   */
  public void create(SharedFoodRequest request) {
    repository.save(SharedFoodMapper.toModel(request));
  }

  /**
   * Retrieves all shared food items and maps them to response objects.
   *
   * @return a list of shared food response objects
   */
  public List<SharedFoodResponse> getAll() {
    return repository.findAll().stream()
        .map(SharedFoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Updates the amount of an existing shared food entry.
   *
   * @param request the request with updated amount
   * @return true if updated, false if entry not found
   */
  public boolean update(SharedFoodRequest request) {
    return repository.update(SharedFoodMapper.toModel(request));
  }

  /**
   * Deletes a shared food entry by composite key.
   *
   * @param foodId the ID of the food item
   * @param groupHouseholdId the ID of the group household
   * @return true if deleted, false if not found
   */
  public boolean delete(int foodId, int groupHouseholdId) {
    return repository.deleteById(new SharedFoodKey(foodId, groupHouseholdId));
  }

  /**
   * Moves a portion of food to a shared group.
   * This reduces the original food amount and creates a shared food entry.
   *
   * @param request the request containing food ID, group household ID, and amount to share
   * @return true if successful, false otherwise (e.g. not enough food)
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
}
