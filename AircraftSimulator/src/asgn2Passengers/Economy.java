/**
 *
 * This file is part of the AircraftSimulator Project, written as
 * part of the assessment for CAB302, Semester 1, 2016.
 *
 */
package asgn2Passengers;

/**
 * @author hogan
 *
 */
public class Economy extends Passenger {

	/**
	 * Economy Constructor (Partially Supplied)
	 * Passenger is created in New state, later given a Confirmed Economy Class reservation, 
	 * Queued, or Refused booking if waiting list is full. 
	 * 
	 * @param bookingTime <code>int</code> day of the original booking. 
	 * @param departureTime <code>int</code> day of the intended flight.  
	 * @throws PassengerException if invalid bookingTime or departureTime 
	 * @see asgnPassengers.Passenger#Passenger(int,int)
	 */
	public Economy(int bookingTime,int departureTime) throws PassengerException {
		//TODO was it just this below?
		super(bookingTime, departureTime);
		this.passID = "Y:" + this.passID;
	}
	
	@Override
	public String noSeatsMsg() {
		return "No seats available in Economy";
	}

	@Override
	public Passenger upgrade() {
		Passenger tempPremium = new Premium();
		this.copyPassengerState(tempPremium);
		return tempPremium;
	}
}
