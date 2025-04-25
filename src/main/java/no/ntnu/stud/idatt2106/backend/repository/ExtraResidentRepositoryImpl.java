package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
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
public class ExtraResidentRepositoryImpl implements ExtraResidentRepository {

  private final JdbcTemplate jdbc;

  public ExtraResidentRepositoryImpl(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private final RowMapper<ExtraResident> rowMapper = (rs, rowNum) -> {
    ExtraResident resident = new ExtraResident();
    resident.setId(rs.getInt("id"));
    resident.setHouseholdid(rs.getInt("household_id"));
    resident.setTypeId(rs.getInt("type_id"));
    return resident;
  };

  @Override
  public Optional<ExtraResident> findById(int id) {
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
    String sql = "INSERT INTO extra_resident (household_id, type_id) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, extraResident.getHouseholdid());
      ps.setInt(2, extraResident.getTypeId());
      return ps;
    }, keyHolder);

    if (keyHolder.getKey() != null) {
      extraResident.setId(keyHolder.getKey().intValue());
    }
  }

  @Override
  public void update(ExtraResident extraResident) {
    String sql = "UPDATE extra_resident SET household_id = ?, type_id = ? WHERE id = ?";
    jdbc.update(sql, extraResident.getHouseholdid(), extraResident.getTypeId(), extraResident.getId());
  }

  @Override
  public void deleteById(int id) {
    jdbc.update("DELETE FROM extra_resident WHERE id = ?", id);
  }
}
