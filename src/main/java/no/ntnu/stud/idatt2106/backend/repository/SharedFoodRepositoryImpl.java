package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFood;
import no.ntnu.stud.idatt2106.backend.model.base.SharedFoodKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of the SharedFoodRepository.
 */
@Repository
@RequiredArgsConstructor
public class SharedFoodRepositoryImpl implements SharedFoodRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<SharedFood> rowMapper = (rs, rowNum) -> new SharedFood(
      new SharedFoodKey(
          rs.getObject("food_id", Long.class),
          rs.getObject("group_household_id", Long.class)),
      rs.getFloat("amount"));

  @Override
  public void save(SharedFood food) {
    String sql = "INSERT INTO shared_food (food_id, group_household_id, amount) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql,
        food.getId().getFoodId(),
        food.getId().getGroupHouseholdId(),
        food.getAmount());
  }

  @Override
  public Optional<SharedFood> findById(SharedFoodKey id) {
    String sql = "SELECT * FROM shared_food WHERE food_id = ? AND group_household_id = ?";
    List<SharedFood> result = jdbcTemplate.query(sql, rowMapper, id.getFoodId(),
        id.getGroupHouseholdId());
    return result.stream().findFirst();
  }

  @Override
  public List<SharedFood> findAll() {
    String sql = "SELECT * FROM shared_food";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public boolean update(SharedFood food) {
    String sql = "UPDATE shared_food SET amount = ? WHERE food_id = ? AND group_household_id = ?";
    int updated = jdbcTemplate.update(sql,
        food.getAmount(),
        food.getId().getFoodId(),
        food.getId().getGroupHouseholdId());
    return updated > 0;
  }

  @Override
  public boolean deleteById(SharedFoodKey id) {
    String sql = "DELETE FROM shared_food WHERE food_id = ? AND group_household_id = ?";
    int deleted = jdbcTemplate.update(sql, id.getFoodId(), id.getGroupHouseholdId());
    return deleted > 0;
  }

  @Override
  public List<SharedFood> findByGroupHouseholdId(Long groupHouseholdId) {
    String sql = "SELECT * FROM shared_food WHERE group_household_id = ?";
    return jdbcTemplate.query(sql, rowMapper, groupHouseholdId);
  }

}
