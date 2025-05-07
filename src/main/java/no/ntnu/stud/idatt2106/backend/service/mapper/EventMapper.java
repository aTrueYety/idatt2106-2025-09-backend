package no.ntnu.stud.idatt2106.backend.service.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.request.EventRequest;

/**
 * Factory class for creating Event objects and converting between different
 * representations of events.
 */
public class EventMapper {
  /**
   * Converts an EventRequest object to an Event object.
   *
   * @param eventRequest the EventRequest object to convert
   * @return the converted Event object
   */
  public static Event requestToEvent(EventRequest eventRequest) {
    return new Event(
        null,
        eventRequest.getName(),
        eventRequest.getInfoPageId(),
        eventRequest.getLatitude(),
        eventRequest.getLongitude(),
        eventRequest.getRadius(),
        eventRequest.getStartTime(),
        eventRequest.getEndTime(),
        eventRequest.getSeverityId(),
        eventRequest.getRecomendation());
  }

  /**
   * Converts an Event object to an EventRequest object.
   *
   * @param event the Event object to convert
   * @return the converted EventRequest object
   */
  public static EventRequest eventToRequest(Event event) {
    return new EventRequest(
        event.getInfoPageId(),
        event.getName(),
        event.getLatitude(),
        event.getLongitude(),
        event.getRadius(),
        event.getStartTime(),
        event.getEndTime(),
        event.getSeverityId(),
        event.getRecomendation());
  }
  
}
