package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing an information page.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoPageRequest {
  private String title;
  private String shortDescription;
  private String content;
}