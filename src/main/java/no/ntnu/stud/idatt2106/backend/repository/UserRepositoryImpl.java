package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of UserRepository.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

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

  @Override
  public User findById(Long id) {
    String sql = "SELECT * FROM `user` WHERE id = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public void addUser(User user) {
    String sql = "INSERT INTO `user` (household_id, email, username, password, email_confirmed, "
        + "is_admin, is_super_admin, first_name, last_name, share_position_household, "
        + "share_position_group, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(),
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(),
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(),
        user.isSharePositionGroup(), user.getPicture());
  }

  @Override
  public void updateUser(User user) {
    String sql = "UPDATE `user` SET household_id = ?, email = ?, username = ?, password = ?, "
        + "email_confirmed = ?, is_admin = ?, is_super_admin = ?, first_name = ?, last_name = ?, "
        + "share_position_household = ?, share_position_group = ?, picture = ? WHERE id = ?";
    jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(),
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(),
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(),
        user.isSharePositionGroup(), user.getPicture(), user.getId());
  }

  @Override
  public User findUserByUsername(String username) {
    String sql = "SELECT * FROM `user` WHERE username = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public User findUserByEmail(String email) {
    String sql = "SELECT * FROM `user` WHERE email = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
    return users.isEmpty() ? null : users.get(0);
  }

  @Override
  public List<User> findUsersByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM `user` WHERE household_id = ?";
    return jdbcTemplate.query(sql, userRowMapper, householdId);
  }

  @Override
  public void updateSharePositionHousehold(Long userId, boolean value) {
    String sql = "UPDATE `user` SET share_position_household = ? WHERE id = ?";
    jdbcTemplate.update(sql, value, userId);
  }
}
