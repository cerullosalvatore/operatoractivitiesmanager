package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;


public class OperatorMongoRepostoryIT {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private MongoClient mongoClient;
	private OperatorMongoRepository operatorMongoRepository;
	private MongoCollection<Document> operatorCollection;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		operatorMongoRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		operatorCollection = database.getCollection(COLLECTION_NAME);
	}

	@Test
	public void testFindAll() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		Operator operator2 = new Operator("matricola2", "name2", "surname2");
		addOperatorToDB(operator1);
		addOperatorToDB(operator2);

		// Exercise
		List<Operator> operators = operatorMongoRepository.findAll();

		// Verify
		assertThat(operators).containsExactly(operator1, operator2);
	}

	private void addOperatorToDB(Operator operator) {
		operatorCollection.insertOne(new Document().append("_id", operator.getMatricola())
				.append("name", operator.getName()).append("surname", operator.getSurname()));
	}
}
