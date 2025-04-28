package no.ntnu.stud.idatt2106.backend.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a kit.
 *
 * <p>A kit consists of an ID, a name, and a description.
 * It can be used to group related items together.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kit {
  private Long id;
  private String name;
  private String description;
}
