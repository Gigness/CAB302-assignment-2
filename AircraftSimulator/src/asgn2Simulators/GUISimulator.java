/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import asgn2Aircraft.AircraftException;
import asgn2Passengers.PassengerException;
import com.sun.tools.internal.jxc.ap.Const;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

    public static final int SETTINGS_PANEL_HEIGHT = 200;
    public static final int SCROLL_PANE_HEIGHT = 400;
    public static final int TEXT_AREA_PANEL_HEIGHT = 400;

    public static final double FIELD_Y_WEIGHT = 0.2;
    public static final int TITLE_LABEL_Y_WEIGHT = 1;

    private JScrollPane textScrollPane;
    private JTextArea textArea;
	private JPanel settingsPanel;

    private JTextField rngField;
    private JTextField meanField;
    private JTextField queueField;
    private JTextField cancelField;
    private JTextField firstField;
    private JTextField businessField;
    private JTextField premiumField;
    private JTextField econField;

    private JButton runButton;
    private JButton chartButton;

    private Simulator sim;
    private String simulatorArgs;
    private String[] args;
    private Log l;
    private GUISimulator guiSim;


    /**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0, String[] args) throws HeadlessException {
		super(arg0);
        this.args = args;
        this.guiSim = this;
	}

	private void createGUI() {

        setLayout(new BorderLayout());  // basic frame

        // create swing components
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setEnabled(false);

        textScrollPane = new JScrollPane(textArea);
        settingsPanel = new JPanel();

        // Dimensions
        Dimension sizeSettingsPanel = settingsPanel.getPreferredSize();
        sizeSettingsPanel.width = WIDTH;
        sizeSettingsPanel.height = SETTINGS_PANEL_HEIGHT;
        settingsPanel.setPreferredSize(sizeSettingsPanel);

//        Dimension sizeTextScrollPane = textScrollPane.getPreferredSize();
////        sizeTextScrollPane.width = WIDTH;
//        sizeTextScrollPane.height = SCROLL_PANE_HEIGHT;
//        textArea.setPreferredSize(sizeTextScrollPane);

        // Add to main content pane
        Container c = getContentPane();
        c.add(textScrollPane, BorderLayout.CENTER);
        c.add(settingsPanel, BorderLayout.SOUTH);

        // Main Labels
        JLabel simulationLabel = new JLabel("Simulation");
        JLabel fareClassesLabel = new JLabel("Fare Classes");
        JLabel operationLabel = new JLabel("Operation");

        // Simulation labels
        JLabel rngLabel = new JLabel("RNG Seed");
        JLabel meanLabel = new JLabel("Daily Mean");
        JLabel queueLabel = new JLabel("Queue Size");
        JLabel cancelLabel = new JLabel("Cancellation");

        // Fare class labels
        JLabel firstLabel = new JLabel("First");
        JLabel businessLabel = new JLabel("Business");
        JLabel premiumLabel = new JLabel("Premium");
        JLabel econLabel = new JLabel("Economy");

        // buttons
        runButton = new JButton("Run Simulation");
        chartButton = new JButton("Show Chart");

        // Simulation Text Fields
        rngField = new JTextField(10);
        meanField = new JTextField(10);
        queueField = new JTextField(10);
        cancelField = new JTextField(10);

        // Fare classs Text Fields
        firstField = new JTextField(10);
        businessField = new JTextField(10);
        premiumField = new JTextField(10);
        econField = new JTextField(10);

        settingsPanel.setBorder(BorderFactory.createEtchedBorder());
        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // col 1
        gc.weighty = 1;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        settingsPanel.add(simulationLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        settingsPanel.add(rngLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        settingsPanel.add(meanLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 3;
        settingsPanel.add(queueLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 4;
        settingsPanel.add(cancelLabel, gc);

        // col 2
        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 1;
        gc.gridy = 1;
        settingsPanel.add(rngField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 1;
        gc.gridy = 2;
        settingsPanel.add(meanField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 1;
        gc.gridy = 3;
        settingsPanel.add(queueField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 1;
        gc.gridy = 4;
        settingsPanel.add(cancelField, gc);

        // col 3
        gc.weighty = TITLE_LABEL_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 2;
        gc.gridy = 0;
        settingsPanel.add(fareClassesLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 2;
        gc.gridy = 1;
        settingsPanel.add(firstLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 2;
        gc.gridy = 2;
        settingsPanel.add(businessLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 2;
        gc.gridy = 3;
        settingsPanel.add(premiumLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 2;
        gc.gridy = 4;
        settingsPanel.add(econLabel, gc);

        // col 4
        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 3;
        gc.gridy = 1;
        settingsPanel.add(firstField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 3;
        gc.gridy = 2;
        settingsPanel.add(businessField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 3;
        gc.gridy = 3;
        settingsPanel.add(premiumField, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.weightx = 1;
        gc.gridx = 3;
        gc.gridy = 4;
        settingsPanel.add(econField, gc);

        // col 5
        gc.weightx = 1;
        gc.gridx = 4;
        gc.gridy = 0;
        settingsPanel.add(operationLabel, gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.gridx = 4;
        gc.gridy = 2;
        settingsPanel.add(runButton,gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.gridx = 4;
        gc.gridy = 3;
        settingsPanel.add(chartButton,gc);

        // default values for input fields
        rngField.setText(Integer.toString(Constants.DEFAULT_SEED));
        meanField.setText(Double.toString(Constants.DEFAULT_DAILY_BOOKING_MEAN));
        queueField.setText(Integer.toString(Constants.DEFAULT_MAX_QUEUE_SIZE));
        cancelField.setText(Double.toString(Constants.DEFAULT_CANCELLATION_PROB));
        firstField.setText(Double.toString(Constants.DEFAULT_FIRST_PROB));
        businessField.setText(Double.toString(Constants.DEFAULT_BUSINESS_PROB));
        premiumField.setText(Double.toString(Constants.DEFAULT_PREMIUM_PROB));
        econField.setText(Double.toString(Constants.DEFAULT_ECONOMY_PROB));


        // Add actions to buttons
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean inputsInvalid = false;
                // validate all textField
                String rngInput = rngField.getText();
                String meanInput = meanField.getText();
                String cancelInput = cancelField.getText();
                String queueInput = queueField.getText();
                String firstInput = firstField.getText();
                String businessInput = businessField.getText();
                String premiumInput = premiumField.getText();
                String economyInput = econField.getText();

                
                if(!isInteger(rngInput)) {
                    rngLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    rngLabel.setForeground(Color.BLACK);
                }
                if(!isDouble(meanInput)) {
                    meanLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    meanLabel.setForeground(Color.BLACK);
                }
                if(!isDouble(cancelInput)) {
                    cancelLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    cancelLabel.setForeground(Color.BLACK);
                }
                if(!isInteger(queueInput)) {
                    queueLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    queueLabel.setForeground(Color.BLACK);
                }

                // TODO validate probabalites are less than or equal to 1?
                if(!isDouble(firstInput)) {
                    firstLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    firstLabel.setForeground(Color.BLACK);
                }
                if(!isDouble(businessInput)) {
                    businessLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    businessLabel.setForeground(Color.BLACK);
                }
                if(!isDouble(premiumInput)) {
                    premiumLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    premiumLabel.setForeground(Color.BLACK);
                }
                if(!isDouble(economyInput)) {
                    econLabel.setForeground(Color.RED);
                    inputsInvalid = true;
                } else {
                    econLabel.setForeground(Color.BLACK);
                }

                if (!inputsInvalid) {
                    double stdDev = 0.33 * Double.parseDouble(meanInput);

                    int rngValue = Integer.parseInt(rngInput);
                    double meanValue = Double.parseDouble(meanInput);
                    double cancelValue = Double.parseDouble(cancelInput);
                    int queueValue = Integer.parseInt(queueInput);
                    double firstValue = Double.parseDouble(firstInput);
                    double businessValue = Double.parseDouble(businessInput);
                    double premiumValue = Double.parseDouble(premiumInput);
                    double econValue = Double.parseDouble(economyInput);

                    try {
                        sim = new Simulator(rngValue, queueValue, meanValue,
                                stdDev, firstValue, businessValue, premiumValue, econValue, cancelValue);

                    } catch (SimulationException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        l = new Log();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    SimulationRunner sr = new SimulationRunner(sim,l);
                    textArea.setText(null);
                    try {
                        sr.runSimulation(guiSim);
                    } catch (AircraftException e1) {
                        e1.printStackTrace();
                    } catch (PassengerException e1) {
                        e1.printStackTrace();
                    } catch (SimulationException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }
            }
        });

        chartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("swag\n\n");
            }
        });
        pack();
        this.setVisible(true);
        this.setSize(WIDTH, HEIGHT);

        //Setup
//		this.setSize(WIDTH, HEIGHT);
//		GridBagLayout layout = new GridBagLayout();
//		this.setTitle("Aircraft Booking Simulator");
//		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//		this.setLayout(layout);
//		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        displayGraph();
//        displaySettings();
//
//        // Display the window
//        pack();
//        setLocationRelativeTo(null);
//	    repaint();
//	    this.setVisible(true);
	}

    public void writeText(String message) {
        textArea.append(message);
    }

	private void displayGraph() {
		//TODO idk, fix JFreeChart
//		JPanel graph = new JPanel();
//		graph.setBackground(Color.BLACK);
//
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.NONE;
//		gbc.anchor = GridBagConstraints.PAGE_START;
//		gbc.weightx = 100;
//		gbc.weighty = 100;
//		gbc.gridwidth = 1;
//		gbc.gridheight = 1;
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		Insets inset = new Insets(0,0,10,0);
//		gbc.insets = inset;
//
//		JTextArea text = new JTextArea();
//		Dimension size = new Dimension();
//		size.height = (int) (this.getSize().getHeight()*0.6);
//		size.width = (int) (this.getSize().getWidth()*0.97);
//		text.setPreferredSize(size);
//		Dimension minSize = new Dimension();
//		minSize.height = (int) (this.getSize().getHeight()*0.6);
//		minSize.width = (int) (this.getSize().getWidth()*0.97);
//		text.setMinimumSize(minSize);
//		graph.add(text);
//		getContentPane().add(graph, gbc);
		
//		JFreeChart chart = new JFreeChart("asdf", null);
//		ChartPanel chartPanel = new ChartPanel(chart);
//		graph.add(chart,BorderLayout.CENTER);
	}

	private void displaySettings() {
		//Wrapper JPanel constraints
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.BOTH;
//		gbc.anchor = GridBagConstraints.PAGE_START;
//		gbc.gridx = 0;
//		gbc.gridy = 1;
//		gbc.gridwidth = 1;
//		gbc.gridheight = 1;
//		gbc.weightx = 100;
//		gbc.weighty = 100;
//		JPanel settings = new JPanel();
//		Dimension size = new Dimension();
//		size.height = (int) (this.getSize().getHeight()/2*0.9);
//		size.width = (int) (this.getSize().getWidth()*0.95);
//		settings.setSize(size);
//		getContentPane().add(settings, gbc);
//
//		//TODO 8 text areas with 8 labels attached to them
//		// Settings Options
//		JLabel simulationLabel = new JLabel("Simulation");
//		JLabel fareClassesLabel = new JLabel("Fare Classes");
//		JLabel OperationLabel = new JLabel("Operation");
//		JButton run = new JButton("Run Simulation");
//		JButton showChart2 = new JButton("Show Chart 2");
//		showChart2.setEnabled(false);
//
//		//TODO settingsgbc isnt affecting the settings idk
//		//TODO make this into a function passing constraints after I figure out how this works
//		GridBagConstraints settingsgbc = new GridBagConstraints();
//		settingsgbc.gridx = 0;
//		settingsgbc.gridy = 1;
//		settingsgbc.gridwidth = 1;
//		settingsgbc.gridheight = 1;
//		settingsgbc.weightx = 100;
//		settingsgbc.weighty = 100;
//
//		settings.add(simulationLabel,settingsgbc);
//		settingsgbc.gridx = 0;
//		settingsgbc.gridy = 0;
//		settingsgbc.gridwidth = 1;
//		settingsgbc.gridheight = 1;
//		settingsgbc.weightx = 100;
//		settingsgbc.weighty = 100;
//
//		settings.add(fareClassesLabel,settingsgbc);
//		settings.add(OperationLabel);
//		settings.add(run);
//		settings.add(showChart2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		createGUI();
	}

    private boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    private boolean isDouble( String input ) {
        try {
            Double.parseDouble( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
