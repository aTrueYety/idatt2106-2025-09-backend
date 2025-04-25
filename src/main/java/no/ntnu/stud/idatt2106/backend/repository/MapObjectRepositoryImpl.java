package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing map objects.
 * This class provides methods to perform CRUD operations on map objects in the
 * database.
 */
@Repository
public class MapObjectRepositoryImpl implements MapObjectRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<MapObject> mapObjectRowMapper = (rs, rowNum) -> {
    MapObject mapObject = new MapObject();
    mapObject.setId(rs.getLong("id"));
    mapObject.setTypeId(rs.getLong("type_id"));
    mapObject.setLatitude(rs.getFloat("latitude"));
    mapObject.setLongitude(rs.getFloat("longitude"));
    mapObject.setOpening(rs.getTimestamp("opening"));
    mapObject.setClosing(rs.getTimestamp("closing"));
    mapObject.setContactPhone(rs.getString("contact_phone"));
    mapObject.setContactEmail(rs.getString("contact_email"));
    mapObject.setContactName(rs.getString("contact_name"));
    mapObject.setDescription(rs.getString("description"));
    return mapObject;
  };

  private final RowMapper<MapObjectResponse> mapObjectResponseRowMapper = (rs, rowNum) -> {
    MapObjectResponse mapObject = new MapObjectResponse();
    mapObject.setId(rs.getLong("id"));
    mapObject.setTypeId(rs.getLong("type_id"));
    mapObject.setTypeName(rs.getString("type_name"));
    mapObject.setTypeIcon(rs.getString("type_icon"));
    mapObject.setLatitude(rs.getFloat("latitude"));
    mapObject.setLongitude(rs.getFloat("longitude"));
    mapObject.setOpening(rs.getTimestamp("opening"));
    mapObject.setClosing(rs.getTimestamp("closing"));
    mapObject.setContactPhone(rs.getString("contact_phone"));
    mapObject.setContactEmail(rs.getString("contact_email"));
    mapObject.setContactName(rs.getString("contact_name"));
    mapObject.setDescription(rs.getString("description"));
    return mapObject;
  };

  @Override
  public List<MapObject> findAll() {
    String sql = "SELECT * FROM map_object";
    return jdbcTemplate.query(sql, mapObjectRowMapper);
  }

  @Override
  public MapObjectResponse findByIdWithDetail(Long id) {
    String sql = "SELECT mo.*, mot.name AS type_name, mot.icon AS type_icon FROM map_object mo "
        + "JOIN map_object_type mot ON mo.type_id = mot.id WHERE mo.id = ?";
    List<MapObjectResponse> objects = jdbcTemplate.query(sql, mapObjectResponseRowMapper, id);
    return objects.isEmpty() ? null : objects.get(0);
  }

  @Override
  public void save(MapObject mapObject) {
    String sql = "INSERT INTO map_object (type_id, latitude, longitude, opening, closing, "
        + "contact_phone, contact_email, contact_name, description) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, mapObject.getTypeId(), mapObject.getLatitude(),
        mapObject.getLongitude(), mapObject.getOpening(), mapObject.getClosing(),
        mapObject.getContactPhone(), mapObject.getContactEmail(), mapObject.getContactName(),
        mapObject.getDescription());
  }

  @Override
  public void update(MapObject mapObject) {
    String sql = "UPDATE map_object SET type_id = ?, latitude = ?, longitude = ?, opening = ?, " 
        + "closing = ?, contact_phone = ?, contact_email = ?, contact_name = ?, description = ? " 
        + "WHERE id = ?";
    jdbcTemplate.update(sql, mapObject.getTypeId(), mapObject.getLatitude(), 
        mapObject.getLongitude(), mapObject.getOpening(), mapObject.getClosing(), 
        mapObject.getContactPhone(), mapObject.getContactEmail(), mapObject.getContactName(), 
        mapObject.getDescription(), mapObject.getId());
  }

  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM map_object WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Finds all map objects within the specified geographical bounds.
   *
   * @param minLat  The minimum latitude of the bounding box.
   * @param maxLat  The maximum latitude of the bounding box.
   * @param minLong The minimum longitude of the bounding box.
   * @param maxLong The maximum longitude of the bounding box.
   * @return A list of map objects within the specified bounds.
   */
  @Override
  public List<MapObjectResponse> findAllInBoundsWithDetail(
      double minLat, double maxLat, double minLong, double maxLong) {
    String sql = "SELECT mo.*, mot.name AS type_name, mot.icon AS type_icon FROM map_object mo "
        + "JOIN map_object_type mot ON mo.type_id = mot.id "
        + "WHERE mo.latitude BETWEEN ? AND ? AND mo.longitude BETWEEN ? AND ?";
    return jdbcTemplate.query(sql, mapObjectResponseRowMapper, minLat, maxLat, minLong, maxLong);
  }
}
