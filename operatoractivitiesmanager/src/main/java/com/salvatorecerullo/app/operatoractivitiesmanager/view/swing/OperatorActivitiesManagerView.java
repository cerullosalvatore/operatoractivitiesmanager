package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import javax.swing.BoxLayout;

public class OperatorActivitiesManagerView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4591844476430172042L;
	private JPanel contentPane;
	private ActivitiesPanel activitiesPanel;
	private OperatorsPanel operatorsPanel;
	private BasicOperationPanel basicOperationPanel;

	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { OperatorActivitiesManagerView frame =
	 * new OperatorActivitiesManagerView(); frame.setVisible(true); } catch
	 * (Exception e) { e.printStackTrace(); } } }); }
	 */
	/**
	 * Create the frame.
	 */
	public OperatorActivitiesManagerView() {
		setTitle("Operator Activities Manager");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 734);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setName("contentPane");
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane);
		tabbedPane.setName("tabbedPane");

		activitiesPanel = new ActivitiesPanel();
		tabbedPane.addTab("Activities", null, activitiesPanel, null);
		operatorsPanel = new OperatorsPanel();
		tabbedPane.addTab("Operators", null, operatorsPanel, null);
		basicOperationPanel = new BasicOperationPanel();
		tabbedPane.addTab("Basic Operations", null, basicOperationPanel, null);

		tabbedPane.addChangeListener(e -> changeListenerTabbedPane());

		setNames();
	}

	// GETTERS AND SETTERs
	public ActivitiesPanel getActivitiesPanel() {
		return activitiesPanel;
	}

	public OperatorsPanel getOperatorsPanel() {
		return operatorsPanel;
	}

	public BasicOperationPanel getBasicOperationPanel() {
		return basicOperationPanel;
	}

	// LISTENER
	private void changeListenerTabbedPane() {
		switch (tabbedPane.getSelectedIndex()) {
		case 1:
			operatorsPanel.getOperatorController().allOperators();
			break;
		case 2:
			basicOperationPanel.getBasicOperationController().allBasicOperations();
			break;
		default:
			activitiesControllerInvocation();
			break;
		}

	}

	// UTILS
	private void setNames() {
		activitiesPanel.setName("activitiesPanel");
		operatorsPanel.setName("operatorsPanel");
		basicOperationPanel.setName("basicOperationPanel");
	}

	private void activitiesControllerInvocation() {
		activitiesPanel.getActivityController().allActivities();
		activitiesPanel.getActivityController().allOperators();
		activitiesPanel.getActivityController().allBasicOperation();
	}

}