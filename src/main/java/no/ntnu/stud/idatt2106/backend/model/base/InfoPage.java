package no.ntnu.stud.idatt2106.backend.model.base;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing an information page.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoPage {
  private Long id;
  private String title;
  private String shortDescription;
  private String content;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}
