package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Request object for creating or updating an emergency group.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyGroupRequest {

  @NotBlank(message = "Group name is required")
  @Size(max = 100, message = "Name must be under 100 characters")
  private String name;

  @Size(max = 1000, message = "Description must be under 1000 characters")
  private String description;
}
