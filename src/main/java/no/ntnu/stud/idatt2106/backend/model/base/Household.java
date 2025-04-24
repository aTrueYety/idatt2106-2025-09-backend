package no.ntnu.stud.idatt2106.backend.model.base;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a HouseHold.
 * 
 * @version 1.0
 * @since 23.04.2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Household {
    private Long id;
    private String adress;
    private double longitude;
    private double latitude;
    private double waterAmountLiters;
    private Date lastWaterChangeDate;
    private boolean hasFirstAidKit;
}