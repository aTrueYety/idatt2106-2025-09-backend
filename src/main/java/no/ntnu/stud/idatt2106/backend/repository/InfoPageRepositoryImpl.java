package no.ntnu.stud.idatt2106.backend.repository;

import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for managing information pages.
 */
@Repository
public class InfoPageRepositoryImpl implements InfoPageRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<InfoPage> rowMapper = (rs, rowNum) -> {
    InfoPage infoPage = new InfoPage();
    infoPage.setId(rs.getLong("id"));
    infoPage.setTitle(rs.getString("title"));
    infoPage.setShortDescription(rs.getString("short_description"));
    infoPage.setContent(rs.getString("content"));
    infoPage.setCreatedAt(rs.getTimestamp("created_at"));
    infoPage.setUpdatedAt(rs.getTimestamp("updated_at"));
    return infoPage;
  };

  @Override
  public void save(InfoPage infoPage) {
    String sql = "INSERT INTO info_page (title, short_description, content) "
        + "VALUES (?, ?, ?)";
    jdbcTemplate.update(
          sql, infoPage.getTitle(), infoPage.getShortDescription(), infoPage.getContent());
  }

  @Override
  public InfoPage findById(Long id) {
    String sql = "SELECT * FROM info_page WHERE id = ?";
    List<InfoPage> infoPages = jdbcTemplate.query(sql, rowMapper, id);
    return infoPages.isEmpty() ? null : infoPages.get(0);
  }

  @Override
  public List<InfoPage> findAll() {
    String sql = "SELECT * FROM info_page";
    return jdbcTemplate.query(sql, rowMapper);
  }

  @Override
  public void update(InfoPage infoPageUpdate) {
    String sql = "UPDATE info_page SET title = ?, short_description = ?, content = ? WHERE id = ?";
    jdbcTemplate.update(sql, infoPageUpdate.getTitle(), infoPageUpdate.getShortDescription(), 
        infoPageUpdate.getContent(), infoPageUpdate.getId());
  }

  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM info_page WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
