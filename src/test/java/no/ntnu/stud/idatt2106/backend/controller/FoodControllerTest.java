package no.ntnu.stud.idatt2106.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.stud.idatt2106.backend.config.SecurityConfigTest;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@Import(SecurityConfigTest.class)
public class FoodControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private FoodService service;

  @Test
  void shouldGetAllFood() throws Exception {
    FoodResponse response = new FoodResponse();
    response.setId(1);
    response.setAmount(3);
    response.setTypeId(1);
    response.setHouseholdId(10);
    response.setExpirationDate(LocalDate.of(2025, 5, 1));

    when(service.getAll()).thenReturn(List.of(response));

    mockMvc.perform(get("/api/food"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].amount").value(3));
  }

  @Test
  void shouldGetFoodById() throws Exception {
    FoodResponse response = new FoodResponse();
    response.setId(1);
    response.setAmount(3);
    response.setExpirationDate(LocalDate.of(2025, 5, 1));

    when(service.getById(1)).thenReturn(Optional.of(response));

    mockMvc.perform(get("/api/food/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturnNotFoundIfFoodMissing() throws Exception {
    when(service.getById(999)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/food/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateFood() throws Exception {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1);
    request.setHouseholdId(10);
    request.setAmount(2);
    request.setExpirationDate(LocalDate.of(2025, 6, 1));

    mockMvc.perform(post("/api/food")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(service).create(any(FoodRequest.class));
  }

  @Test
  void shouldUpdateFood() throws Exception {
    FoodUpdate update = new FoodUpdate();
    update.setTypeId(2);
    update.setHouseholdId(12);
    update.setAmount(5);
    update.setExpirationDate(LocalDate.of(2025, 6, 30));

    when(service.update(eq(1), any(FoodUpdate.class))).thenReturn(true);

    mockMvc.perform(put("/api/food/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenUpdatingMissing() throws Exception {
    FoodUpdate update = new FoodUpdate();
    update.setTypeId(2);
    update.setHouseholdId(12);
    update.setAmount(5);
    update.setExpirationDate(LocalDate.of(2025, 6, 30));

    when(service.update(eq(1), any())).thenReturn(false);

    mockMvc.perform(put("/api/food/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteFood() throws Exception {
    when(service.delete(1)).thenReturn(true);

    mockMvc.perform(delete("/api/food/1"))
        .andExpect(status().isNoContent());

    verify(service).delete(1);
  }

  @Test
  void shouldReturnNotFoundOnDeleteIfMissing() throws Exception {
    when(service.delete(999)).thenReturn(false);

    mockMvc.perform(delete("/api/food/999"))
        .andExpect(status().isNotFound());
  }
}
