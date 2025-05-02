package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

  private final String token = "Bearer admintoken";

  @Nested
  class SaveEventTests {

    @Test
    void shouldSaveEventIfUserIsAdmin() {
      EventRequest request = new EventRequest();
      Event event = new Event();

      try (MockedStatic<EventFactory> eventFactory = Mockito.mockStatic(EventFactory.class)) {
        when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(true);
        eventFactory.when(() -> EventFactory.requestToEvent(request)).thenReturn(event);
        when(repository.save(event)).thenReturn(1);

        int result = eventService.saveEvent(request, token);

        assertEquals(1, result);
        verify(repository).save(event);
      }
    }

    @Test
    void shouldThrowIfUserIsNotAdmin() {
      EventRequest request = new EventRequest();

      when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(false);

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        eventService.saveEvent(request, token);
      });

      assertTrue(exception.getMessage().contains("User is not an admin"));
      verify(repository, never()).save(any());
    }
  }

  @Nested
  class UpdateEventTests {
    
    @Test
    void shouldUpdateEventIfUserIsAdmin() {
      Event event = new Event();
      when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(true);
      when(repository.update(event)).thenReturn(1);

      int result = eventService.updateEvent(event, token);

      verify(repository).update(event);
      assertEquals(1, result);
    }
  }

  @Test
  void shouldThrowIfUserIsNotAdmin() {
    Event event = new Event();
    when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(false);
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      eventService.updateEvent(event, token);
    });

    assertTrue(exception.getMessage().contains("User is not an admin"));
    verify(repository, never()).update(any());
  }
}
