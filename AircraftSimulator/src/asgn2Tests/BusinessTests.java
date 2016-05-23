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
        assertTrue(first instanceof First);
    }
}

