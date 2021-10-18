package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
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
		Document documentRetrieved = basicOperationCollection.find(Filters.eq("_id", id)).first();
		if (documentRetrieved != null) {
			return fromDocumentToBasicOperation(documentRetrieved);
		} else {
			return null;
		}
	}

	@Override
	public void save(BasicOperation newBasicOperation) {
		if (basicOperationCollection.find(Filters.eq("_id", newBasicOperation.getId())).first() == null) {
			basicOperationCollection.insertOne(
					new Document().append("_id", newBasicOperation.getId()).append("name", newBasicOperation.getName())
							.append("description", newBasicOperation.getDescription()));
		}
	}

	@Override
	public void delete(String id) {
		basicOperationCollection.deleteOne(Filters.eq("_id", id));
	}

	@Override
	public void update(BasicOperation newBasicOperation) {
		Bson update1 = Updates.set("name", newBasicOperation.getName());
		Bson update2 = Updates.set("description", newBasicOperation.getDescription());
		Bson updates = Updates.combine(update1, update2);
		basicOperationCollection.updateOne(Filters.eq("_id", newBasicOperation.getId()), updates);
	}

	private BasicOperation fromDocumentToBasicOperation(Document document) {
		return new BasicOperation(document.getString("_id"), document.getString("name"),
				document.getString("description"));
	}
}
