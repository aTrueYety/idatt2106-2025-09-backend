package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FoodTypeMapperTest {

    @Test
    void shouldMapRequestToModel() {
        FoodTypeRequest request = new FoodTypeRequest();
        request.setName("Yoghurt");
        request.setUnit("dl");
        request.setCaloriesPerUnit(60.0f);
        request.setPicture(new byte[]{1, 2, 3});

        FoodType model = FoodTypeMapper.toModel(request);

        assertThat(model.getName()).isEqualTo("Yoghurt");
        assertThat(model.getUnit()).isEqualTo("dl");
        assertThat(model.getCaloriesPerUnit()).isEqualTo(60.0f);
        assertThat(model.getPicture()).containsExactly(1, 2, 3);
    }

    @Test
    void shouldMapModelToResponse() {
        FoodType model = new FoodType();
        model.setId(42);
        model.setName("Milk");
        model.setUnit("liter");
        model.setCaloriesPerUnit(64.0f);
        model.setPicture(new byte[]{9, 8});

        FoodTypeResponse response = FoodTypeMapper.toResponse(model);

        assertThat(response.getId()).isEqualTo(42);
        assertThat(response.getName()).isEqualTo("Milk");
        assertThat(response.getUnit()).isEqualTo("liter");
        assertThat(response.getCaloriesPerUnit()).isEqualTo(64.0f);
        assertThat(response.getPicture()).containsExactly(9, 8);
    }
}
