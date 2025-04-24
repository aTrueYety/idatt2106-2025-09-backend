package no.ntnu.stud.idatt2106_2025_9.backend.model.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import no.ntnu.stud.idatt2106.backend.model.base.Household;

/**
 * Contains tests for the Household class.
 */
public class HouseholdTest {

    /**
     * Tests the all argument constructor and getters.
     */
    @Test
    void testAllArgsConstructorAndGetters() {
        Date now = new Date();
        Household household = new Household(1L, "123 Main St", 40.7128, -74.0060, 500.0, now, true);

        assertEquals(1L, household.getId());
        assertEquals("123 Main St", household.getAdress());
        assertEquals(40.7128, household.getLongitude());
        assertEquals(-74.0060, household.getLatitude());
        assertEquals(500.0, household.getWaterAmountLiters());
        assertEquals(now, household.getLastWaterChangeDate());
        assertEquals(true, household.isHasFirstAidKit());
    }

    /**
     * Tests the setters.
     */
    @Test
    void testSetters() {
        Household household = new Household();
        Date now = new Date();

        household.setId(2L);
        household.setAdress("456 Side St");
        household.setLongitude(12.3456);
        household.setLatitude(65.4321);
        household.setWaterAmountLiters(750.0);
        household.setLastWaterChangeDate(now);

        assertEquals(2L, household.getId());
        assertEquals("456 Side St", household.getAdress());
        assertEquals(12.3456, household.getLongitude());
        assertEquals(65.4321, household.getLatitude());
        assertEquals(750.0, household.getWaterAmountLiters());
        assertEquals(now, household.getLastWaterChangeDate());
    }
}
