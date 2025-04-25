package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ExtraResidentTypeRepositoryImpl implements ExtraResidentTypeRepository {

  private final JdbcTemplate jdbc;

  public ExtraResidentTypeRepositoryImpl(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private final RowMapper<ExtraResidentType> rowMapper = (rs, rowNum) -> {
    ExtraResidentType type = new ExtraResidentType();
    type.setId(rs.getInt("id"));
    type.setName(rs.getString("name"));
    type.setConsumptionWater(rs.getFloat("consumption_water"));
    type.setConsumptionFood(rs.getFloat("consumption_food"));
    return type;
  };

  @Override
  public Optional<ExtraResidentType> findById(int id) {
    String sql = "SELECT * FROM extra_resident_type WHERE id = ?";
    return jdbc.query(sql, rowMapper, id).stream().findFirst();
  }

  @Override
  public List<ExtraResidentType> findAll() {
    String sql = "SELECT * FROM extra_resident_type";
    return jdbc.query(sql, rowMapper);
  }

  @Override
  public void save(ExtraResidentType type) {
    String sql = "INSERT INTO extra_resident_type (name, consumption_water, consumption_food) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, type.getName());
      ps.setFloat(2, type.getConsumptionWater());
      ps.setFloat(3, type.getConsumptionFood());
      return ps;
    }, keyHolder);

    if (keyHolder.getKey() != null) {
      type.setId(keyHolder.getKey().intValue());
    }
  }

  @Override
  public void update(ExtraResidentType type) {
    String sql = "UPDATE extra_resident_type SET name = ?, consumption_water = ?, consumption_food = ? WHERE id = ?";
    jdbc.update(sql, type.getName(), type.getConsumptionWater(), type.getConsumptionFood(), type.getId());
  }

  @Override
  public void deleteById(int id) {
    jdbc.update("DELETE FROM extra_resident_type WHERE id = ?", id);
  }
}
