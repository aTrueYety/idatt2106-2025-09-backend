package no.ntnu.stud.idatt2106_2025_9.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import no.ntnu.stud.idatt2106_2025_9.model.Household;

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
        Household household = new Household(1L, "123 Main St", 40.7128, -74.0060, 500.0, now);

        assertEquals(1L, household.getId());
        assertEquals("123 Main St", household.getAdress());
        assertEquals(40.7128, household.getLongditude());
        assertEquals(-74.0060, household.getLatitude());
        assertEquals(500.0, household.getWaterAmountLiters());
        assertEquals(now, household.getLastWaterChange());
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
        household.setLongditude(12.3456);
        household.setLatitude(65.4321);
        household.setWaterAmountLiters(750.0);
        household.setLastWaterChange(now);

        assertEquals(2L, household.getId());
        assertEquals("456 Side St", household.getAdress());
        assertEquals(12.3456, household.getLongditude());
        assertEquals(65.4321, household.getLatitude());
        assertEquals(750.0, household.getWaterAmountLiters());
        assertEquals(now, household.getLastWaterChange());
    }
}
