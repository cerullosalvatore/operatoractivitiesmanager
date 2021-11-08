package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

import javax.swing.JButton;

public class OperatorsPanel extends JPanel {

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
	private JPanel listBottomMenuPanel;
	private JPanel listBottomMenuPanelButton;
	private JButton btnModifyOperator;
	private JButton btnDeleteOperator;
	private JLabel lblMessageStatus;

	/**
	 * Create the panel.
	 */
	public OperatorsPanel() {
		setLayout(new GridLayout(2, 0, 0, 0));

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

		lblName = new JLabel("Name:");
		formOperatorPanel.add(lblName);

		textFieldName = new JTextField();
		formOperatorPanel.add(textFieldName);
		textFieldName.setColumns(10);

		lblSurname = new JLabel("Surname:");
		formOperatorPanel.add(lblSurname);

		textFieldSurname = new JTextField();
		formOperatorPanel.add(textFieldSurname);
		textFieldSurname.setColumns(10);

		buttonsFormOperatorPanel = new JPanel();
		newOperatorPanel.add(buttonsFormOperatorPanel, BorderLayout.SOUTH);

		btnAddOperator = new JButton("Add Operator");
		buttonsFormOperatorPanel.add(btnAddOperator);
		btnAddOperator.setEnabled(false);

		btnUpdateOperator = new JButton("Update Operator");
		buttonsFormOperatorPanel.add(btnUpdateOperator);
		btnUpdateOperator.setEnabled(false);
		
		listOperatorsPanel = new JPanel();
		add(listOperatorsPanel);
		listOperatorsPanel.setLayout(new BorderLayout(0, 0));

		listOperators = new JList<>();
		listOperatorsPanel.add(listOperators, BorderLayout.CENTER);

		listBottomMenuPanel = new JPanel();
		listOperatorsPanel.add(listBottomMenuPanel, BorderLayout.SOUTH);
		listBottomMenuPanel.setLayout(new GridLayout(2, 1, 0, 0));

		listBottomMenuPanelButton = new JPanel();
		listBottomMenuPanel.add(listBottomMenuPanelButton);

		btnModifyOperator = new JButton("MODIFY");
		listBottomMenuPanelButton.add(btnModifyOperator);
		btnModifyOperator.setEnabled(false);
		
		btnDeleteOperator = new JButton("DELETE");
		listBottomMenuPanelButton.add(btnDeleteOperator);
		btnDeleteOperator.setEnabled(false);
		
		lblMessageStatus = new JLabel("");
		listBottomMenuPanel.add(lblMessageStatus);

		setNames();
	}

	// UTILITY

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
}
