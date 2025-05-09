package no.ntnu.stud.idatt2106.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.config.SecurityConfigTest;
import no.ntnu.stud.idatt2106.backend.model.request.FoodRequest;
import no.ntnu.stud.idatt2106.backend.model.response.FoodResponse;
import no.ntnu.stud.idatt2106.backend.model.update.FoodUpdate;
import no.ntnu.stud.idatt2106.backend.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for FoodController.
 */
@WebMvcTest(FoodController.class)
@Import(SecurityConfigTest.class)
public class FoodControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FoodService service;

  private final String TOKEN = "Bearer test.jwt.token";

  @Test
  void shouldGetAllFood() throws Exception {
    FoodResponse response = new FoodResponse();
    response.setId(1L);
    response.setAmount(3);
    response.setTypeId(1L);
    response.setHouseholdId(10L);
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
    response.setId(1L);
    response.setAmount(3);
    response.setExpirationDate(LocalDate.of(2025, 5, 1));

    when(service.getById(1L)).thenReturn(Optional.of(response));

    mockMvc.perform(get("/api/food/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturnNotFoundIfFoodMissing() throws Exception {
    when(service.getById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/food/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateFood() throws Exception {
    FoodRequest request = new FoodRequest();
    request.setTypeId(1L);
    request.setHouseholdId(10L);
    request.setAmount(2);
    request.setExpirationDate(LocalDate.of(2025, 6, 1));

    mockMvc.perform(post("/api/food")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(service).create(any(FoodRequest.class), eq(TOKEN));
  }

  @Test
  void shouldUpdateFood() throws Exception {
    FoodUpdate update = new FoodUpdate();
    update.setTypeId(2L);
    update.setHouseholdId(12L);
    update.setAmount(5);
    update.setExpirationDate(LocalDate.of(2025, 6, 30));

    when(service.update(eq(1L), any(FoodUpdate.class), eq(TOKEN))).thenReturn(true);

    mockMvc.perform(put("/api/food/1")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenUpdatingMissing() throws Exception {
    FoodUpdate update = new FoodUpdate();
    update.setTypeId(2L);
    update.setHouseholdId(12L);
    update.setAmount(5);
    update.setExpirationDate(LocalDate.of(2025, 6, 30));

    when(service.update(eq(1L), any(), eq(TOKEN))).thenReturn(false);

    mockMvc.perform(put("/api/food/1")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteFood() throws Exception {
    when(service.delete(1L, TOKEN)).thenReturn(true);

    mockMvc.perform(delete("/api/food/1")
            .header("Authorization", TOKEN))
        .andExpect(status().isNoContent());

    verify(service).delete(1L, TOKEN);
  }

  @Test
  void shouldReturnNotFoundOnDeleteIfMissing() throws Exception {
    when(service.delete(999L, TOKEN)).thenReturn(false);

    mockMvc.perform(delete("/api/food/999")
            .header("Authorization", TOKEN))
        .andExpect(status().isNotFound());
  }
}
