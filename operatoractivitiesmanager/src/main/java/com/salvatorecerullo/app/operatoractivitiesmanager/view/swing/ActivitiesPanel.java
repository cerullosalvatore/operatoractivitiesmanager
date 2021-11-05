package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.bson.types.ObjectId;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

import java.awt.Dimension;
import javax.swing.ListSelectionModel;

public class ActivitiesPanel extends JPanel implements ActivityView {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6391077047327115858L;
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String HOUR_FORMAT = "HH:mm";
	
	private JPanel formActivityPanel;
	private JLabel labelNewActivity;
	private JLabel labelOperatorActivity;
	private JComboBox<Operator> comboBoxOperatorActivity;
	private DefaultComboBoxModel<Operator> comboBoxOperatorsModel;
	private JLabel labelBasicOperationActivity;
	private JComboBox<BasicOperation> comboBoxBasicOperationActivity;
	private DefaultComboBoxModel<BasicOperation> comboBoxOperationsModel;
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
	private JList<Activity> listActivities;
	private DefaultListModel<Activity> listActivitiesModel;
	private JPanel listBottomMenuPanel;
	private JButton btnModifyActivity;
	private JButton btnDeleteActivity;
	private JButton btnUpdateActivity;
	private JPanel buttonsFormActivityPanel;
	private JButton btnShowAll;
	private JButton btnFindByOperator;
	private JButton btnFindByBasicOperation;
	private JButton btnFindByData;
	private boolean updateInProgress;
	private String activityIdTemp;
	private JOptionPane infoDialog;

	private ActivityController activityController;

	/**
	 * Create the panel.
	 */

