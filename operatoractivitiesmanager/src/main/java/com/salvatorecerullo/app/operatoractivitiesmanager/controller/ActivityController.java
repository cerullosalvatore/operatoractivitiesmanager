package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import java.util.Date;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

public class ActivityController {
	private static final String THEOPERATOR = "The Operator: ";
	private static final String BASICOPERATION = "The BasicOperation with ID: ";
	private static final String THEACTIVITY = "The Activity: ";
	private static final String NOTEXIST = " does not exist.";

	private ActivityRepository activityRepository;
	private OperatorRepository operatorRepository;
	private BasicOperationRepository basicOperationRepository;

	private ActivityView activityView;

	public void allActivities() {
		activityView.showActivities(activityRepository.findAll());
	}

	public void addActivity(Activity activity) {
		if (activityRepository.findById(activity.getId()) != null) {
			activityView.showError(THEACTIVITY + activity.getId() + " already exist.");
		} else {
			if (operatorRepository.findByMatricola(activity.getOperatorMatricola()) == null) {
				activityView.showError(THEOPERATOR + activity.getOperatorMatricola() + NOTEXIST);
			} else {
				if (basicOperationRepository.findById(activity.getOperationId()) == null) {
					activityView.showError(BASICOPERATION + activity.getOperationId() + NOTEXIST);
				} else {
					if (activity.getStartTime().after(activity.getEndTime())) {
						activityView.showError("The Start Date: " + activity.getStartTime() + "follow the End Date: "
								+ activity.getEndTime());
					} else {
						activityRepository.save(activity);
						activityView
								.showSuccessfull(THEOPERATOR + activity.getOperatorMatricola() + " has been added.");
					}
				}
			}
		}
	}

	public void removeActivity(Activity activity) {
		if (activityRepository.findById(activity.getId()) != null) {
			activityRepository.delete(activity.getId());
			activityView.showSuccessfull(THEOPERATOR + activity.getOperatorMatricola() + " has been removed.");
		} else {
			activityView.showError(THEACTIVITY + activity.getId() + NOTEXIST);
		}
	}

	public void findByOperator(String matricola) {
		if (activityRepository.findByOperatorMatricola(matricola) != null) {
			activityView.showActivities(activityRepository.findByOperatorMatricola(matricola));
		} else {
			activityView.showError(THEOPERATOR + matricola + NOTEXIST);
		}
	}

	public void findByBasicOperation(String id) {
		if (activityRepository.findByBasicOperationId(id) != null) {
			activityView.showActivities(activityRepository.findByBasicOperationId(id));
		} else {
			activityView.showError(BASICOPERATION + id + NOTEXIST);
		}
	}

	public void findByDay(Date startTime) {
		activityView.showActivities(activityRepository.findByDay(startTime));
	}

	public void updadeActivity(Activity activityUpdated) {
		if (activityRepository.findById(activityUpdated.getId()) == null) {
			activityView.showError(THEACTIVITY + activityUpdated.getId() + NOTEXIST);
		} else {
			if (operatorRepository.findByMatricola(activityUpdated.getOperatorMatricola()) == null) {
				activityView.showError(THEOPERATOR + activityUpdated.getOperatorMatricola() + NOTEXIST);
			} else {
				if (basicOperationRepository.findById(activityUpdated.getOperationId()) == null) {
					activityView.showError(BASICOPERATION + activityUpdated.getOperationId() + NOTEXIST);
				} else {
					if (activityUpdated.getStartTime().after(activityUpdated.getEndTime())) {
						activityView.showError("The Start Date: " + activityUpdated.getStartTime()
								+ "follow the End Date: " + activityUpdated.getEndTime());
					} else {
						activityRepository.update(activityUpdated);
						activityView.showSuccessfull(THEACTIVITY + activityUpdated.getId() + " has been updated.");
					}
				}
			}
		}
	}

}
