package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
import no.ntnu.stud.idatt2106.backend.repository.EventRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.service.factory.EventFactory;
import no.ntnu.stud.idatt2106.backend.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing events.
 */
@Service
public class EventService {
  @Autowired
  private EventRepositoryImpl eventRepository;
  @Autowired
  private JwtService jwtService;

  private static void validateEvent(Event event) {
    // severity exists
  }

  /**
   * Saves an event to the repository.
   *
   * @param request the event to be saved
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int saveEvent(EventRequest request, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    Event event = EventFactory.requestToEvent(request);
    validateEvent(event);
    return eventRepository.save(event);
  }

  /**
   * Finds an event by its ID.
   *
   * @param id the ID of the event to be found
   * @return the event with the specified ID, or null if not found
   */
  public Event findEventById(long id) {
    return eventRepository.findEventById(id);
  }

  /**
   * Finds all events in the repository.
   *
   * @return a list of all events
   */
  public List<Event> findAllEvents() {
    return eventRepository.findAll();
  }

  /**
   * Finds all events in the repository within specified bounds.
   *
   * @param minLat the minimum latitude
   * @param maxLat the maximum latitude
   * @param minLong the minimum longitude
   * @param maxLong the maximum longitude
   * @return a list of all events within the specified bounds
   */
  public List<Event> findAllEventsInBounds(
      double minLat, double maxLat, double minLong, double maxLong) {
    return eventRepository.findAllInBounds(minLat, maxLat, minLong, maxLong);
  }

  /**
   * Finds an event and assoiated severity details by its ID in the repository.
   *
   * @return the event with the specified ID and its severity details, or null if not found
   */
  public EventResponse findEventWithSeverityById(long id) {
    return eventRepository.findWithSeverityById(id);
  }

  /**
   * Finds all events and assoiated severity details in the repository.
   *
   * @return a list of all events with severity details
   */
  public List<EventResponse> findAllEventsWithSeverity() {
    return eventRepository.findAllWithSeverity();
  }

  /**
   * Finds all events and assoiated severity details in the repository within specified bounds.
   *
   * @param minLat the minimum latitude
   * @param maxLat the maximum latitude
   * @param minLong the minimum longitude
   * @param maxLong the maximum longitude
   * @return a list of all events with severity details within the specified bounds
   */
  public List<EventResponse> findAllEventsWithSeverityInBounds(
      double minLat, double maxLat, double minLong, double maxLong) {
    return eventRepository.findAllWithSeverityInBounds(minLat, maxLat, minLong, maxLong);
  }

  /**
   * Updates an event in the repository.
   *
   * @param event the event to be updated
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int updateEvent(Event event, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    validateEvent(event);
    return eventRepository.update(event);
  }

  /**
   * Deletes an event from the repository.
   *
   * @param id the ID of the event to be deleted
   * @param token the authentication token of the user making the request
   * @return the number of rows affected
   */
  public int deleteEvent(long id, String token) {
    Validate.that(jwtService.extractIsAdmin(token.substring(7)), 
        Validate.isTrue(), "User is not an admin");
    return eventRepository.delete(id);
  }
}
