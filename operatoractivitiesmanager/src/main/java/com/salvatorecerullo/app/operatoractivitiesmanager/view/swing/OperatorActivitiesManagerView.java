package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;

public class OperatorActivitiesManagerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4591844476430172042L;
	private JPanel contentPane;
	private ActivitiesPanel activitiesPanel;
	
	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperatorActivitiesManagerView frame = new OperatorActivitiesManagerView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public OperatorActivitiesManagerView() {
		setTitle("Operator Activities Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 734);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setName("contentPane");
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		tabbedPane.setName("tabbedPane");

		activitiesPanel = new ActivitiesPanel();
		tabbedPane.addTab("Activities", null, activitiesPanel, null);
		
		JPanel operatorPanel = new JPanel();
		tabbedPane.addTab("Operators", null, operatorPanel, null);

		JPanel basicOperationPanel = new JPanel();
		tabbedPane.addTab("Basic Operations", null, basicOperationPanel, null);
	}

	public ActivitiesPanel getActivitiesPanel() {
		return activitiesPanel;
	}
	
}
