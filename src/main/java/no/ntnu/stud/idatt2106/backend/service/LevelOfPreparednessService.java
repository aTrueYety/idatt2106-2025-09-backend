package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdResponse;
import no.ntnu.stud.idatt2106.backend.model.response.LevelOfPreparednessResponse;
import no.ntnu.stud.idatt2106.backend.model.response.UserResponse;
import org.springframework.stereotype.Service;

/**
 * Service class for calculating the level of preparedness for a household.
 */
@Service
public class LevelOfPreparednessService {

  private final FoodService foodService;
  private final HouseholdKitService householdKitService;
  private final KitService kitService;
  private final ExtraResidentService extraResidentService;
  private final ExtraResidentTypeService extraResidentTypeService;
  private final UserService userService;

  /**
   * Service class for calculating the level of preparedness for a
   * household.
   *
   * @param foodService         Service for managing food items
   * @param householdKitService Service for managing household kits
   */
  public LevelOfPreparednessService(FoodService foodService,
      HouseholdKitService householdKitService,
      KitService kitService, ExtraResidentService extraResidentService,
      ExtraResidentTypeService extraResidentTypeService, UserService userService) {
    this.foodService = foodService;
    this.householdKitService = householdKitService;
    this.kitService = kitService;
    this.extraResidentService = extraResidentService;
    this.extraResidentTypeService = extraResidentTypeService;
    this.userService = userService;
  }

  /**
   * Calculate the daily water intake for all extra residents in a household.
   *
   * @param householdId The ID of the household to calculate daily water need for
   *                    extra residents
   * @return The total daily water need for extra residents as a double value
   */
  public double getDailyWaterNeedExtraResidents(Long householdId) {

    // Get all extra residents in the household
    List<ExtraResidentResponse> residents = extraResidentService.getAll().stream()
        .filter(resident -> resident.getHouseholdId() == (householdId))
        .toList();

    // Sum up their daily water needs using their type's info
    double totalWater = residents.stream()
        .mapToDouble(resident -> extraResidentTypeService.getById(resident.getTypeId())
            .map(ExtraResidentTypeResponse::getConsumptionWater)
            .orElse(0.0f))
        .sum();
    return totalWater;
  }

  /**
   * Calculates the level of preparedness for a household based on the food items
   * and household kit.
   *
   * @param household The ID of the household to calculate the level of
   *                    preparedness for
   * @return The level of preparedness as a double value
   */
  public double calculateLevelOfPreparednessWater(HouseholdResponse household) {

    double householdWaterAmount = household.getWaterAmountLiters();
    List<UserResponse> users = userService.getUsersByHouseholdId(household.getId());
    double amountOfUsersInHousehold = users != null ? users.size() : 0;

    double extraResidentsWaterWeek = getDailyWaterNeedExtraResidents(household.getId()) * 3;
    double totalWaterNeed = (amountOfUsersInHousehold * 20) + extraResidentsWaterWeek;

    // 1 in value equals 4 liters of water per day per person

    if (totalWaterNeed == 0) {
    
      return 0.0;
    }
    double householdWaterPreparedness = householdWaterAmount / totalWaterNeed;

    if (householdWaterPreparedness > 1.0) {
      householdWaterPreparedness = 1.0;
    }
    return householdWaterPreparedness;
  }

  /**
   * Calculates the daily calorie need for all extra residents in a household.
   *
   * @param householdId The ID of the household to calculate the daily calorie
   *                    need for
   * @return The total daily calorie need as a double value
   */
  public double getDailyCaloryNeedExtraResidents(Long householdId) {

    // Get all extra residents in the household
    List<ExtraResidentResponse> residents = extraResidentService.getAll().stream()
        .filter(resident -> resident.getHouseholdId() == (householdId))
        .toList();

    // Sum up their daily caloriy needs using their type's info
    double totalCalories = residents.stream()
        .mapToDouble(resident -> extraResidentTypeService.getById(resident.getTypeId())
            .map(ExtraResidentTypeResponse::getConsumptionFood)
            .orElse(0.0f))
        .sum();
    return totalCalories;
  }

  /**
   * Calculates the level of preparedness for a household based on the food items
   * and household kit.
   *
   * @param householdId The ID of the household to calculate the level of
   *                    preparedness for
   * @return The level of preparedness as a double value
   */
  public double calculateLevelOfFoodPreparedness(long householdId) {

    List<UserResponse> users = userService.getUsersByHouseholdId(householdId);
    double amountOfUsersInHousehold = users != null ? users.size() : 0;

    double caloriesInHousehold = foodService.getCaloriesByHouseholdId(householdId);

    double extraResidentsConsumptionWeek = getDailyCaloryNeedExtraResidents(householdId) * 3;

    double totalFoodConsumption = (amountOfUsersInHousehold * 2000 * 3)
        + extraResidentsConsumptionWeek;

    if (caloriesInHousehold == 0) {
      return 0.0;

    }
    double foodPreparedness = caloriesInHousehold / totalFoodConsumption;

    if (foodPreparedness > 1.0) {
      foodPreparedness = 1.0;
    }

    return foodPreparedness;
  }

  /**
   * Calculates the level of preparedness for a household based on household kit.
   */
  public double calculateLevelOfPreparednessKit(long householdId) {

    // Numbers of different kits
    int numberOfKitItems = kitService.getAll().size();

    // Get the household kit amount
    int numberOfKitsForHousehold = householdKitService.getByHouseholdId(householdId).size();

    if (numberOfKitItems == 0) {
      return 0.0;
    }

    double kitPreparedness = numberOfKitsForHousehold / numberOfKitItems;

    if (kitPreparedness > 1.0) {
      kitPreparedness = 1.0;
    }
    return kitPreparedness;
  }

  /**
   * Calculates the overall level of preparedness for a household based on food,
   * water, and kit preparedness.
   *
   * @param household The ID of the household to calculate the overall level of
   *                    preparedness for
   * @return The overall level of preparedness as a double value
   */
  public double calculateOverallLevelOfPreparedness(HouseholdResponse household) {
    double foodPreparedness = calculateLevelOfFoodPreparedness(household.getId());
    double waterPreparedness = calculateLevelOfPreparednessWater(household);
    double kitPreparedness = calculateLevelOfPreparednessKit(household.getId());
    return (foodPreparedness + waterPreparedness + kitPreparedness) / 3;
  }

  /**
   * Retrieves the preparedness levels for a specific household.
   *
   * @param household The ID of the household to retrieve preparedness levels
   *                    for
   * @return A LevelOfPreparednessResponse object containing the preparedness
   *         levels
   */
  public LevelOfPreparednessResponse getPreparednessForHousehold(HouseholdResponse household) {
    LevelOfPreparednessResponse preparedness = new LevelOfPreparednessResponse();
    preparedness.setLevelOfPreparednessWater(calculateLevelOfPreparednessWater(household));
    preparedness.setLevelOfPreparednessFood(calculateLevelOfFoodPreparedness(household.getId()));
    preparedness.setLevelOfPreparednessKit(calculateLevelOfPreparednessKit(household.getId()));
    preparedness.setLevelOfPreparedness(calculateOverallLevelOfPreparedness(household));
    return preparedness;
  }

}
