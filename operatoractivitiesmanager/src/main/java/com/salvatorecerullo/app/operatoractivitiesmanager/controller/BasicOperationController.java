package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

public class BasicOperationController {
	private BasicOperationRepository basicOperationRepository;
	private BasicOperationView basicOperatoinView;
	
	public void allBasicOperations() {
		basicOperatoinView.showAllBasicOperations(basicOperationRepository.findAll());
	}

}
