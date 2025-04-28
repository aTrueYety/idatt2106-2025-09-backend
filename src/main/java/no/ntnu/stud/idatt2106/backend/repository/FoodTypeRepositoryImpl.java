package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implements the methods defined in FoodTypeRepository using JDBC.
 * This class is responsible for interacting with the database to perform CRUD
 * operations on food types.
 */
@Repository
public class FoodTypeRepositoryImpl implements FoodTypeRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<FoodType> rowMapper = new RowMapper<>() {
    @Override
    public FoodType mapRow(ResultSet rs, int rowNum) throws SQLException {
      FoodType foodType = new FoodType();
      foodType.setId(rs.getInt("id"));
      foodType.setName(rs.getString("name"));
      foodType.setUnit(rs.getString("unit"));
      foodType.setCaloriesPerUnit(rs.getFloat("calories_per_unit"));
      foodType.setPicture(rs.getBytes("picture"));
      return foodType;
    }
  };

  @Override
  public Optional<FoodType> findById(int id) {
    String sql = "SELECT * FROM food_type WHERE id = ?";
    List<FoodType> result = jdbcTemplate.query(sql, rowMapper, id);
    return result.stream().findFirst();
  }

  @Override
  public List<FoodType> findAll() {
    String sql = "SELECT * FROM food_type";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public void save(FoodType foodType) {
    String sql = "INSERT INTO food_type (name, unit, calories_per_unit, picture) " 
        + "VALUES (?, ?, ?, ?)";
    var keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      var ps = connection.prepareStatement(sql, new String[] { "id" });
      ps.setString(1, foodType.getName());
      ps.setString(2, foodType.getUnit());
      ps.setFloat(3, foodType.getCaloriesPerUnit());
      ps.setBytes(4, foodType.getPicture());
      return ps;
    }, keyHolder);

    if (keyHolder.getKey() != null) {
      foodType.setId(keyHolder.getKey().intValue());
    }
  }

  @Override
  public void update(FoodType foodType) {
    String sql = "UPDATE food_type SET name = ?, unit = ?, calories_per_unit = ?, " 
        + "picture = ? WHERE id = ?";
    jdbcTemplate.update(sql, foodType.getName(), foodType.getUnit(),
        foodType.getCaloriesPerUnit(), foodType.getPicture(), foodType.getId());
  }

  @Override
  public void deleteById(int id) {
    String sql = "DELETE FROM food_type WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
