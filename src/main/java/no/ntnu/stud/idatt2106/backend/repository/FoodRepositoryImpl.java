package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for managing food items in the database.
 * This class uses JdbcTemplate to perform CRUD operations on the food table.
 */
@Repository
public class FoodRepositoryImpl implements FoodRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<Food> rowMapper = (rs, rowNum) -> {
    Food food = new Food();
    food.setId(rs.getObject("id", Long.class));
    food.setTypeId(rs.getObject("type_id", Long.class));
    food.setHouseholdId(rs.getObject("household_id", Long.class));
    food.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
    food.setAmount(rs.getFloat("amount"));
    return food;
  };

  @Override
  public Optional<Food> findById(Long id) {
    String sql = "SELECT * FROM food WHERE id = ?";
    List<Food> results = jdbcTemplate.query(sql, rowMapper, id);
    return results.stream().findFirst();
  }

  @Override
  public List<Food> findAll() {
    String sql = "SELECT * FROM food";
    return jdbcTemplate.query(sql, rowMapper);
  }

  /**
   * Inserts a new food item into the database and sets its generated ID.
   *
   * @param food the food item to save
   * @return the generated ID of the inserted food item
   */
  @Override
  public Long save(Food food) {
    String sql = "INSERT INTO food (type_id, household_id, expiration_date, amount) "
        + "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, food.getTypeId());
      ps.setLong(2, food.getHouseholdId());
      ps.setDate(3, Date.valueOf(food.getExpirationDate()));
      ps.setDouble(4, food.getAmount());
      return ps;
    }, keyHolder);

    if (keyHolder.getKey() != null) {
      Long generatedId = keyHolder.getKey().longValue();
      food.setId(generatedId);
      return generatedId;
    } else {
      throw new RuntimeException("Failed to retrieve generated key for food insert.");
    }
  }

  /**
   * Updates an existing food item.
   *
   * @param food the food item with updated fields
   */
  @Override
  public void update(Food food) {
    String sql = "UPDATE food SET type_id = ?, household_id = ?, expiration_date = ?, "
        + "amount = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        food.getTypeId(),
        food.getHouseholdId(),
        Date.valueOf(food.getExpirationDate()),
        food.getAmount(),
        food.getId());
  }

  /**
   * Deletes a food item by its ID.
   *
   * @param id the ID of the food item to delete
   */
  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM food WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Retrieves all food items belonging to a given household.
   *
   * @param householdId the ID of the household
   * @return a list of food items for the specified household
   */
  @Override
  public List<Food> findByHouseholdId(long householdId) {
    String sql = "SELECT * FROM food WHERE household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, householdId);
  }
}
