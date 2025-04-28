package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;

/**
 * Repository interface for managing extra resident types.
 */
public interface ExtraResidentTypeRepository {
  /**
   * Find an extra resident type by its ID.
   *
   * @param id the ID of the extra resident type
   * @return an Optional containing the ExtraResidentType if found, empty
   *         otherwise
   */
  Optional<ExtraResidentType> findById(int id);

  /**
   * Find all extra resident types.
   *
   * @return a list of all ExtraResidentType objects
   */
  List<ExtraResidentType> findAll();

  /**
   * Save a new extra resident type or update an existing one.
   *
   * @param type the ExtraResidentType object to save
   */
  void save(ExtraResidentType type);

  /**
   * Update an existing extra resident type.
   *
   * @param type the ExtraResidentType object to update
   */
  void update(ExtraResidentType type);

  /**
   * Delete an extra resident type by its ID.
   *
   * @param id the ID of the extra resident type to delete
   */
  void deleteById(int id);
}
