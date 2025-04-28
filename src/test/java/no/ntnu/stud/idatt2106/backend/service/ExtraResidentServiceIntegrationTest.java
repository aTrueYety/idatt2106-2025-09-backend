package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.ActiveProfiles;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import({ExtraResidentService.class, ExtraResidentRepositoryImpl.class})
public class ExtraResidentServiceIntegrationTest {

  @Autowired
  private ExtraResidentService service;

  @Autowired
  private JdbcTemplate jdbc;

  private int householdId;
  private int typeId;

  @BeforeEach
  void setUp() {
    // Insert household
    var householdKey = new GeneratedKeyHolder();
    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO household (adress, latitude, longitude, amount_water, last_water_change) VALUES (?, ?, ?, ?, CURRENT_DATE)",
        Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, "Test Street");
      ps.setFloat(2, 1.1f);
      ps.setFloat(3, 2.2f);
      ps.setFloat(4, 100.0f);
      return ps;
    }, householdKey);
    householdId = householdKey.getKey().intValue();

    // Insert extra_resident_type
    var typeKey = new GeneratedKeyHolder();
    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO extra_resident_type (name, consumption_water, consumption_food) VALUES (?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, "Guest");
      ps.setFloat(2, 1.5f);
      ps.setFloat(3, 1.2f);
      return ps;
    }, typeKey);
    typeId = typeKey.getKey().intValue();
  }

  @Test
  void shouldCreateAndReturnResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId(householdId);
    request.setTypeId(typeId);
    request.setName("Test Resident");

    service.create(request);

    List<ExtraResidentResponse> all = service.getAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getHouseholdId()).isEqualTo(householdId);
    assertThat(all.get(0).getTypeId()).isEqualTo(typeId);
    assertThat(all.get(0).getName()).isEqualTo("Test Resident");
  }

  @Test
  void shouldUpdateResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId(householdId);
    request.setTypeId(typeId);
    request.setName("Test Resident");
    service.create(request);

    int id = service.getAll().get(0).getId();

    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(householdId);
    update.setTypeId(typeId);
    update.setName("Updated Resident");
    boolean success = service.update(id, update);

    assertThat(success).isTrue();
    ExtraResidentResponse updated = service.getById(id).orElseThrow();
    assertThat(updated.getHouseholdId()).isEqualTo(householdId);
  }

  @Test
  void shouldDeleteResident() {
    ExtraResidentRequest request = new ExtraResidentRequest();
    request.setHouseholdId(householdId);
    request.setTypeId(typeId);
    request.setName("Test Resident");
    service.create(request);

    int id = service.getAll().get(0).getId();
    boolean deleted = service.delete(id);
    assertThat(deleted).isTrue();
    assertThat(service.getAll()).isEmpty();
  }
}
