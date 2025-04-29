package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import(ExtraResidentRepositoryImpl.class)
public class ExtraResidentRepositoryTest {

  @Autowired
  private ExtraResidentRepository repository;

  @Autowired
  private JdbcTemplate jdbc;

  private void insertRequiredForeignKeys() {
    jdbc.update("INSERT INTO extra_resident_type (id, name, consumption_water, consumption_food) VALUES (?, ?, ?, ?)",
            1, "Visitor", 1.0f, 1.5f);

    jdbc.update("INSERT INTO household (id, adress, latitude, longitude, amount_water, last_water_change) " +
                    "VALUES (?, ?, ?, ?, ?, CURRENT_DATE)",
            1, "Somewhere", 1.1f, 1.2f, 50.0f);
  }

  @Test
  void shouldSaveAndFindExtraResident() {
    insertRequiredForeignKeys();

    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(1);
    resident.setTypeId(1);
    resident.setName("John Doe");

    repository.save(resident);

    List<ExtraResident> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getHouseholdid()).isEqualTo(1);
    assertThat(all.get(0).getTypeId()).isEqualTo(1);
    assertThat(all.get(0).getName()).isEqualTo("John Doe");
  }

  @Test
  void shouldUpdateResident() {
    insertRequiredForeignKeys();

    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(1);
    resident.setTypeId(1);
    resident.setName("John Doe");
    repository.save(resident);

    resident.setTypeId(1);
    resident.setName("Updated Name");
    repository.update(resident);

    ExtraResident updated = repository.findAll().get(0);
    assertThat(updated.getTypeId()).isEqualTo(1);
    assertThat(updated.getHouseholdid()).isEqualTo(1);
  }

  @Test
  void shouldDeleteResident() {
    insertRequiredForeignKeys();

    ExtraResident resident = new ExtraResident();
    resident.setHouseholdid(1);
    resident.setTypeId(1);
    resident.setName("John Doe");
    repository.save(resident);

    assertThat(repository.findAll()).hasSize(1);
    repository.deleteById(resident.getId());
    assertThat(repository.findAll()).isEmpty();
  }
}
