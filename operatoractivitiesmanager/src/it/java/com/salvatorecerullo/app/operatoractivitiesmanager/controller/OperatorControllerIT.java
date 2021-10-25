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
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorControllerIT {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private OperatorRepository operatorRepository;

	@Mock
	private OperatorView operatorView;

	@InjectMocks
	private OperatorController operatorController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		operatorController = new OperatorController(operatorRepository, operatorView);
	}
	
	@Test
	public void testAllOperators() {
		// Setup
		Operator operator1 = new Operator("matricola1", "testName1", "testSurname1");
		Operator operator2 = new Operator("matricola2", "testName2", "testSurname2");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);
		// Exercise
		operatorController.allOperators();
		// Verify
		verify(operatorView).showAllOperators(Arrays.asList(operator1,operator2));
	}

}
