package asgn2Tests;

import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FirstTests {
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
        pNew = new First(BOOKING_TIME, DEPARTURE_TIME);
        pConfirmed = new First(BOOKING_TIME, DEPARTURE_TIME);
        pRefused = new First(BOOKING_TIME, DEPARTURE_TIME);
        pQueued = new First(BOOKING_TIME, DEPARTURE_TIME);
        pFlown = new First(BOOKING_TIME, DEPARTURE_TIME);

        // set states
        pConfirmed.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        pRefused.refusePassenger(REFUSED_TIME);
        pQueued.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
        pFlown.confirmSeat(CONFIRM_TIME,DEPARTURE_TIME);
        pFlown.flyPassenger(DEPARTURE_TIME);
    }

    /**
     * Test method for {@link asgn2Passengers.First#First(int, int)}
     */
    @Test(expected = PassengerException.class)
    public void FirstConstructor_BookingTimeLessZero() throws PassengerException {
         test = new First(-1, 100);
    }

    @Test(expected = PassengerException.class)
    public void FirstConstructor_DepartureTimeZero() throws PassengerException {
        test = new First(1, 0);
    }

    @Test(expected = PassengerException.class)
    public void FirstConstructor_DepartureTimeLessZero() throws PassengerException {
        test = new First(1, -1);
    }

    @Test(expected = PassengerException.class)
    public void FirstConstructor_BookingAndDeparture() throws PassengerException {
        test = new First(-1, 0);
    }

    @Test(expected = PassengerException.class)
    public void FirstConstructor_DepartureLessBooking() throws PassengerException {
        test = new First(600, 200);
    }

    @Test
    public void FirstConstructor_DepartureEqualBooking() throws PassengerException {
        test = new First(600, 600);
    }

    @Test
    public void FirstConstructor_BookingTimeZero() throws PassengerException {
        test = new First(0, 100);
    }

    @Test
    public void FirstConstructor_CorrectVariables() throws PassengerException {
        test = new First(830, 1630);
        assertEquals(test.getBookingTime(),830);
        assertEquals(test.getDepartureTime(), 1630);
        assertTrue(test.isNew());
        assertFalse(test.isConfirmed());
        assertFalse(test.isFlown());
        assertFalse(test.isQueued());
        assertFalse(test.isRefused());
    }


    /**
     * Test method for {@link asgn2Passengers.First#cancelSeat(int)}
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

    @Test
    public void CancelSeat_CancellationTimeZero() throws PassengerException {
        pConfirmed.cancelSeat(0);
    }

    /**
     * Test method for {@link asgn2Passengers.First#confirmSeat(int, int)}
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
    public void ConfirmSeat_NewDepartureTimeLessConfirmTime() throws PassengerException {
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
        assertEquals(pNew.getConfirmationTime(), CONFIRM_TIME);
        assertEquals(pNew.getDepartureTime(), DEPARTURE_TIME);
    }

    @Test
    public void ConfirmSeat_Queued() throws PassengerException {
        pQueued.confirmSeat(QUEUED_TIME, DEPARTURE_TIME);
        assertTrue(pQueued.isConfirmed());
        assertFalse(pQueued.isNew());
        assertFalse(pQueued.isQueued());
        assertFalse(pQueued.isFlown());
        assertFalse(pQueued.isRefused());
        assertEquals(pQueued.getConfirmationTime(), QUEUED_TIME);
        assertEquals(pQueued.getDepartureTime(), DEPARTURE_TIME);
    }

    @Test
    public void ConfirmSeat_NewConfirmTimeEqualDepartureTime() throws PassengerException {
        pNew.confirmSeat(DEPARTURE_TIME, DEPARTURE_TIME);
        assertFalse(pNew.isNew());
        assertTrue(pNew.isConfirmed());
        assertFalse(pNew.isQueued());
        assertFalse(pNew.isFlown());
        assertFalse(pNew.isRefused());
        assertEquals(pNew.getConfirmationTime(), DEPARTURE_TIME);
        assertEquals(pNew.getDepartureTime(), DEPARTURE_TIME);
    }

    /**
     * Test method for {@link asgn2Passengers.First#flyPassenger(int)}
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
     * Test method for {@link asgn2Passengers.First#queuePassenger(int, int)}
     */
    @Test
    public void QueuePassenger_New() throws PassengerException {
        pNew.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_Confirmed() throws PassengerException {
        pConfirmed.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_Refused() throws PassengerException {
        pRefused.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_Queued() throws PassengerException {
        pQueued.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_Flown() throws PassengerException {
        pFlown.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_QueueTimeLessZero() throws PassengerException {
        pNew.queuePassenger(-1, DEPARTURE_TIME);
    }

    @Test
    public void QueuePassenger_QueueTimeZero() throws PassengerException {
        pNew.queuePassenger(0, DEPARTURE_TIME);
        assertTrue(pNew.isQueued());
        assertFalse(pNew.isNew());
    }

    @Test(expected = PassengerException.class)
    public void QueuePassenger_DepartureTimeLessQueueTime() throws PassengerException {
        pNew.queuePassenger(DEPARTURE_TIME, QUEUED_TIME);
    }

    @Test
    public void QueuePassenger_DepartureTimeEqualQueueTime() throws PassengerException {
        pNew.queuePassenger(DEPARTURE_TIME, DEPARTURE_TIME);
        assertTrue(pNew.isQueued());
        assertFalse(pNew.isNew());
    }

    /**
     * Test method for {@link asgn2Passengers.First#refusePassenger(int)}
     */
    @Test(expected = PassengerException.class)
    public void RefusePassenger_Confirmed() throws PassengerException {
        pConfirmed.refusePassenger(REFUSED_TIME);
    }

    @Test(expected = PassengerException.class)
    public void RefusePassenger_Refused() throws PassengerException {
        pRefused.refusePassenger(REFUSED_TIME);
    }

    @Test(expected = PassengerException.class)
    public void RefusePassenger_Flown() throws PassengerException {
        pFlown.refusePassenger(REFUSED_TIME);
    }

    @Test(expected = PassengerException.class)
    public void RefusePassenger_RefusedTimeLessZero() throws PassengerException {
        pNew.refusePassenger(-1);
    }

    @Test(expected = PassengerException.class)
    public void RefusePassenger_RefusedTimeLessBookingTime() throws PassengerException {
        pNew.refusePassenger(BOOKING_TIME - 1);
    }

    @Test
    public void RefusePassenger_New() throws PassengerException {
        pNew.refusePassenger(REFUSED_TIME);
        assertTrue(pNew.isRefused());
    }

    @Test
    public void RefusePassenger_RefusedTimeZero() throws PassengerException {
        test = new First(0, 200);
        test.refusePassenger(0);
        assertTrue(test.isRefused());
    }

    @Test
    public void RefusePassenger_Queued() throws PassengerException {
        pQueued.refusePassenger(REFUSED_TIME);
        assertFalse(pQueued.isQueued());
        assertTrue(pQueued.isRefused());
        assertFalse(pQueued.isNew());
    }

    @Test
    public void RefusePassenger_RefusedTimeEqualBookingTime() throws PassengerException {
        pQueued.refusePassenger(BOOKING_TIME);
        assertTrue(pQueued.isRefused());
    }

    /**
     * Test method for {@link asgn2Passengers.First#isConfirmed()}
     */
    @Test
    public void IsConfirmed_Confirmed() {
        assertTrue(pConfirmed.isConfirmed());
    }

    @Test
    public void IsConfirmed_NewToConfirmed() throws PassengerException {
        pNew.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        assertTrue(pNew.isConfirmed());
        assertFalse(pNew.isFlown());
        assertFalse(pNew.isQueued());
        assertFalse(pNew.isRefused());
        assertFalse(pNew.isNew());
    }

    @Test
    public void IsConfirmed_NotConfirmed() {
        assertFalse(pNew.isConfirmed());
        assertFalse(pFlown.isConfirmed());
        assertFalse(pQueued.isConfirmed());
        assertFalse(pRefused.isConfirmed());
    }

    /**
     * Test method for {@link asgn2Passengers.First#isFlown()}
     */
    @Test
    public void IsFlown_Flown() {
        assertTrue(pFlown.isFlown());
    }

    @Test
    public void IsFlown_ConfirmedToFlown() throws PassengerException {
        pConfirmed.flyPassenger(DEPARTURE_TIME);
        assertTrue(pConfirmed.isFlown());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isQueued());
        assertFalse(pConfirmed.isRefused());
        assertFalse(pConfirmed.isNew());
    }

    @Test
    public void IsFlown_NotFlown() {
        assertFalse(pNew.isFlown());
        assertFalse(pConfirmed.isFlown());
        assertFalse(pQueued.isFlown());
        assertFalse(pRefused.isFlown());
    }

    /**
     * Test method for {@link asgn2Passengers.First#isNew()}
     */
    @Test
    public void IsNew_New() {
        assertTrue(pNew.isNew());
        assertFalse(pNew.isConfirmed());
        assertFalse(pNew.isQueued());
        assertFalse(pNew.isRefused());
        assertFalse(pNew.isFlown());
    }

    @Test
    public void IsNew_NotNew() {
        assertFalse(pFlown.isNew());
        assertFalse(pConfirmed.isNew());
        assertFalse(pQueued.isNew());
        assertFalse(pRefused.isNew());
    }

    @Test
    public void IsNew_ConfirmedToNew() throws PassengerException {
        pConfirmed.cancelSeat(CONFIRM_TIME + 50);
        assertTrue(pConfirmed.isNew());
        assertFalse(pConfirmed.isConfirmed());
        assertFalse(pConfirmed.isQueued());
        assertFalse(pConfirmed.isRefused());
        assertFalse(pConfirmed.isFlown());
    }



    /**
     * Test method for {@link asgn2Passengers.First#isQueued()}
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

    @Test
    public void IsQueued_NewToQueued() throws PassengerException {
        pNew.queuePassenger(QUEUED_TIME, DEPARTURE_TIME);
        assertTrue(pNew.isQueued());
        assertFalse(pNew.isConfirmed());
        assertFalse(pNew.isNew());
        assertFalse(pNew.isRefused());
        assertFalse(pNew.isFlown());
    }

    /**
     * Test method for {@link asgn2Passengers.First#isRefused()}
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

    @Test
    public void IsRefused_NewToRefused() throws PassengerException {
        pNew.refusePassenger(REFUSED_TIME);
        assertTrue(pNew.isRefused());
        assertFalse(pNew.isConfirmed());
        assertFalse(pNew.isNew());
        assertFalse(pNew.isQueued());
        assertFalse(pNew.isFlown());
    }

    /**
     * Test method for {@link asgn2Passengers.First#wasConfirmed()}
     */
    @Test
    public void WasConfirmed_Confirmed() {
        assertTrue(pConfirmed.wasConfirmed());
        assertTrue(pFlown.wasConfirmed());
    }

    @Test
    public void WasConfirmed_NotConfirmed() {
        assertFalse(pNew.wasConfirmed());
        assertFalse(pQueued.wasConfirmed());
        assertFalse(pRefused.wasConfirmed());
    }

    @Test
    public void WasConfirmed_ConfirmedToFlown() throws PassengerException {
        pConfirmed.flyPassenger(DEPARTURE_TIME);
        assertTrue(pConfirmed.wasConfirmed());
    }

    @Test
    public void WasConfirmed_ConfirmedToNew() throws PassengerException {
        pConfirmed.cancelSeat(DEPARTURE_TIME);
        assertTrue(pConfirmed.wasConfirmed());
    }

    /**
     * Test method for {@link asgn2Passengers.First#wasQueued()}
     */
    @Test
    public void WasQueued_Queued() {
        assertTrue(pQueued.wasQueued());
    }

    @Test
    public void WasQueued_NeverQueued() {
        assertFalse(pNew.wasQueued());
        assertFalse(pConfirmed.wasQueued());
        assertFalse(pRefused.wasQueued());
        assertFalse(pFlown.wasQueued());
    }

    @Test
    public void WasQueued_QueuedToConfirmed() throws PassengerException {
        pQueued.confirmSeat(CONFIRM_TIME, DEPARTURE_TIME);
        assertTrue(pQueued.wasQueued());
    }

    @Test
    public void WasQueued_QueuedToRefused() throws PassengerException {
        pQueued.refusePassenger(REFUSED_TIME);
        assertTrue(pQueued.wasQueued());
    }

    /**
     * Test method for {@link asgn2Passengers.First#upgrade()}
     */
    @Test
    public void Upgrade_First() {
        Passenger upgradedP = pConfirmed.upgrade();
        assertTrue(upgradedP instanceof First);
    }

    @Test
    public void Upgrade_PassId() {
        Passenger upgradedP = pConfirmed.upgrade();
        System.out.println(upgradedP);
        System.out.println(pConfirmed);
        assertEquals(pConfirmed.getPassID(), upgradedP.getPassID());
    }
}