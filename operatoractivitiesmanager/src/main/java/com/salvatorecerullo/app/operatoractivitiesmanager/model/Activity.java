package com.salvatorecerullo.app.operatoractivitiesmanager.model;

import java.util.Date;

public class Activity {
	private long id;
	private String operatorMatricola;
	private long operationId;
	private Date startTime;
	private Date endTime;

	public Activity(long id, String operatorMatricola, long operationId, Date startTime, Date endTime) {
		super();
		this.id = id;
		this.operatorMatricola = operatorMatricola;
		this.operationId = operationId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOperatorMatricola() {
		return operatorMatricola;
	}

	public void setOperatorMatricola(String operatorMatricola) {
		this.operatorMatricola = operatorMatricola;
	}

	public long getOperationId() {
		return operationId;
	}

	public void setOperationId(long operationId) {
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
