package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
      rs.getInt("id"),
      rs.getInt("household_id"),
      rs.getInt("group_id"));

  @Override
  public void save(GroupHousehold groupHousehold) {
    String sql = "INSERT INTO group_household (household_id, group_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, groupHousehold.getHouseholdId(), groupHousehold.getGroupId());
  }

  @Override
  public Optional<GroupHousehold> findById(int id) {
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
  public List<GroupHousehold> findByGroupId(int groupId) {
    String sql = "SELECT * FROM group_household WHERE group_id = ?";
    return jdbcTemplate.query(sql, rowMapper, groupId);
  }

  @Override
  public boolean deleteById(int id) {
    String sql = "DELETE FROM group_household WHERE id = ?";
    return jdbcTemplate.update(sql, id) > 0;
  }
}
