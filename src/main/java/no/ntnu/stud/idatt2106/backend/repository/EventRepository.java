package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;

/**
 * Repository interface for managing Event entities in the database.
 */
public interface EventRepository {
  /**
   * Saves an event to the repository.
   *
   * @param event the event to be saved
   * @return the number of rows affected
   */
  public int save(Event event);

  /**
   * Finds an event by its ID.
   *
   * @param id the ID of the event to be found
   * @return the event with the specified ID, or null if not found
   */
  public Event findEventById(Long id);

  /**
   * Retrieves all events from the repository.
   *
   * @return a list of all events
   */
  public List<Event> findAll();

  /**
   * Retrives all events within specified bounds.
   *
   * @param minLat the minimum latitude
   * @param maxLat the maximum latitude
   * @param minLong the minimum longitude
   * @param maxLong the maximum longitude
   * @return a list of events within the specified bounds
   */
  public List<Event> findAllInBounds(double minLat, double maxLat, double minLong, double maxLong);

  /**
   * Finds an event by its ID and its associated severity information.
   *
   * @param id the ID of the event to be found
   * @return the event with the specified ID, or null if not found
   */
  public EventResponse findWithSeverityById(Long id);

  /**
   * Retrieves all events with their associated severity information.
   *
   * @return a list of all events with severity information
   */
  public List<EventResponse> findAllWithSeverity();

  /**
   * Retrieves all events with their associated severity information within specified bounds.
   *
   * @param minLat the minimum latitude
   * @param maxLat the maximum latitude
   * @param minLong the minimum longitude
   * @param maxLong the maximum longitude
   * @return a list of events with severity information within the specified bounds
   */
  public List<EventResponse> findAllWithSeverityInBounds(
      double minLat, double maxLat, double minLong, double maxLong);

  /**
   * Updates an event in the repository.
   *
   * @param event the event to be updated
   * @return the number of rows affected
   */
  public int update(Event event);

  /**
   * Deletes an event from the repository.
   *
   * @param id the ID of the event to be deleted
   * @return the number of rows affected
   */
  public int delete(Long id);
}
