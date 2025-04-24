package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.FoodTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-types")
public class FoodTypeController {

    private final FoodTypeService service;

    public FoodTypeController(FoodTypeService service) {
        this.service = service;
    }

    /**
     * Get all food types.
     */
    @GetMapping
    public ResponseEntity<List<FoodTypeResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Get a single food type by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FoodTypeResponse> getById(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new food type.
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FoodTypeRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update an existing food type.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody FoodTypeRequest request) {
        boolean updated = service.update(id, request);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a food type by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
