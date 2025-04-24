package no.ntnu.stud.idatt2106.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.repo.UserRepo;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {
  @Autowired
  private UserRepo userRepo;

  /**
   * Adds a new user to the system.
   *
   * @param user The user to be added.
   */
  public void addUser(User user) {
    userRepo.addUser(user);
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username The username of the user to be retrieved.
   * @return The user with the specified username, or null if not found.
   */
  public User getUserByUsername(String username) {
    return userRepo.findUserByUsername(username);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param user The user to be retrieved.
   */
  public void updateUserCredentials(User user) {
    userRepo.updateUser(user);
  }
}
