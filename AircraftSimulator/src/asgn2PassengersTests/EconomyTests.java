package asgn2PassengersTests;

import asgn2Passengers.Economy;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EconomyTests {
    /**
     * Test Variables
     */
    private Passenger test;
    private Passenger pNew;
    private Passenger pConfirmed;
    private Passenger pRefused;
    private Passenger pQueued;
    private Passenger pFlown;

    private static final int BOOKING_TIME = 100;
    private static final int DEPARTURE_TIME = 600;
    private static final int CONFIRM_TIME = 200;
    private static final int REFUSED_TIME = 300;
    private static final int QUEUED_TIME = 300;
    private static final int FLOWN_TIME = 600;




    /**
     * Set up
     */
    @Before
    public void setUp() throws PassengerException {
        pNew = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        pConfirmed = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        pRefused = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        pQueued = new Economy(BOOKING_TIME, DEPARTURE_TIME);
        pFlown = new Economy(BOOKING_TIME, DEPARTURE_TIME);

        // set states
        pConfirmed.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        pRefused.refusePassenger(REFUSED_TIME);
        pQueued.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
        pFlown.flyPassenger(DEPARTURE_TIME);
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#Economy(int, int)}
     */
    @Test(expected = PassengerException.class)
    public void EconomyConstructor_BookingTimeLessZero() throws PassengerException {
         test = new Economy(-1, 100);
    }

    @Test(expected = PassengerException.class)
    public void EconomyConstructor_DepartureTimeZero() throws PassengerException {
        test = new Economy(1, 0);
    }

    @Test(expected = PassengerException.class)
    public void EconomyConstructor_DepartureTimeLessZero() throws PassengerException {
        test = new Economy(1, -1);
    }

    @Test(expected = PassengerException.class)
    public void EconomyConstructor_BookingAndDeparture() throws PassengerException {
        test = new Economy(-1, 0);
    }

    @Test(expected = PassengerException.class)
    public void EconomyConstructor_DepartureLessBooking() throws PassengerException {
        test = new Economy(600, 200);
    }

    @Test(expected = PassengerException.class)
    public void EconomyConstructor_DepartureEqualBooking() throws PassengerException {
        test = new Economy(600, 600);
    }

    @Test
    public void EconomyConstructor_CorrectVariables() throws PassengerException {
        test = new Economy(830, 1630);
        assertEquals(test.getBookingTime(),830);
        assertEquals(test.getDepartureTime(), 1630);
        assertTrue(test.isNew());
        assertFalse(test.isConfirmed());
        assertFalse(test.isFlown());
        assertFalse(test.isQueued());
        assertFalse(test.isRefused());
    }

    //
    /**
     * Test method for {@link asgn2Passengers.Economy#cancelSeat(int)}
     */
    @Test(expected = PassengerException.class)
    public void CancelSeat_New() throws PassengerException {
        pNew.cancelSeat(CONFIRM_TIME);
    }

    @Test(expected = PassengerException.class)
    public void CancelSeat_Refused() throws PassengerException {
        pRefused.cancelSeat(REFUSED_TIME);
    }

    @Test(expected = PassengerException.class)
    public void CancelSeat_Queued() throws PassengerException {
        pQueued.cancelSeat(QUEUED_TIME);
    }

    @Test(expected = PassengerException.class)
    public void CancelSeat_Flown() throws PassengerException {
        pFlown.cancelSeat(FLOWN_TIME);
    }

    @Test(expected = PassengerException.class)
    public void CancelSeat_CancellationTimeLessZero() throws PassengerException {
        pConfirmed.cancelSeat(-1);
    }

    @Test(expected = PassengerException.class)
    public void CancelSeat_DepartureTimeLessCancellationTime() throws PassengerException {
        int cancellationTime = DEPARTURE_TIME + 1;
        pConfirmed.cancelSeat(cancellationTime);
    }

    @Test
    public void CancelSeat_CancellationTimeEqualConfirmTime() throws PassengerException {
        int cancellationTime = CONFIRM_TIME;
        pConfirmed.cancelSeat(cancellationTime);
        assertTrue(pConfirmed.isQueued());
        assertFalse(pConfirmed.isNew());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pConfirmed.isRefused());
    }

    @Test
    public void CancelSeat_CancellationTimeBeforeDepartureTime() throws PassengerException {
        int cancellationTime = DEPARTURE_TIME - 100;
        pConfirmed.cancelSeat(cancellationTime);
        assertTrue(pConfirmed.isQueued());
        assertFalse(pConfirmed.isNew());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pConfirmed.isRefused());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#confirmSeat(int, int)}
     */
    









    











}