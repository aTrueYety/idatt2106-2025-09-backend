package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository class for managing Event entities in the database.
 */
@Repository
public class EventRepositoryImpl implements EventRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
    return new Event(
      rs.getObject("id", Long.class),
      rs.getObject("info_page_id", Long.class),
      rs.getDouble("latitude"),
      rs.getDouble("longitude"),
      rs.getDouble("radius"),
      rs.getTimestamp("start_time"),
      rs.getTimestamp("end_time"),
      rs.getObject("severity_id", Long.class),
      rs.getString("recomendation"));
  };

  private final RowMapper<EventResponse> eventResponseRowMapper = (rs, rowNum) -> {
    return new EventResponse(
      rs.getObject("id", Long.class),
      rs.getObject("info_page_id", Long.class),
      rs.getDouble("latitude"),
      rs.getDouble("longitude"),
      rs.getDouble("radius"),
      rs.getTimestamp("start_time"),
      rs.getTimestamp("end_time"),
      rs.getObject("severity_id", Long.class),
      rs.getString("recomendation"),
      rs.getString("colour"),
      rs.getString("name"),
      rs.getString("description"));
  };

  /**
   * Saves an event to the repository.
   *
   * @param event the event to be saved
   * @return the number of rows affected
   */
  public int save(Event event) {
    String sql = "INSERT INTO event "
        + "(info_page_id, latitude, longitude, radius, start_time, end_time, severity_id, " 
        + "recomendation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql,
        event.getInfoPageId(),
        event.getLatitude(),
        event.getLongitude(),
        event.getRadius(),
        event.getStartTime(),
        event.getEndTime(),
        event.getSeverityId(),
        event.getRecomendation());
  }

  /**
   * Finds an event by its ID.
   *
   * @param id the ID of the event to be found
   * @return the event with the specified ID, or null if not found
   */
  public Event findEventById(Long id) {
    String sql = "SELECT * FROM event WHERE id = ?";
    List<Event> events = jdbcTemplate.query(sql, eventRowMapper, id);
    return events.isEmpty() ? null : events.get(0);
  }

  /**
   * Retrieves all events from the repository.
   *
   * @return a list of all events
   */
  public List<Event> findAll() {
    String sql = "SELECT * FROM event";
    return jdbcTemplate.query(sql, eventRowMapper);
  }

  /**
   * Retrives all events within specified bounds.
   *
   * @param minLat the minimum latitude
   * @param maxLat the maximum latitude
   * @param minLong the minimum longitude
   * @param maxLong the maximum longitude
   * @return a list of events within the specified bounds
   */
  public List<Event> findAllInBounds(double minLat, double maxLat, double minLong, double maxLong) {
    String sql = "SELECT * FROM event WHERE (latitude + (radius / 111320.0) >= ? AND latitude - (radius / 111320.0) <= ?) "
        + "AND (longitude + (radius / (111320.0 * COS(RADIANS(latitude)))) >= ? AND longitude - (radius / (111320.0 * COS(RADIANS(latitude)))) <= ?)";
    return jdbcTemplate.query(sql, eventRowMapper, minLat, maxLat, minLong, maxLong);
  }

  /**
   * Finds an event by its ID and its associated severity information.
   *
   * @param id the ID of the event to be found
   * @return the event with the specified ID, or null if not found
   */
  public EventResponse findWithSeverityById(Long id) {
    String sql = "SELECT event.*, severity.colour, severity.name, severity.description "
        + "FROM event JOIN severity ON event.severity_id = severity.id WHERE event.id = ?";
    List<EventResponse> events = jdbcTemplate.query(sql, eventResponseRowMapper, id);
    return events.isEmpty() ? null : events.get(0);
  }

  /**
   * Retrieves all events with their associated severity information.
   *
   * @return a list of all events with severity information
   */
  public List<EventResponse> findAllWithSeverity() {
    String sql = "SELECT event.*, severity.colour, severity.name, severity.description "
        + "FROM event JOIN severity ON event.severity_id = severity.id";
    return jdbcTemplate.query(sql, eventResponseRowMapper);
  }

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
      double minLat, double maxLat, double minLong, double maxLong) {
    String sql = "SELECT event.*, severity.colour, severity.name, severity.description "
        + "FROM event JOIN severity ON event.severity_id = severity.id "
        + "WHERE (latitude + (radius / 111320.0) >= ? AND latitude - (radius / 111320.0) <= ?) "
        + "AND (longitude + (radius / (111320.0 * COS(RADIANS(latitude)))) >= ? AND longitude - (radius / (111320.0 * COS(RADIANS(latitude)))) <= ?)";
    return jdbcTemplate.query(sql, eventResponseRowMapper, minLat, maxLat, minLong, maxLong);
  }

  /**
   * Updates an event in the repository.
   *
   * @param event the event to be updated
   * @return the number of rows affected
   */
  public int update(Event event) {
    String sql = "UPDATE event SET info_page_id = ?, latitude = ?, longitude = ?, radius = ?, "
        + "start_time = ?, end_time = ?, severity_id = ?, recomendation = ? WHERE id = ?";
    return jdbcTemplate.update(sql,
        event.getInfoPageId(),
        event.getLatitude(),
        event.getLongitude(),
        event.getRadius(),
        event.getStartTime(),
        event.getEndTime(),
        event.getSeverityId(),
        event.getRecomendation(),
        event.getId());
  }

  /**
   * Deletes an event from the repository.
   *
   * @param id the ID of the event to be deleted
   * @return the number of rows affected
   */
  public int delete(Long id) {
    String sql = "DELETE FROM event WHERE id = ?";
    return jdbcTemplate.update(sql, id);
  }
}
