package com.salvatorecerullo.app.operatoractivitiesmanager.model;

import java.util.Date;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
		result = prime * result + ((operatorMatricola == null) ? 0 : operatorMatricola.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;

		return Objects.equals(id, other.id) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(startTime, other.startTime)
				&& Objects.equals(operatorMatricola, other.operatorMatricola)
				&& Objects.equals(operationId, other.operationId);
	}
}
