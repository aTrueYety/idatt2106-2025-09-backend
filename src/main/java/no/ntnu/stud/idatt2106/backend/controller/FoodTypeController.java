package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-types")
public class FoodTypeController {

  private final FoodTypeRepository repository;

  public FoodTypeController(FoodTypeRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<FoodType> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<FoodType> getById(@PathVariable int id) {
    return repository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody FoodType foodType) {
    repository.save(foodType);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, @RequestBody FoodType updated) {
    if (repository.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    updated.setId(id);
    repository.update(updated);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    if (repository.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
