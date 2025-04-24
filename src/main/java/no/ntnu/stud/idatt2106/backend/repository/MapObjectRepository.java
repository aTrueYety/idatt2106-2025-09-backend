package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;

/**
 * Repository interface for MapObject.
 */
public interface MapObjectRepository {
  Optional<MapObject> findById(int id);

  List<MapObject> findAll();

  void save(MapObject mapObject);

  void update(MapObject mapObject);

  void deleteById(int id);
}
