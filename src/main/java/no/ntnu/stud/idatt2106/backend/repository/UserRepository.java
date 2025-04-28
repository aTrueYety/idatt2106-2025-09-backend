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

  RowMapper<User> userRowMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getLong("id"));
    user.setHouseholdId(rs.getLong("household_id"));
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
   * Adds a new user to the database.
   *
   * @param user the user to be added
   * @return the number of rows affected
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
   * @param username the username of the user to be found
   * @return the user with the specified username, or null if not found
   */
  public User findUserByUsername(String username) {
    String sql = "SELECT * FROM `user` WHERE username = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * Finds a user by their email.
   *
   * @param email the email of the user to be found
   * @return the user with the specified email, or null if not found
   */
  public User findUserByEmail(String email) {
    String sql = "SELECT * FROM `user` WHERE email = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * Finds all users with the given household ID.
   * 
   * @param householdId the ID of the household to filter users by
   * @return a list of users belonging to the given household ID, or an empty list if none are found
   */
  public List<User> findUsersByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM `user` WHERE household_id = ?";
    return jdbcTemplate.query(sql, userRowMapper, householdId);
  }

  /**
   * Finds a user by their ID.
   *
   * @param user the user to be found
   * @return the user with the specified ID, or null if not found
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
}
