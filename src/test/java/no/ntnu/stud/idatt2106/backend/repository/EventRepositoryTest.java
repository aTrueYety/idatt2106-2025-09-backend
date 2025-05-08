package no.ntnu.stud.idatt2106.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.response.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for EventRepository.
 */
@JdbcTest
@ActiveProfiles("test")
@Import(EventRepositoryImpl.class)
public class EventRepositoryTest {

  @Autowired
  private EventRepository repository;

  @Autowired
  private JdbcTemplate jdbc;

  private void insertRequiredForeignKeys() {
    jdbc.update("INSERT INTO info_page (id, title, short_description, content) VALUES (?, ?, ?, ?)",
        1, "Test", "Test", "Test");
    jdbc.update("INSERT INTO info_page (id, title, short_description, content) VALUES (?, ?, ?, ?)",
        2, "AnotherTest", "AnotherTest", "AnotherTest");
  }

  @BeforeEach
  void setUp() {
    insertRequiredForeignKeys();
  }

  @Test
  void shouldSaveEvent() {
    Event event = new Event();
    event.setSeverityId(1L);
    event.setInfoPageId(1L);
    event.setLatitude(0.0);
    event.setLongitude(1.0);
    event.setRadius(2.0);
    event.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event.setRecomendation("Test recomendation");

    repository.save(event);

    List<Event> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getSeverityId()).isEqualTo(1);
    assertThat(all.get(0).getInfoPageId()).isEqualTo(1);
    assertThat(all.get(0).getLatitude()).isEqualTo(0.0);
    assertThat(all.get(0).getLongitude()).isEqualTo(1.0);
    assertThat(all.get(0).getRadius()).isEqualTo(2.0);
    assertThat(all.get(0).getStartTime()).isEqualTo(Timestamp.valueOf("2023-10-01 00:00:00"));
    assertThat(all.get(0).getEndTime()).isEqualTo(Timestamp.valueOf("2023-10-02 00:00:00"));
    assertThat(all.get(0).getSeverityId()).isEqualTo(1);
    assertThat(all.get(0).getRecomendation()).isEqualTo("Test recomendation");
  }

  @Test
  void shouldUpdateEvent() {
    Event event = new Event();
    event.setSeverityId(1L);
    event.setInfoPageId(1L);
    event.setLatitude(0.0);
    event.setLongitude(1.0);
    event.setRadius(2.0);
    event.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event.setRecomendation("Test recomendation");

    repository.save(event);
    event = repository.findAll().get(0);

    event.setSeverityId(2L);
    event.setInfoPageId(2L);
    event.setLatitude(3.0);
    event.setLongitude(4.0);
    event.setRadius(5.0);
    event.setStartTime(Timestamp.valueOf("2023-10-03 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-04 00:00:00"));
    event.setRecomendation("Updated recomendation");

    repository.update(event);

    List<Event> all = repository.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getSeverityId()).isEqualTo(2L);
    assertThat(all.get(0).getInfoPageId()).isEqualTo(2L);
    assertThat(all.get(0).getLatitude()).isEqualTo(3.0);
    assertThat(all.get(0).getLongitude()).isEqualTo(4.0);
    assertThat(all.get(0).getRadius()).isEqualTo(5.0);
    assertThat(all.get(0).getStartTime()).isEqualTo(Timestamp.valueOf("2023-10-03 00:00:00"));
    assertThat(all.get(0).getEndTime()).isEqualTo(Timestamp.valueOf("2023-10-04 00:00:00"));
    assertThat(all.get(0).getSeverityId()).isEqualTo(2L);
    assertThat(all.get(0).getRecomendation()).isEqualTo("Updated recomendation");
  }

  @Test
  void shouldDeleteEvent() {
    Event event = new Event();
    event.setSeverityId(1L);
    event.setInfoPageId(1L);
    event.setLatitude(0.0);
    event.setLongitude(1.0);
    event.setRadius(2.0);
    event.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event.setRecomendation("Test recomendation");

    repository.save(event);
    event = repository.findAll().get(0);

    assertThat(repository.findAll()).hasSize(1);
    repository.delete(event.getId());
    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void shouldFindEventById() {
    Event event = new Event();
    event.setSeverityId(1L);
    event.setInfoPageId(1L);
    event.setLatitude(0.0);
    event.setLongitude(1.0);
    event.setRadius(2.0);
    event.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event.setRecomendation("Test recomendation");

    repository.save(event);
    event = repository.findAll().get(0);

    Event foundEvent = repository.findEventById(event.getId());
    assertThat(foundEvent).isNotNull();
    assertThat(foundEvent.getId()).isEqualTo(event.getId());
  }

  @Test
  void shouldReturnNullWhenEventNotFound() {
    Event foundEvent = repository.findEventById(999L);
    assertThat(foundEvent).isNull();
  }

  @Test
  void shouldReturnAllEvents() {
    Event event1 = new Event();
    event1.setSeverityId(1L);
    event1.setInfoPageId(1L);
    event1.setLatitude(0.0);
    event1.setLongitude(1.0);
    event1.setRadius(2.0);
    event1.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event1.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event1.setRecomendation("Test recomendation");

    Event event2 = new Event();
    event2.setSeverityId(2L);
    event2.setInfoPageId(2L);
    event2.setLatitude(3.0);
    event2.setLongitude(4.0);
    event2.setRadius(5.0);
    event2.setStartTime(Timestamp.valueOf("2023-10-03 00:00:00"));
    event2.setEndTime(Timestamp.valueOf("2023-10-04 00:00:00"));
    event2.setRecomendation("Updated recomendation");

    repository.save(event1);
    repository.save(event2);

    List<Event> allEvents = repository.findAll();
    assertThat(allEvents).hasSize(2);
  }

  @Test
  void shouldReturnEmptyListWhenNoEvents() {
    List<Event> allEvents = repository.findAll();
    assertThat(allEvents).isEmpty();
  }

  @Test
  void shouldFindAllInBounds() {
    Event event1 = new Event();
    event1.setSeverityId(1L);
    event1.setInfoPageId(1L);
    event1.setLatitude(0.0);
    event1.setLongitude(1.0);
    event1.setRadius(2.0);
    event1.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event1.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event1.setRecomendation("Test recomendation");

    Event event2 = new Event();
    event2.setSeverityId(2L);
    event2.setInfoPageId(2L);
    event2.setLatitude(3.0);
    event2.setLongitude(4.0);
    event2.setRadius(5.0);
    event2.setStartTime(Timestamp.valueOf("2023-10-03 00:00:00"));
    event2.setEndTime(Timestamp.valueOf("2023-10-04 00:00:00"));
    event2.setRecomendation("Updated recomendation");

    Event event3 = new Event();
    event3.setSeverityId(1L);
    event3.setInfoPageId(2L);
    event3.setLatitude(6.0);
    event3.setLongitude(7.0);
    event3.setRadius(8.0);
    event3.setStartTime(Timestamp.valueOf("2023-10-05 00:00:00"));
    event3.setEndTime(Timestamp.valueOf("2023-10-06 00:00:00"));
    event3.setRecomendation("Another recomendation");

    repository.save(event1);
    repository.save(event2);
    repository.save(event3);

    List<Event> allEvents = repository.findAllInBounds(0, 5, 0, 5);
    assertThat(allEvents).hasSize(2);
  }

  @Test
  void shouldReturnEmptyListWhenNoEventsInBounds() {
    Event event1 = new Event();
    event1.setSeverityId(1L);
    event1.setInfoPageId(1L);
    event1.setLatitude(0.0);
    event1.setLongitude(1.0);
    event1.setRadius(2.0);
    event1.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event1.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event1.setRecomendation("Test recomendation");

    repository.save(event1);

    List<Event> allEvents = repository.findAllInBounds(5, 10, 5, 10);
    assertThat(allEvents).isEmpty();
  }

  @Test
  void shouldFindEventWithSeverityById() {
    Event event = new Event();
    event.setSeverityId(1L);
    event.setInfoPageId(1L);
    event.setLatitude(0.0);
    event.setLongitude(1.0);
    event.setRadius(2.0);
    event.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event.setRecomendation("Test recomendation");

    repository.save(event);
    event = repository.findAll().get(0);

    EventResponse foundEvent = repository.findWithSeverityById(event.getId());
    assertThat(foundEvent).isNotNull();
    assertThat(foundEvent.getId()).isEqualTo(event.getId());
  }

  @Test
  void shouldReturnNullWhenEventWithSeverityNotFound() {
    EventResponse foundEvent = repository.findWithSeverityById(999L);
    assertThat(foundEvent).isNull();
  }

  @Test
  void shouldFindAllWithSeverity() {
    Event event1 = new Event();
    event1.setSeverityId(1L);
    event1.setInfoPageId(1L);
    event1.setLatitude(0.0);
    event1.setLongitude(1.0);
    event1.setRadius(2.0);
    event1.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event1.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event1.setRecomendation("Test recomendation");

    Event event2 = new Event();
    event2.setSeverityId(2L);
    event2.setInfoPageId(2L);
    event2.setLatitude(3.0);
    event2.setLongitude(4.0);
    event2.setRadius(5.0);
    event2.setStartTime(Timestamp.valueOf("2023-10-03 00:00:00"));
    event2.setEndTime(Timestamp.valueOf("2023-10-04 00:00:00"));
    event2.setRecomendation("Updated recomendation");

    repository.save(event1);
    repository.save(event2);

    List<EventResponse> allEvents = repository.findAllWithSeverity();
    assertThat(allEvents).hasSize(2);
  }

  @Test
  void shouldFindEventWithSeverityInBounds() {
    Event event1 = new Event();
    event1.setSeverityId(1L);
    event1.setInfoPageId(1L);
    event1.setLatitude(0.0);
    event1.setLongitude(1.0);
    event1.setRadius(2.0);
    event1.setStartTime(Timestamp.valueOf("2023-10-01 00:00:00"));
    event1.setEndTime(Timestamp.valueOf("2023-10-02 00:00:00"));
    event1.setRecomendation("Test recomendation");

    Event event2 = new Event();
    event2.setSeverityId(2L);
    event2.setInfoPageId(2L);
    event2.setLatitude(3.0);
    event2.setLongitude(4.0);
    event2.setRadius(5.0);
    event2.setStartTime(Timestamp.valueOf("2023-10-03 00:00:00"));
    event2.setEndTime(Timestamp.valueOf("2023-10-04 00:00:00"));
    event2.setRecomendation("Updated recomendation");

    Event event3 = new Event();
    event3.setSeverityId(1L);
    event3.setInfoPageId(2L);
    event3.setLatitude(6.0);
    event3.setLongitude(7.0);
    event3.setRadius(8.0);
    event3.setStartTime(Timestamp.valueOf("2023-10-05 00:00:00"));
    event3.setEndTime(Timestamp.valueOf("2023-10-06 00:00:00"));
    event3.setRecomendation("Another recomendation");

    repository.save(event1);
    repository.save(event2);
    repository.save(event3);

    List<EventResponse> allEvents = repository.findAllWithSeverityInBounds(0, 5, 0, 5);
    assertThat(allEvents).hasSize(2);
  }

}
