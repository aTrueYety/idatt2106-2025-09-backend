package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import no.ntnu.stud.idatt2106.backend.mapper.HouseholdKitMapper;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
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
}
