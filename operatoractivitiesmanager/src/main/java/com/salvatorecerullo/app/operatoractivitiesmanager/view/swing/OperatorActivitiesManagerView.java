package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;

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
	private ActivityController activitiesController;
	private OperatorController operatorController;
	private BasicOperationController basicOperationController;
	
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

		tabbedPane.addChangeListener(getChangeListenerTabbedPane());

		setNames();
	}

	
	// GETTERS AND SETTERs
	public void setActivitiesController(ActivityController activitiesController) {
		this.activitiesController = activitiesController;
	}

	public void setOperatorController(OperatorController operatorController) {
		this.operatorController = operatorController;
	}

	public void setBasicOperationController(BasicOperationController basicOperationController) {
		this.basicOperationController = basicOperationController;
	}
	
	// LISTENER
	private ChangeListener getChangeListenerTabbedPane() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (tabbedPane.getSelectedIndex()) {
				default:
					activitiesControllerInvocation();
					break;
				case 1:
					operatorController.allOperators();
					break;
				case 2:
					basicOperationController.allBasicOperations();
					break;
				}
			}
		};
	}
	
	//UTILS	
	private void setNames() {
		activitiesPanel.setName("activitiesPanel");
		operatorsPanel.setName("operatorsPanel");
		basicOperationPanel.setName("basicOperationPanel");
	}

	private void activitiesControllerInvocation() {
		activitiesController.allActivities();
		activitiesController.allOperators();
		activitiesController.allBasicOperation();
	}
	
}