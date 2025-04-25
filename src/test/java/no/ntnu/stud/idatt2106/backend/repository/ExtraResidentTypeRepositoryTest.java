package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import(ExtraResidentTypeRepositoryImpl.class)
public class ExtraResidentTypeRepositoryTest {

  @Autowired
  private ExtraResidentTypeRepository repository;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldSaveAndRetrieve() {
    ExtraResidentType type = new ExtraResidentType();
    type.setName("Adult");
    type.setConsumptionWater(1.2f);
    type.setConsumptionFood(1.5f);

    repository.save(type);

    List<ExtraResidentType> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getName()).isEqualTo("Adult");

  

  }

  @Test
  void shouldUpdateType() {
    ExtraResidentType type = new ExtraResidentType();
    type.setName("Temporary");
    type.setConsumptionWater(1.0f);
    type.setConsumptionFood(1.1f);
    repository.save(type);

    type.setName("Temporary Updated");
    repository.update(type);

    ExtraResidentType updated = repository.findAll().get(0);
    assertThat(updated.getName()).isEqualTo("Temporary Updated");
  }

  @Test
  void shouldDeleteType() {
    ExtraResidentType type = new ExtraResidentType();
    type.setName("ToDelete");
    type.setConsumptionWater(1.0f);
    type.setConsumptionFood(1.0f);
    repository.save(type);

    repository.deleteById(type.getId());
    assertThat(repository.findAll()).isEmpty();
  }
}
