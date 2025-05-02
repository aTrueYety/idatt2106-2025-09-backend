package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository class for managing user-related database operations.
 */
@Repository
public class UserRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getObject("id", Long.class));
    user.setHouseholdId(rs.getObject("household_id", Long.class));
    user.setEmail(rs.getString("email"));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setEmailConfirmed(rs.getBoolean("email_confirmed"));
    user.setAdmin(rs.getBoolean("is_admin"));
    user.setSuperAdmin(rs.getBoolean("is_super_admin"));
    user.setFirstName(rs.getString("first_name"));
    user.setLastName(rs.getString("last_name"));
    user.setSharePositionHousehold(rs.getBoolean("share_position_household"));
    user.setSharePositionGroup(rs.getBoolean("share_position_group"));
    user.setPicture(rs.getBytes("picture"));
    return user;
  };

  /**
   * Finds a user by their unique ID.
   *
   * @param id the unique ID of the user
   * @return the user with the specified ID, or null if no user is found
   */
  public User findById(Long id) {
    String sql = "SELECT * FROM `user` WHERE id = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * Adds a new user to the database.
   *
   * @param user the user object containing the details to be added
   * @return the number of rows affected by the insert operation
   */
  public int addUser(User user) {
    String sql = "INSERT INTO `user` (household_id, email, username, password, email_confirmed, "
        + "is_admin, is_super_admin, first_name, last_name, share_position_household, "
        + "share_position_group, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(),
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(),
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(),
        user.isSharePositionGroup(), user.getPicture());
  }

  /**
   * Finds a user by their username.
   *
   * @param username the username of the user
   * @return the user with the specified username, or null if no user is found
   */
  public User findUserByUsername(String username) {
    String sql = "SELECT * FROM `user` WHERE username = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * Finds a user by their email address.
   *
   * @param email the email address of the user
   * @return the user with the specified email, or null if no user is found
   */
  public User findUserByEmail(String email) {
    String sql = "SELECT * FROM `user` WHERE email = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * Finds all users belonging to a specific household.
   *
   * @param householdId the ID of the household
   * @return a list of users belonging to the specified household
   */
  public List<User> findUsersByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM `user` WHERE household_id = ?";
    return jdbcTemplate.query(sql, userRowMapper, householdId);
  }

  /**
   * Updates the share position for a user in their household.
   *
   * @return the number of rows affected by the update operation
   */
  public int updateUser(User user) {
    String sql = "UPDATE `user` SET household_id = ?, email = ?, username = ?, password = ?, "
        + "email_confirmed = ?, is_admin = ?, is_super_admin = ?, first_name = ?, last_name = ?, "
        + "share_position_household = ?, share_position_group = ?, picture = ? WHERE id = ?";
    return jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(),
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(),
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(),
        user.isSharePositionGroup(), user.getPicture(), user.getId());
  }

  /**
   * Retrieves all users in the database.
   *
   * @return list of all users
   */
  public List<User> findAll() {
    String sql = "SELECT * FROM `user`";
    return jdbcTemplate.query(sql, userRowMapper);
  }
}
