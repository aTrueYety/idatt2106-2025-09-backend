package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;
import no.ntnu.stud.idatt2106.backend.repository.EventRepository;
import no.ntnu.stud.idatt2106.backend.service.factory.EventFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains tests for the EventService class.
 */
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @InjectMocks
  private EventService eventService;

  @Mock
  private EventRepository repository;

  @Mock
  private JwtService jwtService;

  @Nested
  class SaveEventTests {

    @Test
    void shouldSaveEventIfUserIsAdmin() {
      EventRequest request = new EventRequest();
      Event event = new Event();

      String token = "Bearer admintoken";

      try (MockedStatic<EventFactory> eventFactory = Mockito.mockStatic(EventFactory.class)) {
        when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(true);
        eventFactory.when(() -> EventFactory.requestToEvent(request)).thenReturn(event);
        when(repository.save(event)).thenReturn(1);

        int result = eventService.saveEvent(request, token);

        assertEquals(1, result);
        verify(repository).save(event);

      }
    }
  }
}
