package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of the GroupHouseholdRepository.
 */
@Repository
@RequiredArgsConstructor
public class GroupHouseholdRepositoryImpl implements GroupHouseholdRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<GroupHousehold> rowMapper = (rs, rowNum) -> new GroupHousehold(
      rs.getObject("id", Long.class),
      rs.getObject("household_id", Long.class),
      rs.getObject("group_id", Long.class)
  );

  @Override
  public void save(GroupHousehold groupHousehold) {
    String sql = "INSERT INTO group_household (household_id, group_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, groupHousehold.getHouseholdId(), groupHousehold.getGroupId());
  }

  @Override
  public Optional<GroupHousehold> findById(Long id) {
    String sql = "SELECT * FROM group_household WHERE id = ?";
    List<GroupHousehold> result = jdbcTemplate.query(sql, rowMapper, id);
    return result.stream().findFirst();
  }

  @Override
  public List<GroupHousehold> findAll() {
    String sql = "SELECT * FROM group_household";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public List<GroupHousehold> findByGroupId(Long groupId) {
    String sql = "SELECT * FROM group_household WHERE group_id = ?";
    return jdbcTemplate.query(sql, rowMapper, groupId);
  }

  @Override
  public GroupHousehold findByHouseholdIdAndGroupId(Long householdId, Long groupId) {
    String sql = "SELECT * FROM group_household WHERE household_id = ? AND group_id = ?";
    List<GroupHousehold> result = jdbcTemplate.query(sql, rowMapper, householdId, groupId);
    return result.isEmpty() ? null : result.get(0);
  }

  @Override
  public boolean deleteById(Long id) {
    String sql = "DELETE FROM group_household WHERE id = ?";
    return jdbcTemplate.update(sql, id) > 0;
  }

  @Override
  public List<GroupHousehold> findByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM group_household WHERE household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, householdId);
  }

  @Override
  public int countByGroupId(Long groupId) {
    String sql = "SELECT COUNT(*) FROM group_household WHERE group_id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, groupId);
  }
}
