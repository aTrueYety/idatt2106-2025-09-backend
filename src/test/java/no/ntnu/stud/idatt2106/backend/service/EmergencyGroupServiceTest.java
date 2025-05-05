package no.ntnu.stud.idatt2106.backend.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
