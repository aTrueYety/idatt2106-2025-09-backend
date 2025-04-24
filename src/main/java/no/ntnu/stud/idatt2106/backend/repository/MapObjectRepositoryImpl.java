package no.ntnu.stud.idatt2106.backend.repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for MapObject.
 */
@Repository
public class MapObjectRepositoryImpl implements MapObjectRepository {
  private final JdbcTemplate jdbcTemplate;

  public MapObjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<MapObject> rowMapper = (rs, rowNum) -> {
    MapObject mapObject = new MapObject();
    mapObject.setId(rs.getInt("id"));
    mapObject.setTypeId(rs.getInt("type_id"));
    mapObject.setDescription(rs.getString("description"));
    mapObject.setLatitude(rs.getFloat("latitude"));
    mapObject.setLongitude(rs.getFloat("longitude"));

    mapObject.setOpening(rs.getTimestamp("opening"));
    mapObject.setClosing(rs.getTimestamp("closing"));

    mapObject.setContactPhone(rs.getString("contact_phone"));
    mapObject.setContactEmail(rs.getString("contact_email"));
    mapObject.setContactName(rs.getString("contact_name"));
    mapObject.setImage(rs.getBlob("image"));

    return mapObject;
  };

  @Override
  public Optional<MapObject> findById(int id) {
    String sql = "SELECT * FROM map_object WHERE id = ?";
    List<MapObject> results = jdbcTemplate.query(sql, rowMapper, id);
    return results.stream().findFirst();
  }

  @Override
  public List<MapObject> findAll() {
    return jdbcTemplate.query("SELECT * FROM map_object", rowMapper);
  }

  @Override
  public void save(MapObject mapObject) {
    String sql = "INSERT INTO map_object ("
        + "type_id, latitude, longitude, opening, closing, "
        + "contact_phone, contact_email, contact_name, description, image) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(sql, mapObject.getTypeId(), mapObject.getLatitude(),
        mapObject.getLongitude(), mapObject.getOpening(), mapObject.getClosing(),
        mapObject.getContactPhone(), mapObject.getContactEmail(),
        mapObject.getContactName(), mapObject.getDescription(), mapObject.getImage());
  }

  @Override
  public void update(MapObject mapObject) {
    String sql = "UPDATE map_object SET "
        + "type_id = ?, latitude = ?, longitude = ?, opening = ?, "
        + "closing = ?, contact_phone = ?, contact_email = ?, contact_name = ?, "
        + "description = ?, image = ? WHERE id = ?";

    jdbcTemplate.update(sql, mapObject.getTypeId(), mapObject.getLatitude(),
        mapObject.getLongitude(), mapObject.getOpening(), mapObject.getClosing(),
        mapObject.getContactPhone(), mapObject.getContactEmail(),
        mapObject.getContactName(), mapObject.getDescription(), mapObject.getImage(),
        mapObject.getId());
  }

  @Override
  public void deleteById(int id) {
    jdbcTemplate.update("DELETE FROM map_object WHERE id = ?", id);
  }

}
