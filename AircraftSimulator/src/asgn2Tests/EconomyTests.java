/**
 *
 * This file is part of the testing suite for the AircraftSimulator Project
 * for CAB302, Semester 1, 2016
 * Junit tests for {@link asgn2Passengers.Economy}
 *
 */
package asgn2Tests;

import asgn2Passengers.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EconomyTests {

    private Passenger economy;

    @Before
    public void SetUp() throws PassengerException {
        economy = new Economy(0, 200);
    }

    @Test
    public void Upgrade_Economy() {
        Passenger premium = economy.upgrade();
        assertTrue(premium instanceof Premium);
    }
}

