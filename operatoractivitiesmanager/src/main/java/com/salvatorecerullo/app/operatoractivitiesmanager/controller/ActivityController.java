package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import java.util.Date;

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
		activityView.showActivities(activityRepository.findAll());
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
					if (activity.getStartTime().after(activity.getEndTime())) {
						activityView.showError("The Start Date: " + activity.getStartTime() + "follow the End Date: "
								+ activity.getEndTime());
					} else {
						activityRepository.save(activity);
						activityView.activityAdded();
					}
				}
			}
		}
	}

	public void removeActivity(Activity activity) {
		if (activityRepository.findById(activity.getId()) != null) {
			activityRepository.delete(activity.getId());
			activityView.showActivityRemoved("The activity with Id: " + activity.getId() + " has been removed");
		} else {
			activityView.showError("The activity with Id: " + activity.getId() + " not exist.");
		}
	}

	public void findByOperator(String matricola) {
		activityView.showActivities(activityRepository.findByOperator(matricola));
	}

	public void findByBasicOperation(long id) {
		activityView.showActivities(activityRepository.findByBasicOperation(id));
	}

	public void findByDay(Date startTime) {
		//in the implementation of activity repository i need to take only year-month-day
		activityView.showActivities(activityRepository.findByDay(startTime));
	}

}
