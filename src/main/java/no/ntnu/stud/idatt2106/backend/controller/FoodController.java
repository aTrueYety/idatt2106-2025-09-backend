package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

  private final FoodRepository repository;

  public FoodController(FoodRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Food> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Food> getById(@PathVariable int id) {
    return repository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody Food food) {
    repository.save(food);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Food updated) {
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
