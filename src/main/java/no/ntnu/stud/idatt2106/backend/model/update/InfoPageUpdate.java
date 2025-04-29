package no.ntnu.stud.idatt2106.backend.model.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing an update to an information page.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoPageUpdate {
  private Long id;
  private String title;
  private String content;
}
