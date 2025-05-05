package no.ntnu.stud.idatt2106.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
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

  @Mock
  private EventWebSocketService eventWebSocketService;


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

  @Nested
  class DeleteEventTests {

    @Test
    void shouldDeleteEventIfUserIsAdmin() {
      Long eventId = 2L;

      when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(true);
      when(repository.delete(eventId)).thenReturn(1);

      int result = eventService.deleteEvent(eventId, token);

      verify(repository).delete(eventId);
      assertEquals(1, result);
    }

    @Test
    void shouldThrowIfUserIsNotAdmin() {
      Long eventId = 5L;
      when(jwtService.extractIsAdmin(token.substring(7))).thenReturn(false);
      
      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        eventService.deleteEvent(eventId, token);
      });

      verify(repository, never()).delete(eventId);
      assertTrue(exception.getMessage().contains("User is not an admin"));
    }
  }

  @Nested
  class FindEventByIdTests {

    @Test
    void shouldReturnEventWithId() {
      Long eventId = 2L;
      Event event = new Event();

      when(repository.findEventById(eventId)).thenReturn(event);

      Event result = eventService.findEventById(eventId);

      verify(repository).findEventById(eventId);
      assertEquals(event, result);
    }
  }

  @Nested
  class FindAllEventsTests {

    @Test
    void shouldReturnAllEvents() {
      Event event1 = new Event();
      Event event2 = new Event();
      Event event3 = new Event();
      List<Event> expected = List.of(event1, event2, event3);

      when(repository.findAll()).thenReturn(expected);

      List<Event> result = eventService.findAllEvents();

      verify(repository).findAll();
      assertEquals(expected, result);
    }
  }

  @Nested
  class FindAllEventsInBoundsTests {
    
    @Test
    void shouldReturnTestsWithinBounds() {
      double minLat = 10.0;
      double maxLat = 20.0;
      double minLong = 30.0;
      double maxLong = 40.0;

      Event event1 = new Event();
      Event event2 = new Event();
      List<Event> expected = List.of(event1, event2);

      when(repository.findAllInBounds(minLat, maxLat, minLong, maxLong)).thenReturn(expected);

      List<Event> result = eventService.findAllEventsInBounds(minLat, maxLat, minLong, maxLong);

      assertEquals(expected, result);
      verify(repository).findAllInBounds(minLat, maxLat, minLong, maxLong);
    }
  }

  @Nested
  class FindEventWithSeverityByIdTests {

    @Test
    void shouldReturnEventWithSeverityWithId() {
      Long id = 2L;
      EventResponse event = new EventResponse();
      event.setId(id);

      when(repository.findWithSeverityById(id)).thenReturn(event);

      EventResponse result = eventService.findEventWithSeverityById(id);

      assertEquals(event, result);
      verify(repository).findWithSeverityById(id);
    }
  }

  @Nested
  class FindAllEventsWithSeverityTests {

    @Test
    void shouldReturnAllEventsWithSeverity() {
      EventResponse event1 = new EventResponse();
      EventResponse event2 = new EventResponse();
      List<EventResponse> expected = List.of(event1, event2);

      when(repository.findAllWithSeverity()).thenReturn(expected);

      List<EventResponse> result = eventService.findAllEventsWithSeverity();

      assertEquals(expected, result);
      verify(repository).findAllWithSeverity();
    }
  }

  @Nested
  class FindAllEventsWithSeverityInBoundsTests {

    @Test
    void shouldReturnAllEventsInBoundWithSeverity() {
      double minLat = 10.0;
      double maxLat = 20.0;
      double minLong = 30.0;
      double maxLong = 40.0;
      EventResponse event1 = new EventResponse();
      EventResponse event2 = new EventResponse();
      List<EventResponse> expected = List.of(event1, event2);
      when(repository.findAllWithSeverityInBounds(minLat, maxLat, minLong, maxLong))
          .thenReturn(expected);

      List<EventResponse> result = eventService
          .findAllEventsWithSeverityInBounds(minLat, maxLat, minLong, maxLong);
      
      assertEquals(expected, result);
      verify(repository).findAllWithSeverityInBounds(minLat, maxLat, minLong, maxLong);
    }
  }
}
