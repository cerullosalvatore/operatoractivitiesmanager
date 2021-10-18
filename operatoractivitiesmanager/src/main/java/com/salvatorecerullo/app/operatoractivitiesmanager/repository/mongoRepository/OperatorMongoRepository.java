package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import java.util.Collections;
import java.util.List;

import com.mongodb.MongoClient;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;

public class OperatorMongoRepository implements OperatorRepository {

	public OperatorMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Operator> findAll() {
		return Collections.emptyList();
	}

	@Override
	public void save(Operator newOperator) {
		// TODO Auto-generated method stub

	}

	@Override
	public Operator findByMatricola(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Operator newOperator) {
		// TODO Auto-generated method stub

	}

}
