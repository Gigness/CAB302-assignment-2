/**
 *
 * This file is part of the testing suite for the AircraftSimulator Project
 * for CAB302, Semester 1, 2016
 * Junit tests for {@link asgn2Passengers.Business}
 *
 */
package asgn2Tests;

import asgn2Passengers.Business;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Gigness on 23/05/2016.
 */
public class BusinessTests {

    private Passenger business;

    @Before
    public void SetUp() throws PassengerException {
        business = new Business(0, 200);
    }

    @Test
    public void Upgrade_Business() {
        Passenger first = business.upgrade();
        System.out.println(first);
        System.out.println(first.getPassID());
        assertTrue(first instanceof First);
    }
}

