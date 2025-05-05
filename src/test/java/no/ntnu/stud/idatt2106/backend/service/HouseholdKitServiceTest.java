package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.mapper.HouseholdKitMapper;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.request.MoveHouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdKitRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains test for HouseholdKitService class.
 */
@ExtendWith(MockitoExtension.class)
public class HouseholdKitServiceTest {
  
  @InjectMocks
  private HouseholdKitService householdKitService;

  @Mock
  private HouseholdKitRepository repository;

  @Nested
  class CreateTests {

    @Test
    void shouldMapToEntityAndSave() {
      HouseholdKitRequest request = new HouseholdKitRequest();
      HouseholdKit kit = new HouseholdKit();

      try (MockedStatic<HouseholdKitMapper> mapper 
          = Mockito.mockStatic(HouseholdKitMapper.class)) {
        mapper.when(() -> HouseholdKitMapper.toEntity(request)).thenReturn(kit);

        householdKitService.create(request);

        verify(repository).save(kit);
      }
    }
  }

  @Nested
  class GetByHouseHoldIdTests {
    
    @Test
    void shouldMapToResponseAndReturn() {
      HouseholdKit householdKit1 = new HouseholdKit();
      HouseholdKit householdKit2 = new HouseholdKit();
      List<HouseholdKit> householdKits = List.of(householdKit1, householdKit2);
      HouseholdKitResponse response1 = new HouseholdKitResponse();
      HouseholdKitResponse response2 = new HouseholdKitResponse();
      List<HouseholdKitResponse> responses = List.of(response1, response2);
      Long householdId = 1L;

      when(repository.findByHouseholdId(householdId)).thenReturn(householdKits);

      try (MockedStatic<HouseholdKitMapper> mapper 
           = Mockito.mockStatic(HouseholdKitMapper.class)) {
        mapper.when(() -> HouseholdKitMapper.toResponse(householdKit1)).thenReturn(response1);
        mapper.when(() -> HouseholdKitMapper.toResponse(householdKit2)).thenReturn(response2);

        List<HouseholdKitResponse> result = householdKitService.getByHouseholdId(householdId);

        verify(repository).findByHouseholdId(householdId);
        assertEquals(responses, result);
        assertEquals(2, result.size());
      }
    }
  }

  @Nested
  class GetByKitIdTests {

    @Test
    void shouldMapToResponseAndReturn() {
      HouseholdKit householdKit1 = new HouseholdKit();
      HouseholdKit householdKit2 = new HouseholdKit();
      List<HouseholdKit> householdKits = List.of(householdKit1, householdKit2);
      HouseholdKitResponse response1 = new HouseholdKitResponse();
      HouseholdKitResponse response2 = new HouseholdKitResponse();
      List<HouseholdKitResponse> responses = List.of(response1, response2);
      Long kitId = 1L;

      when(repository.findByKitId(kitId)).thenReturn(householdKits);

      try (MockedStatic<HouseholdKitMapper> mapper 
           = Mockito.mockStatic(HouseholdKitMapper.class)) {
        mapper.when(() -> HouseholdKitMapper.toResponse(householdKit1)).thenReturn(response1);
        mapper.when(() -> HouseholdKitMapper.toResponse(householdKit2)).thenReturn(response2);

        List<HouseholdKitResponse> result = householdKitService.getByKitId(kitId);

        verify(repository).findByKitId(kitId);
        assertEquals(responses, result);
        assertEquals(2, result.size());
      }
    }
  }

  @Nested
  class DeleteTests {

    @Test
    void shouldDeleteAndReturnTrueIfExists() {
      Long householdId = 1L;
      Long kitId = 2L;
      HouseholdKitRequest request = new HouseholdKitRequest();
      request.setHouseholdId(householdId);
      request.setKitId(kitId);

      HouseholdKit householdkit1 = new HouseholdKit();
      householdkit1.setHouseholdId(1L);
      householdkit1.setKitId(3L);
      HouseholdKit householdkit2 = new HouseholdKit();
      householdkit2.setHouseholdId(1L);
      householdkit2.setKitId(2L);
      List<HouseholdKit> householdKits = List.of(householdkit1, householdkit2);

      when(repository.findByHouseholdId(householdId)).thenReturn(householdKits);

      boolean result = householdKitService.delete(request);

      verify(repository).delete(householdkit2);
      assertTrue(result);
    }

    @Test
    void shouldNotDeleteAndReturnFalseIfDoesNotExist() {
      Long householdId = 1L;
      Long kitId = 1L;
      HouseholdKitRequest request = new HouseholdKitRequest();
      request.setHouseholdId(householdId);
      request.setKitId(kitId);

      HouseholdKit householdkit1 = new HouseholdKit();
      householdkit1.setHouseholdId(1L);
      householdkit1.setKitId(3L);
      HouseholdKit householdkit2 = new HouseholdKit();
      householdkit2.setHouseholdId(1L);
      householdkit2.setKitId(2L);
      List<HouseholdKit> householdKits = List.of(householdkit1, householdkit2);

      when(repository.findByHouseholdId(householdId)).thenReturn(householdKits);

      boolean result = householdKitService.delete(request);

      verify(repository, never()).delete(householdkit2);
      assertFalse(result);
    }
  }

  @Nested
  class MoveKitToAnotherHouseholdTests {

    @Test
    void shouldMoveKitAndReturnTrueIfSuccess() {
      Long householdId = 1L;
      Long newHouseholdId = 3L;
      Long kitId = 2L;
      MoveHouseholdKitRequest request = new MoveHouseholdKitRequest();
      request.setOldHouseholdId(householdId);
      request.setKitId(kitId);
      request.setNewHouseholdId(newHouseholdId);


      HouseholdKit householdkit1 = new HouseholdKit();
      householdkit1.setHouseholdId(1L);
      householdkit1.setKitId(3L);
      HouseholdKit householdkit2 = new HouseholdKit();
      householdkit2.setHouseholdId(1L);
      householdkit2.setKitId(2L);
      List<HouseholdKit> householdKits = List.of(householdkit1, householdkit2);

      when(repository.findByHouseholdId(householdId)).thenReturn(householdKits);

      boolean result = householdKitService.moveKitToAnotherHousehold(request);

      verify(repository).updateHouseholdForKit(request.getOldHouseholdId(),
          request.getKitId(), request.getNewHouseholdId());
      assertTrue(result);
    }

    @Test
    void shouldNotMoveKitAndReturnFalseIfRelationDoesNotExist() {
      Long householdId = 1L;
      Long newHouseholdId = 3L;
      Long kitId = 5L;
      MoveHouseholdKitRequest request = new MoveHouseholdKitRequest();
      request.setOldHouseholdId(householdId);
      request.setKitId(kitId);
      request.setNewHouseholdId(newHouseholdId);


      HouseholdKit householdkit1 = new HouseholdKit();
      householdkit1.setHouseholdId(1L);
      householdkit1.setKitId(3L);
      HouseholdKit householdkit2 = new HouseholdKit();
      householdkit2.setHouseholdId(1L);
      householdkit2.setKitId(2L);
      List<HouseholdKit> householdKits = List.of(householdkit1, householdkit2);

      when(repository.findByHouseholdId(householdId)).thenReturn(householdKits);

      boolean result = householdKitService.moveKitToAnotherHousehold(request);

      verify(repository, never()).updateHouseholdForKit(any(), any(), any());
      assertFalse(result);
    }
  }
}
