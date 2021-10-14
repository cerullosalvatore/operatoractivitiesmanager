package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorController {
	private OperatorRepository operatorRepository;
	private OperatorView operatorView;

	public void allOperators() {
		operatorView.showAllOperators(operatorRepository.findAll());
	}

	public void addOperator(Operator newOperator) {
		if (operatorRepository.findByMatricola(newOperator.getMatricola()) == null) {
			operatorRepository.save(newOperator);
			operatorView.showSuccessfull("The Operator: " + newOperator.getMatricola() + " has been added.");
		} else {
			operatorView.showError("The Operator Matricola: " + newOperator.getMatricola() + " already exist.");
		}

	}

	public void removeOperator(Operator operator) {
		if (operatorRepository.findByMatricola(operator.getMatricola()) != null) {
			operatorRepository.delete(operator.getMatricola());
			operatorView.showSuccessfull("The Operator: " + operator.getMatricola() + " has been removed.");
		} else {
			operatorView.showError("The Operator Matricola: " + operator.getMatricola() + " does not exist.");
		}
	}

	public void updateOperator(Operator newOperator) {
		if (operatorRepository.findByMatricola(newOperator.getMatricola()) != null) {
			operatorRepository.update(newOperator);
			operatorView.showSuccessfull("The Operator: " + newOperator.getMatricola() + " has been updated.");
		} else {
			operatorView.showError("The Operator Matricola: " + newOperator.getMatricola() + " does not exist.");
		}
	}

}