package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.AdminRegistrationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for managing AdminRegistrationKey entities.
 * This class provides methods to perform CRUD operations on the database.
 */
@Repository
public class AdminRegistrationKeyRepositoryImpl implements AdminRegistrationKeyRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<AdminRegistrationKey> rowMapper = (rs, rowNum) -> {
    AdminRegistrationKey key = new AdminRegistrationKey();
    key.setUserId(rs.getLong("user_id"));
    key.setKey(rs.getString("key"));
    key.setCreatedAt(rs.getTimestamp("created_at"));
    return key;
  };

  @Override
  public void save(AdminRegistrationKey key) {
    String sql = "INSERT INTO admin_registration_key (user_id, `key`) VALUES (?, ?)";
    jdbcTemplate.update(sql, key.getUserId(), key.getKey());
  }

  @Override
  public AdminRegistrationKey findByKey(String key) {
    String sql = "SELECT * FROM admin_registration_key WHERE `key` = ?";
    List<AdminRegistrationKey> keys = jdbcTemplate.query(sql, rowMapper, key);
    return keys.isEmpty() ? null : keys.get(0);
  }

  @Override
  public AdminRegistrationKey findByUserId(Long userId) {
    String sql = "SELECT * FROM admin_registration_key WHERE user_id = ?";
    List<AdminRegistrationKey> keys = jdbcTemplate.query(sql, rowMapper, userId);
    return keys.isEmpty() ? null : keys.get(0);
  }

  @Override
  public List<AdminRegistrationKey> findAll() {
    String sql = "SELECT * FROM admin_registration_key";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public void update(AdminRegistrationKey key) {
    String sql = "UPDATE admin_registration_key SET `key` = ?, created_at = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, key.getKey(), key.getCreatedAt(), key.getUserId());
  }

  @Override
  public void deleteByKey(String key) {
    String sql = "DELETE FROM admin_registration_key WHERE `key` = ?";
    jdbcTemplate.update(sql, key);
  }

  @Override
  public void deleteByUserId(Long userId) {
    String sql = "DELETE FROM admin_registration_key WHERE user_id = ?";
    jdbcTemplate.update(sql, userId);
  }

}
