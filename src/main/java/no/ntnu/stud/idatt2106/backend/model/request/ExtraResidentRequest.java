package no.ntnu.stud.idatt2106.backend.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request object for creating or updating an extra resident.
 */
@Data
public class ExtraResidentRequest {

  @Min(value = 1, message = "Household ID must be a positive number")
  private int householdId;

  @Min(value = 1, message = "Type ID must be a positive number")
  private int typeId;

  @NotBlank(message = "Name is required")
  private String name;
}
