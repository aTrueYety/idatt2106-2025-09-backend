package no.ntnu.stud.idatt2106.backend.model.response;

import lombok.Data;

@Data
public class ExtraResidentTypeResponse {
  private int id;
  private String name;
  private float consumptionWater;
  private float consumptionFood;

  
}
