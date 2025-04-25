package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing map object types.
 */
@Repository
public class MapObjectTypeRepositoryImpl implements MapObjectTypeRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<MapObjectType> mapObjectTypeRowMapper = (rs, rowNum) -> {
    MapObjectType mapObjectType = new MapObjectType();
    mapObjectType.setId(rs.getLong("id"));
    mapObjectType.setName(rs.getString("name"));
    return mapObjectType;
  };

  @Override
  public List<MapObjectType> findAll() {
    String sql = "SELECT * FROM map_object_type";
    return jdbcTemplate.query(sql, mapObjectTypeRowMapper);
  }

  @Override
  public MapObjectType findById(Long id) {
    String sql = "SELECT * FROM map_object_type WHERE id = ?";
    List<MapObjectType> types = jdbcTemplate.query(sql, mapObjectTypeRowMapper, id);
    return types.isEmpty() ? null : types.get(0);
  }

  @Override
  public void save(MapObjectType mapObjectType) {
    String sql = "INSERT INTO map_object_type (name) VALUES (?)";
    jdbcTemplate.update(sql, mapObjectType.getName());
  }

  @Override
  public void update(MapObjectType mapObjectType) {
    String sql = "UPDATE map_object_type SET name = ? WHERE id = ?";
    jdbcTemplate.update(sql, mapObjectType.getName(), mapObjectType.getId());
  }

  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM map_object_type WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
