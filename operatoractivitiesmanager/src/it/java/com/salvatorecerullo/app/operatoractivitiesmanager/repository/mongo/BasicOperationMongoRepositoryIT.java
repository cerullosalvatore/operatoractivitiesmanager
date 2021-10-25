package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

import de.bwaldvogel.mongo.bson.ObjectId;

public class BasicOperationMongoRepositoryIT {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private MongoClient mongoClient;
	private BasicOperationMongoRepository basicOperationMongoRepository;
	private MongoCollection<Document> basicOperationCollection;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		basicOperationMongoRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		basicOperationCollection = database.getCollection(COLLECTION_NAME);
	}

	@Test
	public void testFindAll() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		addBasicOperationToDB(basicOperation1);
		addBasicOperationToDB(basicOperation2);

		// Exercise
		List<BasicOperation> basicOperations = basicOperationMongoRepository.findAll();

		// Verify
		assertThat(basicOperations).containsExactly(basicOperation1, basicOperation2);
	}

	@Test
	public void testFindByID() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		BasicOperation basicOperation3 = new BasicOperation(new ObjectId().toString(), "name3", "description3");
		addBasicOperationToDB(basicOperation1);
		addBasicOperationToDB(basicOperation2);
		addBasicOperationToDB(basicOperation3);

		// Exercise
		BasicOperation basicOperationRetrieved = basicOperationMongoRepository.findById(basicOperation2.getId());

		// Verify
		assertThat(basicOperationRetrieved).isEqualTo(basicOperation2);
	}

	@Test
	public void testSaveBasicOperation() {
		// Setup
		BasicOperation newBasicOperation1 = new BasicOperation(new ObjectId().toString(), "name", "description");

		// Exercise
		basicOperationMongoRepository.save(newBasicOperation1);

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).containsExactly(newBasicOperation1);
	}
	
	@Test
	public void testDeleteBasicOperation() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		addBasicOperationToDB(basicOperation1);
		addBasicOperationToDB(basicOperation2);

		// Exercise
		basicOperationMongoRepository.delete(basicOperation1.getId());

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).containsExactly(basicOperation2);
	}
	
	@Test
	public void testUpdate() {
		// Setup
		BasicOperation basicOperationOld = new BasicOperation(new ObjectId().toString(), "nameOld", "descriptionOld");
		BasicOperation basicOperationNew = new BasicOperation(basicOperationOld.getId(), "nameNew", "descriptionNew");
		addBasicOperationToDB(basicOperationOld);

		// Exercise
		basicOperationMongoRepository.update(basicOperationNew);

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).containsExactly(basicOperationNew);
	}

	private void addBasicOperationToDB(BasicOperation basicOperation) {
		basicOperationCollection.insertOne(new Document().append("_id", basicOperation.getId())
				.append("name", basicOperation.getName()).append("description", basicOperation.getDescription()));
	}

	private List<BasicOperation> readAllBasicOperatoinFromDB() {
		return StreamSupport.stream(basicOperationCollection.find().spliterator(), false)
				.map(this::fromDocumentToBasicOperation).collect(Collectors.toList());
	}

	private BasicOperation fromDocumentToBasicOperation(Document document) {
		return new BasicOperation(document.getString("_id"), document.getString("name"),
				document.getString("description"));
	}

}
