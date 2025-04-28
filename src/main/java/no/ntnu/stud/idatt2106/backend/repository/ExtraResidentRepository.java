package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;

/**
 * Repository interface for managing extra residents.
 */
public interface ExtraResidentRepository {
  /**
   * Find an extra resident by its ID.
   *
   * @param id the ID of the extra resident
   * @return an Optional containing the ExtraResident if found, empty otherwise
   */
  Optional<ExtraResident> findById(int id);

  /**
   * Find all extra residents.
   *
   * @return a list of all ExtraResident objects
   */
  List<ExtraResident> findAll();

  /**
   * Save a new extra resident or update an existing one.
   *
   * @param extraResident the ExtraResident object to save
   */
  void save(ExtraResident extraResident);

  /**
   * Update an existing extra resident.
   *
   * @param extraResident the ExtraResident object to update
   */
  void update(ExtraResident extraResident);

  /**
   * Delete an extra resident by its ID.
   *
   * @param id the ID of the extra resident to delete
   */
  void deleteById(int id);
}
