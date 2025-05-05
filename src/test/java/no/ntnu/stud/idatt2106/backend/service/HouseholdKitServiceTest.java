package no.ntnu.stud.idatt2106.backend.service;

import static org.mockito.Mockito.verify;

import no.ntnu.stud.idatt2106.backend.mapper.HouseholdKitMapper;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
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
}
