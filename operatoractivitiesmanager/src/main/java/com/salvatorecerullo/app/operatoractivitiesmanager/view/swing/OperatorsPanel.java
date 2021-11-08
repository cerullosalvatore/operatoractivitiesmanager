package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class OperatorsPanel extends JPanel implements OperatorView {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldMatricola;
	private JTextField textFieldName;
	private JTextField textFieldSurname;
	private JPanel newOperatorPanel;
	private JLabel lblOperatorDetails;
	private JPanel formOperatorPanel;
	private JLabel lblMatricola;
	private JLabel lblName;
	private JLabel lblSurname;
	private JPanel buttonsFormOperatorPanel;
	private JButton btnAddOperator;
	private JButton btnUpdateOperator;
	private JPanel listOperatorsPanel;
	private JList<Operator> listOperators;
	private DefaultListModel<Operator> listOperatorsModel;
	private JPanel listBottomMenuPanel;
	private JPanel listBottomMenuPanelButton;
	private JButton btnModifyOperator;
	private JButton btnDeleteOperator;
	private JLabel lblMessageStatus;
	private boolean updateInProgress;
	private OperatorController operatorController;

	/**
	 * Create the panel.
	 */
	public OperatorsPanel() {
		setLayout(new GridLayout(2, 0, 0, 0));
		updateInProgress = false;

		newOperatorPanel = new JPanel();
		add(newOperatorPanel);
		newOperatorPanel.setLayout(new BorderLayout(0, 0));

		lblOperatorDetails = new JLabel("Operator  Details");
		newOperatorPanel.add(lblOperatorDetails, BorderLayout.NORTH);

		formOperatorPanel = new JPanel();
		newOperatorPanel.add(formOperatorPanel, BorderLayout.CENTER);
		formOperatorPanel.setLayout(new GridLayout(3, 2, 0, 0));

		lblMatricola = new JLabel("Matricola:");
		formOperatorPanel.add(lblMatricola);

		textFieldMatricola = new JTextField();
		formOperatorPanel.add(textFieldMatricola);
		textFieldMatricola.setColumns(10);
		textFieldMatricola.addKeyListener(getKeyListenerTextField());

		lblName = new JLabel("Name:");
		formOperatorPanel.add(lblName);

		textFieldName = new JTextField();
		formOperatorPanel.add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.addKeyListener(getKeyListenerTextField());

		lblSurname = new JLabel("Surname:");
		formOperatorPanel.add(lblSurname);

		textFieldSurname = new JTextField();
		formOperatorPanel.add(textFieldSurname);
		textFieldSurname.setColumns(10);
		textFieldSurname.addKeyListener(getKeyListenerTextField());

		buttonsFormOperatorPanel = new JPanel();
		newOperatorPanel.add(buttonsFormOperatorPanel, BorderLayout.SOUTH);

		// BUTTON ADD OPERATOR
		btnAddOperator = new JButton("Add Operator");
		buttonsFormOperatorPanel.add(btnAddOperator);
		btnAddOperator.setEnabled(false);
		btnAddOperator.addActionListener(e -> actionListenerAddButton());
		// BUTTON UPATE OPERATOR
		btnUpdateOperator = new JButton("Update Operator");
		buttonsFormOperatorPanel.add(btnUpdateOperator);
		btnUpdateOperator.setEnabled(false);
		btnUpdateOperator.addActionListener(e -> actionListenerUpdateButton());

		listOperatorsPanel = new JPanel();
		add(listOperatorsPanel);
		listOperatorsPanel.setLayout(new BorderLayout(0, 0));

		// LIST OPEATORS
		listOperatorsModel = new DefaultListModel<>();
		listOperators = new JList<>(listOperatorsModel);
		listOperatorsPanel.add(listOperators, BorderLayout.CENTER);
		listOperators.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOperators.addListSelectionListener(e -> actionListenerListOperators());

		listBottomMenuPanel = new JPanel();
		listOperatorsPanel.add(listBottomMenuPanel, BorderLayout.SOUTH);
		listBottomMenuPanel.setLayout(new GridLayout(2, 1, 0, 0));

		listBottomMenuPanelButton = new JPanel();
		listBottomMenuPanel.add(listBottomMenuPanelButton);

		// BUTTON MODIFY
		btnModifyOperator = new JButton("MODIFY");
		listBottomMenuPanelButton.add(btnModifyOperator);
		btnModifyOperator.setEnabled(false);
		btnModifyOperator.addActionListener(e -> actionListenerModifyButton());

		// BUTTON DELETE OPERATOR
		btnDeleteOperator = new JButton("DELETE");
		listBottomMenuPanelButton.add(btnDeleteOperator);
		btnDeleteOperator.setEnabled(false);
		btnDeleteOperator.addActionListener(e -> actionListenereDeleteButton());

		lblMessageStatus = new JLabel("");
		listBottomMenuPanel.add(lblMessageStatus);

		setNames();
	}

	// INTERFACE METHODS IMPLEMENTED
	@Override
	public void showAllOperators(List<Operator> operators) {
		listOperatorsModel.clear();
		operators.stream().forEach(listOperatorsModel::addElement);
		lblMessageStatus.setText("");
	}

	@Override
	public void showSuccessfull(String string) {
		operatorController.allOperators();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.GREEN);
	}

	@Override
	public void showError(String string) {
		operatorController.allOperators();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.RED);
	}

	// GETTERS AND SETTERS
	public void setOperatorController(OperatorController operatorController) {
		this.operatorController = operatorController;
	}

	// ACTION KEY LISTENERS

	private KeyAdapter getKeyListenerTextField() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddOperator.setEnabled(setButtonAddEnabled());
				btnUpdateOperator.setEnabled(setButtonUpdateEnabled());
			}
		};
	}

	// ACTION LISTENERS
	private void actionListenerListOperators() {
		btnModifyOperator.setEnabled(listOperators.getSelectedIndex() != -1);
		btnDeleteOperator.setEnabled(listOperators.getSelectedIndex() != -1);
	}

	private void actionListenerModifyButton() {
		updateInProgress = true;

		// Retrieve selected operator
		Operator operatorSelected = listOperatorsModel.getElementAt(listOperators.getSelectedIndex());

		// Setting the field
		textFieldMatricola.setText(operatorSelected.getMatricola());
		textFieldMatricola.setEditable(false);

		textFieldName.setText(operatorSelected.getName());
		textFieldSurname.setText(operatorSelected.getSurname());

		btnUpdateOperator.setEnabled(setButtonUpdateEnabled());
		btnAddOperator.setEnabled(setButtonAddEnabled());
		btnDeleteOperator.setEnabled(false);
		listOperators.setEnabled(false);
	}

	private void actionListenerUpdateButton() {
		Operator operatorSelected = listOperatorsModel.getElementAt(listOperators.getSelectedIndex());
		Operator operatorUpdated = new Operator(operatorSelected.getMatricola(), textFieldName.getText(),
				textFieldSurname.getText());
		operatorController.updateOperator(operatorUpdated);
		textFieldMatricola.setEditable(true);
		updateInProgress = false;
		listOperators.setEnabled(true);
		btnDeleteOperator.setEnabled(true);
		textFieldMatricola.setText("");
		textFieldName.setText("");
		textFieldSurname.setText("");
		btnUpdateOperator.setEnabled(false);
		btnAddOperator.setEnabled(false);
	}

	private void actionListenerAddButton() {
		Operator newOperator = new Operator(textFieldMatricola.getText(), textFieldName.getText(),
				textFieldSurname.getText());
		operatorController.addOperator(newOperator);
	}

	private void actionListenereDeleteButton() {
		Operator operator = listOperatorsModel.getElementAt(listOperators.getSelectedIndex());
		operatorController.removeOperator(operator);
	}

	// UTILITY

	public DefaultListModel<Operator> getListOperatorsModel() {
		return listOperatorsModel;
	}

	private void setNames() {
		newOperatorPanel.setName("newOperatorPanel");
		textFieldMatricola.setName("textFieldMatricola");
		textFieldName.setName("textFieldName");

		textFieldSurname.setName("textFieldSurname");
		lblOperatorDetails.setName("lblOperatorDetails");
		formOperatorPanel.setName("formOperatorPanel");
		lblMatricola.setName("lblMatricola");
		lblName.setName("lblName");
		lblSurname.setName("lblSurname");
		buttonsFormOperatorPanel.setName("buttonsFormOperatorPanel");
		btnAddOperator.setName("btnAddOperator");
		btnUpdateOperator.setName("btnUpdateOperator");
		listOperatorsPanel.setName("listOperatorsPanel");

		listOperators.setName("listOperators");
		listBottomMenuPanel.setName("listBottomMenuPanel");
		listBottomMenuPanelButton.setName("listBottomMenuPanelButton");
		btnModifyOperator.setName("btnModifyOperator");
		btnDeleteOperator.setName("btnDeleteOperator");
		lblMessageStatus.setName("lblMessageStatus");
	}

	private boolean setButtonAddEnabled() {
		return !updateInProgress && statusFieldCompiled();
	}

	private boolean setButtonUpdateEnabled() {
		return updateInProgress && statusFieldCompiled();
	}

	private boolean statusFieldCompiled() {
		return !textFieldMatricola.getText().isEmpty() && !textFieldName.getText().isEmpty()
				&& !textFieldSurname.getText().isEmpty();
	}

}
