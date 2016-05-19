/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Aircraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import asgn2Passengers.Business;
import asgn2Passengers.Economy;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import asgn2Passengers.Premium;
import asgn2Simulators.Log;

/**
 * The <code>Aircraft</code> class provides facilities for modelling a commercial jet 
 * aircraft with multiple travel classes. New aircraft types are created by explicitly 
 * extending this class and providing the necessary configuration information. 
 * 
 * In particular, <code>Aircraft</code> maintains a collection of currently booked passengers, 
 * those with a Confirmed seat on the flight. Queueing and Refused bookings are handled by the 
 * main {@link asgn2Simulators.Simulator} class. 
 *   
 * The class maintains a variety of constraints on passengers, bookings and movement 
 * between travel classes, and relies heavily on the asgn2Passengers hierarchy. Reports are 
 * also provided for logging and graphical display. 
 * 
 * @author hogan
 *
 */
public abstract class Aircraft {

	protected int firstCapacity;
	protected int businessCapacity;
	protected int premiumCapacity;
	protected int economyCapacity;
	protected int capacity;
		
	protected int numFirst;
	protected int numBusiness;
	protected int numPremium; 
	protected int numEconomy; 

	protected String flightCode;
	protected String type; 
	protected int departureTime; 
	protected String status;
	protected List<Passenger> seats;

	/**
	 * Constructor sets flight info and the basic size parameters. 
	 * 
	 * @param flightCode <code>String</code> containing flight ID 
	 * @param departureTime <code>int</code> scheduled departure time
	 * @param first <code>int</code> capacity of First Class 
	 * @param business <code>int</code> capacity of Business Class 
	 * @param premium <code>int</code> capacity of Premium Economy Class 
	 * @param economy <code>int</code> capacity of Economy Class 
	 * @throws AircraftException if isNull(flightCode) OR (departureTime <=0) OR ({first,business,premium,economy} <0)
	 */
	public Aircraft(String flightCode,int departureTime, int first, int business, int premium, int economy) throws AircraftException {
        if (flightCode == null) {
            throw new AircraftException("flightCode is null");
        }
        else if (departureTime <= 0) {
            throw new AircraftException("departureTime must be positive");
        }
        else if (first < 0 || business < 0 || premium < 0 || economy < 0) {
            throw new AircraftException("Cannot have a negative flight class capacity");
        }
		this.status = "";
		this.flightCode = flightCode;
        this.departureTime = departureTime;
        this.firstCapacity = first;
        this.businessCapacity = business;
        this.premiumCapacity = premium;
        this.economyCapacity = economy;
        this.capacity = first + business + premium + economy;
	}



    // TODO testing
	/**
	 * Method to remove passenger from the aircraft - passenger must have a confirmed 
	 * seat prior to entry to this method.   
	 *
	 * @param p <code>Passenger</code> to be removed from the aircraft 
	 * @param cancellationTime <code>int</code> time operation performed 
	 * @throws PassengerException if <code>Passenger</code> is not Confirmed OR cancellationTime 
	 * is invalid. See {@link asgn2Passengers.Passenger#cancelSeat(int)}
	 * @throws AircraftException if <code>Passenger</code> is not recorded in aircraft seating 
	 */
	public void cancelBooking(Passenger p, int cancellationTime) throws PassengerException, AircraftException {
        if (!seats.contains(p)) {
            throw new AircraftException("passenger not recorded in seating");
        }
		p.cancelSeat(cancellationTime); // this throws passenger exception

        // Given code
		this.status += Log.setPassengerMsg(p,"C","N");
        // remove passenger from seats,  decrement corresponding passenger counter
        seats.remove(p);
        if (p instanceof Economy) {
            this.numEconomy--;
            seats.remove(p);
        }
        else if (p instanceof Premium) {
            this.numPremium--;
            seats.remove(p);
        }
        else if (p instanceof Business) {
            this.numBusiness--;
            seats.remove(p);
        }
        else {
            this.numFirst--;
            seats.remove(p);
        }
	}

    // TODO testing
	/**
	 * Method to add a Passenger to the aircraft seating. 
	 * Precondition is a test that a seat is available in the required fare class
	 * 
	 * @param p <code>Passenger</code> to be added to the aircraft 
	 * @param confirmationTime <code>int</code> time operation performed 
	 * @throws PassengerException if <code>Passenger</code> is in incorrect state 
	 * OR confirmationTime OR departureTime is invalid. See {@link asgn2Passengers.Passenger#confirmSeat(int, int)}
	 * @throws AircraftException if no seats available in <code>Passenger</code> fare class. 
	 */
	public void confirmBooking(Passenger p,int confirmationTime) throws AircraftException, PassengerException {
        // check available seats first
        if (!seatsAvailable(p)) {
            throw new AircraftException("No seats available");
        }

        // may throw passenger exception
        p.confirmSeat(confirmationTime, this.departureTime);

        this.status += Log.setPassengerMsg(p,"N/Q","C");

        // increment passenger class counter, add to seat
        if (p instanceof Economy) {
            this.numEconomy++;
            seats.add(p);
        }
        else if (p instanceof Premium) {
            this.numPremium++;
            seats.add(p);
        }
        else if (p instanceof Business) {
            this.numBusiness++;
            seats.add(p);
        }
        else {
            this.numFirst++;
            seats.add(p);
        }
	}


