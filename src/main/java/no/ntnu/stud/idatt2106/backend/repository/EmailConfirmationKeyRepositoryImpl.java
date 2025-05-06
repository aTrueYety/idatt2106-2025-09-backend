package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.EmailConfirmationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Repository implementation for managing EmailConfirmationKey entities.
 */
public class EmailConfirmationKeyRepositoryImpl implements EmailConfirmationKeyRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<EmailConfirmationKey> rowMapper = (rs, rowNum) -> {
    EmailConfirmationKey key = new EmailConfirmationKey();
    key.setUserId(rs.getLong("user_id"));
    key.setCreatedAt(rs.getTimestamp("created_at"));
    key.setKey(rs.getString("key"));
    return key;
  };

  @Override
  public void save(EmailConfirmationKey key) {
    String sql = "INSERT INTO email_confirmation_key (user_id, created_at, `key`) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, key.getUserId(), key.getCreatedAt(), key.getKey());
  }

  @Override
  public EmailConfirmationKey findByKey(String key) {
    String sql = "SELECT * FROM email_confirmation_key WHERE `key` = ?";
    List<EmailConfirmationKey> keys = jdbcTemplate.query(sql, rowMapper, key);
    return keys.isEmpty() ? null : keys.get(0);
  }

  @Override
  public void update(EmailConfirmationKey key) {
    String sql = "UPDATE email_confirmation_key SET `key` = ?, created_at = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, key.getKey(), key.getCreatedAt(), key.getUserId());
  }

  @Override
  public void deleteByKey(String key) {
    String sql = "DELETE FROM email_confirmation_key WHERE `key` = ?";
    jdbcTemplate.update(sql, key);
  }

}
