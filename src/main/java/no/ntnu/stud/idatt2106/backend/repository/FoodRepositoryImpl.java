package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.Food;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FoodRepositoryImpl implements FoodRepository {

  private final JdbcTemplate jdbcTemplate;

  public FoodRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Food> rowMapper = (rs, rowNum) -> {
    Food food = new Food();
    food.setId(rs.getInt("id"));
    food.setTypeId(rs.getInt("type_id"));
    food.setHouseholdId(rs.getInt("household_id"));
    food.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
    food.setAmount(rs.getInt("amount"));
    return food;
  };

  @Override
  public Optional<Food> findById(int id) {
    String sql = "SELECT * FROM food WHERE id = ?";
    List<Food> results = jdbcTemplate.query(sql, rowMapper, id);
    return results.stream().findFirst();
  }

  @Override
  public List<Food> findAll() {
    return jdbcTemplate.query("SELECT * FROM food", rowMapper);
  }

  @Override
  public void save(Food food) {
    String sql = "INSERT INTO food (type_id, household_id, expiration_date, amount) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, food.getTypeId(), food.getHouseholdId(), Date.valueOf(food.getExpirationDate()), food.getAmount());
  }

  @Override
  public void update(Food food) {
    String sql = "UPDATE food SET type_id = ?, household_id = ?, expiration_date = ?, amount = ? WHERE id = ?";
    jdbcTemplate.update(sql, food.getTypeId(), food.getHouseholdId(), Date.valueOf(food.getExpirationDate()), food.getAmount(), food.getId());
  }

  @Override
  public void deleteById(int id) {
    jdbcTemplate.update("DELETE FROM food WHERE id = ?", id);
  }
}
