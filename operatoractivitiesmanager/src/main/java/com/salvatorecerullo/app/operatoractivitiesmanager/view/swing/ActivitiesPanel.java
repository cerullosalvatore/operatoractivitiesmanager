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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JList;

import org.bson.types.ObjectId;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

import java.awt.Dimension;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;

public class ActivitiesPanel extends JPanel implements ActivityView {
	private static final long serialVersionUID = 1L;
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
	private JLabel lblMessageStatus;
	private JPanel listBottomMenuPanelButton;

	private boolean updateInProgress;
	private String activityIdTemp;

	private transient ActivityController activityController;
	private JLabel labelId;
	private JTextField textFieldIdActivity;

	public ActivitiesPanel() {
		// INITIALIZING VARIABLE
		activityIdTemp = new ObjectId().toString();
		updateInProgress = false;

		setMinimumSize(new Dimension(0, 0));
		setLayout(new GridLayout(0, 1, 0, 0));

		// ACTIVITY PANEL
		newActivityPanel = new JPanel();
		add(newActivityPanel);
		newActivityPanel.setLayout(new BorderLayout(0, 0));

		// LABEL NEW ACTIVITY
		labelNewActivity = new JLabel("Activity");
		newActivityPanel.add(labelNewActivity, BorderLayout.NORTH);

		// LABEL FORM ACTIVITY PANEL
		formActivityPanel = new JPanel();
		newActivityPanel.add(formActivityPanel, BorderLayout.CENTER);
		formActivityPanel.setLayout(new GridLayout(0, 2, 0, 0));

		// LABEL ID
		labelId = new JLabel("New label");
		formActivityPanel.add(labelId);

		// FIELD ID
		textFieldIdActivity = new JTextField();
		formActivityPanel.add(textFieldIdActivity);
		textFieldIdActivity.setText(activityIdTemp);
		textFieldIdActivity.setColumns(10);
		textFieldIdActivity.setEditable(false);

		// LABEL OPERATOR
		labelOperatorActivity = new JLabel("Operator:");
		formActivityPanel.add(labelOperatorActivity);

		// COMBO BOX OPERATOR
		comboBoxOperatorsModel = new DefaultComboBoxModel<>();
		comboBoxOperatorActivity = new JComboBox<>(comboBoxOperatorsModel);
		formActivityPanel.add(comboBoxOperatorActivity);
		comboBoxOperatorActivity.addActionListener(e -> actionListenerComboBoxOperatorsModel());

		// LABEL BASIC OPERATION
		labelBasicOperationActivity = new JLabel("Basic Operation:");
		formActivityPanel.add(labelBasicOperationActivity);

		// COMBO BOX BASIC OPERATION
		comboBoxOperationsModel = new DefaultComboBoxModel<>();
		comboBoxBasicOperationActivity = new JComboBox<>(comboBoxOperationsModel);
		formActivityPanel.add(comboBoxBasicOperationActivity);
		comboBoxBasicOperationActivity.addActionListener(e -> actionListenerComboBoxOperationsModel());

		// LABEL START DATA
		labelStartDataActivity = new JLabel("Start Data:");
		formActivityPanel.add(labelStartDataActivity);

		// FIELD DATA START
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setLenient(false);
		textFieldStartDataActivity = new JFormattedTextField(df);
		textFieldStartDataActivity.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formActivityPanel.add(textFieldStartDataActivity);
		textFieldStartDataActivity.addKeyListener(getKeyListenerDataTextField());

		// LABEL START HOUR
		labelStartHourActivity = new JLabel("Start Hour:");
		formActivityPanel.add(labelStartHourActivity);

		// FIELD START DATA
		SimpleDateFormat hf = new SimpleDateFormat(HOUR_FORMAT);
		textFieldEndHourActivity = new JFormattedTextField(hf);
		textFieldStartHourActivity = new JFormattedTextField(hf);
		textFieldStartHourActivity.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formActivityPanel.add(textFieldStartHourActivity);
		textFieldStartHourActivity.addKeyListener(getKeyListenerInputTextField());

		// LABEL END DATA
		labelEndDataActivity = new JLabel("End Data:");
		formActivityPanel.add(labelEndDataActivity);

		// FIELD END DATA
		textFieldEndDataActivity = new JFormattedTextField(df);
		textFieldEndDataActivity.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formActivityPanel.add(textFieldEndDataActivity);
		textFieldEndDataActivity.addKeyListener(getKeyListenerInputTextField());

		// LABEL END HOUR
		labelEndHourActivity = new JLabel("End Hour:");
		formActivityPanel.add(labelEndHourActivity);

		// FIELD END HOUR
		textFieldEndHourActivity.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formActivityPanel.add(textFieldEndHourActivity);
		textFieldEndHourActivity.addKeyListener(getKeyListenerInputTextField());

		// PANEL BUTTONS FORM
		buttonsFormActivityPanel = new JPanel();
		newActivityPanel.add(buttonsFormActivityPanel, BorderLayout.SOUTH);

		// BUTTON ADD ACTIVITY
		btnAddActivity = new JButton("Add Activity");
		btnAddActivity.setEnabled(false);
		buttonsFormActivityPanel.add(btnAddActivity);
		btnAddActivity.addActionListener(e -> actionListenerAddButton());

		// BUTTON UPDATE ACTIVITY
		btnUpdateActivity = new JButton("UpdateActivity");
		buttonsFormActivityPanel.add(btnUpdateActivity);
		btnUpdateActivity.setEnabled(false);
		btnUpdateActivity.addActionListener(e -> actionListenerUpdateButton());

		// LIST ACTIVITIES PANEL
		listActivitiesPanel = new JPanel();
		add(listActivitiesPanel);
		listActivitiesPanel.setLayout(new BorderLayout(0, 0));

		// BOTTOM MENU PANEL
		listBottomMenuPanel = new JPanel();
		listActivitiesPanel.add(listBottomMenuPanel, BorderLayout.SOUTH);
		listBottomMenuPanel.setLayout(new GridLayout(2, 1, 0, 0));

		// PANEL BOTTOM MENU
		listBottomMenuPanelButton = new JPanel();
		listBottomMenuPanel.add(listBottomMenuPanelButton);

		// MODIFY ACTIVITY BUTTON
		btnModifyActivity = new JButton("MODIFY");
		listBottomMenuPanelButton.add(btnModifyActivity);
		btnModifyActivity.setEnabled(false);

		// DELETE ACTIVITY BUTTON
		btnDeleteActivity = new JButton("DELETE");
		listBottomMenuPanelButton.add(btnDeleteActivity);
		btnDeleteActivity.setEnabled(false);

		// LABEL MESSAGE STATUS
		lblMessageStatus = new JLabel("");
		listBottomMenuPanel.add(lblMessageStatus);
		btnDeleteActivity.addActionListener(e -> actionListenerDeleteButton());
		btnModifyActivity.addActionListener(e -> actionListenerModifyButton());

		// TOP MENU PANEL
		listTopMenuPanel = new JPanel();
		listActivitiesPanel.add(listTopMenuPanel, BorderLayout.NORTH);
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
		listActivitiesPanel.add(listActivities, BorderLayout.CENTER);
		listActivities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listActivities.addListSelectionListener(e -> actionListenerListActivities());

		// Call Set Names
		setNames();

	}

