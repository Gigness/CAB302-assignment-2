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
import javax.swing.border.Border;

import asgn2Aircraft.AircraftException;
import asgn2Passengers.PassengerException;
import javafx.scene.chart.XYChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
    public static final int SETTINGS_PANEL_HEIGHT = 200;
    public static final double FIELD_Y_WEIGHT = 0.2;
    public static final int TITLE_LABEL_Y_WEIGHT = 1;
    int count = 0;
    private int oldTotalRefused = 0;
    private int oldTotalEcon = 0;
    private int oldTotalPremium = 0;
    private int oldTotalBusiness = 0;
    private int oldTotalFirst = 0;
    private int oldTotalTotal = 0;
    private int oldTotalEmpty = 0;
    
    private int tempOldTotalRefused = 0;
    private int tempOldTotalEcon = 0;
    private int tempOldTotalPremium = 0;
    private int tempOldTotalBusiness = 0;
    private int tempOldTotalFirst = 0;
    private int tempOldTotalTotal = 0;
    private int tempOldTotalEmpty = 0;

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
    private JButton textButton;
    private JButton chartButton;
    private JButton chartButton2;


    private JFreeChart progressChart;
    ChartPanel progressChartPanel;
    JFreeChart summaryChart;
    ChartPanel summaryChartPanel;

    // TODO - For progress chart
    XYSeriesCollection dailyDataset;
    XYSeries econData;
    XYSeries premiumData;
    XYSeries businessData;
    XYSeries firstData;
    XYSeries totalData;
    XYSeries emptySeatsData;
    XYSeries queData;
    XYSeries refusedData;

    // TODO - For summary chart
    XYSeriesCollection summaryDataset;
    XYSeries queueSize;
    XYSeries refusedPassengers;

    XYPlot chart1;
    XYPlot chart2;

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

        // Add to main content pane
        Container c = getContentPane();

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
        textButton = new JButton("Show Text Log");
        chartButton = new JButton("Show Daily Chart");
        chartButton2 = new JButton("Show Summary Chart");
        textButton.setEnabled(false);
        chartButton.setEnabled(false);
        chartButton2.setEnabled(false);

        // Chart
        econData = new XYSeries("Economy");
        premiumData = new XYSeries("Premium");
        businessData = new XYSeries("Business");
        firstData = new XYSeries("First");
        totalData = new XYSeries("Total");
        emptySeatsData = new XYSeries("Empty");

        dailyDataset = new XYSeriesCollection();
        
        queData = new XYSeries("Qued");
        refusedData = new XYSeries("Refused");
        
        summaryDataset = new XYSeriesCollection();

        progressChart = ChartFactory.createXYLineChart(
                "Simulation Results: Daily Progress",
                "Days",
                "Passengers",
                dailyDataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        progressChartPanel = new ChartPanel(progressChart);
        c.add(progressChartPanel, BorderLayout.CENTER);
        progressChartPanel.setVisible(false);
        
        //Chart 2
        summaryChart = ChartFactory.createXYLineChart(
                "Simulation Results: Summary",
                "Days",
                "Passengers",
                summaryDataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        summaryChartPanel = new ChartPanel(summaryChart);
        c.add(summaryChartPanel, BorderLayout.CENTER);
        summaryChartPanel.setVisible(false);
        
        //adding color for chart 1 
        chart1 = progressChart.getXYPlot();
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        renderer1.setSeriesPaint(0, Color.GRAY);
        renderer1.setSeriesPaint(1, Color.CYAN);
        renderer1.setSeriesPaint(2, Color.BLUE);
        renderer1.setSeriesPaint(3, Color.BLACK);
        renderer1.setSeriesPaint(4, Color.GREEN);
        renderer1.setSeriesPaint(5, Color.RED);
        renderer1.setShapesVisible(false);
        chart1.setRenderer(renderer1);
        
        //adding color for chart 2
        chart2 = summaryChart.getXYPlot();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.BLACK);
        renderer2.setSeriesPaint(1, Color.RED);
        renderer2.setShapesVisible(false);
        chart2.setRenderer(renderer2);
        
        c.add(textScrollPane, BorderLayout.CENTER);
        c.add(settingsPanel, BorderLayout.SOUTH);
        
        // Simulation Text Fields
        rngField = new JTextField(10);
        meanField = new JTextField(10);
        queueField = new JTextField(10);
        cancelField = new JTextField(10);

        // Fare class Text Fields
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
        gc.gridy = 1;
        settingsPanel.add(runButton,gc);

        gc.weighty = FIELD_Y_WEIGHT;
        gc.gridx = 4;
        gc.gridy = 2;
        settingsPanel.add(textButton,gc);
        
        gc.weighty = FIELD_Y_WEIGHT;
        gc.gridx = 4;
        gc.gridy = 3;
        settingsPanel.add(chartButton,gc);
        
        gc.weighty = FIELD_Y_WEIGHT;
        gc.gridx = 4;
        gc.gridy = 4;
        settingsPanel.add(chartButton2,gc);

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
                        chartButton.setEnabled(true);
                        chartButton2.setEnabled(true);
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

        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	textButton.setEnabled(false);
            	chartButton.setEnabled(true);
            	chartButton2.setEnabled(true);
            	textScrollPane.setVisible(true);
            	summaryChartPanel.setVisible(false);
                progressChartPanel.setVisible(false);
                c.revalidate();
            }
        });
        
        chartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	textScrollPane.setVisible(false);
            	textButton.setEnabled(true);
            	summaryChartPanel.setVisible(false);
            	c.add(progressChartPanel, BorderLayout.CENTER);
                progressChartPanel.setVisible(true);
                chartButton.setEnabled(false);
                chartButton2.setEnabled(true);
                c.revalidate();
            }
        });
        
        chartButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	textScrollPane.setVisible(false);
            	textButton.setEnabled(true);
            	progressChartPanel.setVisible(false);
            	c.add(summaryChartPanel, BorderLayout.CENTER);
            	summaryChartPanel.setVisible(true);
            	chartButton2.setEnabled(false);
            	chartButton.setEnabled(true);
                c.revalidate();
            }
        });
        
        pack();
        this.setVisible(true);
        this.setSize(WIDTH, HEIGHT);
	}

    public void writeText(String message) {
        textArea.append(message);
    }
    
    public void addDataToChart1(int day, int econ, int premium, int business, int first, int empty){
    	//storing the overall totals for the sim
    	int total;
    	oldTotalEcon = econ;
    	oldTotalPremium = premium;
    	oldTotalBusiness = business;
    	oldTotalFirst = first;
    	oldTotalTotal = econ + premium + business + first;
    	oldTotalEmpty = empty;
    	
    	//isolating only the daily total from each total
    	econ -= tempOldTotalEcon;
    	premium -= tempOldTotalPremium;
    	business -= tempOldTotalBusiness;
    	first -= tempOldTotalFirst;
    	total = oldTotalTotal - tempOldTotalTotal;
    	empty -= tempOldTotalEmpty;
    	
    	//adding each total to each series
    	econData.add(day, econ);
    	premiumData.add(day, premium);
    	businessData.add(day, business);
    	firstData.add(day, first);
    	totalData.add(day, total);
    	emptySeatsData.add(day, empty);
    	
    	//preparing the temp totals for the next call
    	tempOldTotalEcon = oldTotalEcon;
    	tempOldTotalPremium = oldTotalPremium;
    	tempOldTotalBusiness = oldTotalBusiness;
    	tempOldTotalFirst = oldTotalFirst;
    	tempOldTotalTotal = oldTotalTotal;
    	tempOldTotalEmpty = oldTotalEmpty;
    }
    
    public void addDataToChart2(int day, int qued, int refused){
    	//storing the overall total for the refused
    	oldTotalRefused = refused;
    	
    	//isolating only the daily total from each total
    	refused -= tempOldTotalRefused;
    	
    	refusedData.add(day, refused);
    	//this process is not needed for que as it is current que not cumulative
    	queData.add(day, qued);
    	
    	tempOldTotalRefused = oldTotalRefused;
    }

    public void addDataToXYSeriesCollections(){
    	count++;
    	dailyDataset.addSeries(econData);
    	dailyDataset.addSeries(premiumData);
    	dailyDataset.addSeries(businessData);
    	dailyDataset.addSeries(firstData);
    	dailyDataset.addSeries(totalData);
    	dailyDataset.addSeries(emptySeatsData);


    	summaryDataset.addSeries(queData);
    	summaryDataset.addSeries(refusedData);
    }

    public void clearGraphingData() {
        oldTotalRefused = 0;
        oldTotalEcon = 0;
        oldTotalPremium = 0;
        oldTotalBusiness = 0;
        oldTotalFirst = 0;
        oldTotalTotal = 0;
        oldTotalEmpty = 0;

        tempOldTotalRefused = 0;
        tempOldTotalEcon = 0;
        tempOldTotalPremium = 0;
        tempOldTotalBusiness = 0;
        tempOldTotalFirst = 0;
        tempOldTotalTotal = 0;
        tempOldTotalEmpty = 0;

        econData.clear();
        premiumData.clear();
        businessData.clear();
        firstData.clear();
        totalData.clear();
        emptySeatsData.clear();
        queData.clear();
        refusedData.clear();

        dailyDataset.removeAllSeries();
        summaryDataset.removeAllSeries();

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
