package asgn2AircraftTests;

import asgn2Aircraft.Aircraft;
import asgn2Aircraft.B747;
import asgn2Passengers.Economy;
import asgn2Passengers.Passenger;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Gigness on 15/05/2016.
 */
public class B747Tests {

	private B747 testCraft;
	private Passenger testPassenger;
	
	static final int FIRST = 14;
	static final int BUSINESS = 52;
	static final int PREMIUM = 32;
	static final int ECONOMY = 255;
	
    private static final int BOOKING_TIME = 100;
    private static final int DEPARTURE_TIME = 600;
    private static final int CONFIRM_TIME = 200;
    private static final int CANCELLATION_TIME = 200;
    private static final int REFUSED_TIME = 300;
    private static final int QUEUED_TIME = 300;
    private static final int FLOWN_TIME = 600;
    
	private static final String FLIGHT_CODE = "test_flight";
	
	@Before
    public void setUp() throws Exception {
		testCraft = new B747(FLIGHT_CODE, DEPARTURE_TIME);
		testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
    }

    @Test
    public void cancelBooking() throws Exception {
    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
    	assertEquals(testCraft.getNumEonomy(),1);
    	
    	testCraft.cancelBooking(testPassenger, CANCELLATION_TIME);
    	assertEquals(testCraft.getNumEonomy(),0);
    }

    @Test
    public void confirmBooking() throws Exception {
    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
    	assertEquals(testCraft.getNumEonomy(),1);
    }

    @Test
    public void finalState() throws Exception {
    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
    	assertEquals(testCraft.getNumEonomy(),1);
    	assertEquals(testCraft.finalState(),"B747:test_flight:600 Pass: 1\npassID: Y:1\nBT: 100\nNotQ\nConfT: 200 NotFlown\n\n");
    	
    }

    @Test
    public void flightEmpty() throws Exception {
    	assertEquals(testCraft.flightEmpty(), true);
    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
    	assertEquals(testCraft.flightEmpty(), false);
    }

    @Test
    public void flightFull() throws Exception {
    	assertEquals(testCraft.flightFull(), false);
    	for(int i = 0;i <= ECONOMY; i++){
    		testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
    	}
    	assertEquals(testCraft.getNumEonomy(), 255);
    }

    @Test
    public void flyPassengers() throws Exception {

    }

    @Test
    public void getBookings() throws Exception {

    }

    @Test
    public void getNumBusiness() throws Exception {

    }

    @Test
    public void getNumEonomy() throws Exception {

    }

    @Test
    public void getNumFirst() throws Exception {

    }

    @Test
    public void getNumPassengers() throws Exception {

    }

    @Test
    public void getNumPremium() throws Exception {

    }

    @Test
    public void getPassengers() throws Exception {

    }

    @Test
    public void getStatus() throws Exception {

    }

    @Test
    public void hasPassenger() throws Exception {

    }

    @Test
    public void initialState() throws Exception {

    }

    @Test
    public void seatsAvailable() throws Exception {

    }

    @Test
    public void upgradeBookings() throws Exception {

    }
}