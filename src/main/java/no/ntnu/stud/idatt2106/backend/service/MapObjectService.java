package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.mapper.MapObjectMapper;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing map objects.
 */
@Service
public class MapObjectService {
  private final MapObjectRepository repository;

  public MapObjectService(MapObjectRepository repository) {
    this.repository = repository;
  }

  public void create(MapObjectRequest request) {
    MapObject mapObject = MapObjectMapper.toModel(request);
    repository.save(mapObject);
  }

  public Optional<MapObject> getById(int id) {
    return repository.findById(id);
  }

  public List<MapObject> getAll() {
    return repository.findAll();
  }

  /**
   * Updates a map object in the database.
   */
  public boolean update(int id, MapObject mapObject) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    mapObject.setId(id);
    repository.update(mapObject);
    return true;
  }

  /**
   * Deletes a map object from the database.
   */
  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) {
      return false;
    }
    repository.deleteById(id);
    return true;
  }
}
