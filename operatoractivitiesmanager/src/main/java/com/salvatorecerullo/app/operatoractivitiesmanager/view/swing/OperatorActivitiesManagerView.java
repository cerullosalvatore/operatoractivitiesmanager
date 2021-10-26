package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class OperatorActivitiesManagerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4591844476430172042L;
	private JPanel contentPane;
	private JTextField textFieldStartDataActivity;
	private JTextField textFieldEndDataActivity;
	private JTextField textFieldEndHourActivity;
	private JTextField textFieldStartHourActivity;

	/**
	 * Launch the application.
	 */
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
	}

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
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setName("contentPane");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		tabbedPane.setName("tabbedPane");

		JPanel activitiesPanel = new JPanel();
		tabbedPane.addTab("Activities", null, activitiesPanel, null);
		activitiesPanel.setName("activitiesPanel");
		activitiesPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel newActivityPanel = new JPanel();
		activitiesPanel.add(newActivityPanel);
		activitiesPanel.setName("newActivityPanel");
		newActivityPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel labelOperatorActivity = new JLabel("Operator:");
		newActivityPanel.add(labelOperatorActivity);
		labelOperatorActivity.setName("labelOperatorActivity");

		JComboBox comboBoxOperatorActivity = new JComboBox();
		newActivityPanel.add(comboBoxOperatorActivity);
		comboBoxOperatorActivity.setName("comboBoxOperatorActivity");

		JLabel labelBasicOperationActivity = new JLabel("Basic Operation:");
		newActivityPanel.add(labelBasicOperationActivity);
		labelBasicOperationActivity.setName("labelBasicOperationActivity");

		JComboBox comboBoxBasicOperationActivity = new JComboBox();
		newActivityPanel.add(comboBoxBasicOperationActivity);
		comboBoxBasicOperationActivity.setName("comboBoxBasicOperationActivity");

		JLabel labelStartDataActivity = new JLabel("Start Data:");
		newActivityPanel.add(labelStartDataActivity);
		labelStartDataActivity.setName("labelStartDataActivity");

		textFieldStartDataActivity = new JTextField();
		newActivityPanel.add(textFieldStartDataActivity);
		textFieldStartDataActivity.setColumns(10);
		textFieldStartDataActivity.setName("textFieldStartDataActivity");

		JLabel labelStartHourActivity = new JLabel("Start Hour:");
		newActivityPanel.add(labelStartHourActivity);
		labelStartHourActivity.setName("labelStartHourActivity");

		textFieldStartHourActivity = new JTextField();
		newActivityPanel.add(textFieldStartHourActivity);
		textFieldStartHourActivity.setColumns(10);
		textFieldStartHourActivity.setName("textFieldStartHourActivity");

		JLabel labelEndDataActivity = new JLabel("End Data:");
		newActivityPanel.add(labelEndDataActivity);
		labelEndDataActivity.setName("labelEndDataActivity");

		textFieldEndDataActivity = new JTextField();
		newActivityPanel.add(textFieldEndDataActivity);
		textFieldEndDataActivity.setColumns(10);
		textFieldEndDataActivity.setName("textFieldEndDataActivity");

		JLabel labelEndHourActivity = new JLabel("End Hour:");
		newActivityPanel.add(labelEndHourActivity);
		labelEndHourActivity.setName("labelEndHourActivity");

		textFieldEndHourActivity = new JTextField();
		newActivityPanel.add(textFieldEndHourActivity);
		textFieldEndHourActivity.setColumns(10);
		textFieldEndHourActivity.setName("textFieldEndHourActivity");

		JPanel operatorPanel = new JPanel();
		tabbedPane.addTab("Operators", null, operatorPanel, null);

		JPanel basicOperationPanel = new JPanel();
		tabbedPane.addTab("Basic Operations", null, basicOperationPanel, null);
	}
}
