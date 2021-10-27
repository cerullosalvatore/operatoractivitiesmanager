package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JToggleButton;

public class ActivitiesPanel extends JPanel {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6391077047327115858L;
	private JPanel formActivityPanel;
	private JLabel labelNewActivity;
	private JLabel labelOperatorActivity;
	private JComboBox comboBoxOperatorActivity;
	private JLabel labelBasicOperationActivity;
	private JComboBox comboBoxBasicOperationActivity;
	private JLabel labelStartDataActivity;
	private JFormattedTextField textFieldStartDataActivity;
	private JLabel labelStartHourActivity;
	private JFormattedTextField textFieldStartHourActivity;
	private JLabel labelEndDataActivity;
	private JFormattedTextField textFieldEndDataActivity;
	private JLabel labelEndHourActivity;
	private JFormattedTextField textFieldEndHourActivity;
	private JButton btnAddActivity;
	private JPanel listActivitiesPanel;
	private JPanel newActivityPanel;
	private JPanel listTopMenuPanel;
	private JList listActivities;
	private JToggleButton tglbtnShowAll;
	private JToggleButton tglbtnFindByOperator;
	private JToggleButton tglbtnFindByBasicOperation;
	private JComboBox comboBoxSelectorSearch;
	private JPanel listBottomMenuPanel;
	private JButton btnModifyActivity;
	private JButton btnDeleteActivity;

	/**
	 * Create the panel.
	 */
	public ActivitiesPanel() {
		setLayout(new GridLayout(0, 1, 0, 0));

		newActivityPanel = new JPanel();
		add(newActivityPanel);
		newActivityPanel.setLayout(new BorderLayout(0, 0));

		formActivityPanel = new JPanel();
		newActivityPanel.add(formActivityPanel, BorderLayout.CENTER);
		formActivityPanel.setLayout(new GridLayout(0, 2, 0, 0));

		labelOperatorActivity = new JLabel("Operator:");
		formActivityPanel.add(labelOperatorActivity);

		comboBoxOperatorActivity = new JComboBox();
		formActivityPanel.add(comboBoxOperatorActivity);

		labelBasicOperationActivity = new JLabel("Basic Operation:");
		formActivityPanel.add(labelBasicOperationActivity);

		comboBoxBasicOperationActivity = new JComboBox();
		formActivityPanel.add(comboBoxBasicOperationActivity);

		labelStartDataActivity = new JLabel("Start Data:");
		formActivityPanel.add(labelStartDataActivity);

		textFieldStartDataActivity = new JFormattedTextField();
		formActivityPanel.add(textFieldStartDataActivity);

		labelStartHourActivity = new JLabel("Start Hour:");
		formActivityPanel.add(labelStartHourActivity);

		textFieldStartHourActivity = new JFormattedTextField();
		formActivityPanel.add(textFieldStartHourActivity);

		labelEndDataActivity = new JLabel("End Data:");
		formActivityPanel.add(labelEndDataActivity);

		textFieldEndDataActivity = new JFormattedTextField();
		formActivityPanel.add(textFieldEndDataActivity);

		labelEndHourActivity = new JLabel("End Hour:");
		formActivityPanel.add(labelEndHourActivity);

		textFieldEndHourActivity = new JFormattedTextField();
		formActivityPanel.add(textFieldEndHourActivity);

		labelNewActivity = new JLabel("New Activity");
		newActivityPanel.add(labelNewActivity, BorderLayout.NORTH);

		btnAddActivity = new JButton("Add Activity");
		newActivityPanel.add(btnAddActivity, BorderLayout.SOUTH);

		listActivitiesPanel = new JPanel();
		add(listActivitiesPanel);
		listActivitiesPanel.setLayout(new GridLayout(0, 1, 0, 0));

		listTopMenuPanel = new JPanel();
		listActivitiesPanel.add(listTopMenuPanel);
		listTopMenuPanel.setLayout(new GridLayout(0, 3, 0, 0));

		tglbtnShowAll = new JToggleButton("Show All");
		listTopMenuPanel.add(tglbtnShowAll);

		tglbtnFindByOperator = new JToggleButton("Find By Operator");
		listTopMenuPanel.add(tglbtnFindByOperator);

		tglbtnFindByBasicOperation = new JToggleButton("Find By Operation");
		listTopMenuPanel.add(tglbtnFindByBasicOperation);

		comboBoxSelectorSearch = new JComboBox();
		listActivitiesPanel.add(comboBoxSelectorSearch);

		listActivities = new JList();
		listActivitiesPanel.add(listActivities);
		
		listBottomMenuPanel = new JPanel();
		listActivitiesPanel.add(listBottomMenuPanel);
		listBottomMenuPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		btnModifyActivity = new JButton("New button");
		listBottomMenuPanel.add(btnModifyActivity);
		
		btnDeleteActivity = new JButton("New button");
		listBottomMenuPanel.add(btnDeleteActivity);

		setNames();

	}

	private void setNames() {
		newActivityPanel.setName("newActivityPanel");
		labelNewActivity.setName("labelNewActivity");
		formActivityPanel.setName("formActivityPanel");
		labelOperatorActivity.setName("labelOperatorActivity");
		comboBoxOperatorActivity.setName("comboBoxOperatorActivity");
		labelBasicOperationActivity.setName("labelBasicOperationActivity");
		comboBoxBasicOperationActivity.setName("comboBoxBasicOperationActivity");
		labelStartDataActivity.setName("labelStartDataActivity");
		textFieldStartDataActivity.setName("textFieldStartDataActivity");
		labelStartHourActivity.setName("labelStartHourActivity");
		textFieldStartHourActivity.setName("textFieldStartHourActivity");
		labelEndDataActivity.setName("labelEndDataActivity");
		textFieldEndDataActivity.setName("textFieldEndDataActivity");
		labelEndHourActivity.setName("labelEndHourActivity");
		textFieldEndHourActivity.setName("textFieldEndHourActivity");
		btnAddActivity.setName("btnAddActivity");
		listActivitiesPanel.setName("listActivitiesPanel");
		listTopMenuPanel.setName("listTopMenuPanel");
		listActivities.setName("listActivities");

		tglbtnShowAll.setName("tglbtnShowAll");
		tglbtnFindByOperator.setName("tglbtnFindByOperator");
		tglbtnFindByBasicOperation.setName("tglbtnFindByBasicOperation");
		tglbtnFindByOperator.setName("tglbtnFindByOperator");
		tglbtnFindByBasicOperation.setName("tglbtnFindByBasicOperation");
		comboBoxSelectorSearch.setName("comboBoxSelectorSearch");
		listBottomMenuPanel.setName("listBottomMenuPanel");
		btnModifyActivity.setName("btnModifyActivity");
		btnDeleteActivity.setName("btnDeleteActivity");

	}
}
