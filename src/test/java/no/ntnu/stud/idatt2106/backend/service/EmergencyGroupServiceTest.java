package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import no.ntnu.stud.idatt2106.backend.mapper.EmergencyGroupMapper;
import no.ntnu.stud.idatt2106.backend.model.base.EmergencyGroup;
import no.ntnu.stud.idatt2106.backend.model.request.EmergencyGroupRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EmergencyGroupResponse;
import no.ntnu.stud.idatt2106.backend.repository.EmergencyGroupRepository;

/**
 * Contains tests for the EmergencyGroupService class.
 */
@ExtendWith(MockitoExtension.class)
public class EmergencyGroupServiceTest {
  
  @InjectMocks
  private EmergencyGroupService emergencyGroupService;

  @Mock
  private EmergencyGroupRepository repository;

  @Nested
  class CreateTests {

    @Test
    void shouldConvertToModelAndSave() {
      EmergencyGroupRequest request = new EmergencyGroupRequest();
      EmergencyGroup group = new EmergencyGroup();

      try (MockedStatic<EmergencyGroupMapper> mapper = 
          Mockito.mockStatic(EmergencyGroupMapper.class)) {
        mapper.when(() -> EmergencyGroupMapper.toModel(request)).thenReturn(group);

        emergencyGroupService.create(request);

        verify(repository).save(group);
      }
    }
  }

  @Nested
  class GetAllTests {

    @Test
    void shouldMapToResponseAndReturnAll() {
      EmergencyGroup group1 = new EmergencyGroup();
      EmergencyGroup group2 = new EmergencyGroup();
      List<EmergencyGroup> groups = List.of(group1, group2);

      EmergencyGroupResponse response1 = new EmergencyGroupResponse();
      EmergencyGroupResponse response2 = new EmergencyGroupResponse();

      when(repository.findAll()).thenReturn(groups);

      try (MockedStatic<EmergencyGroupMapper> mapper 
          = Mockito.mockStatic(EmergencyGroupMapper.class)) {

        mapper.when(() -> EmergencyGroupMapper.toResponse(group1)).thenReturn(response1);
        mapper.when(() -> EmergencyGroupMapper.toResponse(group2)).thenReturn(response2);

        List<EmergencyGroupResponse> result = emergencyGroupService.getAll();

        assertEquals(List.of(response1, response2), result);
        verify(repository).findAll();
      }
    }
  }
}
