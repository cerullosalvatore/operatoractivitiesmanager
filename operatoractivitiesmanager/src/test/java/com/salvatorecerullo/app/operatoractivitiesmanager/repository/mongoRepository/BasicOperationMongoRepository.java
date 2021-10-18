package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;

public class BasicOperationMongoRepository implements BasicOperationRepository {
	private MongoCollection<Document> basicOperationCollection;

	public BasicOperationMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		basicOperationCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	@Override
	public List<BasicOperation> findAll() {
		return StreamSupport.stream(basicOperationCollection.find().spliterator(), false)
				.map(this::fromDocumentToBasicOperation).collect(Collectors.toList());
	}

	@Override
	public BasicOperation findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(BasicOperation newBasicOperation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(BasicOperation newBasicOperation) {
		// TODO Auto-generated method stub

	}

	private BasicOperation fromDocumentToBasicOperation(Document document) {
		return new BasicOperation(document.getString("_id"), document.getString("name"),
				document.getString("description"));
	}
}
