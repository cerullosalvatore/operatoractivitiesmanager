package com.salvatorecerullo.app.operatoractivitiesmanager.repository;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;

public interface ActivityRepository {

	public List<Activity> findAll();

	public void save(Activity activity);

	public Activity findById(long id);

}
