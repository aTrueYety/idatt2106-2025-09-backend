package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;

/**
 * Repository interface for MapObjectType.
 */
public interface MapObjectTypeRepository {
  MapObjectType findById(int id);

  List<MapObjectType> findAll();

  void save(MapObjectType mapObjectType);

  void update(MapObjectType mapObjectType);

  void deleteById(int id);
}