	public ActivitiesPanel() {
		activityIdTemp = new ObjectId().toString();
		updateInProgress = false;
		setMinimumSize(new Dimension(0, 0));
		setLayout(new GridLayout(0, 1, 0, 0));

		newActivityPanel = new JPanel();
		add(newActivityPanel);
		newActivityPanel.setLayout(new BorderLayout(0, 0));

		formActivityPanel = new JPanel();
		newActivityPanel.add(formActivityPanel, BorderLayout.CENTER);
		formActivityPanel.setLayout(new GridLayout(0, 2, 0, 0));

		labelOperatorActivity = new JLabel("Operator:");
		formActivityPanel.add(labelOperatorActivity);

		// COMBO BOX OPERATOR
		comboBoxOperatorsModel = new DefaultComboBoxModel<>();
		comboBoxOperatorActivity = new JComboBox<>(comboBoxOperatorsModel);
		formActivityPanel.add(comboBoxOperatorActivity);
		comboBoxOperatorActivity.addActionListener(e -> actionListenerComboBoxOperatorsModel());

		labelBasicOperationActivity = new JLabel("Basic Operation:");
		formActivityPanel.add(labelBasicOperationActivity);

		// COMBO BOX BASIC OPERATION
		comboBoxOperationsModel = new DefaultComboBoxModel<>();
		comboBoxBasicOperationActivity = new JComboBox<>(comboBoxOperationsModel);
		formActivityPanel.add(comboBoxBasicOperationActivity);

		comboBoxBasicOperationActivity.addActionListener(e -> actionListenerComboBoxOperationsModel());

		labelStartDataActivity = new JLabel("Start Data:");
		formActivityPanel.add(labelStartDataActivity);

		// FIELD DATA START
		textFieldStartDataActivity = new JFormattedTextField(new SimpleDateFormat(DATE_FORMAT));
		formActivityPanel.add(textFieldStartDataActivity);
		KeyAdapter textInputDataKeyListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
				btnFindByData.setEnabled(!textFieldStartDataActivity.getText().isEmpty()
						&& dateIsValid(textFieldStartDataActivity.getText()) != null);
			}
		};

		textFieldStartDataActivity.addKeyListener(textInputDataKeyListener);

		labelStartHourActivity = new JLabel("Start Hour:");
		formActivityPanel.add(labelStartHourActivity);

		textFieldStartHourActivity = new JFormattedTextField(new SimpleDateFormat(HOUR_FORMAT));
		formActivityPanel.add(textFieldStartHourActivity);

		labelEndDataActivity = new JLabel("End Data:");
		formActivityPanel.add(labelEndDataActivity);

		textFieldEndDataActivity = new JFormattedTextField(new SimpleDateFormat(DATE_FORMAT));
		formActivityPanel.add(textFieldEndDataActivity);

		labelEndHourActivity = new JLabel("End Hour:");
		formActivityPanel.add(labelEndHourActivity);

		textFieldEndHourActivity = new JFormattedTextField(new SimpleDateFormat(HOUR_FORMAT));
		formActivityPanel.add(textFieldEndHourActivity);

		labelNewActivity = new JLabel("Activity");
		newActivityPanel.add(labelNewActivity, BorderLayout.NORTH);

		buttonsFormActivityPanel = new JPanel();
		newActivityPanel.add(buttonsFormActivityPanel, BorderLayout.SOUTH);

		// BUTTON ADD ACTIVITY
		btnAddActivity = new JButton("Add Activity");
		btnAddActivity.setEnabled(false);
		buttonsFormActivityPanel.add(btnAddActivity);
		btnAddActivity.addActionListener(e -> actionListenerAddButton());

		KeyAdapter textInputKeyListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
			}
		};

		textFieldStartHourActivity.addKeyListener(textInputKeyListener);
		textFieldEndDataActivity.addKeyListener(textInputKeyListener);
		textFieldEndHourActivity.addKeyListener(textInputKeyListener);

		// BUTTON UPDATE ACTIVITY
		btnUpdateActivity = new JButton("UpdateActivity");
		buttonsFormActivityPanel.add(btnUpdateActivity);
		btnUpdateActivity.setEnabled(false);
		btnUpdateActivity.addActionListener(e -> actionListenerUpdateButton());

		// LIST ACTIVITIES PANEL
		listActivitiesPanel = new JPanel();
		add(listActivitiesPanel);
		listActivitiesPanel.setLayout(new GridLayout(0, 1, 0, 0));

		// TOP MENU PANEL
		listTopMenuPanel = new JPanel();
		listActivitiesPanel.add(listTopMenuPanel);
		listTopMenuPanel.setLayout(new GridLayout(0, 4, 0, 0));

		// BUTTON SHOW ALL
		btnShowAll = new JButton("ShowAll");
		listTopMenuPanel.add(btnShowAll);
		btnShowAll.addActionListener(e -> actionListenerShowAllButton());

		// BUTTON FIND BY OPERATOR
		btnFindByOperator = new JButton("FindByOperator");
		listTopMenuPanel.add(btnFindByOperator);
		btnFindByOperator.setEnabled(false);
		btnFindByOperator.addActionListener(e -> actionListenerFindByOperatorButton());

		// BUTTON FIND BY BASIC OPERATION
		btnFindByBasicOperation = new JButton("FindByOperation");
		listTopMenuPanel.add(btnFindByBasicOperation);
		btnFindByBasicOperation.setEnabled(false);
		btnFindByBasicOperation.addActionListener(e -> actionListenerFindByBasicOperationButton());

		// BUTTON FIND BY DATA
		btnFindByData = new JButton("FindByData");
		listTopMenuPanel.add(btnFindByData);
		btnFindByData.setEnabled(false);
		btnFindByData.addActionListener(e -> actionListenerFindByDataButton());

		// LIST ACTIVITIES
		listActivitiesModel = new DefaultListModel<>();
		listActivities = new JList<>(listActivitiesModel);
		listActivities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listActivitiesPanel.add(listActivities);
		listActivities.addListSelectionListener(e -> actionListenerListActivities());
		
		// BOTTOM MENU PANEL
		listBottomMenuPanel = new JPanel();
		listActivitiesPanel.add(listBottomMenuPanel);
		listBottomMenuPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// MODIFY ACTIVITY BUTTON
		btnModifyActivity = new JButton("MODIFY");
		listBottomMenuPanel.add(btnModifyActivity);
		btnModifyActivity.setEnabled(false);
		btnModifyActivity.addActionListener(e -> actionListenerModifyButton());

		// DELETE ACTIVITY BUTTON
		btnDeleteActivity = new JButton("DELETE");
		listBottomMenuPanel.add(btnDeleteActivity);
		btnDeleteActivity.setEnabled(false);
		btnDeleteActivity.addActionListener(e -> actionListenerDeleteButton());

		// Call Set Names
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

		buttonsFormActivityPanel.setName("buttonsFormActivityPanel");
		btnAddActivity.setName("btnAddActivity");
		btnUpdateActivity.setName("btnUpdateActivity");

		btnShowAll.setName("btnShowAll");
		btnFindByOperator.setName("btnFindByOperator");
		btnFindByBasicOperation.setName("btnFindByBasicOperation");
		btnFindByData.setName("btnFindByData");

		listActivitiesPanel.setName("listActivitiesPanel");
		listTopMenuPanel.setName("listTopMenuPanel");
		listActivities.setName("listActivities");
		listBottomMenuPanel.setName("listBottomMenuPanel");
		btnModifyActivity.setName("btnModifyActivity");
		btnDeleteActivity.setName("btnDeleteActivity");

	}

	public DefaultComboBoxModel<Operator> getComboBoxOperatorsModel() {
		return comboBoxOperatorsModel;
	}

	public DefaultComboBoxModel<BasicOperation> getComboBoxOperationsModel() {
		return comboBoxOperationsModel;
	}

	public DefaultListModel<Activity> getListActivitiesModel() {
		return listActivitiesModel;
	}

	public void setActivityController(ActivityController activityController) {
		this.activityController = activityController;
	}

	public String getActivityIdTemp() {
		return activityIdTemp;
	}

	private boolean setButtonAddEnabled() {
		return !updateInProgress && statusFieldCompiled();
	}

	private boolean setButtonUpdateEnabled() {
		return updateInProgress && statusFieldCompiled();
	}

	private boolean statusFieldCompiled() {
		return comboBoxOperatorActivity.getSelectedIndex() != -1
				&& comboBoxBasicOperationActivity.getSelectedIndex() != -1
				&& !textFieldStartDataActivity.getText().isEmpty() && !textFieldStartHourActivity.getText().isEmpty()
				&& !textFieldEndDataActivity.getText().isEmpty() && !textFieldEndHourActivity.getText().isEmpty()
				&& dateIsValid(textFieldStartDataActivity.getText()) != null
				&& hourIsValid(textFieldStartHourActivity.getText()) != null
				&& dateIsValid(textFieldEndDataActivity.getText()) != null
				&& hourIsValid(textFieldEndHourActivity.getText()) != null;
	}

	private Date dateIsValid(String dateString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			return dateFormatter.parse(dateString);
		} catch (ParseException e) {
			return null;
		}

	}

	private Date hourIsValid(String hourString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HOUR_FORMAT);
		try {
			dateFormatter.parse(hourString);
			return dateFormatter.parse(hourString);
		} catch (ParseException e) {
			return null;
		}

	}

	// ACTION LISTENERS

	private void actionListenerComboBoxOperatorsModel() {
		btnAddActivity.setEnabled(setButtonAddEnabled());
		btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
		btnFindByOperator.setEnabled(comboBoxOperatorActivity.getSelectedIndex() != -1);
	}

	private void actionListenerComboBoxOperationsModel() {
		btnFindByBasicOperation.setEnabled(comboBoxBasicOperationActivity.getSelectedIndex() != -1);
	}

	private void actionListenerAddButton() {
		Operator operator = comboBoxOperatorsModel.getElementAt(comboBoxOperatorActivity.getSelectedIndex());
		BasicOperation basicOperation = comboBoxOperationsModel
				.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex());

		Date startData = transformDayHourInDate(dateIsValid(textFieldStartDataActivity.getText()),
				hourIsValid(textFieldStartHourActivity.getText()));
		Date endData = transformDayHourInDate(dateIsValid(textFieldEndDataActivity.getText()),
				hourIsValid(textFieldEndHourActivity.getText()));

		Activity newActivity = new Activity(activityIdTemp, operator.getMatricola(), basicOperation.getId(), startData,
				endData);

		activityController.addActivity(newActivity);
		activityIdTemp = new ObjectId().toString();
	}

	private void actionListenerUpdateButton() {
		Operator operator = comboBoxOperatorsModel.getElementAt(comboBoxOperatorActivity.getSelectedIndex());
		BasicOperation basicOperation = comboBoxOperationsModel
				.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex());
		Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());

		Date startData = transformDayHourInDate(dateIsValid(textFieldStartDataActivity.getText()),
				hourIsValid(textFieldStartHourActivity.getText()));
		Date endData = transformDayHourInDate(dateIsValid(textFieldEndDataActivity.getText()),
				hourIsValid(textFieldEndHourActivity.getText()));

		Activity updatedActivity = new Activity(activitySelected.getId(), operator.getMatricola(),
				basicOperation.getId(), startData, endData);
		activityController.updadeActivity(updatedActivity);

		updateInProgress = false;
		listActivities.setEnabled(true);
		btnDeleteActivity.setEnabled(true);
		comboBoxOperatorActivity.setSelectedIndex(0);
		comboBoxBasicOperationActivity.setSelectedIndex(0);
		textFieldStartDataActivity.setText("");
		textFieldStartHourActivity.setText("");
		textFieldEndDataActivity.setText("");
		textFieldEndHourActivity.setText("");
		btnUpdateActivity.setEnabled(false);
		btnAddActivity.setEnabled(false);
	}

	private void actionListenerShowAllButton() {
		activityController.allActivities();
	}

	private void actionListenerFindByOperatorButton() {
		activityController.findByOperator(
				comboBoxOperatorsModel.getElementAt(comboBoxOperatorActivity.getSelectedIndex()).getMatricola());
	}

	private void actionListenerFindByBasicOperationButton() {
		activityController.findByBasicOperation(
				comboBoxOperationsModel.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex()).getId());
	}

	private void actionListenerFindByDataButton() {
		Date startData = dateIsValid(textFieldStartDataActivity.getText());
		activityController.findByDay(startData);
	}

	private void actionListenerModifyButton() {
		updateInProgress = true;
		Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());
		int indexOperator = 0;
		for (int i = 0; i < comboBoxOperatorsModel.getSize(); i++) {
			if (comboBoxOperatorsModel.getElementAt(i).getMatricola().equals(activitySelected.getOperatorMatricola())) {
				indexOperator = i;
				break;
			}
			indexOperator = 0;
		}
		int indexBasicOperation = 0;
		for (int i = 0; i < comboBoxOperationsModel.getSize(); i++) {
			if (comboBoxOperationsModel.getElementAt(i).getId().equals(activitySelected.getOperationId())) {
				indexBasicOperation = i;
				break;
			}
			indexBasicOperation = 0;
		}

		comboBoxOperatorActivity.setSelectedIndex(indexOperator);
		comboBoxBasicOperationActivity.setSelectedIndex(indexBasicOperation);

		Date startTime = activitySelected.getStartTime();
		Date endTime = activitySelected.getEndTime();
		String formattedStartDate = new SimpleDateFormat(DATE_FORMAT).format(startTime);
		String formattedStartHour = new SimpleDateFormat(HOUR_FORMAT).format(startTime);
		String formattedEndDate = new SimpleDateFormat(DATE_FORMAT).format(endTime);
		String formattedEndHour = new SimpleDateFormat(HOUR_FORMAT).format(endTime);

		textFieldStartDataActivity.setText(formattedStartDate);
		textFieldStartHourActivity.setText(formattedStartHour);
		textFieldEndDataActivity.setText(formattedEndDate);
		textFieldEndHourActivity.setText(formattedEndHour);

		btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
		btnAddActivity.setEnabled(setButtonAddEnabled());
		btnDeleteActivity.setEnabled(false);
		listActivities.setEnabled(false);
	}

	private void actionListenerListActivities() {
		btnDeleteActivity.setEnabled(listActivities.getSelectedIndex() != -1);
		btnModifyActivity.setEnabled(listActivities.getSelectedIndex() != -1);
	}

	private void actionListenerDeleteButton() {
		Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());
		activityController.removeActivity(activitySelected);
	}

	private Date transformDayHourInDate(Date date, Date hour) {
		Calendar calTempHour = Calendar.getInstance();
		calTempHour.setTime(hour);
		Calendar calFinal = Calendar.getInstance();
		calFinal.setTime(date);
		calFinal.set(Calendar.HOUR_OF_DAY, calTempHour.get(Calendar.HOUR_OF_DAY));
		calFinal.set(Calendar.MINUTE, calTempHour.get(Calendar.MINUTE));
		calFinal.set(Calendar.SECOND, calTempHour.get(Calendar.SECOND));
		return calFinal.getTime();
	}

	
	//INTERFACE METHODS IMPLEMENTED
	@Override
	public void showActivities(List<Activity> activities) {
		listActivitiesModel.clear();
		activities.stream().forEach(listActivitiesModel::addElement);
	}
	

	@Override
	public void showSuccessfull(String string) {
	}

	@Override
	public void showError(String string) {
		
	}

}
