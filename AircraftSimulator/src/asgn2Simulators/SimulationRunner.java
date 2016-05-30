/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		final int NUM_ARGS = 9;
        final int NUM_ARGS_GUI_OPTION = 10;
        boolean run_gui = true;

		Simulator s = null;
		Log l = null;
        try {
            // TODO - provide user input here
			switch (args.length) {
				case NUM_ARGS: {
					s = createSimulatorUsingArgs(args);
					break;
				}
                case NUM_ARGS_GUI_OPTION: {
                    String optionGui = args[NUM_ARGS_GUI_OPTION - 1];
                    if (optionGui.equals("0")) {
                        run_gui = false;
                    }
                }
				case 0: {
                    // TODO, default launch GUI
					s = new Simulator();
					break;
				}
				default: {
					printErrorAndExit();
				}
			}
			l = new Log();
		} catch (SimulationException | IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}

		//Run the simulation 
		SimulationRunner sr = new SimulationRunner(s,l);
		try {
            if (run_gui) {
                System.out.println("Run gui with given settings");
                SwingUtilities.invokeLater(new GUISimulator("Aircraft Simulator", args));
            } else if(!run_gui) {
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
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String capacities = sim.getFlights(Constants.FIRST_FLIGHT).initialState();
            System.out.println(timeLog);
            System.out.println(this.sim.toString());
            System.out.println(capacities);
            gui.writeText(timeLog);
            gui.writeText(this.sim.toString());
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
                System.out.println(dailySum);
                gui.writeText(dailySum);
            }
        }
		this.sim.finaliseQueuedAndCancelledPassengers(Constants.DURATION);
        // TODO stuff from here can go to gui somewhere - text area podcast4: 11.30
        // TODO -> Gui
        if (gui != null) {
            String endTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String endSimString = "\n" + endTime  + ": End of Simulation\n";
            String finalState = this.sim.finalState();
            System.out.println(endSimString + finalState);
            gui.writeText(endSimString + finalState);
        }

        this.log.logQREntries(Constants.DURATION, sim); // individual queue refused transitions savestatus method
		this.log.finalise(this.sim);
	}
}
