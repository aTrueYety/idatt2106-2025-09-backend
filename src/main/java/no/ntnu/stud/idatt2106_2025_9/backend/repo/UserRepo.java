package no.ntnu.stud.idatt2106_2025_9.backend.repo;

import no.ntnu.stud.idatt2106_2025_9.backend.model.base.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  RowMapper<User> userRowMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getInt("id"));
    user.setHouseholdId(rs.getObject("household_id", Integer.class));
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

  public int addUser(User user) {
    String sql = "INSERT INTO user (household_id, email, username, password, email_confirmed, " 
        + "is_admin, is_super_admin, first_name, last_name, share_position_household, " 
        + "share_position_group, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(), 
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(), 
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(), 
        user.isSharePositionGroup(), user.getPicture());
  }

  public User findUserByUsername(String username) {
    String sql = "SELECT * FROM user WHERE username = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
    return users.isEmpty() ? null : users.get(0);
  }

  public int updateUser(User user) {
    String sql = "UPDATE user SET household_id = ?, email = ?, username = ?, password = ?, " 
        + "email_confirmed = ?, is_admin = ?, is_super_admin = ?, first_name = ?, last_name = ?, " 
        + "share_position_household = ?, share_position_group = ?, picture = ? WHERE id = ?";
    return jdbcTemplate.update(sql, user.getHouseholdId(), user.getEmail(), user.getUsername(), 
        user.getPassword(), user.isEmailConfirmed(), user.isAdmin(), user.isSuperAdmin(), 
        user.getFirstName(), user.getLastName(), user.isSharePositionHousehold(), 
        user.isSharePositionGroup(), user.getPicture(), user.getId());
  }
}
