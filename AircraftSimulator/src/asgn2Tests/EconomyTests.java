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

