package com.salvatorecerullo.app.operatoractivitiesmanager.view;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

public interface BasicOperationView {

	public void showAllBasicOperations(List<BasicOperation> basicOperations);

	public void showSuccessfull(String string);

	public void showError(String string);

}
