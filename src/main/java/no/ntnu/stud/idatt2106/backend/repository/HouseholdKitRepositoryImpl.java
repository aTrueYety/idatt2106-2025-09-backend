package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of the HouseholdKitRepository interface.
 */
@Repository
public class HouseholdKitRepositoryImpl implements HouseholdKitRepository {

  private final JdbcTemplate jdbcTemplate;

  public HouseholdKitRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<HouseholdKit> rowMapper = new RowMapper<>() {
    @Override
    public HouseholdKit mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new HouseholdKit(
          rs.getLong("household_id"),
          rs.getLong("kit_id")
      );
    }
  };

  @Override
  public void save(HouseholdKit householdKit) {
    String sql = "INSERT INTO household_kit (household_id, kit_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, householdKit.getHouseholdId(), householdKit.getKitId());
  }

  @Override
  public void delete(HouseholdKit householdKit) {
    String sql = "DELETE FROM household_kit WHERE household_id = ? AND kit_id = ?";
    jdbcTemplate.update(sql, householdKit.getHouseholdId(), householdKit.getKitId());
  }

  @Override
  public List<HouseholdKit> findByHouseholdId(Long householdId) {
    String sql = "SELECT * FROM household_kit WHERE household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, householdId);
  }

  @Override
  public List<HouseholdKit> findByKitId(Long kitId) {
    String sql = "SELECT * FROM household_kit WHERE kit_id = ?";
    return jdbcTemplate.query(sql, rowMapper, kitId);
  }

  @Override
  public void updateHouseholdForKit(Long oldHouseholdId, Long kitId, Long newHouseholdId) {
    String sql = "UPDATE household_kit SET household_id = ? WHERE household_id = ? AND kit_id = ?";
    jdbcTemplate.update(sql, newHouseholdId, oldHouseholdId, kitId);
  }
}
