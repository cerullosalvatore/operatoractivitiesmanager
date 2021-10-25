package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.BasicOperationMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

import de.bwaldvogel.mongo.bson.ObjectId;

public class BasicOperationControllerIT {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "basicoperation";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private BasicOperationRepository basicOperationRepository;
	
	@Mock
	private BasicOperationView basicOperationView;

	@InjectMocks
	private BasicOperationController basicOperationController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		basicOperationController = new BasicOperationController(basicOperationRepository, basicOperationView);
	}

	@Test
	public void testAllBasicOperations() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);
		
		// Exercise
		basicOperationController.allBasicOperations();
		// Verify
		verify(basicOperationView).showAllBasicOperations(Arrays.asList(basicOperation1,basicOperation2));
	}
	
}
