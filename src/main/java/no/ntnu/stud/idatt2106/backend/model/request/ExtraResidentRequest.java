package no.ntnu.stud.idatt2106.backend.model.request;

import lombok.Data;

@Data
public class ExtraResidentRequest {
  private int householdId;
  private int typeId;
}
