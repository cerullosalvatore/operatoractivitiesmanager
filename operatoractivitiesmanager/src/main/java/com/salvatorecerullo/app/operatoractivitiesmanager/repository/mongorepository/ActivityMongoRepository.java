package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
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
		if (activityCollection.find(Filters.eq("_id", activity.getId())).first() == null) {
			activityCollection.insertOne(new Document().append("_id", activity.getId())
					.append("operatorMatricola", activity.getOperatorMatricola())
					.append("operationID", activity.getOperationId()).append("startTime", activity.getStartTime())
					.append("endTime", activity.getEndTime()));
		}
	}

	@Override
	public Activity findById(String id) {
		Document documentRetrieved = activityCollection.find(Filters.eq("_id", id)).first();
		if (documentRetrieved != null) {
			return fromDocumentToActivity(documentRetrieved);
		} else {
			return null;
		}
	}

	@Override
	public void delete(String id) {
		activityCollection.deleteOne(Filters.eq("_id", id));
	}

	@Override
	public List<Activity> findByOperatorMatricola(String matricolaOperator) {
		return StreamSupport
				.stream(activityCollection.find(Filters.eq("operatorMatricola", matricolaOperator)).spliterator(),
						false)
				.map(this::fromDocumentToActivity).collect(Collectors.toList());
	}

	@Override
	public List<Activity> findByBasicOperationId(String operationId) {
		return StreamSupport
				.stream(activityCollection.find(Filters.eq("operationID", operationId)).spliterator(), false)
				.map(this::fromDocumentToActivity).collect(Collectors.toList());
	}

	@Override
	public List<Activity> findByDay(Date date) {
		Date startTimeToFind = settingDate(date, 0, 0, 0);
		Date endTimeToFind = settingDate(date, 23, 59, 59);

		return StreamSupport.stream(activityCollection.find(Filters.and(Filters.lte("startTime", endTimeToFind), Filters.gte("startTime", startTimeToFind))).spliterator(), false)
				.map(this::fromDocumentToActivity).collect(Collectors.toList());
	}

	@Override
	public void update(Activity activity) {
		Bson update1 = Updates.set("operatorMatricola", activity.getOperatorMatricola());
		Bson update2 = Updates.set("operationID", activity.getOperationId());
		Bson update3 = Updates.set("startTime", activity.getStartTime());
		Bson update4 = Updates.set("endTime", activity.getEndTime());
		Bson updates = Updates.combine(update1, update2, update3, update4);
		activityCollection.updateOne(Filters.eq("_id", activity.getId()), updates);
	}

	private Activity fromDocumentToActivity(Document document) {
		return new Activity(document.getString("_id"), document.getString("operatorMatricola"),
				document.getString("operationID"), document.getDate("startTime"), document.getDate("endTime"));
	}
	
	private Date settingDate(Date date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}
}
