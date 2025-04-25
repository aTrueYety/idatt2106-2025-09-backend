package no.ntnu.stud.idatt2106.backend.service;

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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {

  private final FoodRepository repository;
  private final FoodTypeRepository foodTypeRepository;

  public FoodService(FoodRepository repository, FoodTypeRepository foodTypeRepository) {
    this.repository = repository;
    this.foodTypeRepository = foodTypeRepository;
  }

  public void create(FoodRequest request) {
    Food food = FoodMapper.toModel(request);
    repository.save(food);
  }

  public Optional<FoodResponse> getById(int id) {
    return repository.findById(id).map(FoodMapper::toResponse);
  }

  public List<FoodResponse> getAll() {
    return repository.findAll().stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

  public boolean update(int id, FoodUpdate update) {
    if (repository.findById(id).isEmpty()) return false;
    Food food = FoodMapper.toModel(update);
    food.setId(id);
    repository.update(food);
    return true;
  }

  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) return false;
    repository.deleteById(id);
    return true;
  }

  public List<FoodResponse> getByHouseholdId(int householdId) {
    return repository.findByHouseholdId(householdId).stream()
        .map(FoodMapper::toResponse)
        .collect(Collectors.toList());
  }

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

  public List<FoodDetailedResponse> getFoodDetailedByHousehold(int householdId) {
    List<Food> foods = repository.findByHouseholdId(householdId);

    Map<Integer, List<Food>> grouped = foods.stream()
        .collect(Collectors.groupingBy(Food::getTypeId));

    return grouped.entrySet().stream()
        .map(entry -> {
          int typeId = entry.getKey();
          List<Food> foodList = entry.getValue();

          Optional<FoodType> typeOpt = foodTypeRepository.findById(typeId);
          if (typeOpt.isEmpty()) return null;

          FoodType type = typeOpt.get();
          FoodDetailedResponse summary = new FoodDetailedResponse();
          summary.setTypeId(typeId);
          summary.setTypeName(type.getName());
          summary.setUnit(type.getUnit());
          summary.setTotalAmount(foodList.stream().mapToInt(Food::getAmount).sum());

          List<FoodBatchResponse> batches = foodList.stream()
              .map(f -> {
                FoodBatchResponse batch = new FoodBatchResponse();
                batch.setAmount(f.getAmount());
                batch.setExpirationDate(f.getExpirationDate());
                return batch;
              }).collect(Collectors.toList());

          summary.setBatches(batches);
          return summary;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
