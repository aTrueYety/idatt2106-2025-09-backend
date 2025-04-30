package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;

/**
 * Interface for shared food persistence operations.
 */
public interface SharedFoodRepository {
  void save(SharedFood food);

  Optional<SharedFood> findById(SharedFoodKey id);

  List<SharedFood> findAll();

  boolean update(SharedFood food);

  boolean deleteById(SharedFoodKey id);
}
