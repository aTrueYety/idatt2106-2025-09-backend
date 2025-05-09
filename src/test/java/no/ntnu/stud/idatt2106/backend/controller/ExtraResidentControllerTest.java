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
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.service.ExtraResidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for ExtraResidentController.
 */
@WebMvcTest(controllers = ExtraResidentController.class)
@Import(SecurityConfigTest.class)
public class ExtraResidentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ExtraResidentService service;

  @Test
  void shouldGetAllExtraResidents() throws Exception {
    ExtraResidentResponse response = new ExtraResidentResponse();
    response.setId(1);
    response.setHouseholdId(10);
    response.setTypeId(2);

    when(service.getAll()).thenReturn(List.of(response));

    mockMvc.perform(get("/api/extra-residents"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].typeId").value(2));
  }

  @Test
  void shouldGetExtraResidentById() throws Exception {
    ExtraResidentResponse response = new ExtraResidentResponse();
    response.setId(1);
    response.setHouseholdId(10);
    response.setTypeId(2);

    when(service.getById(1L)).thenReturn(Optional.of(response));

    mockMvc.perform(get("/api/extra-residents/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturnNotFoundIfExtraResidentMissing() throws Exception {
    when(service.getById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/extra-residents/999"))
        .andExpect(status().isNotFound());
  }


  @Test
  void shouldUpdateExtraResident() throws Exception {
    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(1);
    update.setTypeId(3);
    update.setName("Updated Name");

    when(service.update(eq(1L), any(ExtraResidentUpdate.class))).thenReturn(true);

    mockMvc.perform(put("/api/extra-residents/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk());

    verify(service).update(eq(1L), any(ExtraResidentUpdate.class));
  }

  @Test
  void shouldReturnNotFoundWhenUpdatingMissingResident() throws Exception {
    ExtraResidentUpdate update = new ExtraResidentUpdate();
    update.setHouseholdId(12);
    update.setTypeId(3);

    when(service.update(eq(999L), any())).thenReturn(false);

    mockMvc.perform(put("/api/extra-residents/999")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteExtraResident() throws Exception {
    when(service.delete(1L)).thenReturn(true);

    mockMvc.perform(delete("/api/extra-residents/1"))
        .andExpect(status().isNoContent());

    verify(service).delete(1L);
  }

  @Test
  void shouldReturnNotFoundWhenDeletingMissing() throws Exception {
    when(service.delete(999L)).thenReturn(false);

    mockMvc.perform(delete("/api/extra-residents/999"))
        .andExpect(status().isNotFound());
  }
}
