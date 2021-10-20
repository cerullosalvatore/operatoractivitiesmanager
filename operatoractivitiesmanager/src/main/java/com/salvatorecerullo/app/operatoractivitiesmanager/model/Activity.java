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
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operationId == null) {
			if (other.operationId != null)
				return false;
		} else if (!operationId.equals(other.operationId))
			return false;
		if (operatorMatricola == null) {
			if (other.operatorMatricola != null)
				return false;
		} else if (!operatorMatricola.equals(other.operatorMatricola))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	
	

}
