package no.ntnu.stud.idatt2106.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.config.SecurityConfigTest;
import no.ntnu.stud.idatt2106.backend.model.request.FoodTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.FoodTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for FoodTypeController.
 */
@WebMvcTest(FoodTypeController.class)
@Import(SecurityConfigTest.class)
public class FoodTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FoodTypeService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldGetAllFoodTypes() throws Exception {
    FoodTypeResponse milk = new FoodTypeResponse();
    milk.setId(1L);
    milk.setName("Milk");
    milk.setUnit("liter");
    milk.setCaloriesPerUnit(64.0f);

    when(service.getAll()).thenReturn(List.of(milk));

    mockMvc.perform(get("/api/food-types"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Milk"))
        .andExpect(jsonPath("$[0].unit").value("liter"));
  }

  @Test
  void shouldReturnFoodTypeById() throws Exception {
    FoodTypeResponse milk = new FoodTypeResponse();
    milk.setId(1L);
    milk.setName("Milk");
    milk.setUnit("liter");
    milk.setCaloriesPerUnit(64.0f);

    when(service.getById(1L)).thenReturn(Optional.of(milk));

    mockMvc.perform(get("/api/food-types/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Milk"));
  }

  @Test
  void shouldReturnNotFoundForMissingId() throws Exception {
    when(service.getById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/food-types/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateNewFoodType() throws Exception {
    FoodTypeRequest request = new FoodTypeRequest();
    request.setName("Yogurt");
    request.setUnit("gram");
    request.setCaloriesPerUnit(50.0f);

    mockMvc.perform(post("/api/food-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(service).create(any(FoodTypeRequest.class));
  }

  @Test
  void shouldUpdateFoodType() throws Exception {
    FoodTypeRequest updated = new FoodTypeRequest();
    updated.setName("Skim Milk");
    updated.setUnit("liter");
    updated.setCaloriesPerUnit(40.0f);

    when(service.update(eq(1L), any(FoodTypeRequest.class))).thenReturn(true);

    mockMvc.perform(put("/api/food-types/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundOnUpdateIfMissing() throws Exception {
    FoodTypeRequest updated = new FoodTypeRequest();
    updated.setName("Skim Milk");
    updated.setUnit("liter");
    updated.setCaloriesPerUnit(40.0f);

    when(service.update(eq(1L), any())).thenReturn(false);

    mockMvc.perform(put("/api/food-types/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteFoodType() throws Exception {
    when(service.delete(1L)).thenReturn(true);

    mockMvc.perform(delete("/api/food-types/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundOnDeleteIfMissing() throws Exception {
    when(service.delete(999L)).thenReturn(false);

    mockMvc.perform(delete("/api/food-types/999"))
        .andExpect(status().isNotFound());
  }
}
