package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.User;

/**
 * Interface for user-related database operations.
 */
public interface UserRepository {

  User findUserByUsername(String username);

  User findUserByEmail(String email);

  List<User> findUsersByHouseholdId(Long householdId);

  User findById(Long id);

  void addUser(User user);

  void updateUser(User user);

  void updateSharePositionHousehold(Long userId, boolean value);
}
