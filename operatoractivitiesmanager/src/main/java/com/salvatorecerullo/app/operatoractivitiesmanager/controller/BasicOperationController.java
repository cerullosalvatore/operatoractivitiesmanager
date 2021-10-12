package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

public class BasicOperationController {
	private BasicOperationRepository basicOperationRepository;
	private BasicOperationView basicOperationView;

	public void allBasicOperations() {
		basicOperationView.showAllBasicOperations(basicOperationRepository.findAll());
	}

	public void addBasicOperation(BasicOperation basicOperation) {
		if (basicOperationRepository.findById(basicOperation.getId()) == null) {
			basicOperationRepository.save(basicOperation);
			basicOperationView.basicOperationAdded();
		} else {
			basicOperationView.showError("The BasicOperation with ID: " + basicOperation.getId() + " already exist.");
		}
	}

	public void removeBasicOperation(BasicOperation basicOperation) {
		if (basicOperationRepository.findById(basicOperation.getId()) != null) {
			basicOperationRepository.delete(basicOperation.getId());
			basicOperationView.basicOperationRemoved();
		} else {
			basicOperationView.showError("The BasicOperation with ID: " + basicOperation.getId() + " does not exist.");
		}
	}

	public void updateBasicOperation(BasicOperation newBasicOperation) {
		if (basicOperationRepository.findById(newBasicOperation.getId()) != null) {
			basicOperationRepository.update(newBasicOperation);
			basicOperationView.basicOperationUpdated();
		}else {
			basicOperationView.showError("The BasicOperation with ID: " + newBasicOperation.getId() + " does not exist.");
		}
	}

}
