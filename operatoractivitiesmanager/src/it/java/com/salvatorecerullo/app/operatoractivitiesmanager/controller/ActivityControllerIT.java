package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
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
	
	@Test
	public void testAddActivity() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity newActivity = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		operatorRepository.save(operator);
		basicOperationRepository.save(basicOperation);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView)
				.showSuccessfull("The Activity: " + newActivity.getId() + " has been added.");
		verifyNoMoreInteractions(activityView);
	}
	
	@Test
	public void testRemoveActivity() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity oldActivity = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		operatorRepository.save(operator);
		basicOperationRepository.save(basicOperation);
		activityRepository.save(oldActivity);
		// Exercise
		activityController.removeActivity(oldActivity);
		// Verify
		verify(activityView)
				.showSuccessfull("The Activity: " + oldActivity.getId() + " has been removed.");
		verifyNoMoreInteractions(activityView);
	}
	
	@Test
	public void testFindActivityByOperator() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity activity1 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		operatorRepository.save(operator);
		basicOperationRepository.save(basicOperation);
		activityRepository.save(activity1);
		activityRepository.save(activity2);
		// Exercise
		activityController.findByOperator("testMatricola");
		// Verify
		verify(activityView).showActivities(Arrays.asList(activity1,activity2));
		verifyNoMoreInteractions(activityView);
	}
	
	@Test
	public void testFindActivityByBasicOperation() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity activity1 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		operatorRepository.save(operator);
		basicOperationRepository.save(basicOperation);
		activityRepository.save(activity1);
		activityRepository.save(activity2);
		// Exercise
		activityController.findByBasicOperation(basicOperation.getId());
		// Verify
		verify(activityView).showActivities(Arrays.asList(activity1,activity2));
		verifyNoMoreInteractions(activityView);
	}
	
	@Test
	public void testFindActivityByDate() {
		// Setup
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTimeSearch = cal.getTime();
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity activity1 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		operatorRepository.save(operator);
		basicOperationRepository.save(basicOperation);
		activityRepository.save(activity1);
		activityRepository.save(activity2);
		// Exercise
		activityController.findByDay(startTimeSearch);
		// Verify
		verify(activityView).showActivities(Arrays.asList(activity1,activity2));
		verifyNoMoreInteractions(activityView);
	}
	
	@Test
	public void testUpdateActivity() {
		// Setup
		Operator operatorOld = new Operator("testMatricolaOld", "testNameOld", "testSurnameOld");
		BasicOperation basicOperationOld = new BasicOperation(new ObjectId().toString(), "testNameOld", "testDescriptionOld");
		Activity activityOld = new Activity(new ObjectId().toString(), operatorOld.getMatricola(), basicOperationOld.getId(), startTime, endTime);
		Operator operatorNew = new Operator("testMatricolaNew", "testNameNew", "testSurnameNew");
		BasicOperation basicOperationNew = new BasicOperation(new ObjectId().toString(), "testName", "testDescription");
		Activity activityNew = new Activity(activityOld.getId(), operatorNew.getMatricola(), basicOperationNew.getId(), startTime, endTime);
		operatorRepository.save(operatorOld);
		basicOperationRepository.save(basicOperationOld);
		activityRepository.save(activityOld);
		operatorRepository.save(operatorNew);
		basicOperationRepository.save(basicOperationNew);
		activityRepository.save(activityNew);
		// Exercise
		activityController.updadeActivity(activityNew);
		// Verify
		verify(activityView).showSuccessfull("The Activity: " + activityNew.getId() + " has been updated.");
		verifyNoMoreInteractions(activityView);
	}
	
}
