package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;

public class OperatorMongoRepository implements OperatorRepository {
	private MongoCollection<Document> operatorCollection;

	public OperatorMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		operatorCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	@Override
	public List<Operator> findAll() {
		return StreamSupport.stream(operatorCollection.find().spliterator(), false).map(this::fromDocumentToOperator)
				.collect(Collectors.toList());
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

	private Operator fromDocumentToOperator(Document document) {
		return new Operator(document.getString("matricola"), document.getString("name"), document.getString("surname"));
	}

}
