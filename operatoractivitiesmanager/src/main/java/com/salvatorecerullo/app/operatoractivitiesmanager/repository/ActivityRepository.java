package com.salvatorecerullo.app.operatoractivitiesmanager.repository;

import java.util.Date;
import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;

public interface ActivityRepository {

	public List<Activity> findAll();

	public void save(Activity activity);

	public Activity findById(long id);

	public void delete(long id);

	public List<Activity> findByOperatorMatricola(String matricolaOperator);

	public List<Activity> findByBasicOperationId(long id);

	//in the implementation of activity repository i need to take only year-month-day
	public List<Activity> findByDay(Date startTime);

	public void update(Activity activity);

}
