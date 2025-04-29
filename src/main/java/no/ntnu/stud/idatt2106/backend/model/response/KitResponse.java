package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




/**
 * Response object for a kit.
 *
 * <p>This class includes name and description of the kit.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitResponse {
  private Long id;
  private String name;
  private String description; 
}
