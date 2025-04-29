package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
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
      rs.getInt("id"),
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

}
