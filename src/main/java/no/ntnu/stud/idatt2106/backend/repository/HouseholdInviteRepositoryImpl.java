package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


/**
 * Repository implementation for managing HouseholdInvite entities.
 */
@Repository
public class HouseholdInviteRepositoryImpl implements HouseholdInviteRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<HouseholdInvite> rowMapper = (rs, rowNum) -> {
    HouseholdInvite invite = new HouseholdInvite();
    invite.setUserId(rs.getObject("user_id", Long.class));
    invite.setHouseholdId(rs.getObject("household_id", Long.class));
    invite.setInviteKey(rs.getString("invite_key"));
    return invite;
  };

  @Override
  public void save(HouseholdInvite invite) {
    String sql = "INSERT INTO household_invite (user_id, household_id, invite_key) " 
        + "VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, invite.getUserId(), invite.getHouseholdId(), invite.getInviteKey());
  }

  @Override
  public HouseholdInvite findByKey(String key) {
    String sql = "SELECT * FROM household_invite WHERE invite_key = ?";
    List<HouseholdInvite> invites = jdbcTemplate.query(sql, rowMapper, key);
    return invites.isEmpty() ? null : invites.get(0);
  }

  @Override
  public List<HouseholdInvite> findByUserId(Long userId) {
    String sql = "SELECT * FROM household_invite WHERE user_id = ?";
    return jdbcTemplate.query(sql, rowMapper, userId);
  }

  @Override
  public List<HouseholdInvite> findByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM household_invite WHERE household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, householdId);
  }

  @Override
  public List<HouseholdInvite> findByUserIdAndHouseholdId(Long userId, Long householdId) {
    String sql = "SELECT * FROM household_invite WHERE user_id = ? AND household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, userId, householdId);
  }

  @Override
  public void delete(String key) {
    String sql = "DELETE FROM household_invite WHERE invite_key = ?";
    jdbcTemplate.update(sql, key);
  }
}
