package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Implements the methods defined in ExtraResidentRepository using JDBC.
 * This class is responsible for interacting with the database to perform CRUD
 * operations on extra residents.
 */
@Repository
public class ExtraResidentRepositoryImpl implements ExtraResidentRepository {
  @Autowired
  private JdbcTemplate jdbc;

  private final RowMapper<ExtraResident> rowMapper = (rs, rowNum) -> {
    ExtraResident resident = new ExtraResident();
    resident.setId(rs.getObject("id", Long.class));
    resident.setHouseholdid(rs.getObject("household_id", Long.class));
    resident.setTypeId(rs.getObject("type_id", Long.class));
    resident.setName(rs.getString("name"));
    return resident;
  };

  @Override
  public Optional<ExtraResident> findById(long id) {
    String sql = "SELECT * FROM extra_resident WHERE id = ?";
    return jdbc.query(sql, rowMapper, id).stream().findFirst();
  }

  @Override
  public List<ExtraResident> findAll() {
    String sql = "SELECT * FROM extra_resident";
    return jdbc.query(sql, rowMapper);
  }

  @Override
  public void save(ExtraResident extraResident) {
    String sql = "INSERT INTO extra_resident (household_id, type_id, name) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, extraResident.getHouseholdid());
      ps.setLong(2, extraResident.getTypeId());
      ps.setString(3, extraResident.getName());
      return ps;
    }, keyHolder);

    if (keyHolder.getKey() != null) {
      extraResident.setId(keyHolder.getKey().intValue());
    }
  }

  @Override
  public void update(ExtraResident extraResident) {
    String sql = "UPDATE extra_resident SET household_id = ?, type_id = ?, name = ? WHERE id = ?";
    jdbc.update(
        sql, extraResident.getHouseholdid(), extraResident.getTypeId(), extraResident.getName(),
        extraResident.getId());
  }

  @Override
  public void deleteById(long id) {
    jdbc.update("DELETE FROM extra_resident WHERE id = ?", id);
  }
}
