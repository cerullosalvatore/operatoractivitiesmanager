package com.salvatorecerullo.app.operatoractivitiesmanager.view;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public interface OperatorView {

	public void showAllOperators(List<Operator> operators);

	public void showSuccessfull(String string);

	public void showError(String string);
}
