//package asgn2Tests;
//
//import asgn2Aircraft.Aircraft;
//import asgn2Aircraft.B747;
//import asgn2Passengers.Business;
//import asgn2Passengers.Economy;
//import asgn2Passengers.First;
//import asgn2Passengers.Passenger;
//import asgn2Passengers.Premium;
//
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Created by Gigness on 15/05/2016.
// */
//public class B747Tests {
//
//	private B747 testCraft;
//	private Passenger testPassenger;
//
//    private static final int BOOKING_TIME = 100;
//    private static final int DEPARTURE_TIME = 600;
//    private static final int CONFIRM_TIME = 200;
//    private static final int CANCELLATION_TIME = 200;
//    private static final int REFUSED_TIME = 300;
//    private static final int QUEUED_TIME = 300;
//    private static final int FLOWN_TIME = 600;
//
//	private static final String FLIGHT_CODE = "test_flight";
//
//	@Before
//    public void setUp() throws Exception {
//		testCraft = new B747(FLIGHT_CODE, DEPARTURE_TIME);
//		testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    }
//
//    @Test
//    public void cancelBooking() throws Exception {
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//
//    	testCraft.cancelBooking(testPassenger, CANCELLATION_TIME);
//    	assertEquals(testCraft.getNumEonomy(),0);
//    }
//
//    @Test
//    public void confirmBooking() throws Exception {
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumEonomy(),1);
//    }
//
//    @Test
//    public void finalState() throws Exception {
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.finalState(),"B747:test_flight:600 Pass: 1\npassID: Y:1\nBT: 100\nNotQ\nConfT: 200 NotFlown\n\n");
//
//    }
//
//    @Test
//    public void flightEmpty() throws Exception {
//    	assertEquals(testCraft.flightEmpty(), true);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.flightEmpty(), false);
//    }
//
//    @Test
//    public void flightFull() throws Exception {
//    	B747 testCraftSmall = new B747(FLIGHT_CODE, DEPARTURE_TIME, 0, 0, 0, 1);
//    	assertEquals(testCraftSmall.flightFull(), false);
//    	testCraftSmall.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraftSmall.flightFull(), true);
//    }
//
//    @Test
//    public void flyPassengers() throws Exception {
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testPassenger.isFlown(), false);
//    	testCraft.flyPassengers(DEPARTURE_TIME);
//    	assertEquals(testPassenger.isFlown(), true);
//    }
//
//    @Test
//    public void getBookings() throws Exception {
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	//TODO maybe add more asserts
//    	assertEquals(testCraft.getBookings().getAvailable(), 352);
//    }
//
//    /* TODO prolly add more degrees to these tests, all of these could have the code of getNumPassengers*/
//    @Test
//    public void getNumBusiness() throws Exception {
//    	testPassenger = new Business(BOOKING_TIME, DEPARTURE_TIME);
//    	assertEquals(testCraft.getNumBusiness(), 0);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumBusiness(), 1);
//    }
//
//    @Test
//    public void getNumEonomy() throws Exception {
//    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    	assertEquals(testCraft.getNumEonomy(), 0);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumEonomy(), 1);
//    }
//
//    @Test
//    public void getNumFirst() throws Exception {
//    	testPassenger = new First(BOOKING_TIME, DEPARTURE_TIME);
//    	assertEquals(testCraft.getNumFirst(), 0);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumFirst(), 1);
//    }
//
//    @Test
//    public void getNumPassengers() throws Exception {
//    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    	assertEquals(testCraft.getNumPassengers(), 0);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumPassengers(), 1);
//    }
//
//    @Test
//    public void getNumPremium() throws Exception {
//    	testPassenger = new Premium(BOOKING_TIME, DEPARTURE_TIME);
//    	assertEquals(testCraft.getNumPremium(), 0);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getNumPremium(), 1);
//    }
//
//    @Test
//    public void getPassengers() throws Exception {
//    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getPassengers().get(0).getPassID(), "Y:11" );
//    }
//
//    @Test
//    public void getStatus() throws Exception {
//    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	assertEquals(testCraft.getStatus(BOOKING_TIME),"100::1::F:0::J:0::P:0::Y:1|Y:N/Q>C|\n");
//    }
//
//    /* TODO why does adding this screw with some other tests*/
//    @Test
//    public void hasPassenger() throws Exception {
////    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
////    	assertEquals(testCraft.hasPassenger(testPassenger), false);
////    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
////    	assertEquals(testCraft.hasPassenger(testPassenger), true);
//    }
//
//    @Test
//    public void initialState() throws Exception {
//    	assertEquals(testCraft.initialState(), "B747:test_flight:600 Capacity: 353 [F: 14 J: 52 P: 32 Y: 255]");
//    }
//
//    /* TODO why does adding this screw with some other tests*/
//    @Test
//    public void seatsAvailable() throws Exception {
////    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
////    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
////    	assertEquals(testCraft.seatsAvailable(testPassenger), true);
//    }
//
//    @Test
//    public void upgradeBookings() throws Exception {
//    	testPassenger = new Economy(BOOKING_TIME, DEPARTURE_TIME);
//    	testCraft.confirmBooking(testPassenger, CONFIRM_TIME);
//    	testCraft.upgradeBookings();
//    	assertEquals(testPassenger.getClass().getSimpleName(),"Premium");
//    }
//}