package asgn2Tests;

import asgn2Aircraft.Aircraft;
import asgn2Aircraft.A380;
import asgn2Aircraft.AircraftException;
import asgn2Aircraft.Bookings;
import asgn2Passengers.*;

import static org.junit.Assert.*;

import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gigness on 15/05/2016.
 */
public class A380Tests {

    private A380 testCraft;
    private A380 fourSeatsCraft;
    private A380 upgradeTestCraft;
    private A380 getStatusTestCraft;
    private Passenger testPassenger0;
    private Passenger testPassenger1;
    private Passenger testPassenger2;
    private Passenger testPassenger3;
    private Passenger testPassenger4;

    private ArrayList<Passenger> economyList= new ArrayList<>();
    private ArrayList<Passenger> premiumList= new ArrayList<>();
    private ArrayList<Passenger> businessList= new ArrayList<>();
    private ArrayList<Passenger> firstList= new ArrayList<>();

    private HashMap<Character, Integer> classSeats= new HashMap<>();

    private int economyPassengers = 20;
    private int premiumPassengers = 15;
    private int businessPassengers = 10;
    private int firstPassengers = 5;

    private static final int BOOKING_TIME = 100;
    private static final int DEPARTURE_TIME = 600;
    private static final int CONFIRM_TIME = 200;
    private static final int CANCELLATION_TIME = 200;
    private static final int REFUSED_TIME = 300;
    private static final int QUEUED_TIME = 300;
    private static final int FLOWN_TIME = 600;

    private static final String FLIGHT_CODE = "test_flight";

    @Before
    public void SetUp_Passengers() throws PassengerException {
        classSeats.put('Y', economyPassengers);
        classSeats.put('P', premiumPassengers);
        classSeats.put('J', businessPassengers);
        classSeats.put('F', firstPassengers);

        for (Map.Entry numSeats: classSeats.entrySet()) {
            Character fareClass = (Character) numSeats.getKey();
            if (fareClass == 'Y') {
                for(int i = 0; i < (int) numSeats.getValue(); i++) {
                    economyList.add(new Economy(BOOKING_TIME, DEPARTURE_TIME));
                }
            } else if (fareClass == 'P') {
                for(int i = 0; i < (int) numSeats.getValue(); i++) {
                    premiumList.add(new Premium(BOOKING_TIME, DEPARTURE_TIME));
                }
            } else if (fareClass == 'J') {
                for(int i = 0; i < (int) numSeats.getValue(); i++) {
                    businessList.add(new Business(BOOKING_TIME, DEPARTURE_TIME));
                }
            } else {
                for(int i = 0; i < (int) numSeats.getValue(); i++) {
                    firstList.add(new First(BOOKING_TIME, DEPARTURE_TIME));
                }
            }
        }

        testPassenger0 = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        testPassenger1 = new Premium(BOOKING_TIME, DEPARTURE_TIME);
        testPassenger2 = new Business(BOOKING_TIME, DEPARTURE_TIME);
        testPassenger3 = new First(BOOKING_TIME, DEPARTURE_TIME);
    }

    @Before
    public void SetUp_Aircraft() throws Exception {
        testCraft = new A380(FLIGHT_CODE, DEPARTURE_TIME);
        fourSeatsCraft = new A380(FLIGHT_CODE, DEPARTURE_TIME, 1, 1, 1, 1);
        upgradeTestCraft = new A380(FLIGHT_CODE, DEPARTURE_TIME, 5, 10, 15, 20);
        getStatusTestCraft = new A380(FLIGHT_CODE, DEPARTURE_TIME, 1, 1, 1, 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#A380(String, int)}
     */
    @Test (expected = AircraftException.class)
    public void A380_FlightCodeNull() throws AircraftException {
        String flightCode = null;
        Aircraft a = new A380(flightCode, DEPARTURE_TIME);
    }

    @Test (expected = AircraftException.class)
    public void A380_FlightDepartureTimeZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, 0);
    }

