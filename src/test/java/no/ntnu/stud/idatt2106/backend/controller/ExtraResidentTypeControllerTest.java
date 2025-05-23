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
import no.ntnu.stud.idatt2106.backend.config.JwtAuthFilter;
import no.ntnu.stud.idatt2106.backend.config.SecurityConfigTest;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for ExtraResidentType.
 */
@WebMvcTest(controllers = ExtraResidentTypeController.class, 
    excludeFilters = @ComponentScan.Filter(type 
    = FilterType.ASSIGNABLE_TYPE, value = JwtAuthFilter.class))
@Import(SecurityConfigTest.class)
public class ExtraResidentTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ExtraResidentTypeService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldGetAllExtraResidentTypes() throws Exception {
    ExtraResidentTypeResponse type = new ExtraResidentTypeResponse();
    type.setId(1);
    type.setName("Visitor");
    type.setConsumptionWater(1.5f);
    type.setConsumptionFood(2.0f);

    when(service.getAll()).thenReturn(List.of(type));

    mockMvc.perform(get("/api/extra-resident-types"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Visitor"))
        .andExpect(jsonPath("$[0].consumptionWater").value(1.5f));
  }

  @Test
  void shouldReturnExtraResidentTypeById() throws Exception {
    ExtraResidentTypeResponse type = new ExtraResidentTypeResponse();
    type.setId(1);
    type.setName("Guest");
    type.setConsumptionWater(1.2f);
    type.setConsumptionFood(1.7f);

    when(service.getById(1)).thenReturn(Optional.of(type));

    mockMvc.perform(get("/api/extra-resident-types/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Guest"));
  }

  @Test
  void shouldReturnNotFoundForMissingId() throws Exception {
    when(service.getById(999)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/extra-resident-types/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateNewExtraResidentType() throws Exception {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Guest");
    request.setConsumptionWater(1.2f);
    request.setConsumptionFood(1.7f);

    mockMvc.perform(post("/api/extra-resident-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(service).create(any(ExtraResidentTypeRequest.class));
  }

  @Test
  void shouldReturnNotFoundOnUpdateIfMissing() throws Exception {
    ExtraResidentTypeRequest request = new ExtraResidentTypeRequest();
    request.setName("Doesn't Exist");
    request.setConsumptionWater(1.0f);
    request.setConsumptionFood(1.0f);

    when(service.update(eq(1), any())).thenReturn(false);

    mockMvc.perform(put("/api/extra-resident-types/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteExtraResidentType() throws Exception {
    when(service.delete(1)).thenReturn(true);

    mockMvc.perform(delete("/api/extra-resident-types/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundOnDeleteIfMissing() throws Exception {
    when(service.delete(999)).thenReturn(false);

    mockMvc.perform(delete("/api/extra-resident-types/999"))
        .andExpect(status().isNotFound());
  }
}
