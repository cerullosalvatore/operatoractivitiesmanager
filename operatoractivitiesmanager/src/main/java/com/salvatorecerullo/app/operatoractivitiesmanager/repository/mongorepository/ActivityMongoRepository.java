package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;

public class ActivityMongoRepository implements ActivityRepository {
	private MongoCollection<Document> activityCollection;

	public ActivityMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		activityCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	@Override
	public List<Activity> findAll() {
		return StreamSupport.stream(activityCollection.find().spliterator(), false).map(this::fromDocumentToActivity)
				.collect(Collectors.toList());
	}

	@Override
	public void save(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Activity findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Activity> findByOperatorMatricola(String matricolaOperator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Activity> findByBasicOperationId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Activity> findByDay(Date startTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Activity activity) {
		// TODO Auto-generated method stub

	}

	private Activity fromDocumentToActivity(Document document) {
		return new Activity(document.getString("_id"), document.getString("operatorMatricola"),
				document.getString("operationID"), document.getDate("startTime"), document.getDate("endTime"));
	}
}
