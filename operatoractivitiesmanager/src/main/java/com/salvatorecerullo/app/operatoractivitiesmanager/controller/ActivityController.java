package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

public class ActivityController {

	private ActivityRepository activityRepository;
	private OperatorRepository operatorRepository;
	private BasicOperationRepository basicOperationRepository;

	private ActivityView activityView;

	public void allActivities() {
		activityView.showAllActivities(activityRepository.findAll());
	}

	public void addActivity(Activity activity) {
		if (activityRepository.findById(activity.getId()) != null) {
			activityView.showError("The Activity Id: " + activity.getId() + " already exist.");
		} else {
			if (operatorRepository.findByMatricola(activity.getOperatorMatricola()) == null) {
				activityView
						.showError("The Operator Matricola: " + activity.getOperatorMatricola() + " does not exist.");
			} else {
				if (basicOperationRepository.findById(activity.getOperationId()) == null) {
					activityView.showError("The BasicOperation Id: " + activity.getOperationId() + " does not exist.");
				} else {
					activityRepository.save(activity);
					activityView.activityAdded();
				}
			}
		}
	}
	
}
