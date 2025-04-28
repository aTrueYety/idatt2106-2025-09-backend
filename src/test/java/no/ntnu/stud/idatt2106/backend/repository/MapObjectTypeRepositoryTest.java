package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@JdbcTest
@ActiveProfiles("test")
@Import(MapObjectTypeRepositoryImpl.class)
public class MapObjectTypeRepositoryTest {

  @Autowired
  private MapObjectTypeRepositoryImpl repository;

  @Autowired
  private JdbcTemplate jdbc;

  @Test
  void shouldSaveAndFindById() {
    repository.save(new MapObjectType(null, "Shelter", "shelter-icon.png"));
    repository.save(new MapObjectType(null, "Storage", "storage-icon.png"));

    List<MapObjectType> types = repository.findAll();
    assertThat(types).hasSize(2);

    MapObjectType saved = types.get(0);
    MapObjectType fetched = repository.findById(saved.getId());

    assertThat(fetched).isNotNull();
    assertThat(fetched.getName()).isEqualTo("Shelter");
    assertThat(fetched.getIcon()).isEqualTo("shelter-icon.png");
  }

  @Test
  void shouldUpdate() {
    jdbc.update("INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)", 1L, "Shelter", "old-icon.png");

    MapObjectType updateType = new MapObjectType(1L, "Updated Shelter", "new-icon.png");
    repository.update(updateType);

    MapObjectType updated = repository.findById(1L);
    assertThat(updated.getName()).isEqualTo("Updated Shelter");
    assertThat(updated.getIcon()).isEqualTo("new-icon.png");
  }

  @Test
  void shouldDeleteById() {
    jdbc.update("INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)", 2L, "Shelter", "icon.png");

    repository.deleteById(2L);

    MapObjectType deleted = repository.findById(2L);
    assertThat(deleted).isNull();
  }

  @Test
  void findAllShouldReturnMultiple() {
    jdbc.update("INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)", 1L, "Shelter", "icon1.png");
    jdbc.update("INSERT INTO map_object_type (id, name, icon) VALUES (?, ?, ?)", 2L, "Storage", "icon2.png");

    List<MapObjectType> types = repository.findAll();
    assertThat(types).hasSize(2);
  }

  @Test
  void findByIdShouldReturnNullIfNotExists() {
    MapObjectType type = repository.findById(999L);
    assertThat(type).isNull();
  }
}