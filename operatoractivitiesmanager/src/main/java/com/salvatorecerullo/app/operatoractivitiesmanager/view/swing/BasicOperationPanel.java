package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.bson.types.ObjectId;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class BasicOperationPanel extends JPanel implements BasicOperationView {
	private static final long serialVersionUID = 1L;

	private JTextField textFieldId;
	private JTextField textFieldName;
	private JPanel newBasicOperationPanel;
	private JLabel lblOperationDetails;
	private JPanel formBasicOperationPanel;
	private JLabel lblId;
	private JLabel lblName;
	private JLabel lblDescription;
	private JTextArea textAreaDescription;
	private JPanel buttonsFormBasicOperationPanel;
	private JButton btnAddOperation;
	private JButton btnUpdateOperation;
	private JPanel listBasicOperationsPanel;
	private JList<BasicOperation> listBasicOperations;
	private DefaultListModel<BasicOperation> listBasicOpeationModel;
	private JPanel listBottomMenuPanel;
	private JPanel listBottomMenuPanelButton;
	private JButton btnModify;
	private JButton btnDelete;
	private JLabel lblMessageStatus;

	private Boolean updateInProgress;
	private String basicOperationIdTemp;
	private transient BasicOperationController basicOperationController;

	public BasicOperationPanel() {
		// INIZIALIZING VARIABLE
		basicOperationIdTemp = new ObjectId().toString();
		updateInProgress = false;

		setLayout(new GridLayout(0, 1, 0, 0));

		// PANEL BASIC OPERATION
		newBasicOperationPanel = new JPanel();
		add(newBasicOperationPanel);
		newBasicOperationPanel.setLayout(new BorderLayout(0, 0));

		// LABEL OPERATION DETAILS
		lblOperationDetails = new JLabel("BasicOperationDetails");
		newBasicOperationPanel.add(lblOperationDetails, BorderLayout.NORTH);

		// PANEL FORM BASIC OPERATION
		formBasicOperationPanel = new JPanel();
		newBasicOperationPanel.add(formBasicOperationPanel);
		formBasicOperationPanel.setLayout(new GridLayout(3, 2, 0, 0));

		// LABEL ID
		lblId = new JLabel("ID:");
		formBasicOperationPanel.add(lblId);

		// TEXT FIELD ID
		textFieldId = new JTextField();
		formBasicOperationPanel.add(textFieldId);
		textFieldId.setColumns(10);
		textFieldId.setText(basicOperationIdTemp);
		textFieldId.setEditable(false);

		// LABEL NAME
		lblName = new JLabel("Name:");
		formBasicOperationPanel.add(lblName);

		// TEXT FIELD NAME
		textFieldName = new JTextField();
		formBasicOperationPanel.add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.addKeyListener(getKeyListenerTextField());

		// LABEL DESCRIPTION
		lblDescription = new JLabel("Description:");
		formBasicOperationPanel.add(lblDescription);

		// TEXT AREA DESCRIPTION
		textAreaDescription = new JTextArea();
		formBasicOperationPanel.add(textAreaDescription);
		textAreaDescription.addKeyListener(getKeyListenerTextField());

		// PANEL BUTTONS FORM
		buttonsFormBasicOperationPanel = new JPanel();
		newBasicOperationPanel.add(buttonsFormBasicOperationPanel, BorderLayout.SOUTH);

		// BUTTON ADD OPERATION
		btnAddOperation = new JButton("Add Operation");
		buttonsFormBasicOperationPanel.add(btnAddOperation);
		btnAddOperation.setEnabled(false);
		btnAddOperation.addActionListener(e -> actionListenerAddButton());

		// BUTTON UPDATE OPERATION
		btnUpdateOperation = new JButton("Update Operation");
		buttonsFormBasicOperationPanel.add(btnUpdateOperation);
		btnUpdateOperation.setEnabled(false);
		btnUpdateOperation.addActionListener(e -> actionListenerUpdateButton());

		// PANEL LIST BASIC OPERATION
		listBasicOperationsPanel = new JPanel();
		add(listBasicOperationsPanel);
		listBasicOperationsPanel.setLayout(new BorderLayout(0, 0));

		// LIST BASIC OPERATION
		listBasicOpeationModel = new DefaultListModel<>();
		listBasicOperations = new JList<>(listBasicOpeationModel);
		listBasicOperationsPanel.add(listBasicOperations, BorderLayout.CENTER);
		listBasicOperations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBasicOperations.addListSelectionListener(e -> actionListenerListOperators());

		// PANEL BOTOM MENU
		listBottomMenuPanel = new JPanel();
		listBasicOperationsPanel.add(listBottomMenuPanel, BorderLayout.SOUTH);
		listBottomMenuPanel.setLayout(new GridLayout(2, 0, 0, 0));

		// PANEL BOTTOM MENU BUTTON
		listBottomMenuPanelButton = new JPanel();
		listBottomMenuPanel.add(listBottomMenuPanelButton);

		// BUTTON MODIFY
		btnModify = new JButton("MODIFY");
		listBottomMenuPanelButton.add(btnModify);
		btnModify.setEnabled(false);
		btnModify.addActionListener(e -> actionListenerModifyButton());

		// BUTTON DELETE
		btnDelete = new JButton("DELETE");
		listBottomMenuPanelButton.add(btnDelete);
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(e -> actionListenerDeleteButton());

		// LABEL MESSAGE STATUS
		lblMessageStatus = new JLabel("");
		listBottomMenuPanel.add(lblMessageStatus);

		setNames();
	}

	// INTERFACE METHODS
	@Override
	public void showAllBasicOperations(List<BasicOperation> basicOperations) {
		listBasicOpeationModel.clear();
		basicOperations.stream().forEach(listBasicOpeationModel::addElement);
		lblMessageStatus.setText("");
	}

	@Override
	public void showSuccessfull(String string) {
		basicOperationController.allBasicOperations();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.GREEN);
	}

	@Override
	public void showError(String string) {
		basicOperationController.allBasicOperations();
		lblMessageStatus.setText(string);
		lblMessageStatus.setForeground(Color.RED);
	}

	// GETTERS AND SETTERS
	public String getBasicOperationIdTemp() {
		return basicOperationIdTemp;
	}

	public BasicOperationController getBasicOperationController() {
		return basicOperationController;
	}

	public DefaultListModel<BasicOperation> getListBasicOpeationModel() {
		return listBasicOpeationModel;
	}

	public void setBasicOperationController(BasicOperationController basicOperationController) {
		this.basicOperationController = basicOperationController;
	}
	// ACTION KEY LISTENERS

	private KeyAdapter getKeyListenerTextField() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddOperation.setEnabled(setButtonAddEnabled());
				btnUpdateOperation.setEnabled(setButtonUpdateEnabled());
			}
		};
	}

	// ACTION LISTENERSS

	private void actionListenerModifyButton() {
		updateInProgress = true;

		BasicOperation basicOperationSelected = listBasicOpeationModel
				.getElementAt(listBasicOperations.getSelectedIndex());

		textFieldId.setText(basicOperationSelected.getId());
		textFieldName.setText(basicOperationSelected.getName());
		textAreaDescription.setText(basicOperationSelected.getDescription());

		btnUpdateOperation.setEnabled(true);
		btnAddOperation.setEnabled(false);
		btnDelete.setEnabled(false);
		listBasicOperations.setEnabled(false);
	}

	private void actionListenerUpdateButton() {
		updateInProgress = false;

		BasicOperation basicOperationSelected = listBasicOpeationModel
				.getElementAt(listBasicOperations.getSelectedIndex());
		BasicOperation basicOperationUpdated = new BasicOperation(basicOperationSelected.getId(),
				textFieldName.getText(), textAreaDescription.getText());
		basicOperationController.updateBasicOperation(basicOperationUpdated);
		listBasicOperations.setEnabled(true);
		textFieldId.setText(basicOperationIdTemp);
		textFieldName.setText("");
		textAreaDescription.setText("");
		btnUpdateOperation.setEnabled(false);
		btnAddOperation.setEnabled(false);
	}

	private void actionListenerListOperators() {
		btnModify.setEnabled(listBasicOperations.getSelectedIndex() != -1);
		btnDelete.setEnabled(listBasicOperations.getSelectedIndex() != -1);
	}

	private void actionListenerAddButton() {
		BasicOperation basicOperation = new BasicOperation(basicOperationIdTemp, textFieldName.getText(),
				textAreaDescription.getText());
		basicOperationController.addBasicOperation(basicOperation);
		basicOperationIdTemp = new ObjectId().toString();

		textFieldId.setText(basicOperationIdTemp);
		textFieldName.setText("");
		textAreaDescription.setText("");
		btnAddOperation.setEnabled(false);
	}

	private void actionListenerDeleteButton() {
		BasicOperation basicOperation = listBasicOpeationModel.getElementAt(listBasicOperations.getSelectedIndex());
		basicOperationController.removeBasicOperation(basicOperation);
	}

	// UTILITY
	private void setNames() {
		textFieldId.setName("textFieldId");
		textFieldName.setName("textFieldName");
		newBasicOperationPanel.setName("newBasicOperationPanel");
		lblOperationDetails.setName("lblOperationDetails");
		formBasicOperationPanel.setName("formBasicOperationPanel");
		lblId.setName("lblId");
		lblName.setName("lblName");
		lblDescription.setName("lblDescription");
		textAreaDescription.setName("textAreaDescription");
		buttonsFormBasicOperationPanel.setName("buttonsFormBasicOperationPanel");
		btnAddOperation.setName("btnAddOperation");
		btnUpdateOperation.setName("btnUpdateOperation");
		listBasicOperationsPanel.setName("listBasicOperationsPanel");
		listBasicOperations.setName("listBasicOperations");
		listBottomMenuPanel.setName("listBottomMenuPanel");
		listBottomMenuPanelButton.setName("listBottomMenuPanelButton");
		btnModify.setName("btnModify");
		btnDelete.setName("btnDelete");
		lblMessageStatus.setName("lblMessageStatus");
	}

	private boolean setButtonAddEnabled() {
		return !updateInProgress && statusFieldCompiled();
	}

	private boolean setButtonUpdateEnabled() {
		return updateInProgress && statusFieldCompiled();
	}

	private boolean statusFieldCompiled() {
		return !textFieldName.getText().isEmpty() && !textAreaDescription.getText().isEmpty();
	}

}
