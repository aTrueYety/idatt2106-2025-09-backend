package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.mapper.FoodTypeMapper;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodTypeService {

    private final FoodTypeRepository repository;

    public FoodTypeService(FoodTypeRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new food type.
     */
    public void create(FoodTypeRequest request) {
        FoodType foodType = FoodTypeMapper.toModel(request);
        repository.save(foodType);
    }

    /**
     * Get all food types.
     */
    public List<FoodTypeResponse> getAll() {
        return repository.findAll().stream()
                .map(FoodTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get food type by ID.
     */
    public Optional<FoodTypeResponse> getById(int id) {
        return repository.findById(id).map(FoodTypeMapper::toResponse);
    }

    /**
     * Update existing food type.
     *
     * @return true if updated, false if not found
     */
    public boolean update(int id, FoodTypeRequest request) {
        return repository.findById(id).map(existing -> {
            FoodType updated = FoodTypeMapper.toModel(request);
            updated.setId(id);
            repository.update(updated);
            return true;
        }).orElse(false);
    }

    /**
     * Delete food type by ID.
     *
     * @return true if deleted, false if not found
     */
    public boolean delete(int id) {
        return repository.findById(id).map(existing -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public List<FoodTypeResponse> searchByName(String query) {
      return repository.findByNameContainingIgnoreCase(query).stream()
          .map(FoodTypeMapper::toResponse)
          .collect(Collectors.toList());
    } 
  }