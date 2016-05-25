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
import java.awt.HeadlessException;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	public static final int WIDTH = 700;
	public static final int HEIGHT = 400;
	
//	private JPanel pnlOne;
//	private JPanel pnlTwo;
//	private JPanel pnlThree;
//	private JPanel pnlFour;
//	private JPanel pnlFive;
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void createGUI() {
		JTabbedPane pane = new JTabbedPane();
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Graph"));
        JPanel panel2 = new JPanel();
        panel2.add(new JButton("Settings"));

        pane.add("Graph", panel1);
        pane.add("Settings", panel2);
        getContentPane().add(pane);

        
        // Display the window.
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	    
	    //Solution code uses different colours to highlight different panels 
//	    pnlOne = createPanel(Color.BLUE);
//	    pnlTwo = createPanel(Color.CYAN);
//	    pnlThree = createPanel(Color.DARK_GRAY);
//	    pnlFour = createPanel(Color.RED);
//	    pnlFive = createPanel(Color.GREEN);
	        
//	    this.getContentPane().add(pnlOne,BorderLayout.CENTER);
//	    this.getContentPane().add(pnlTwo,BorderLayout.NORTH);
//	    this.getContentPane().add(pnlThree,BorderLayout.SOUTH);
//	    this.getContentPane().add(pnlFour,BorderLayout.EAST);
//	    this.getContentPane().add(pnlFive,BorderLayout.WEST);
	    repaint();
	    this.setVisible(true);
	}
	private JPanel createPanel(Color c) {
			JPanel jp = new JPanel();
			jp.setBackground(c);
			return jp;
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
