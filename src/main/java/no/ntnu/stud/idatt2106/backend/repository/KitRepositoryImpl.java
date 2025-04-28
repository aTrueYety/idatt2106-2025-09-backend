package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the KitRepository interface for managing Kit entities using JDBC.
 */
@Repository
public class KitRepositoryImpl implements KitRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<Kit> rowMapper = new RowMapper<>() {
    @Override
    public Kit mapRow(ResultSet rs, int rowNum) throws SQLException {
      Kit kit = new Kit();
      kit.setId(rs.getLong("id"));
      kit.setName(rs.getString("name"));
      kit.setDescription(rs.getString("description"));
      return kit;
    }
  };

  /**
   * Finds a Kit by its ID.
   *
   * @param id the ID of the Kit to find
   * @return an Optional containing the Kit if found, or empty if not found
   */
  @Override
  public Optional<Kit> findById(Long id) {
    String sql = "SELECT * FROM kit WHERE id = ?";
    List<Kit> result = jdbcTemplate.query(sql, rowMapper, id);
    return result.stream().findFirst();
  }

  /**
   * Retrieves all Kit entities.
   *
   * @return a list of all Kits
   */
  @Override
  public List<Kit> findAll() {
    String sql = "SELECT * FROM kit";
    return jdbcTemplate.query(sql, rowMapper);
  }

  /**
   * Saves a new Kit entity.
   *
   * @param kit the Kit to save
   */
  @Override
  public void save(Kit kit) {
    String sql = "INSERT INTO kit (name, description) VALUES (?, ?)";
    jdbcTemplate.update(sql, kit.getName(), kit.getDescription());
  }

  /**
   * Updates an existing Kit entity.
   *
   * @param kit the Kit with updated data
   */
  @Override
  public void update(Kit kit) {
    String sql = "UPDATE kit SET name = ?, description = ? WHERE id = ?";
    jdbcTemplate.update(sql, kit.getName(), kit.getDescription(), kit.getId());
  }

  /**
   * Deletes a Kit by its ID.
   *
   * @param id the ID of the Kit to delete
   */
  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM kit WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Finds Kits by name containing the given query, ignoring case.
   *
   * @param query the search query to match against Kit names
   * @return a collection of matching KitResponse entities
   */
  @Override
  public List<Kit> findByNameContainingIgnoreCase(String query) {
    String sql = "SELECT * FROM kit WHERE LOWER(name) LIKE LOWER(?)";
    return jdbcTemplate.query(sql, rowMapper, "%" + query + "%");
  }
}