	/**
	 * State dump intended for use in logging the final state of the aircraft. (Supplied) 
	 * 
	 * @return <code>String</code> containing dump of final aircraft state 
	 */
	public String finalState() {
		String str = aircraftIDString() + " Pass: " + this.seats.size() + "\n";
		for (Passenger p : this.seats) {
			str += p.toString() + "\n";
		}
		return str + "\n";
	}
	
	/**
	 * Simple status showing whether aircraft is empty
	 * 
	 * @return <code>boolean</code> true if aircraft empty; false otherwise 
	 */
	public boolean flightEmpty() {
		return seats.isEmpty();
	}
	
	/**
	 * Simple status showing whether aircraft is full
	 * 
	 * @return <code>boolean</code> true if aircraft full; false otherwise 
	 */
	public boolean flightFull() {
		return this.capacity == seats.size();
	}

    // TODO needs a testing
	/**
	 * Method to finalise the aircraft seating on departure. 
	 * Effect is to change the state of each passenger to Flown. 
	 * departureTime parameter allows for rescheduling 
	 * 
	 * @param departureTime <code>int</code> actual departureTime from simulation  
	 * @throws PassengerException if <code>Passenger</code> is in incorrect state 
	 * See {@link asgn2Passengers.Passenger#flyPassenger(int)}. 
	 */
	public void flyPassengers(int departureTime) throws PassengerException {
        for (Passenger p: this.seats) {
            p.flyPassenger(departureTime);
        }
    }

    // TODO needs testing
	/**
	 * Method to return an {@link asgn2Aircraft.Bookings} object containing the Confirmed 
	 * booking status for this aircraft. 
	 * 
	 * @return <code>Bookings</code> object containing the status.  
	 */
	public Bookings getBookings() {
        int remainingCapacity = this.capacity - seats.size();
		Bookings status = new Bookings(this.numFirst, this.numBusiness, this.numPremium, this.numEconomy,
                this.capacity, remainingCapacity);
        return status;
	}
	
	/**
	 * Simple getter for number of confirmed Business Class passengers
	 * 
	 * @return <code>int</code> number of Business Class passengers 
	 */
	public int getNumBusiness() {
		return this.numBusiness;
	}
	
	
	/**
	 * Simple getter for number of confirmed Economy passengers
	 * 
	 * @return <code>int</code> number of Economy Class passengers 
	 */
	public int getNumEonomy() {
		return this.numEconomy;
	}

	/**
	 * Simple getter for number of confirmed First Class passengers
	 * 
	 * @return <code>int</code> number of First Class passengers 
	 */
	public int getNumFirst() {
		return this.numFirst;
	}

	/**
	 * Simple getter for the total number of confirmed passengers 
	 * 
	 * @return <code>int</code> number of Confirmed passengers 
	 */
	public int getNumPassengers() {
		return seats.size();
	}
	
	/**
	 * Simple getter for number of confirmed Premium Economy passengers
	 * 
	 * @return <code>int</code> number of Premium Economy Class passengers
	 */
	public int getNumPremium() {
		return this.numPremium;
	}
	
	/**
	 * Method to return an {@link java.util.List} object containing a copy of 
	 * the list of passengers on this aircraft. 
	 * 
	 * @return <code>List<Passenger></code> object containing the passengers.  
	 */
	public List<Passenger> getPassengers() {
		return seats;
	}
	
	/**
	 * Method used to provide the current status of the aircraft for logging. (Supplied) 
	 * Uses private status <code>String</code>, set whenever a transition occurs. 
	 *  
	 * @return <code>String</code> containing current aircraft state 
	 */
	public String getStatus(int time) {
		String str = time +"::"
		+ this.seats.size() + "::"
		+ "F:" + this.numFirst + "::J:" + this.numBusiness 
		+ "::P:" + this.numPremium + "::Y:" + this.numEconomy; 
		str += this.status;
		this.status="";
		return str+"\n";
	}

    // TODO testing
	/**
	 * Simple boolean to check whether a passenger is included on the aircraft 
	 * 
	 * @param p <code>Passenger</code> whose presence we are checking
	 * @return <code>boolean</code> true if isConfirmed(p); false otherwise 
	 */
	public boolean hasPassenger(Passenger p) {
		return seats.contains(p);
	}

	/**
	 * State dump intended for logging the aircraft parameters (Supplied) 
	 * 
	 * @return <code>String</code> containing dump of initial aircraft parameters 
	 */ 
	public String initialState() {
		return aircraftIDString() + " Capacity: " + this.capacity 
				+ " [F: " 
				+ this.firstCapacity + " J: " + this.businessCapacity 
				+ " P: " + this.premiumCapacity + " Y: " + this.economyCapacity
				+ "]";
	}
	
