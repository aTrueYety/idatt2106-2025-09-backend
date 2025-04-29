package no.ntnu.stud.idatt2106.backend.repository;



import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Severity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository class for managing Severity entities in the database.
 */
@Repository
public class SeverityRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<Severity> severityRowMapper = (rs, rowNum) -> {
    return new Severity(
        rs.getLong("id"),
        rs.getString("colour"),
        rs.getString("name"),
        rs.getString("description"));
  };

  /**
   * Saves a severity level to the repository.
   *
   * @param severity the severity level to be saved
   * @return the number of rows affected
   */
  public int save(Severity severity) {
    String sql = "INSERT INTO severity "
        + "(id, colour, name, description) VALUES (?, ?, ?, ?)";
    return jdbcTemplate.update(sql,
        severity.getId(),
        severity.getColour(),
        severity.getName(),
        severity.getDescription());
  }

  /**
   * Finds a severity level by its ID.
   *
   * @param id the ID of the severity level to be found
   * @return the severity level with the specified ID, or null if not found
   */
  public Severity findSeverityById(long id) {
    String sql = "SELECT * FROM severity WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, severityRowMapper, id);
  }

  /**
   * Updates a severity level in the repository.
   *
   * @param severity the severity level to be updated
   * @return the number of rows affected
   */
  public int update(Severity severity) {
    String sql = "UPDATE severity SET colour = ?, name = ?, description = ? WHERE id = ?";
    return jdbcTemplate.update(sql,
        severity.getColour(),
        severity.getName(),
        severity.getDescription(),
        severity.getId());
  }

  /**
   * Deletes a severity level from the repository.
   *
   * @param id the ID of the severity level to be deleted
   * @return the number of rows affected
   */
  public int delete(long id) {
    String sql = "DELETE FROM severity WHERE id = ?";
    return jdbcTemplate.update(sql, id);
  }

  /**
   * Finds all severity levels in the repository.
   *
   * @return a list of all severity levels
   */
  public List<Severity> findAll() {
    String sql = "SELECT * FROM severity";
    return jdbcTemplate.query(sql, severityRowMapper);
  }
}
