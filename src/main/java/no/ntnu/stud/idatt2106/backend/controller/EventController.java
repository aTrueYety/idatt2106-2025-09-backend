package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
import no.ntnu.stud.idatt2106.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling event-related requests.
 * This class is responsible for processing incoming requests related to events
 * and returning appropriate responses.
 */
@Tag(name = "Events", description = "Endpoints for operations related to events.")
@RestController
@RequestMapping("/api/events")
public class EventController {

  @Autowired
  private EventService eventService;

  /**
   * Adds a new event to the system.
   *
   * @param event The event to be added.
   * @param token The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(
      summary = "Registers a new event",
      description = "Regusters a new event. Requires the user to be an admin."
  )
  @PostMapping
  public ResponseEntity<String> addEvent(
      @Valid @RequestBody EventRequest event,
      @RequestHeader("Authorization") String token) {
    eventService.saveEvent(event, token);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Retrieves an event by its ID.
   *
   * @param eventId The ID of the event to be retrieved.
   * @return a ResponseEntity containing the event details if found.
   */
  @Operation(
      summary = "Retrieves the event with the given ID"
  )
  @GetMapping("/{eventId}")
  public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId) {
    EventResponse event = eventService.findEventWithSeverityById(eventId);
    if (event != null) {
      return ResponseEntity.ok(event);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Updates an existing event.
   *
   * @param event The event with updated details.
   * @param token The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(
      summary = "Updates an existing event",
      description = "Updates an existing event. Requires the user to be an admin."
  )
  @PostMapping("/update")
  public ResponseEntity<String> updateEvent(
      @RequestBody Event event,
      @RequestHeader("Authorization") String token) {
    eventService.updateEvent(event, token);
    return ResponseEntity.ok("Event updated successfully");
  }

  /**
   * Deletes an event by its ID.
   *
   * @param eventId The ID of the event to be deleted.
   * @param token   The authentication token of the user making the request.
   * @return A ResponseEntity indicating the success or failure of the operation.
   */
  @Operation(
      summary = "Deletes an existing event",
      description = "Deletes an existing event. Requires the user to be an admin."
  )
  @DeleteMapping("/{eventId}")
  public ResponseEntity<String> deleteEvent(
      @PathVariable Long eventId,
      @RequestHeader("Authorization") String token) {
    eventService.deleteEvent(eventId, token);
    return ResponseEntity.ok("Event deleted successfully");
  }

  /**
   * Retrieves all events within specified geographical bounds.
   *
   * @param minLat  The minimum latitude of the bounding box.
   * @param maxLat  The maximum latitude of the bounding box.
   * @param minLong The minimum longitude of the bounding box.
   * @param maxLong The maximum longitude of the bounding box.
   * @return A ResponseEntity containing a list of events within the specified
   *         bounds.
   */
  @Operation(
      summary = "Retrieves all of the events in the specified bounds"
  )
  @GetMapping("/bounds")
  public ResponseEntity<List<EventResponse>> getEventsInBounds(
      @RequestParam double minLat, 
      @RequestParam double maxLat, 
      @RequestParam double minLong, 
      @RequestParam double maxLong) {
    List<EventResponse> events = eventService.findAllEventsWithSeverityInBounds(
        minLat, maxLat, minLong, maxLong);
    if (events.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(events);
    }
  }
}
