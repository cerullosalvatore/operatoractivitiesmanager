package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.ActivityMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.BasicOperationMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

import de.bwaldvogel.mongo.bson.ObjectId;

public class ActivityControllerIT {
	private Date startTime;
	private Date endTime;

	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME_ACTIVITY = "activity";
	private static final String COLLECTION_NAME_OPERATOR = "operator";
	private static final String COLLECTION_NAME_BASICOPERATION = "basicoperation";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private ActivityRepository activityRepository;
	private OperatorRepository operatorRepository;
	private BasicOperationRepository basicOperationRepository;
	
	@Mock
	private ActivityView activityView;

	@InjectMocks
	private ActivityController activityController;

	@Before
	public void setup() {
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		endTime = cal.getTime();
		
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		activityRepository = new ActivityMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_ACTIVITY);
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_OPERATOR);
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_BASICOPERATION);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		activityController = new ActivityController(activityRepository, operatorRepository, basicOperationRepository, activityView);
	}

	@Test
	public void testAllActivities() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "matricolaTest1", "0", startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), "matricolaTest2", "0", startTime, endTime);
		activityRepository.save(activity1);		
		activityRepository.save(activity2);		
		// Exercise
		activityController.allActivities();
		// Verify
		verify(activityView).showActivities(Arrays.asList(activity1,activity2));
	}
}