    @Test (expected = AircraftException.class)
    public void A380_FlightDepartureTimeLessZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, -1);
    }

    @Test (expected = AircraftException.class)
    public void A380_FirstLessZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, DEPARTURE_TIME, -1, 0, 0, 0);
    }

    @Test (expected = AircraftException.class)
    public void A380_BusinessLessZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, DEPARTURE_TIME, 0, -1, 0, 0);
    }

    @Test (expected = AircraftException.class)
    public void A380_PremiumLessZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, DEPARTURE_TIME, 0, 0, -1, 0);
    }

    @Test (expected = AircraftException.class)
    public void A380_EconomyLessZero() throws AircraftException {
        String flightCode = "swag flight";
        Aircraft a = new A380(flightCode, DEPARTURE_TIME, 0, 0, 0, -1);
    }

    @Test
    public void A380_AllSeatsZero() throws AircraftException {
        Aircraft a = new A380("swag flight", DEPARTURE_TIME, 0, 0, 0, 0);
        assertEquals(a.getNumPassengers(),0);
    }

    @Test
    public void A380_Normal() throws AircraftException {
        Aircraft a = new A380("swag flight", DEPARTURE_TIME);
    }
    
    /**
     * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}
     */
    @Test (expected = AircraftException.class)
    public void CancelBooking_NoPassenger() throws AircraftException, PassengerException {
        testCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
    }

    @Test
    public void CancelBooking_Economy() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
        assertEquals(fourSeatsCraft.getNumEonomy(),0);
    }

    @Test
    public void CancelBooking_Premium() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.cancelBooking(testPassenger1, CANCELLATION_TIME);
        assertEquals(fourSeatsCraft.getNumPremium(),0);
    }

    @Test
    public void CancelBooking_Business() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.cancelBooking(testPassenger2, CANCELLATION_TIME);
        assertEquals(fourSeatsCraft.getNumBusiness(),0);
    }

    @Test
    public void CancelBooking_First() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        fourSeatsCraft.cancelBooking(testPassenger3, CANCELLATION_TIME);
        assertEquals(fourSeatsCraft.getNumFirst(),0);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_CancellationTimeLessZero() throws PassengerException, AircraftException {
        testPassenger4 = new Economy(BOOKING_TIME, DEPARTURE_TIME);

        testCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
        testCraft.cancelBooking(testPassenger4, -1);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_DepartureTimeLessCancellationTime() throws PassengerException, AircraftException {
        testPassenger4 = new Economy(BOOKING_TIME, DEPARTURE_TIME);

        testCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
        testCraft.cancelBooking(testPassenger4, DEPARTURE_TIME + 1);
    }

    @Test
    public void CancelBooking_DepartureTimeEqualCancellationTime() throws PassengerException, AircraftException {
        testPassenger4 = new Economy(BOOKING_TIME, DEPARTURE_TIME);

        testCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
        testCraft.cancelBooking(testPassenger4, DEPARTURE_TIME);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_Flown() throws PassengerException, AircraftException {
        testPassenger4 = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        testCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
        testCraft.flyPassengers(DEPARTURE_TIME);
        testCraft.cancelBooking(testPassenger4, CANCELLATION_TIME);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_Queued() throws PassengerException, AircraftException {
        testCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        testPassenger0.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
        testCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_New() throws PassengerException, AircraftException {
        testCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        testPassenger0.cancelSeat(CANCELLATION_TIME);
        testCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
    }

    @Test (expected = PassengerException.class)
    public void CancelBooking_Refused() throws PassengerException, AircraftException {
        testCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        testPassenger0.refusePassenger(REFUSED_TIME);
        testCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
    }


    /**
     * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}
     */
    @Test (expected = AircraftException.class)
    public void ConfirmBooking_NoSeatsEconomy() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger4 = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
    }

    @Test (expected = AircraftException.class)
    public void ConfirmBooking_NoSeatsPremium() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger4 = new Premium(BOOKING_TIME, DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
    }

    @Test (expected = AircraftException.class)
    public void ConfirmBooking_NoSeatsBusiness() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger4 = new Business(BOOKING_TIME, DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
    }

    @Test (expected = AircraftException.class)
    public void ConfirmBooking_NoSeatsFirst() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger4 = new First(BOOKING_TIME, DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger4, CONFIRM_TIME);
    }

    @Test
    public void ConfirmBooking() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        assertEquals(fourSeatsCraft.getNumEonomy(), 1);
        assertEquals(fourSeatsCraft.getNumPremium(), 1);
        assertEquals(fourSeatsCraft.getNumBusiness(), 1);
        assertEquals(fourSeatsCraft.getNumFirst(), 1);
    }

    @Test (expected = PassengerException.class)
    public void ConfirmBooking_Confirmed() throws PassengerException, AircraftException {
        testPassenger0.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
    }

    @Test (expected = PassengerException.class)
    public void ConfirmBooking_Refused() throws PassengerException, AircraftException {
        testPassenger0.refusePassenger(REFUSED_TIME);
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
    }

    @Test (expected = PassengerException.class)
    public void ConfirmBooking_Flown() throws PassengerException, AircraftException {
        testPassenger0.flyPassenger(DEPARTURE_TIME);
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
    }

    @Test (expected = PassengerException.class)
    public void ConfirmBooking_ConfirmationTimeLessZero() throws PassengerException, AircraftException {
        fourSeatsCraft.confirmBooking(testPassenger0, -1);
    }

    @Test (expected = PassengerException.class)
    public void ConfirmBooking_DepartureTimeLessConfirmTime() throws PassengerException, AircraftException {
        fourSeatsCraft.confirmBooking(testPassenger0, DEPARTURE_TIME + 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#finalState()}
     */
    @Test
    public void FinalState() throws Exception {
        // TEST CANNOT BE RUN IN ISOLATION - as the passID will be incorrect
        getStatusTestCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertEquals(getStatusTestCraft.finalState(),"A380:test_flight:600 Pass: 1\npassID: Y:1136\nBT: 100\nNotQ\nConfT: 200 NotFlown\n\n");
    }

    @Test
    public void FinalState_ZeroPassengers() throws Exception {
        assertEquals(getStatusTestCraft.finalState(),"A380:test_flight:600 Pass: 0\n\n");
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#flightEmpty()}
     */
    @Test
    public void FlightEmpty() throws Exception {
        assertEquals(testCraft.flightEmpty(), true);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#flightFull()}
     */
    @Test
    public void FlightFull() throws Exception {
        A380 testCraftSmall = new A380(FLIGHT_CODE, DEPARTURE_TIME, 0, 0, 0, 1);
        assertEquals(testCraftSmall.flightFull(), false);
        testCraftSmall.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertEquals(testCraftSmall.flightFull(), true);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}
     */
    @Test
    public void FlyPassengers() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        assertFalse(testPassenger0.isFlown());
        assertFalse(testPassenger1.isFlown());
        assertFalse(testPassenger2.isFlown());
        assertFalse(testPassenger3.isFlown());

        fourSeatsCraft.flyPassengers(DEPARTURE_TIME);

        assertTrue(testPassenger0.isFlown());
        assertTrue(testPassenger1.isFlown());
        assertTrue(testPassenger2.isFlown());
        assertTrue(testPassenger3.isFlown());
    }

    @Test (expected = PassengerException.class)
    public void FlyPassengers_PassengerNew() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger0.cancelSeat(CANCELLATION_TIME);

        fourSeatsCraft.flyPassengers(DEPARTURE_TIME);
    }

    @Test (expected = PassengerException.class)
    public void FlyPassengers_PassengerQueued() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger0.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);

        fourSeatsCraft.flyPassengers(DEPARTURE_TIME);
    }

    @Test (expected = PassengerException.class)
    public void FlyPassengers_PassengerRefused() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger0.refusePassenger(REFUSED_TIME);

        fourSeatsCraft.flyPassengers(DEPARTURE_TIME);
    }

    @Test (expected = PassengerException.class)
    public void FlyPassengers_PassengerFlown() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        testPassenger0.flyPassenger(DEPARTURE_TIME);

        fourSeatsCraft.flyPassengers(DEPARTURE_TIME);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getBookings()}
     */
    @Test
    public void GetBookings() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        Bookings bookings = fourSeatsCraft.getBookings();
        assertEquals(bookings.getNumFirst(), 1);
        assertEquals(bookings.getNumBusiness(), 1);
        assertEquals(bookings.getNumPremium(), 1);
        assertEquals(bookings.getNumEconomy(), 1);
        assertEquals(bookings.getTotal(), 4);
        assertEquals(bookings.getAvailable(), 0);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getNumBusiness()}
     */
    @Test
    public void GetNumBusiness() throws Exception {
        assertEquals(testCraft.getNumBusiness(), 0);
        testCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        assertEquals(testCraft.getNumBusiness(), 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getNumEonomy()}
     */
    @Test
    public void GetNumEonomy() throws Exception {
        assertEquals(testCraft.getNumEonomy(), 0);
        testCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertEquals(testCraft.getNumEonomy(), 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getNumFirst()}
     */
    @Test
    public void GetNumFirst() throws Exception {
        assertEquals(testCraft.getNumFirst(), 0);
        testCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        assertEquals(testCraft.getNumFirst(), 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getNumPassengers()}
     */
    @Test
    public void GetNumPassengers() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        assertEquals(fourSeatsCraft.getNumPassengers(), 4);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getNumPremium()}
     */
    @Test
    public void GetNumPremium() throws Exception {
        testPassenger0 = new Premium(BOOKING_TIME, DEPARTURE_TIME);
        assertEquals(testCraft.getNumPremium(), 0);
        testCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        assertEquals(testCraft.getNumPremium(), 1);
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getPassengers())}
     */
    @Test
    public void GetPassengers() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        List<Passenger> passengers = fourSeatsCraft.getPassengers();

        for(Passenger p: passengers) {
            assertTrue(p instanceof Passenger);
        }
        assertEquals(passengers.size(), 4);
    }

    @Test
    public void GetPassengers_Empty() {
        assertTrue(fourSeatsCraft.getPassengers().isEmpty());
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#getStatus(int))}

     */

    @Test
    public void GetStatus_Empty() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        assertEquals(tempCraft.getStatus(100), "100::0::F:0::J:0::P:0::Y:0\n");
    }

    @Test
    public void GetStatus_Economy() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::1::F:0::J:0::P:0::Y:1|Y:N/Q>C|\n");
    }

    @Test
    public void GetStatus_Premium() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::1::F:0::J:0::P:1::Y:0|P:N/Q>C|\n");
    }

    @Test
    public void GetStatus_Business() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::1::F:0::J:1::P:0::Y:0|J:N/Q>C|\n");
    }

    @Test
    public void GetStatus_First() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::1::F:1::J:0::P:0::Y:0|F:N/Q>C|\n");
    }

    @Test
    public void GetStatus_Full() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::4::F:1::J:1::P:1::Y:1|Y:N/Q>C|" +
                "|P:N/Q>C||J:N/Q>C||F:N/Q>C|\n");
    }

    @Test
    public void GetStatus_Cancel() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        tempCraft.cancelBooking(testPassenger3, CANCELLATION_TIME);
        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::0::F:0::J:0::P:0::Y:0|F:N/Q>C||F:C>N|\n");
    }

    @Test
    public void GetStatus_CancelFullFlight() throws Exception {
        A380 tempCraft = new A380("swag flight", DEPARTURE_TIME);
        tempCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        tempCraft.confirmBooking(testPassenger3, CONFIRM_TIME);
        tempCraft.cancelBooking(testPassenger0, CANCELLATION_TIME);
        tempCraft.cancelBooking(testPassenger1, CANCELLATION_TIME);
        tempCraft.cancelBooking(testPassenger2, CANCELLATION_TIME);
        tempCraft.cancelBooking(testPassenger3, CANCELLATION_TIME);

        assertEquals(tempCraft.getStatus(BOOKING_TIME),"100::0::F:0::J:0::P:0::Y:0" +
                "|Y:N/Q>C||P:N/Q>C||J:N/Q>C||F:N/Q>C|" +
                "|Y:C>N||P:C>N||J:C>N||F:C>N|\n");
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#hasPassenger(Passenger)}
     */
    @Test
    public void HasPassenger() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertTrue(fourSeatsCraft.hasPassenger(testPassenger0));
    }

    @Test
    public void HasPassenger_NoPassenger() throws Exception {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        assertFalse(fourSeatsCraft.hasPassenger(testPassenger3));
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#seatsAvailable(Passenger)}
     */
    @Test
    public void SeatsAvailable() throws Exception {
        assertTrue(testCraft.seatsAvailable(testPassenger0));
        assertTrue(testCraft.seatsAvailable(testPassenger1));
        assertTrue(testCraft.seatsAvailable(testPassenger2));
        assertTrue(testCraft.seatsAvailable(testPassenger3));
    }

    @Test
    public void SeatsAvailable_Full() throws AircraftException, PassengerException {
        fourSeatsCraft.confirmBooking(testPassenger0, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger1, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger2, CONFIRM_TIME);
        fourSeatsCraft.confirmBooking(testPassenger3, CONFIRM_TIME);

        assertFalse(fourSeatsCraft.seatsAvailable(new Economy(BOOKING_TIME, DEPARTURE_TIME)));
        assertFalse(fourSeatsCraft.seatsAvailable(new Business(BOOKING_TIME, DEPARTURE_TIME)));
        assertFalse(fourSeatsCraft.seatsAvailable(new Premium(BOOKING_TIME, DEPARTURE_TIME)));
        assertFalse(fourSeatsCraft.seatsAvailable(new First(BOOKING_TIME, DEPARTURE_TIME)));
    }

    /**
     * Test method for {@link asgn2Aircraft.A380#upgradeBookings()}
     */
    @Test
    public void UpgradeBookings_SmallCase() throws AircraftException, PassengerException {
        // Setup test aircraft: 1F 4J 10P 20Y
        // J -> F: 5F 0J 10P 20Y
        // P -> J: 5F 10J 0P 20Y
        // Y -> P: 5F 10J 15P 5Y

        int initialF = 1;
        int initialJ = 4;
        int initialP = 10;
        int initialY = 20;

        for (int i = 0; i < initialF; i++) {
            upgradeTestCraft.confirmBooking(firstList.get(i), CONFIRM_TIME);
        }

        for (int i = 0; i < initialJ; i++) {
            upgradeTestCraft.confirmBooking(businessList.get(i), CONFIRM_TIME);
        }

        for (int i = 0; i < initialP; i++) {
            upgradeTestCraft.confirmBooking(premiumList.get(i), CONFIRM_TIME);
        }

        for (int i = 0; i < initialY; i++) {
            upgradeTestCraft.confirmBooking(economyList.get(i), CONFIRM_TIME);
        }

        assertEquals(upgradeTestCraft.getNumFirst(), initialF);
        assertEquals(upgradeTestCraft.getNumBusiness(), initialJ);
        assertEquals(upgradeTestCraft.getNumPremium(), initialP);
        assertEquals(upgradeTestCraft.getNumEonomy(), initialY);

        upgradeTestCraft.upgradeBookings();

        assertEquals(upgradeTestCraft.getNumFirst(), 5);
        assertEquals(upgradeTestCraft.getNumBusiness(), 10);
        assertEquals(upgradeTestCraft.getNumPremium(), 15);
        assertEquals(upgradeTestCraft.getNumEonomy(), 5);

//        System.out.println(upgradeTestCraft.getNumEonomy());
//        System.out.println(upgradeTestCraft.getNumPremium());
//        System.out.println(upgradeTestCraft.getNumBusiness());
//        System.out.println(upgradeTestCraft.getNumFirst());
    }

    @Test
    public void UpgradeBookings_AirCraftFull() throws AircraftException, PassengerException {
        // Setup test aircraft: 5F 10J 15P 20Y


        for (int i = 0; i < firstPassengers; i++) {
            upgradeTestCraft.confirmBooking(firstList.get(i), CONFIRM_TIME);
        }
        for (int i = 0; i < businessPassengers; i++) {
            upgradeTestCraft.confirmBooking(businessList.get(i), CONFIRM_TIME);

        }
        for (int i = 0; i < premiumPassengers; i++) {
            upgradeTestCraft.confirmBooking(premiumList.get(i), CONFIRM_TIME);

        }
        for (int i = 0; i < economyPassengers; i++) {
            upgradeTestCraft.confirmBooking(economyList.get(i), CONFIRM_TIME);
        }

        upgradeTestCraft.upgradeBookings();

        assertEquals(upgradeTestCraft.getNumFirst(), firstPassengers);
        assertEquals(upgradeTestCraft.getNumBusiness(), businessPassengers);
        assertEquals(upgradeTestCraft.getNumPremium(), premiumPassengers);
        assertEquals(upgradeTestCraft.getNumEonomy(), economyPassengers);
    }

    @Test
    public void UpgradeBookings_FirstFullOnly() throws AircraftException, PassengerException {
        // Setup test aircraft: 5F 8J 2P 20Y

        for (int i = 0; i < firstPassengers; i++) {
            upgradeTestCraft.confirmBooking(firstList.get(i), CONFIRM_TIME);
        }
        for (int i = 0; i < 8; i++) {
            upgradeTestCraft.confirmBooking(businessList.get(i), CONFIRM_TIME);

        }
        for (int i = 0; i < 2; i++) {
            upgradeTestCraft.confirmBooking(premiumList.get(i), CONFIRM_TIME);

        }
        for (int i = 0; i < economyPassengers; i++) {
            upgradeTestCraft.confirmBooking(economyList.get(i), CONFIRM_TIME);
        }

        upgradeTestCraft.upgradeBookings();

        assertEquals(upgradeTestCraft.getNumFirst(), firstPassengers);
        assertEquals(upgradeTestCraft.getNumBusiness(), 10);
        assertEquals(upgradeTestCraft.getNumPremium(), 15);
        assertEquals(upgradeTestCraft.getNumEonomy(), 5);
    }

}