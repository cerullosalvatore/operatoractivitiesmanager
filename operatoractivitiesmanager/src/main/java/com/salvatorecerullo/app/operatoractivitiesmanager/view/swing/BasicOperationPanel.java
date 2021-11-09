package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.bson.types.ObjectId;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class BasicOperationPanel extends JPanel {
	/**
	 * 
	 */
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
	
	private String basicOperationIdTemp;

	/**
	 * Create the panel.
	 */
	public BasicOperationPanel() {
		basicOperationIdTemp = new ObjectId().toString();

		setLayout(new GridLayout(0, 1, 0, 0));
		
		newBasicOperationPanel = new JPanel();
		add(newBasicOperationPanel);
		newBasicOperationPanel.setLayout(new BorderLayout(0, 0));
		
		// LABEL OPERATION DETAILS
		lblOperationDetails = new JLabel("BasicOperationDetails");
		newBasicOperationPanel.add(lblOperationDetails, BorderLayout.NORTH);
		
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
		
		// LABEL DESCRIPTION
		lblDescription = new JLabel("Description:");
		formBasicOperationPanel.add(lblDescription);
		
		// TEXT AREA DESCRIPTION
		textAreaDescription = new JTextArea();
		formBasicOperationPanel.add(textAreaDescription);
		
		buttonsFormBasicOperationPanel = new JPanel();
		newBasicOperationPanel.add(buttonsFormBasicOperationPanel, BorderLayout.SOUTH);
		
		btnAddOperation = new JButton("Add Operation");
		buttonsFormBasicOperationPanel.add(btnAddOperation);
		btnAddOperation.setEnabled(false);
		
		btnUpdateOperation = new JButton("Update Operation");
		buttonsFormBasicOperationPanel.add(btnUpdateOperation);
		btnUpdateOperation.setEnabled(false);
		
		listBasicOperationsPanel = new JPanel();
		add(listBasicOperationsPanel);
		listBasicOperationsPanel.setLayout(new BorderLayout(0, 0));
		
		listBasicOpeationModel = new DefaultListModel<>();
		listBasicOperations = new JList<>(listBasicOpeationModel);
		listBasicOperationsPanel.add(listBasicOperations, BorderLayout.CENTER);
		listBasicOperations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listBottomMenuPanel = new JPanel();
		listBasicOperationsPanel.add(listBottomMenuPanel, BorderLayout.SOUTH);
		listBottomMenuPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		listBottomMenuPanelButton = new JPanel();
		listBottomMenuPanel.add(listBottomMenuPanelButton);
		
		btnModify = new JButton("MODIFY");
		listBottomMenuPanelButton.add(btnModify);
		btnModify.setEnabled(false);
		
		btnDelete = new JButton("DELETE");
		listBottomMenuPanelButton.add(btnDelete);
		btnDelete.setEnabled(false);
		
		lblMessageStatus = new JLabel("");
		listBottomMenuPanel.add(lblMessageStatus);

		setNames();
	}
	
	// GETTERS AND SETTERS
	public String getBasicOperationIdTemp() {
		return basicOperationIdTemp;
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
}
