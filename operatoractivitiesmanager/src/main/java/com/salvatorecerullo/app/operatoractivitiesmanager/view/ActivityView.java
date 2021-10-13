package com.salvatorecerullo.app.operatoractivitiesmanager.view;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;

public interface ActivityView {

	public void showAllActivities(List<Activity> activities);

	public void activityAdded();

	public void showError(String string);

}
