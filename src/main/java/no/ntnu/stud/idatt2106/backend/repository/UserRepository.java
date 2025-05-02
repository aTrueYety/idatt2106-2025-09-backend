package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.User;

/**
 * Repository interface for user persistence operations.
 */
public interface UserRepository {

  User findById(Long id);

  void addUser(User user);

  void updateUser(User user);

  User findUserByUsername(String username);

  User findUserByEmail(String email);

  List<User> findUsersByHouseholdId(Long householdId);

  List<User> findAll();

  void updateSharePositionHousehold(Long userId, boolean value);
}
