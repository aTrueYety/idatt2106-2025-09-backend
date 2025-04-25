package no.ntnu.stud.idatt2106.backend.controller;

import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extra-resident-types")
public class ExtraResidentTypeController {

  private final ExtraResidentTypeService service;

  public ExtraResidentTypeController(ExtraResidentTypeService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<ExtraResidentTypeResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExtraResidentTypeResponse> getById(@PathVariable int id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody ExtraResidentTypeRequest request) {
    service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable int id, @RequestBody ExtraResidentTypeRequest request) {
    boolean success = service.update(id, request);
    return success ? ResponseEntity.ok().build() : 
    ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    return service.delete(id) ? ResponseEntity.noContent().build() :
    ResponseEntity.notFound().build();
  }
}
