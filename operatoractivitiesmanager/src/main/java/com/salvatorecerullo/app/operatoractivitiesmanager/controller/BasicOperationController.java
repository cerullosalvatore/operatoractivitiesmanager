package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

public class BasicOperationController {
	private static final String BASICOPERATION = "The BasicOperation with ID: ";
	private static final String NOTEXIST = " does not exist.";

	private BasicOperationRepository basicOperationRepository;
	private BasicOperationView basicOperationView;

	public BasicOperationController(BasicOperationRepository basicOperationRepository,
			BasicOperationView basicOperationView) {
		super();
		this.basicOperationRepository = basicOperationRepository;
		this.basicOperationView = basicOperationView;
	}

	public void allBasicOperations() {
		basicOperationView.showAllBasicOperations(basicOperationRepository.findAll());
	}

	public void addBasicOperation(BasicOperation basicOperation) {
		if (basicOperationRepository.findById(basicOperation.getId()) == null) {
			basicOperationRepository.save(basicOperation);
			basicOperationView.showSuccessfull(BASICOPERATION + basicOperation.getId() + " has been added.");
		} else {
			basicOperationView.showError(BASICOPERATION + basicOperation.getId() + " already exist.");
		}
	}

	public void removeBasicOperation(BasicOperation basicOperation) {
		if (basicOperationRepository.findById(basicOperation.getId()) != null) {
			basicOperationRepository.delete(basicOperation.getId());
			basicOperationView.showSuccessfull(BASICOPERATION + basicOperation.getId() + " has been removed.");
		} else {
			basicOperationView.showError(BASICOPERATION + basicOperation.getId() + NOTEXIST);
		}
	}

	public void updateBasicOperation(BasicOperation newBasicOperation) {
		if (basicOperationRepository.findById(newBasicOperation.getId()) != null) {
			basicOperationRepository.update(newBasicOperation);
			basicOperationView.showSuccessfull(BASICOPERATION + newBasicOperation.getId() + " has been updated.");
		} else {
			basicOperationView.showError(BASICOPERATION + newBasicOperation.getId() + NOTEXIST);
		}
	}

}