	/**
	 * Given a Passenger, method determines whether there are seats available in that 
	 * fare class. 
	 *   
	 * @param p <code>Passenger</code> to be Confirmed
	 * @return <code>boolean</code> true if seats in Class(p); false otherwise
	 */
	public boolean seatsAvailable(Passenger p) {
        if (p instanceof Economy) {
            return numEconomy < economyCapacity;
        }
        else if (p instanceof Premium) {
            return numPremium < premiumCapacity;
        }
        else if (p instanceof Business) {
            return numBusiness < businessCapacity;
        }
        else {
            return numFirst < firstCapacity;
        }
	}

	/* 
	 * (non-Javadoc) (Supplied) 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return aircraftIDString() + " Count: " + this.seats.size() 
				+ " [F: " + numFirst
				+ " J: " + numBusiness 
				+ " P: " + numPremium 
				+ " Y: " + numEconomy 
			    + "]";
	}


	/**
	 * Method to upgrade Passengers to try to fill the aircraft seating. 
	 * Called at departureTime. Works through the aircraft fare classes in 
	 * descending order of status. No upgrades are possible from First, so 
	 * we consider Business passengers (upgrading if there is space in First), 
	 * then Premium, upgrading to fill spaces already available and those created 
	 * by upgrades to First), and then finally, we do the same for Economy, upgrading 
	 * where possible to Premium.  
	 * 
	 * @throws PassengerException if <code>Passenger</code> is in incorrect state 
	 * See {@link asgn2Passengers.Passenger#upgrade()}
	 */
	public void upgradeBookings() throws PassengerException { 
		// use java streams??

        // business to first
        seats.stream()
                .filter(p -> p instanceof Business)
                .forEach(p -> {
                    if(firstAvailable()) {
                        try {
                            upgradePassenger(p);
                        } catch (PassengerException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        // First class should now be populated via business upgrades
        // if it is not, (there weren't enough business class passengers)
        // Fill first class from the queue
        // perform this intermediate step after each stream is filled
        // this is where passenger exceptions can be thrown

        // premium to business
        seats.stream()
                .filter(p -> p instanceof Premium)
                .forEach(p -> {
                    if(premiumAvailable()) {
                        try {
                            upgradePassenger(p);
                        } catch (PassengerException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        // Economy to Premium
        seats.stream()
                .filter(p -> p instanceof Economy)
                .forEach(p -> {
                    if(economyAvailable()) {
                        try {
                            upgradePassenger(p);
                        } catch (PassengerException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        // Fill economy seats via the Queue
        // this is where passenger exceptions can be thrown
	}

	/**
	 * Simple String method for the Aircraft ID 
	 * 
	 * @return <code>String</code> containing the Aircraft ID 
	 */
	private String aircraftIDString() {
		return this.type + ":" + this.flightCode + ":" + this.departureTime;
	}


	//Various private helper methods to check arguments and throw exceptions, to increment 
	//or decrement counts based on the class of the Passenger, and to get the number of seats 
	//available in a particular class


	//Used in the exception thrown when we can't confirm a passenger 
	/** 
	 * Helper method with error messages for failed bookings
	 * @param p Passenger seeking a confirmed seat
	 * @return msg string failure reason 
	 */
	private String noSeatsAvailableMsg(Passenger p) {
		String msg = "";
		return msg + p.noSeatsMsg(); 
	}

    // TODO test
    /**
     * Gets the class of the passenger
     * @param p
     * @return class String Y F J P etc
     */
    private char passengerType(Passenger p) {
        String passId = p.getPassID();
        char passType = passId.charAt(0);
        return passType;
    }

    private boolean economyAvailable() {
        return this.numEconomy < this.economyCapacity;
    }

    private boolean premiumAvailable() {
        return this.numPremium < this.premiumCapacity;
    }

    private boolean businessAvailable() {
        return this.numBusiness < this.businessCapacity;
    }

    private boolean firstAvailable() {
        return this.numFirst < this.firstCapacity;
    }

    /**
     * Given a passenger, upgrade it to the next class
     * WARNING - Does not check anything, just does it
     * @param p
     */
    private void upgradePassenger(Passenger p) throws PassengerException {
        Passenger upgradedP = p.upgrade(); // Throws exception?

        if (upgradedP instanceof First) {
            this.numFirst++;
            this.numBusiness--;
            seats.remove(p);
            seats.add(upgradedP);
        }
        else if (upgradedP instanceof Business) {
            this.numBusiness++;
            this.numPremium--;
            seats.remove(p);
            seats.add(upgradedP);
        }
        else if (upgradedP instanceof Premium) {
            this.numPremium++;
            this.numEconomy--;
            seats.remove(p);
            seats.add(upgradedP);
        }
    }

}
