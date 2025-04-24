package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for MapObjectType.
 */
@Repository
public class MapObjectTypeRepositoryImpl implements MapObjectTypeRepository {
  private final JdbcTemplate jdbcTemplate;

  public MapObjectTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<MapObjectType> rowMapper = (rs, rowNum) -> {
    MapObjectType mapObjectType = new MapObjectType();
    mapObjectType.setId(rs.getInt("id"));
    mapObjectType.setName(rs.getString("name"));
    mapObjectType.setIcon(rs.getString("icon"));
    return mapObjectType;
  };

  @Override
  public MapObjectType findById(int id) {
    String sql = "SELECT * FROM map_object_type WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, rowMapper, id);
  }

  @Override
  public List<MapObjectType> findAll() {
    String sql = "SELECT * FROM map_object_type";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public void save(MapObjectType mapObjectType) {
    String sql = "INSERT INTO map_object_type (name, icon) VALUES (?, ?)";
    jdbcTemplate.update(sql, mapObjectType.getName(), mapObjectType.getIcon());
  }

  @Override
  public void update(MapObjectType mapObjectType) {
    String sql = "UPDATE map_object_type SET name = ?, icon = ? WHERE id = ?";
    jdbcTemplate.update(sql, mapObjectType.getName(),
        mapObjectType.getIcon(), mapObjectType.getId());
  }

  @Override
  public void deleteById(int id) {
    String sql = "DELETE FROM map_object_type WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
