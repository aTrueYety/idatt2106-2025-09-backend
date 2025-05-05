package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.GroupInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the GroupInviteRepository interface using JDBC.
 */
@Repository
public class GroupInviteRepositoryImpl implements GroupInviteRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<GroupInvite> rowMapper = (rs, rowNum) -> new GroupInvite(
      rs.getLong("group_id"),
      rs.getLong("household_id")
  );

  @Override
  public GroupInvite save(GroupInvite groupInvite) {
    String sql = "INSERT INTO group_invite (group_id, household_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, groupInvite.getGroupId(), groupInvite.getHouseholdId());
    return groupInvite;
  }

  @Override
  public GroupInvite findByHouseholdIdAndGroupId(Long householdId, Long groupId) {
    String sql = "SELECT * FROM group_invite WHERE household_id = ? AND group_id = ?";
    List<GroupInvite> invites = jdbcTemplate.query(sql, rowMapper, householdId, groupId);
    return invites.isEmpty() ? null : invites.get(0);
  }

  @Override
  public List<GroupInvite> findAll() {
    String sql = "SELECT * FROM group_invite";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public void deleteByHouseholdIdAndGroupId(Long householdId, Long groupId) {
    String sql = "DELETE FROM group_invite WHERE household_id = ? AND group_id = ?";
    jdbcTemplate.update(sql, householdId, groupId);
  }
}
