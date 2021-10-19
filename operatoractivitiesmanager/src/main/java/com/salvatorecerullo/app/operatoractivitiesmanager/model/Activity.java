package com.salvatorecerullo.app.operatoractivitiesmanager.model;

import java.util.Date;

public class Activity {
	private String id;
	private String operatorMatricola;
	private String operationId;
	private Date startTime;
	private Date endTime;

	public Activity() {
	}

	public Activity(String id, String operatorMatricola, String operationId, Date startTime, Date endTime) {
		this.id = id;
		this.operatorMatricola = operatorMatricola;
		this.operationId = operationId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperatorMatricola() {
		return operatorMatricola;
	}

	public void setOperatorMatricola(String operatorMatricola) {
		this.operatorMatricola = operatorMatricola;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
