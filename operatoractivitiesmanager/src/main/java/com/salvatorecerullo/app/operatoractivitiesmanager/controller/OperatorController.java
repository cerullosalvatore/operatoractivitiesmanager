package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorController {
	private OperatorView operatorView;
	private OperatorRepository operatorReopository;
	
	public void allOperators() {
		operatorView.showAllOperators(operatorReopository.findAll());
	}

}