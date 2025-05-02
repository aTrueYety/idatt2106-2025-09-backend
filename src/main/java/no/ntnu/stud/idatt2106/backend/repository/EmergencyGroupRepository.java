package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;


/**
 * Interface for emergency group persistence operations.
 */
public interface EmergencyGroupRepository {
  void save(EmergencyGroup group);

  Optional<EmergencyGroup> findById(int id);

  List<EmergencyGroup> findAll();

  boolean update(int id, EmergencyGroup group);

  boolean deleteById(int id);

  List<EmergencyGroupSummaryResponse> findGroupSummariesByHouseholdId(int householdId);




}
