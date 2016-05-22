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
        pFlown.confirmSeat(CONFIRM_TIME,DEPARTURE_TIME);
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

    @Test
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
        assertTrue(pConfirmed.isNew());
        assertFalse(pConfirmed.isQueued());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pConfirmed.isRefused());
    }

    @Test
    public void CancelSeat_CancellationTimeBeforeDepartureTime() throws PassengerException {
        int cancellationTime = DEPARTURE_TIME - 100;
        pConfirmed.cancelSeat(cancellationTime);
        assertTrue(pConfirmed.isNew());
        assertFalse(pConfirmed.isQueued());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pConfirmed.isRefused());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#confirmSeat(int, int)}
     */
    @Test(expected = PassengerException.class)
    public void ConfirmSeat_Refused() throws PassengerException {
        pRefused.confirmSeat(REFUSED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void ConfirmSeat_Flown() throws PassengerException {
        pFlown.confirmSeat(FLOWN_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void ConfirmSeat_Confirmed() throws PassengerException {
        pConfirmed.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void ConfirmSeat_ConfirmTimeLessZero() throws PassengerException {
        pNew.confirmSeat(-1, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void ConfirmSeat_NewDepartureTimeBeforeConfirmTime() throws PassengerException {
        pNew.confirmSeat(CONFIRM_TIME, CONFIRM_TIME - 1);
    }

    @Test(expected = PassengerException.class)
    public void ConfirmSeat_QueuedDepartureTimeBeforeConfirmTime() throws PassengerException {
        pQueued.confirmSeat(CONFIRM_TIME, CONFIRM_TIME - 1);
    }

    @Test
    public void ConfirmSeat_New() throws PassengerException {
        pNew.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        assertFalse(pNew.isNew());
        assertFalse(pNew.isQueued());
        assertTrue(pNew.isConfirmed());
        assertFalse(pNew.isFlown());
        assertFalse(pNew.isRefused());
    }


    @Test
    public void ConfirmSeat_Queued() throws PassengerException {
        pQueued.confirmSeat(QUEUED_TIME, DEPARTURE_TIME);
        assertTrue(pQueued.isConfirmed());
        assertFalse(pQueued.isNew());
        assertFalse(pQueued.isQueued());
        assertFalse(pQueued.isFlown());
        assertFalse(pQueued.isRefused());
    }

    @Test
    public void ConfirmSeat_NewConfirmTimeEqualDepartureTime() throws PassengerException {
        pNew.confirmSeat(DEPARTURE_TIME, DEPARTURE_TIME);
        assertFalse(pNew.isNew());
        assertTrue(pNew.isConfirmed());
        assertFalse(pNew.isQueued());
        assertFalse(pNew.isFlown());
        assertFalse(pNew.isRefused());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#flyPassenger(int)}
     */
    @Test(expected = PassengerException.class)
    public void FlyPassenger_New() throws PassengerException {
        pNew.flyPassenger(DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void FlyPassenger_Queued() throws PassengerException {
        pQueued.flyPassenger(DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void FlyPassenger_Flown() throws PassengerException {
        pFlown.flyPassenger(DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void FlyPassenger_Refused() throws PassengerException {
        pRefused.flyPassenger(DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void FlyPassenger_DepartureTimeLessZero() throws PassengerException {
        pConfirmed.flyPassenger(-1);
    }

    @Test(expected = PassengerException.class)
    public void FlyPassenger_DepartureTimeZero() throws PassengerException {
        pConfirmed.flyPassenger(0);
    }

    @Test
    public void FlyPassenger_Test() throws PassengerException {
        pConfirmed.flyPassenger(FLOWN_TIME);
        assertTrue(pConfirmed.isFlown());
    }

    //TODO broken tests, what if flown time != departure time
//    @Test
//    public void FlyPassenger_WrongDepartureTimeAhead() throws PassengerException {
//        pConfirmed.flyPassenger(FLOWN_TIME - 100);
////        assertTrue(pConfirmed.isFlown());
//    }
//
//    @Test
//    public void FlyPassenger_WrongDepartureTimeLate() throws PassengerException {
//        pConfirmed.flyPassenger(FLOWN_TIME + 100);
//        System.out.println(pConfirmed.isFlown());
//        System.out.println(pConfirmed.isConfirmed());
//        System.out.println(pConfirmed.isQueued());
//        System.out.println(pConfirmed.isRefused());
//        System.out.println(pConfirmed.isNew());
////        assertTrue(pConfirmed.isFlown());
//    }

    /**
     * Test method for {@link asgn2Passengers.Economy#isConfirmed()}
     */
    @Test
    public void IsConfirmed_Confirmed() {
        assertTrue(pConfirmed.isConfirmed());
    }

    @Test
    public void IsConfirmed_NotConfirmed() {
        assertFalse(pNew.isConfirmed());
        assertFalse(pFlown.isConfirmed());
        assertFalse(pQueued.isConfirmed());
        assertFalse(pRefused.isConfirmed());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#isFlown()}
     */
    @Test
    public void IsFlown_Flown() {
        assertTrue(pFlown.isFlown());
    }

    @Test
    public void IsFlown_NotFlown() {
        assertFalse(pNew.isFlown());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pQueued.isFlown());
        assertFalse(pRefused.isFlown());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#isNew()}
     */
    @Test
    public void IsNew_New() {
        assertTrue(pNew.isNew());
    }

    @Test
    public void IsNew_NotNew() {
        assertFalse(pFlown.isNew());
        assertFalse(pConfirmed.isNew());
        assertFalse(pQueued.isNew());
        assertFalse(pRefused.isNew());
    }


    /**
     * Test method for {@link asgn2Passengers.Economy#isQueued()}
     */
    @Test
    public void IsQueued_Queued() {
        assertTrue(pQueued.isQueued());
    }

    @Test
    public void IsQueued_NotQueued() {
        assertFalse(pFlown.isQueued());
        assertFalse(pConfirmed.isQueued());
        assertFalse(pNew.isQueued());
        assertFalse(pRefused.isQueued());
    }

    /**
     * Test method for {@link asgn2Passengers.Economy#isRefused()}
     */
    @Test
    public void IsRefused_Refused() {
        assertTrue(pRefused.isRefused());
    }

    @Test
    public void IsRefused_NotRefused() {
        assertFalse(pFlown.isRefused());
        assertFalse(pConfirmed.isRefused());
        assertFalse(pNew.isRefused());
        assertFalse(pQueued.isRefused());
    }
























}