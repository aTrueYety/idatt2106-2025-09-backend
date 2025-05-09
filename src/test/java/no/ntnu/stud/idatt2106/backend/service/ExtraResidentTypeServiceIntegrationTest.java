package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for ExtraResidentTypeService.
 */
@JdbcTest
@ActiveProfiles("test")
@Import({ ExtraResidentTypeService.class, ExtraResidentTypeRepositoryImpl.class })
public class ExtraResidentTypeServiceIntegrationTest {

  @Autowired
  private ExtraResidentTypeService service;

  @Test
  void shouldCreateAndReturnExtraResidentType() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Adult");
    request.setConsumptionWater(1.3f);
    request.setConsumptionFood(1.8f);

    service.create(request);

    List<ExtraResidentTypeResponse> all = service.getAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getName()).isEqualTo("Adult");
    assertThat(all.get(0).getConsumptionWater()).isEqualTo(1.3f);
    assertThat(all.get(0).getConsumptionFood()).isEqualTo(1.8f);
  }

  @Test
  void shouldReturnExtraResidentTypeById() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Adult");
    request.setConsumptionWater(1.1f);
    request.setConsumptionFood(1.4f);
    service.create(request);

    long id = service.getAll().get(0).getId();
    var result = service.getById(id);

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo("Adult");
  }

  @Test
  void shouldUpdateExtraResidentType() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Child");
    request.setConsumptionWater(0.8f);
    request.setConsumptionFood(1.0f);
    service.create(request);

    long id = service.getAll().get(0).getId();

    ExtraResidentTypeRequest update = new ExtraResidentTypeRequest();
    update.setName("Updated Child");
    update.setConsumptionWater(1.0f);
    update.setConsumptionFood(1.2f);

    boolean updated = service.update(id, update);
    assertThat(updated).isTrue();

    var updatedType = service.getById(id).orElseThrow();
    assertThat(updatedType.getName()).isEqualTo("Updated Child");
    assertThat(updatedType.getConsumptionWater()).isEqualTo(1.0f);
  }

  @Test
  void shouldDeleteExtraResidentType() {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Guest");
    request.setConsumptionWater(0.9f);
    request.setConsumptionFood(1.1f);
    service.create(request);

    long id = service.getAll().get(0).getId();

    boolean deleted = service.delete(id);
    assertThat(deleted).isTrue();
    assertThat(service.getById(id)).isEmpty();
  }
}
