/**
 *
 * This file is part of the testing suite for the AircraftSimulator Project
 * for CAB302, Semester 1, 2016
 * Junit tests for {@link asgn2Passengers.Premium}
 *
 */
package asgn2Tests;

import asgn2Passengers.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PremiumTests {

    private Passenger premium;

    @Before
    public void SetUp() throws PassengerException {
        premium = new Premium(0, 200);
    }

    @Test
    public void Upgrade_Premium() {
        Passenger business = premium.upgrade();
        assertTrue(business instanceof Business);
    }
}

