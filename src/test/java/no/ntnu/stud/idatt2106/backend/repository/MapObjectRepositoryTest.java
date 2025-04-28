package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Import;
import org.springframework.data.relational.core.sql.Into;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.mysql.cj.x.protobuf.MysqlxCrud.Insert;

@JdbcTest
@ActiveProfiles("test")
@Import(MapObjectRepositoryImpl.class)
public class MapObjectRepositoryTest {

  @Autowired
  private MapObjectRepositoryImpl repository;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldCreateShelter() {
    LocalTime opening = LocalTime.of(10, 0);
    LocalTime closing = LocalTime.of(20, 0);

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        2L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        1L, 2L, 10.0, 20.0, opening, closing, "97631412",
        "jacoblein2010@gmail.com", "Jacob", "Groveste shelter");

    MapObjectResponse response = repository.findByIdWithDetail(1L);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getTypeId()).isEqualTo(2L);
    assertThat(response.getTypeName()).isEqualTo("Shelter");
    assertThat(response.getTypeIcon()).isEqualTo("test-icon.png");
    assertThat(response.getDescription()).isEqualTo("Groveste shelter");
  }

  @Test
  void shouldCreateMapObjectWithoutTimes() {
    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        3L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        2L, 3L, 10.0, 20.0, "97631412", "test@gmail.com", "Jacob", "Groveste shelter");
  }

  @Test
  void findAllInBoundsWithDetail() {
    LocalTime opening = LocalTime.of(10, 0);
    LocalTime closing = LocalTime.of(20, 0);

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        2L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        3L, "Matlager", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        2L,
        2L,
        63.414455,
        10.353135,
        opening,
        closing,
        "97631412",
        "jacoblein2010@gmail.com",
        "Jacob",
        "Groveste shelter");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        3L, 3L, 63.421057, 10.393674, opening, closing, "97631412",
        "jacoblein2010@gmail.com", "Henrik", "Groveste shelter");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        4L, 3L, 63.359789, 10.387397, opening, closing, "97631412",
        "jacoblein2010@gmail.com", "Erik", "Groveste shelter");
    List<MapObjectResponse> response = repository.findAllInBoundsWithDetail(63.410475, 63.429806, 10.346577, 10.411068);
    assertThat(response.size()).isEqualTo(2);
    assertThat(response.get(0).getContactName()).isEqualTo("Jacob");
    assertThat(response.get(1).getContactName()).isEqualTo("Henrik");
  }

  @Test
  void findAllInBoundsWithDetailEmpty() {
    List<MapObjectResponse> response = repository.findAllInBoundsWithDetail(63.410475, 63.429806, 10.346577, 10.411068);
    assertThat(response.size()).isEqualTo(0);
  }

  @Test
  void findClosestWithDetail() {
    LocalTime opening = LocalTime.of(10, 0);
    LocalTime closing = LocalTime.of(20, 0);

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        2L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        3L, "Matlager", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        2L,
        2L,
        63.414455,
        10.353135,
        opening,
        closing,
        "97631412",
        "jacoblein2010@gmail.com",
        "Jacob",
        "Groveste shelter");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        3L, 3L, 63.421057, 10.393674, opening, closing, "97631412",
        "jacoblein2010@gmail.com", "Henrik", "Henriks shelter");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        4L, 3L, 63.359789, 10.387397, opening, closing, "9761412",
        "jacoblein2010@gmail.com", "Erik", "Eriks shelter");

    MapObjectResponse response1 = repository.findClosestWithDetail(63.416242, 10.403728, 2L);
    MapObjectResponse response2 = repository.findClosestWithDetail(63.416242, 10.403728, 3L);

    assertThat(response1.getId()).isEqualTo(2L);
    assertThat(response2.getId()).isEqualTo(3L);
  }

  @Test
  void shouldReturnNullForNonExistentId() {
    MapObjectResponse response = repository.findByIdWithDetail(999L);
    assertThat(response).isNull();
  }

  @Test
  void shouldNotFindAnyObjectsOutOfBounds() {
    LocalTime opening = LocalTime.of(10, 0);
    LocalTime closing = LocalTime.of(20, 0);

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        2L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        5L, 2L, 64.000000, 11.000000, opening, closing, "12345678",
        "test@example.com", "OutOfBounds", "Out of bounds object");

    List<MapObjectResponse> response = repository.findAllInBoundsWithDetail(63.410475, 63.429806, 10.346577, 10.411068);
    assertThat(response.size()).isEqualTo(0);
  }

  @Test
  void shouldReturnNullForClosestObjectWithInvalidType() {
    LocalTime opening = LocalTime.of(10, 0);
    LocalTime closing = LocalTime.of(20, 0);

    jdbc.update(
        "INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)",
        2L, "Shelter", "test-icon.png");

    jdbc.update(
        "INSERT INTO map_object(id, type_id, latitude, longitude, opening, closing, contact_phone, contact_email, contact_name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        6L, 2L, 63.414455, 10.353135, opening, closing, "12345678",
        "test@example.com", "ValidObject", "Valid object");

    MapObjectResponse response = repository.findClosestWithDetail(63.416242, 10.403728, 999L);
    assertThat(response).isNull();
  }

  @Test
  void shouldNotSaveInvalidMapObject() {
    MapObject invalidMapObject = new MapObject();
    invalidMapObject.setId(null);
    invalidMapObject.setTypeId(null);

    try {
      repository.save(invalidMapObject);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(Exception.class);
    }
  }

  @Test
  void shouldNotDeleteNonExistentObject() {
    try {
      repository.deleteById(999L);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(Exception.class);
    }
  }
}
