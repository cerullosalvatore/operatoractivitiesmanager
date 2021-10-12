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
			operatorView.operatorAdded();
		} else {
			operatorView.showError("The Operator Matricola: " + newOperator.getMatricola() + " already exist.");
		}

	}

	public void removeOperator(Operator operator) {
		if (operatorRepository.findByMatricola(operator.getMatricola()) != null) {
			operatorRepository.delete(operator.getMatricola());
			operatorView.operatorRemoved();
		} else {
			operatorView.showError("The Operator Matricola: " + operator.getMatricola() + " does not exist.");
		}
	}

	public void updateOperator(Operator newOperator) {
		if (operatorRepository.findByMatricola(newOperator.getMatricola()) != null) {
			operatorRepository.updateOperator(newOperator);
			operatorView.operatorUpdated();
		} else {
			operatorView.showError("The Operator Matricola: " + newOperator.getMatricola() + " does not exist.");
		}
	}

}