package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;

/**
 * Interface for group-household persistence operations.
 */
public interface GroupHouseholdRepository {
  void save(GroupHousehold groupHousehold);

  Optional<GroupHousehold> findById(int id);

  List<GroupHousehold> findAll();

  List<GroupHousehold> findByGroupId(int groupId);

  boolean deleteById(int id);
}
