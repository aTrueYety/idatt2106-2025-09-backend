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
        rs.getObject("id", Long.class),
        rs.getString("adress"),
        rs.getString("name"),
        rs.getObject("longitude", Double.class),
        rs.getObject("latitude", Double.class),
        rs.getObject("amount_water", Double.class),
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
        + "(adress, name, latitude, longitude, amount_water, last_water_change) "
        + "VALUES (?, ?, ?, ?, ?, ?)";
    
    
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, household.getAddress());
      ps.setString(2, household.getName());
      ps.setDouble(3, household.getLatitude());
      ps.setDouble(4, household.getLongitude());
      ps.setDouble(5, household.getWaterAmountLiters());
      ps.setDate(6, new java.sql.Date(household
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
    List<Household> households = jdbcTemplate.query(sql, householdRowMapper, id);
    return Optional.ofNullable(households.isEmpty() ? null : households.get(0));
  }

  /**
   * Retrieves all of the registered households.
   *
   * @return a List of all registered households
   */
  @Override
  public List<Household> findAll() {
    String sql = "SELECT * FROM household";
    return jdbcTemplate.query(sql, householdRowMapper);
  }

  /**
   * Updates an existing household with the same ID as the request household.
   *
   * @param household a household with the new values, has the ID of the household to be updated
   */
  @Override
  public void update(Household household) {
    String sql = """
        UPDATE household
        SET adress = ?, name = ?, latitude = ?, longitude = ?, amount_water = ?, 
        last_water_change = ? WHERE id = ?
        """;

    jdbcTemplate.update(sql,
        household.getAddress(),
        household.getName(),
        household.getLatitude(),
        household.getLongitude(),
        household.getWaterAmountLiters(),
        household.getLastWaterChangeDate(),
        household.getId()
    );
  }

  /**
   * Deletes the registered household with the specified ID.
   *
   * @param id the ID of the household t be deleted
   */
  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM household WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Retrieves the household of the user with the specified ID.
   *
   * @param userId the ID of the user
   * @return an Optional containing the household of the user, or an empty Optional if not found
   */
  @Override
  public Optional<Household> findByUserId(Long userId) {
    String sql = "SELECT h.* FROM household h "
        + "JOIN user u ON h.id = u.household_id WHERE u.id = ?";
    List<Household> households = jdbcTemplate.query(sql, householdRowMapper, userId);
    return Optional.ofNullable(households.isEmpty() ? null : households.get(0));
  }
}
