package com.salvatorecerullo.app.operatoractivitiesmanager.repository;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

public interface BasicOperationRepository {

	public List<BasicOperation> findAll();

}
