/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Point;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
	}

	private void createGUI() {
		//Setup
		GridBagLayout layout = new GridBagLayout();
		this.setTitle("Aircraft Booking Simulator");
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLayout(layout);
		
		

        displayGraph();
        displaySettings();
        
        // Display the window
        pack();
        setLocationRelativeTo(null);
	    repaint();
	    this.setVisible(true);
	}

	private void displayGraph() {
		//TODO idk, fix JFreeChart
		JPanel graph = new JPanel();
		graph.setBackground(Color.BLACK);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.gridwidth = 10;
		gbc.gridheight = 10;
		
		this.add(graph, gbc);
		
//		JFreeChart chart = new JFreeChart("asdf", null);
//		ChartPanel chartPanel = new ChartPanel(chart);
//		graph.add(chart,BorderLayout.CENTER);
	}

	private void displaySettings() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.gridx = 0;
		gbc.gridy = 0;
		JPanel settings = new JPanel();
		
		JLabel simulationLabel = new JLabel("Simulation");
		JLabel fareClassesLabel = new JLabel("Fare Classes");
		JLabel OperationLabel = new JLabel("Operation");	
		
		//TODO 8 text areas with 8 labels attached to them
		
		JButton run = new JButton();
		JButton showChart2 = new JButton();
		
		getContentPane().add(settings, gbc);
		settings.add(simulationLabel);
		settings.add(fareClassesLabel);
		settings.add(OperationLabel);
		settings.add(run);
		settings.add(showChart2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		createGUI();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 JFrame.setDefaultLookAndFeelDecorated(true);
	     SwingUtilities.invokeLater(new GUISimulator("BorderLayout"));
	}

}
