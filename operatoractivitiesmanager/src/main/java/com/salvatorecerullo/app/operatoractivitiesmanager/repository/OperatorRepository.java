package com.salvatorecerullo.app.operatoractivitiesmanager.repository;

import java.util.List;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public interface OperatorRepository {

	public List<Operator> findAll();

	public void save(Operator newOperator);

	public Operator findByMatricola(String string);

	public void delete(String string);

	public void updateOperator(Operator newOperator);

}
