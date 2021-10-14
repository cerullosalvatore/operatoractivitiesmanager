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
			activityView.showError("The Activity ID: " + activity.getId() + " already exist.");
		} else {
			if (operatorRepository.findByMatricola(activity.getOperatorMatricola()) == null) {
				activityView.showError("The Operator: " + activity.getOperatorMatricola() + " does not exist.");
			} else {
				if (basicOperationRepository.findById(activity.getOperationId()) == null) {
					activityView.showError("The BasicOperation: " + activity.getOperationId() + " does not exist.");
				} else {
					if (activity.getStartTime().after(activity.getEndTime())) {
						activityView.showError("The Start Date: " + activity.getStartTime() + "follow the End Date: "
								+ activity.getEndTime());
					} else {
						activityRepository.save(activity);
						activityView.showSuccessfull(
								"The Operator: " + activity.getOperatorMatricola() + " has been added.");
					}
				}
			}
		}
	}

	public void removeActivity(Activity activity) {
		if (activityRepository.findById(activity.getId()) != null) {
			activityRepository.delete(activity.getId());
			activityView.showSuccessfull("The Operator: " + activity.getOperatorMatricola() + " has been removed.");
		} else {
			activityView.showError("The Activity: " + activity.getId() + " does not exist.");
		}
	}

	public void findByOperator(String matricola) {
		if (activityRepository.findByOperatorMatricola(matricola) != null) {
			activityView.showActivities(activityRepository.findByOperatorMatricola(matricola));
		} else {
			activityView.showError("The Operator: " + matricola + " does not exist.");
		}
	}

	public void findByBasicOperation(long id) {
		if (activityRepository.findByBasicOperationId(id) != null) {
			activityView.showActivities(activityRepository.findByBasicOperationId(id));
		} else {
			activityView.showError("The BasicOperation: " + id + " does not exist.");
		}
	}

	public void findByDay(Date startTime) {
		// in the implementation of activity repository i need to take only
		// year-month-day
		activityView.showActivities(activityRepository.findByDay(startTime));
	}

	public void updadeActivity(Activity activityUpdated) {
		if (activityRepository.findById(activityUpdated.getId()) == null) {
			activityView.showError("The Activity: " + activityUpdated.getId() + " does not exist.");
		} else {
			if (operatorRepository.findByMatricola(activityUpdated.getOperatorMatricola()) == null) {
				activityView.showError("The Operator: " + activityUpdated.getOperatorMatricola() + " does not exist.");
			} else {
				if (basicOperationRepository.findById(activityUpdated.getOperationId()) == null) {
					activityView
							.showError("The Basic Operation: " + activityUpdated.getOperationId() + " does not exist.");
				} else {
					if (activityUpdated.getStartTime().after(activityUpdated.getEndTime())) {
						activityView.showError("The Start Date: " + activityUpdated.getStartTime()
								+ "follow the End Date: " + activityUpdated.getEndTime());
					} else {
						activityRepository.update(activityUpdated);
						activityView.showSuccessfull("The Activity: " + activityUpdated.getId() + " has been updated.");
					}
				}
			}
		}
	}

}
