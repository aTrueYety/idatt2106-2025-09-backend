package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.mapper.FoodTypeMapper;
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
     * Create a new FoodType from request.
     */
    public void create(FoodTypeRequest request) {
        FoodType foodType = FoodTypeMapper.toModel(request);
        repository.save(foodType);
    }

    /**
     * Retrieve all FoodTypes and convert them to response objects.
     */
    public List<FoodTypeResponse> getAll() {
        return repository.findAll().stream()
                .map(FoodTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a FoodType by ID and return as response object.
     */
    public Optional<FoodTypeResponse> getById(int id) {
        return repository.findById(id)
                .map(FoodTypeMapper::toResponse);
    }

    /**
     * Update an existing FoodType with new values from request.
     * Returns true if update was successful, false if not found.
     */
    public boolean update(int id, FoodTypeRequest request) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        FoodType updated = FoodTypeMapper.toModel(request);
        updated.setId(id);
        repository.update(updated);
        return true;
    }

    /**
     * Delete a FoodType by ID.
     * Returns true if deleted, false if not found.
     */
    public boolean delete(int id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
