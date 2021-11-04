package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bson.types.ObjectId;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

import java.awt.Dimension;
import javax.swing.ListSelectionModel;

public class ActivitiesPanel extends JPanel {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6391077047327115858L;
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
		comboBoxOperatorsModel = new DefaultComboBoxModel<Operator>();
		comboBoxOperatorActivity = new JComboBox<Operator>(comboBoxOperatorsModel);
		formActivityPanel.add(comboBoxOperatorActivity);

		ActionListener actionListenerComboBoxOperatorsModel = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
				btnFindByOperator.setEnabled(comboBoxOperatorActivity.getSelectedIndex() != -1);
			}
		};
		comboBoxOperatorActivity.addActionListener(actionListenerComboBoxOperatorsModel);

		labelBasicOperationActivity = new JLabel("Basic Operation:");
		formActivityPanel.add(labelBasicOperationActivity);

		// COMBO BOX BASIC OPERATION
		comboBoxOperationsModel = new DefaultComboBoxModel<BasicOperation>();
		comboBoxBasicOperationActivity = new JComboBox<BasicOperation>(comboBoxOperationsModel);
		formActivityPanel.add(comboBoxBasicOperationActivity);

		ActionListener actionListenerComboBoxOperationsModel = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				btnFindByBasicOperation.setEnabled(comboBoxBasicOperationActivity.getSelectedIndex() != -1);
			}
		};
		comboBoxBasicOperationActivity.addActionListener(actionListenerComboBoxOperationsModel);

		labelStartDataActivity = new JLabel("Start Data:");
		formActivityPanel.add(labelStartDataActivity);

		// FIELD DATA START
		textFieldStartDataActivity = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		formActivityPanel.add(textFieldStartDataActivity);
		KeyAdapter textInputDataKeyListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
				btnFindByData.setEnabled(!textFieldStartDataActivity.getText().isEmpty()
						&& dateIsValid(textFieldStartDataActivity.getText()));
			}
		};

		textFieldStartDataActivity.addKeyListener(textInputDataKeyListener);

		labelStartHourActivity = new JLabel("Start Hour:");
		formActivityPanel.add(labelStartHourActivity);

		textFieldStartHourActivity = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
		formActivityPanel.add(textFieldStartHourActivity);

		labelEndDataActivity = new JLabel("End Data:");
		formActivityPanel.add(labelEndDataActivity);

		textFieldEndDataActivity = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		formActivityPanel.add(textFieldEndDataActivity);

		labelEndHourActivity = new JLabel("End Hour:");
		formActivityPanel.add(labelEndHourActivity);

		textFieldEndHourActivity = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
		formActivityPanel.add(textFieldEndHourActivity);

		labelNewActivity = new JLabel("Activity");
		newActivityPanel.add(labelNewActivity, BorderLayout.NORTH);

		buttonsFormActivityPanel = new JPanel();
		newActivityPanel.add(buttonsFormActivityPanel, BorderLayout.SOUTH);

		// BUTTON ADD ACTIVITY
		btnAddActivity = new JButton("Add Activity");
		btnAddActivity.setEnabled(false);
		buttonsFormActivityPanel.add(btnAddActivity);
		btnAddActivity.addActionListener(getActionListenerAddButton());

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
		btnUpdateActivity.addActionListener(getActionListenerUpdateButton());

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
		btnShowAll.addActionListener(getActionListenerShowAllButton());

		// BUTTON FIND BY OPERATOR
		btnFindByOperator = new JButton("FindByOperator");
		listTopMenuPanel.add(btnFindByOperator);
		btnFindByOperator.setEnabled(false);
		btnFindByOperator.addActionListener(getActionListenerFindByOperatorButton());

		// BUTTON FIND BY BASIC OPERATION
		btnFindByBasicOperation = new JButton("FindByOperation");
		listTopMenuPanel.add(btnFindByBasicOperation);
		btnFindByBasicOperation.setEnabled(false);
		btnFindByBasicOperation.addActionListener(getActionListenerFindByBasicOperationButton());

		// BUTTON FIND BY DATA
		btnFindByData = new JButton("FindByData");
		listTopMenuPanel.add(btnFindByData);
		btnFindByData.setEnabled(false);
		btnFindByData.addActionListener(getActionListenerFindByDataButton());

		// LIST ACTIVITIES
		listActivitiesModel = new DefaultListModel<Activity>();
		listActivities = new JList<Activity>(listActivitiesModel);
		listActivities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listActivitiesPanel.add(listActivities);
		listActivities.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				btnDeleteActivity.setEnabled(listActivities.getSelectedIndex() != -1);
				btnModifyActivity.setEnabled(listActivities.getSelectedIndex() != -1);
			}
		});
		// BOTTOM MENU PANEL
		listBottomMenuPanel = new JPanel();
		listActivitiesPanel.add(listBottomMenuPanel);
		listBottomMenuPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// MODIFY ACTIVITY BUTTON
		btnModifyActivity = new JButton("MODIFY");
		listBottomMenuPanel.add(btnModifyActivity);
		btnModifyActivity.setEnabled(false);
		btnModifyActivity.addActionListener(getActionListenerModifyButton());

		// DELETE ACTIVITY BUTTON
		btnDeleteActivity = new JButton("DELETE");
		listBottomMenuPanel.add(btnDeleteActivity);
		btnDeleteActivity.setEnabled(false);
		btnDeleteActivity.addActionListener(getActionListenerDeleteButton());

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
				&& dateIsValid(textFieldStartDataActivity.getText())
				&& hourIsValid(textFieldStartHourActivity.getText()) && dateIsValid(textFieldEndDataActivity.getText())
				&& hourIsValid(textFieldEndHourActivity.getText());
	}

	private boolean dateIsValid(String dateString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dateFormatter.parse(dateString);
			return true;
		} catch (ParseException e) {
			return false;
		}

	}

	private boolean hourIsValid(String hourString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
		try {
			dateFormatter.parse(hourString);
			return true;
		} catch (ParseException e) {
			return false;
		}

	}

	private ActionListener getActionListenerAddButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Operator operator = comboBoxOperatorsModel.getElementAt(comboBoxOperatorActivity.getSelectedIndex());
				BasicOperation basicOperation = comboBoxOperationsModel
						.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex());
				try {
					Date startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(
							textFieldStartDataActivity.getText() + " " + textFieldStartHourActivity.getText() + ":00");

					Date endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:s").parse(
							textFieldEndDataActivity.getText() + " " + textFieldEndHourActivity.getText() + ":00");

					Activity newActivity = new Activity(activityIdTemp, operator.getMatricola(), basicOperation.getId(),
							startDate, endDate);

					activityController.addActivity(newActivity);
					activityIdTemp = new ObjectId().toString();
				} catch (ParseException e) {

				}
			}
		};
	}

	private ActionListener getActionListenerModifyButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				updateInProgress = true;
				Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());
				int indexOperator = 0;
				for (int i = 0; i < comboBoxOperatorsModel.getSize(); i++) {
					if (comboBoxOperatorsModel.getElementAt(i).getMatricola()
							.equals(activitySelected.getOperatorMatricola())) {
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
				String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
				String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
				String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
				String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

				textFieldStartDataActivity.setText(formattedStartDate);
				textFieldStartHourActivity.setText(formattedStartHour);
				textFieldEndDataActivity.setText(formattedEndDate);
				textFieldEndHourActivity.setText(formattedEndHour);

				btnUpdateActivity.setEnabled(setButtonUpdateEnabled());
				btnAddActivity.setEnabled(setButtonAddEnabled());
				btnDeleteActivity.setEnabled(false);
				listActivities.setEnabled(false);
			}
		};
	}

	private ActionListener getActionListenerDeleteButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());
				activityController.removeActivity(activitySelected);
			}
		};
	}

	private ActionListener getActionListenerShowAllButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				activityController.allActivities();
			}
		};
	}

	private ActionListener getActionListenerFindByOperatorButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				activityController.findByOperator(comboBoxOperatorsModel
						.getElementAt(comboBoxOperatorActivity.getSelectedIndex()).getMatricola());
			}
		};
	}

	private ActionListener getActionListenerFindByBasicOperationButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				activityController.findByBasicOperation(comboBoxOperationsModel
						.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex()).getId());
			}
		};
	}

	private ActionListener getActionListenerFindByDataButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					Date startData = new SimpleDateFormat("dd/MM/yyyy").parse(textFieldStartDataActivity.getText());
					activityController.findByDay(startData);
				} catch (ParseException e) {

				}
			}
		};
	}

	private ActionListener getActionListenerUpdateButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Operator operator = comboBoxOperatorsModel.getElementAt(comboBoxOperatorActivity.getSelectedIndex());
				BasicOperation basicOperation = comboBoxOperationsModel
						.getElementAt(comboBoxBasicOperationActivity.getSelectedIndex());
				Activity activitySelected = listActivitiesModel.getElementAt(listActivities.getSelectedIndex());

				try {
					Date startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(
							textFieldStartDataActivity.getText() + " " + textFieldStartHourActivity.getText() + ":00");

					Date endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:s").parse(
							textFieldEndDataActivity.getText() + " " + textFieldEndHourActivity.getText() + ":00");

					Activity updatedActivity = new Activity(activitySelected.getId(), operator.getMatricola(),
							basicOperation.getId(), startDate, endDate);
					activityController.updadeActivity(updatedActivity);
				} catch (ParseException e) {

				}
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
		};
	}

}
