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
import java.awt.Insets;
import java.awt.Point;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.sun.glass.ui.Screen;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
	}

	private void createGUI() {
		//Setup
		this.setSize(WIDTH, HEIGHT);
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
		//TODO magic numbers galore lmao
		JPanel graph = new JPanel();
		graph.setBackground(Color.BLACK);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		Insets inset = new Insets(0,0,10,0);
		gbc.insets = inset;
		
		JTextArea text = new JTextArea();
		Dimension size = new Dimension();
		size.height = (int) (this.getSize().getHeight()*0.6);
		size.width = (int) (this.getSize().getWidth()*0.97);
		text.setPreferredSize(size);
		Dimension minSize = new Dimension();
		minSize.height = (int) (this.getSize().getHeight()*0.6);
		minSize.width = (int) (this.getSize().getWidth()*0.97);
		text.setMinimumSize(minSize);
		graph.add(text);
		getContentPane().add(graph, gbc);
		
//		JFreeChart chart = new JFreeChart("asdf", null);
//		ChartPanel chartPanel = new ChartPanel(chart);
//		graph.add(chart,BorderLayout.CENTER);
	}

	private void displaySettings() {
		//Wrapper JPanel constraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 100;
		gbc.weighty = 100;
		JPanel settings = new JPanel();
		Dimension size = new Dimension();
		size.height = (int) (this.getSize().getHeight()/2*0.9);
		size.width = (int) (this.getSize().getWidth()*0.95);
		settings.setSize(size);
		getContentPane().add(settings, gbc);
		
		//TODO 8 text areas with 8 labels attached to them
		// Settings Options
		JLabel simulationLabel = new JLabel("Simulation");
		JLabel fareClassesLabel = new JLabel("Fare Classes");
		JLabel OperationLabel = new JLabel("Operation");	
		JButton run = new JButton("Run Simulation");
		JButton showChart2 = new JButton("Show Chart 2");
		showChart2.setEnabled(false);
		
		//TODO settingsgbc isnt affecting the settings idk
		//TODO make this into a function passing constraints after I figure out how this works
		GridBagConstraints settingsgbc = new GridBagConstraints();
		settingsgbc.gridx = 0;
		settingsgbc.gridy = 1;
		settingsgbc.gridwidth = 1;
		settingsgbc.gridheight = 1;
		settingsgbc.weightx = 100;
		settingsgbc.weighty = 100;
		
		settings.add(simulationLabel,settingsgbc);
		settingsgbc.gridx = 0;
		settingsgbc.gridy = 0;
		settingsgbc.gridwidth = 1;
		settingsgbc.gridheight = 1;
		settingsgbc.weightx = 100;
		settingsgbc.weighty = 100;
		
		settings.add(fareClassesLabel,settingsgbc);
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
