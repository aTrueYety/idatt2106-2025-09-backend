package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import({FoodTypeService.class, FoodTypeRepositoryImpl.class})
public class FoodTypeServiceIntegrationTest {

    @Autowired
    private FoodTypeService service;

    @Test
    void shouldCreateAndReturnFoodType() {
        FoodTypeRequest request = new FoodTypeRequest();
        request.setName("Yoghurt");
        request.setUnit("dl");
        request.setCaloriesPerUnit(50.0f);
        request.setPicture(null);

        service.create(request);

        List<FoodTypeResponse> all = service.getAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Yoghurt");
    }
}
