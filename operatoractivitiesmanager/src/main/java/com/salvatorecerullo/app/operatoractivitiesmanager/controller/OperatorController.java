package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorController {
	private static final String THEOPERATOR = "The Operator: ";
	private static final String NOTEXIST = " does not exist.";
	
	private OperatorRepository operatorRepository;
	private OperatorView operatorView;

	public void allOperators() {
		operatorView.showAllOperators(operatorRepository.findAll());
	}

	public void addOperator(Operator newOperator) {
		if (operatorRepository.findByMatricola(newOperator.getMatricola()) == null) {
			operatorRepository.save(newOperator);
			operatorView.showSuccessfull(THEOPERATOR + newOperator.getMatricola() + " has been added.");
		} else {
			operatorView.showError(THEOPERATOR + newOperator.getMatricola() + " already exist.");
		}

	}

	public void removeOperator(Operator operator) {
		if (operatorRepository.findByMatricola(operator.getMatricola()) != null) {
			operatorRepository.delete(operator.getMatricola());
			operatorView.showSuccessfull(THEOPERATOR + operator.getMatricola() + " has been removed.");
		} else {
			operatorView.showError(THEOPERATOR + operator.getMatricola() + NOTEXIST);
		}
	}

	public void updateOperator(Operator newOperator) {
		if (operatorRepository.findByMatricola(newOperator.getMatricola()) != null) {
			operatorRepository.update(newOperator);
			operatorView.showSuccessfull(THEOPERATOR + newOperator.getMatricola() + " has been updated.");
		} else {
			operatorView.showError(THEOPERATOR + newOperator.getMatricola() + NOTEXIST);
		}
	}

}