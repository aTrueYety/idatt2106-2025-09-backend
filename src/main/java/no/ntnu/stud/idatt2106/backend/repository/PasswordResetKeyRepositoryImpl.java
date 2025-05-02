package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.PasswordResetKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the PasswordChangeKeyRepository interface using JDBC.
 */
@Repository
public class PasswordResetKeyRepositoryImpl implements PasswordResetKeyRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<PasswordResetKey> rowMapper = (rs, rowNum) -> new PasswordResetKey(
      rs.getLong("user_id"),
      rs.getString("key"),
      rs.getTimestamp("created_at")
  );

  @Override
  public PasswordResetKey save(PasswordResetKey key) {
    String sql = "INSERT INTO password_change_key (user_id, key) VALUES (?, ?)";
    jdbcTemplate.update(sql, key.getUserId(), key.getKey());
    return key;
  }

  @Override
  public PasswordResetKey findByUserId(Long userId) {
    String sql = "SELECT * FROM password_change_key WHERE user_id = ?";
    List<PasswordResetKey> keys = jdbcTemplate.query(sql, rowMapper, userId);
    return keys.isEmpty() ? null : keys.get(0);
  }

  @Override
  public PasswordResetKey findByKey(String key) {
    String sql = "SELECT * FROM password_change_key WHERE key = ?";
    List<PasswordResetKey> keys = jdbcTemplate.query(sql, rowMapper, key);
    return keys.isEmpty() ? null : keys.get(0);
  }

  @Override
  public void deleteByKey(String key) {
    String sql = "DELETE FROM password_change_key WHERE key = ?";
    jdbcTemplate.update(sql, key);
  }

  @Override
  public void update(PasswordResetKey key) {
    String sql = "UPDATE password_change_key SET key = ?, created_at = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, key.getKey(), key.getCreatedAt(), key.getUserId());
  }
}
