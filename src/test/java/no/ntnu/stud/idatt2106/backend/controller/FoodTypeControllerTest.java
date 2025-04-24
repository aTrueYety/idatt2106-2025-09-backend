package no.ntnu.stud.idatt2106.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.ntnu.stud.idatt2106.backend.config.SecurityConfigTest;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodTypeController.class)
@Import(SecurityConfigTest.class)
public class FoodTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodTypeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllFoodTypes() throws Exception {
        FoodType milk = new FoodType();
        milk.setId(1);
        milk.setName("Milk");
        milk.setUnit("liter");
        milk.setCaloriesPerUnit(64.0f);

        when(repository.findAll()).thenReturn(List.of(milk));

        mockMvc.perform(get("/api/food-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Milk"))
                .andExpect(jsonPath("$[0].unit").value("liter"));
    }

    @Test
    void shouldReturnFoodTypeById() throws Exception {
        FoodType milk = new FoodType();
        milk.setId(1);
        milk.setName("Milk");
        milk.setUnit("liter");
        milk.setCaloriesPerUnit(64.0f);

        when(repository.findById(1)).thenReturn(Optional.of(milk));

        mockMvc.perform(get("/api/food-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Milk"));
    }

    @Test
    void shouldReturnNotFoundForMissingId() throws Exception {
        when(repository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/food-types/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewFoodType() throws Exception {
        FoodType newFood = new FoodType();
        newFood.setName("Yogurt");
        newFood.setUnit("gram");
        newFood.setCaloriesPerUnit(50.0f);

        mockMvc.perform(post("/api/food-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFood)))
                .andExpect(status().isCreated());

        verify(repository, times(1)).save(any(FoodType.class));
    }

    @Test
    void shouldUpdateFoodType() throws Exception {
        FoodType updated = new FoodType();
        updated.setName("Skim Milk");
        updated.setUnit("liter");
        updated.setCaloriesPerUnit(40.0f);

        when(repository.findById(1)).thenReturn(Optional.of(new FoodType()));

        mockMvc.perform(put("/api/food-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository).update(argThat(foodType -> 
            foodType.getName().equals("Skim Milk") &&
            foodType.getCaloriesPerUnit() == 40.0f
        ));
    }

    @Test
    void shouldDeleteFoodType() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.of(new FoodType()));

        mockMvc.perform(delete("/api/food-types/1"))
                .andExpect(status().isNoContent());

        verify(repository).deleteById(1);
    }
}
