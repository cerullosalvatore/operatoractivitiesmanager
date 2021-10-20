package com.salvatorecerullo.app.operatoractivitiesmanager.repository;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

public interface BasicOperationRepository {

	public List<BasicOperation> findAll();

	public BasicOperation findById(String id);

	public void save(BasicOperation newBasicOperation);

	public void delete(String id);

	public void update(BasicOperation newBasicOperation);

}
