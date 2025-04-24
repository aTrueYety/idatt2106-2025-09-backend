package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.mapper.FoodMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository repository;

    public FoodService(FoodRepository repository) {
        this.repository = repository;
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
}
