package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.Kit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the KitRepository interface for managing Kit entities using JDBC.
 */
@Repository
@RequiredArgsConstructor
public class KitRepositoryImpl implements KitRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final RowMapper<Kit> KIT_ROW_MAPPER = new KitRowMapper();

  @Override
  public Optional<Kit> findById(Long id) {
    String sql = "SELECT * FROM kit WHERE id = ?";
    List<Kit> result = jdbcTemplate.query(sql, KIT_ROW_MAPPER, id);
    return result.stream().findFirst();
  }

  @Override
  public List<Kit> findAll() {
    String sql = "SELECT * FROM kit";
    return jdbcTemplate.query(sql, KIT_ROW_MAPPER);
  }

  @Override
  public void save(Kit kit) {
    String sql = "INSERT INTO kit (name, description) VALUES (?, ?)";
    jdbcTemplate.update(sql, kit.getName(), kit.getDescription());
  }

  @Override
  public void update(Kit kit) {
    String sql = "UPDATE kit SET name = ?, description = ? WHERE id = ?";
    jdbcTemplate.update(sql, kit.getName(), kit.getDescription(), kit.getId());
  }

  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM kit WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public List<Kit> findByNameContainingIgnoreCase(String query) {
    String sql = "SELECT * FROM kit WHERE LOWER(name) LIKE LOWER(?)";
    return jdbcTemplate.query(sql, KIT_ROW_MAPPER, "%" + query + "%");
  }

  /**
   * Private static class for mapping ResultSet rows to Kit objects.
   */
  private static class KitRowMapper implements RowMapper<Kit> {
    @Override
    public Kit mapRow(ResultSet rs, int rowNum) throws SQLException {
      Kit kit = new Kit();
      kit.setId(rs.getLong("id"));
      kit.setName(rs.getString("name"));
      kit.setDescription(rs.getString("description"));
      return kit;
    }
  }
}