	// INTERFACE METHODS IMPLEMENTED
	@Override
	public void showActivities(List<Activity> activities) {
		listActivitiesModel.clear();
		activities.stream().forEach(listActivitiesModel::addElement);
		lblMessageStatus.setText("");
	}

	@Override
	public void showSuccessfull(String string) {
		activityController.allActivities();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.GREEN);
	}

	@Override
	public void showError(String string) {
		activityController.allActivities();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.RED);
	}

	@Override
	public void showOperators(List<Operator> operators) {
		comboBoxOperatorsModel.removeAllElements();
		operators.stream().forEach(comboBoxOperatorsModel::addElement);
	}

	@Override
	public void showBasicOperation(List<BasicOperation> basicOperations) {
		comboBoxOperationsModel.removeAllElements();
		basicOperations.stream().forEach(comboBoxOperationsModel::addElement);
	}

	// GETTERS AND SETTERS

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

	public ActivityController getActivityController() {
		return activityController;
	}

	// KEY LISTENERS

	private KeyAdapter getKeyListenerDataTextField() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
				btnFindByData.setEnabled(!textFieldStartDataActivity.getText().isEmpty()
						&& dateIsValid(textFieldStartDataActivity.getText()) != null);
			}
		};
	}

	private KeyAdapter getKeyListenerInputTextField() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
			}
		};
	}

	// ACTION LISTENERS

	private void actionListenerComboBoxOperatorsModel() {
		btnAddActivity.setEnabled(setButtonAddEnabled());
		btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
		btnFindByOperator.setEnabled(comboBoxOperatorActivity.getSelectedIndex() != -1);
	}

	private void actionListenerComboBoxOperationsModel() {
		btnAddActivity.setEnabled(setButtonAddEnabled());
		btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
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

		textFieldIdActivity.setText(activityIdTemp);
		textFieldStartDataActivity.setText("");
		textFieldStartHourActivity.setText("");
		textFieldEndDataActivity.setText("");
		textFieldEndHourActivity.setText("");
		comboBoxOperatorActivity.setSelectedIndex(0);
		comboBoxBasicOperationActivity.setSelectedIndex(0);
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
		textFieldIdActivity.setText(activityIdTemp);
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
		activityController.allOperators();
		activityController.allBasicOperation();
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

		textFieldIdActivity.setText(activitySelected.getId());

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

	// UTILITIES

	private void setNames() {
		newActivityPanel.setName("newActivityPanel");
		labelNewActivity.setName("labelNewActivity");

		formActivityPanel.setName("formActivityPanel");
		labelId.setName("labelId");
		textFieldIdActivity.setName("textFieldIdActivity");
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

		lblMessageStatus.setName("lblMessageStatus");
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
		dateFormatter.setLenient(false);
		try {
			return dateFormatter.parse(dateString);
		} catch (ParseException e) {
			return null;
		}

	}

	private Date hourIsValid(String hourString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HOUR_FORMAT);
		dateFormatter.setLenient(false);

		try {
			dateFormatter.parse(hourString);
			return dateFormatter.parse(hourString);
		} catch (ParseException e) {
			return null;
		}
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

}