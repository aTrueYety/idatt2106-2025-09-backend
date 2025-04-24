package no.ntnu.stud.idatt2106.backend.repository;

import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implements the methods defined in HouseholdRepository using JDBC.
 *
 * @version 1.0
 * @since 23.04.2025
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
        + "(id, adress, latitude, longditude, amount_water, last_water_change)"
        + "VALUES (?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(sql, 
        household.getId(), 
        household.getAdress(), 
        household.getWaterAmountLiters(), 
        household.getLastWaterChangeDate());
          
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

    try {
      Household household = jdbcTemplate.queryForObject(sql, householdRowMapper, id);
      return Optional.ofNullable(household);
    } catch (Exception e) {
      return Optional.empty();
    }
  }
    
}
