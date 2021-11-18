package com.salvatorecerullo.app.operatoractivitiesmanager.view;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public interface ActivityView {

	public void showActivities(List<Activity> activities);
	
	public void showOperators(List<Operator> operators);
	
	public void showBasicOperation(List<BasicOperation> basicOperations);

	public void showSuccessfull(String string);

	public void showError(String string);

}
