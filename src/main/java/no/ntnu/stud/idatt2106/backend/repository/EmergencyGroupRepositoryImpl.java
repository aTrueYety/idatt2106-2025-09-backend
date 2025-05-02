package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupSummaryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of the EmergencyGroupRepository.
 */
@Repository
@RequiredArgsConstructor
public class EmergencyGroupRepositoryImpl implements EmergencyGroupRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<EmergencyGroup> rowMapper = (rs, rowNum) -> new EmergencyGroup(
      rs.getObject("id", Long.class),
      rs.getString("name"),
      rs.getString("description"));

  @Override
  public void save(EmergencyGroup group) {
    String sql = "INSERT INTO emergency_group (name, description) VALUES (?, ?)";
    jdbcTemplate.update(sql, group.getName(), group.getDescription());
  }

  @Override
  public Optional<EmergencyGroup> findById(int id) {
    String sql = "SELECT * FROM emergency_group WHERE id = ?";
    List<EmergencyGroup> result = jdbcTemplate.query(sql, rowMapper, id);
    return result.stream().findFirst();
  }

  @Override
  public List<EmergencyGroup> findAll() {
    return jdbcTemplate.query("SELECT * FROM emergency_group", rowMapper);
  }

  @Override
  public boolean deleteById(int id) {
    String sql = "DELETE FROM emergency_group WHERE id = ?";
    return jdbcTemplate.update(sql, id) > 0;
  }

  @Override
  public boolean update(int id, EmergencyGroup group) {
    String sql = "UPDATE emergency_group SET name = ?, description = ? WHERE id = ?";
    int updated = jdbcTemplate.update(sql, group.getName(), group.getDescription(), id);
    return updated > 0;
  }

  @Override
  public List<EmergencyGroupSummaryResponse> findGroupSummariesByHouseholdId(int householdId) {
    String sql = "SELECT eg.id AS group_id, "
        +
        "       eg.name AS group_name, "
        +
        "       eg.description AS group_description, "
        +
        "       COUNT(DISTINCT gh.household_id) AS total_households, "
        +
        "       COALESCE(SUM(u.count), 0) AS total_users, "
        +
        "       COALESCE(SUM(er.count), 0) AS total_extra_residents "
        +
        "FROM emergency_group eg "
        +
        "JOIN group_household gh ON eg.id = gh.group_id "
        +
        "JOIN household h ON gh.household_id = h.id "
        +
        "LEFT JOIN ( "
        +
        "  SELECT household_id, COUNT(*) AS count "
        +
        "  FROM user "
        +
        "  GROUP BY household_id "
        +
        ") u ON h.id = u.household_id "
        +
        "LEFT JOIN ( "
        +
        "  SELECT household_id, COUNT(*) AS count "
        +
        "  FROM extra_resident "
        +
        "  GROUP BY household_id "
        +
        ") er ON h.id = er.household_id "
        +
        "WHERE eg.id IN ( "
        +
        "  SELECT group_id FROM group_household WHERE household_id = ? "
        +
        ") "
        +
        "GROUP BY eg.id, eg.name, eg.description";

    return jdbcTemplate.query(
        sql,
        (rs, rowNum) -> new EmergencyGroupSummaryResponse(
            rs.getLong("group_id"),
            rs.getString("group_name"),
            rs.getString("group_description"),
            rs.getInt("total_households"),
            rs.getInt("total_users"),
            rs.getInt("total_extra_residents")),
        householdId);
  }
}
