/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import asgn2Aircraft.AircraftException;
import asgn2Passengers.PassengerException;
import javax.swing.*;

/**
 * Class to operate the simulation, taking parameters and utility methods from the Simulator
 * to control the available resources, and using Log to provide a record of operation. 
 * 
 * @author hogan
 *
 */ 
public class SimulationRunner {
	/**
	 * Main program for the simulation 
	 * 
	 * @param args Arguments to the simulation - 
	 * see {@link asgn2Simulators.SimulationRunner#printErrorAndExit()}
	 */
	public static void main(String[] args) {
		final int NUM_ARGS = 10;
        boolean runGui = true;
        String[] newArgs = new String[0];
		Simulator s = null;
		Log l = null;
        try {
			switch (args.length) {
				case 0: {
                    // default case - launch the gui with constants
					s = new Simulator();
					break;
				}
                case 1: {
                    // 1 arguement - set gui flat and launch with constants
                    if (args[0].equals("nogui")) {
                        runGui = false;
                    } else if (args[0].equals("gui")) {
                        runGui = true;
                    } else {
                        printErrorAndExit();
                    }
                    s = new Simulator();
                    break;
                }
                case NUM_ARGS: {
                    // 10 arguments - set gui flag and launch simulator with args
                    String optionGui = args[0];
                    if (optionGui.equals("nogui")) {
                        runGui = false;
                    } else if (args[0].equals("gui")) {
                        runGui = true;
                    } else {
                        printErrorAndExit();
                    }

                    // remove guiFlag from args
                    List<String> parsedArgs = new LinkedList<>();
                    for (String arg: args) {
                        if(!arg.equals("gui") && !arg.equals("nogui")) {
                            parsedArgs.add(arg);
                        }

                    }
                    // pass newArgs to create simulator using args
                    newArgs = parsedArgs.toArray(args);
                    s = createSimulatorUsingArgs(newArgs);
                    break;
                }
				default: {
					printErrorAndExit();
				}
			}

            // set up a log object
			l = new Log();

		} catch (SimulationException | IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}

        // Error checking, ensure a simulator is instantiated
        if (s == null) {
            System.out.println("simulator not instantiated");
            System.exit(-1);
        }

		//Run the simulation
		SimulationRunner sr = new SimulationRunner(s,l);
		try {
            if (runGui) {
                System.out.println("Run gui with given settings");
                SwingUtilities.invokeLater(new GUISimulator("Aircraft Simulator", newArgs));
            } else if(!runGui) {
                // don't run the gui
                System.out.println("No Gui");
                sr.runSimulation(null);
            } else {
                System.out.println("Run Gui with default settings");
                SwingUtilities.invokeLater(new GUISimulator("Aircraft Simulator", null));
            }
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Helper to process args for Simulator  
	 * 
	 * @param args Command line arguments (see usage message) 
	 * @return new <code>Simulator</code> from the arguments 
	 * @throws SimulationException if invalid arguments. 
	 * See {@link asgn2Simulators.Simulator#Simulator(int, int, double, double, double, double, double, double, double)}
	 */
	private static Simulator createSimulatorUsingArgs(String[] args) throws SimulationException {
		int seed = Integer.parseInt(args[0]);
		int maxQueueSize = Integer.parseInt(args[1]);
		double meanBookings = Double.parseDouble(args[2]);
		double sdBookings = Double.parseDouble(args[3]);
		double firstProb = Double.parseDouble(args[4]);
		double businessProb = Double.parseDouble(args[5]);
		double premiumProb = Double.parseDouble(args[6]);
		double economyProb = Double.parseDouble(args[7]);
		double cancelProb = Double.parseDouble(args[8]);
		return new Simulator(seed,maxQueueSize,meanBookings,sdBookings,firstProb,businessProb,
				premiumProb,economyProb,cancelProb);
	}

	/**
	 *  Helper to generate usage message 
	 */
	private static void printErrorAndExit() {
		String str = "Usage: java asgn2Simulators.SimulationRunner [SIM Args]\n";
		str += "SIM Args: seed maxQueueSize meanBookings sdBookings "; 
		str += "firstProb businessProb premiumProb economyProb cancelProb guiSelect\n";
		str += "If no arguments, default values are used\n";
        str += "first arguement must be gui or nogui";
		System.err.println(str);
		System.exit(-1);
	}
	
	
	private Simulator sim;
	private Log log; 

	/**
	 * Constructor just does initialisation 
	 * 
	 * @param sim <code>Simulator</code> containing simulation parameters
	 * @param log <code>Log</code> object supporting record of operation 
	 */
	public SimulationRunner(Simulator sim, Log log) {
		this.sim = sim;
		this.log = log;
	}

	/**
	 * Method to run the simulation from start to finish. 
	 * Exceptions are propagated upwards as necessary 
	 * 
	 * @throws AircraftException See methods from {@link asgn2Simulators.Simulator} 
	 * @throws PassengerException See methods from {@link asgn2Simulators.Simulator} 
	 * @throws SimulationException See methods from {@link asgn2Simulators.Simulator} 
	 * @throws IOException on logging failures See methods from {@link asgn2Simulators.Log} 

	 */
	public void runSimulation(GUISimulator gui) throws AircraftException, PassengerException, SimulationException, IOException {
		this.sim.createSchedule();
		this.log.initialEntry(this.sim);

        //TODO -> Gui

        if(gui != null) {
            gui.clearGraphingData();
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String capacities = sim.getFlights(Constants.FIRST_FLIGHT).initialState();
            gui.writeText(timeLog + ": Start of Simulation\n");
            gui.writeText(this.sim.toString() + "\n");
            gui.writeText(capacities);
        }

        //Main simulation loop
		for (int time=0; time<=Constants.DURATION; time++) {
			this.sim.resetStatus(time);
			this.sim.rebookCancelledPassengers(time);
			this.sim.generateAndHandleBookings(time);
			this.sim.processNewCancellations(time);
			if (time >= Constants.FIRST_FLIGHT) {
				this.sim.processUpgrades(time);
				this.sim.processQueue(time);
				this.sim.flyPassengers(time);
				this.sim.updateTotalCounts(time);
				this.log.logFlightEntries(time, sim);
			} else {
				this.sim.processQueue(time);
			}
			//Log progress 
			this.log.logQREntries(time, sim);
			this.log.logEntry(time,this.sim);

            // TODO -> Gui
            if (gui != null) {
                String dailySum = this.sim.getSummary(time, time >= Constants.FIRST_FLIGHT);
                gui.writeText(dailySum);
                //TODO is getTotalFlown what we want

                gui.addDataToChart1(time, this.sim.getTotalEconomy(), this.sim.getTotalPremium(), this.sim.getTotalBusiness(), this.sim.getTotalFirst(), this.sim.getTotalEmpty());
                gui.addDataToChart2(time, this.sim.numInQueue(), this.sim.numRefused());
            }
        }
		this.sim.finaliseQueuedAndCancelledPassengers(Constants.DURATION);
        // TODO stuff from here can go to gui somewhere - text area podcast4: 11.30
        // TODO -> Gui
        if (gui != null) {
            gui.addDataToXYSeriesCollections();
            String endTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String endSimString = "\n" + endTime  + ": End of Simulation\n";
            String finalState = this.sim.finalState();
            gui.writeText(endSimString + finalState);
        }

        this.log.logQREntries(Constants.DURATION, sim); // individual queue refused transitions savestatus method
		this.log.finalise(this.sim);
	}
}
