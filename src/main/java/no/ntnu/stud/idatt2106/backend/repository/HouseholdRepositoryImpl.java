package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Implements the methods defined in HouseholdRepository using JDBC.
 */
@Repository
public class HouseholdRepositoryImpl implements HouseholdRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private RowMapper<Household> householdRowMapper = (rs, rowNum) -> {
    return new Household(
        rs.getLong("id"),
        rs.getString("adress"),
        rs.getDouble("longitude"),
        rs.getDouble("latitude"),
        rs.getDouble("amount_water"),
        rs.getTimestamp("last_water_change")
    );
  };

  /**
   * Saves a household to the repository.
   *
   * @param household the household to be saved
   * @return the saved household, or null if there is an exception
   */
  @Override
  public Household save(Household household) {
    String sql = "INSERT INTO household "
        + "(adress, latitude, longitude, amount_water, last_water_change) "
        + "VALUES (?, ?, ?, ?, ?)";
    
    
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, household.getAdress());
      ps.setDouble(2, household.getLatitude());
      ps.setDouble(3, household.getLongitude());
      ps.setDouble(4, household.getWaterAmountLiters());
      ps.setDate(5, new java.sql.Date(household
          .getLastWaterChangeDate().getTime()));
      return ps;
    }, keyHolder);

    // Get the generated ID
    Number generatedId = keyHolder.getKey();
    if (generatedId != null) {
      household.setId(generatedId.longValue()); // Assuming Household has a setId() method
    }

    return household;
  }

  /**
   * Retrieves the Household object in the repository with the specified ID.
   *
   * @param id the id of the Household to retrieve
   * @return an Optional containing the Household object with the specified ID,
   *     or an empty Optional if there is no Household with the specified ID
   */
  @Override
  public Optional<Household> findById(Long id) {
    String sql = "SELECT * FROM household WHERE id = ?";
    Household household = jdbcTemplate.queryForObject(sql, householdRowMapper, id);
    return Optional.ofNullable(household);
  }

  /**
   * Retrieves all of the registered households.
   * 
   * 
   * @return a List of all registered households
   */
  @Override
  public List<Household> findAll() {
    String sql = "SELECT * FROM household";
    return jdbcTemplate.query(sql, householdRowMapper);
  }
}
