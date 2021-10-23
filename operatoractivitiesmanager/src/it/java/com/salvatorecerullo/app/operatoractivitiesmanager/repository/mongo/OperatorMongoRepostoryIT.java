package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
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
	
	@Test
	public void testSaveOperator() {
		// Setup
		Operator newOperator = new Operator("newMatricola", "newName", "newSurname");

		// Exercise
		operatorMongoRepository.save(newOperator);

		// Verify
		assertThat(operatorMongoRepository.findAll()).containsExactly(newOperator);
	}
	
	@Test
	public void testFindByMatricola() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		Operator operator2 = new Operator("matricola2", "name2", "surname2");
		addOperatorToDB(operator1);
		addOperatorToDB(operator2);

		// Exercise
		Operator operatorRetrivied = operatorMongoRepository.findByMatricola(operator2.getMatricola());

		// Verify
		assertThat(operatorRetrivied).isEqualTo(operator2);
	}
	
	@Test
	public void testDeleteOperator() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		addOperatorToDB(operator1);

		// Exercise
		operatorMongoRepository.delete(operator1.getMatricola());

		// Verify
		assertThat(readAllOperatorsFromDB()).isEmpty();
	}

	@Test
	public void testUpdateOperator() {
		// Setup
		Operator operatorOld = new Operator("oldMatricola", "oldNmae", "oldSurname");
		Operator operatorNew = new Operator("oldMatricola", "newName", "newSurname");
		addOperatorToDB(operatorOld);

		// Exercise
		operatorMongoRepository.update(operatorNew);

		// Verify
		assertThat(readAllOperatorsFromDB().get(0)).isEqualTo(operatorNew);
	}
	
	private void addOperatorToDB(Operator operator) {
		operatorCollection.insertOne(new Document().append("_id", operator.getMatricola())
				.append("name", operator.getName()).append("surname", operator.getSurname()));
	}
	
	private List<Operator> readAllOperatorsFromDB() {
		return StreamSupport.stream(operatorCollection.find().spliterator(), false).map(this::fromDocumentToOperator)
				.collect(Collectors.toList());
	}
	
	private Operator fromDocumentToOperator(Document document) {
		return new Operator(document.getString("_id"), document.getString("name"), document.getString("surname"));
	}
}
