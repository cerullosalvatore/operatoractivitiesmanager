package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository.BasicOperationMongoRepository;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import de.bwaldvogel.mongo.bson.ObjectId;

public class BasicOperationMongoRepositoryTest {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "basicoperation";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	private MongoClient mongoClient;
	private BasicOperationMongoRepository basicOperationMongoRepository;
	private MongoCollection<Document> basicOperationCollection;

	@BeforeClass
	public static void setupServer() {
		mongoServer = new MongoServer(new MemoryBackend());
		serverAddress = mongoServer.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		mongoServer.shutdown();
	}

	@Before
	public void setup() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		basicOperationMongoRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		basicOperationCollection = database.getCollection(COLLECTION_NAME);
	}

	@Test
	public void testFindAllDBEmpty() {
		assertThat(basicOperationMongoRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllDBNotEmpty() {
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
	public void testSaveBasicOperationSuccessfull() {
		// Setup
		BasicOperation newBasicOperation1 = new BasicOperation(new ObjectId().toString(), "name", "description");
		BasicOperation newBasicOperation2 = new BasicOperation(new ObjectId().toString(), "name", "description");

		// Exercise
		basicOperationMongoRepository.save(newBasicOperation1);
		basicOperationMongoRepository.save(newBasicOperation2);

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).containsExactly(newBasicOperation1, newBasicOperation2);
	}

	@Test
	public void testSaveBasicOperationError() {
		// Setup
		BasicOperation basicOperationOld = new BasicOperation(new ObjectId().toString(), "nameOld", "descriptionOld");
		BasicOperation basicOperationNew = new BasicOperation(basicOperationOld.getId(), "nameNew", "descriptionNew");
		addBasicOperationToDB(basicOperationOld);

		// Exercise
		basicOperationMongoRepository.save(basicOperationNew);

		// Verify
		assertThat(basicOperationMongoRepository.findAll()).containsExactly(basicOperationOld);
	}

	@Test
	public void testFindByIdSuccess() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		addBasicOperationToDB(basicOperation1);
		addBasicOperationToDB(basicOperation2);

		// Exercise
		BasicOperation basicOperationRetrieved = basicOperationMongoRepository.findById(basicOperation2.getId());

		// Verify
		assertThat(basicOperationRetrieved).isEqualTo(basicOperation2);
	}

	@Test
	public void testFindByIdError() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		addBasicOperationToDB(basicOperation1);

		// Exercise
		BasicOperation basicOperationRetrieved = basicOperationMongoRepository.findById(basicOperation2.getId());

		// Verify
		assertThat(basicOperationRetrieved).isNull();
	}

	@Test
	public void testDeleteSuccessfull() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		addBasicOperationToDB(basicOperation1);

		// Exercise
		basicOperationMongoRepository.delete(basicOperation1.getId());

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).isEmpty();
	}

	@Test
	public void testDeleteError() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		addBasicOperationToDB(basicOperation1);

		// Exercise
		basicOperationMongoRepository.delete(new ObjectId().toString());

		// Verify
		assertThat(readAllBasicOperatoinFromDB()).containsExactly(basicOperation1);
	}

	@Test
	public void testUpdateSuccessfull() {
		// Setup
		BasicOperation basicOperationOld = new BasicOperation(new ObjectId().toString(), "nameOld", "descriptionOld");
		BasicOperation basicOperationNew = new BasicOperation(basicOperationOld.getId(), "nameNew", "descriptionNew");
		addBasicOperationToDB(basicOperationOld);

		// Exercise
		basicOperationMongoRepository.update(basicOperationNew);

		// Verify
		assertThat(readAllBasicOperatoinFromDB().get(0)).isEqualTo(basicOperationNew);
	}

	@Test
	public void testUpdateError() {
		// Setup
		BasicOperation basicOperationOld = new BasicOperation(new ObjectId().toString(), "nameOld", "descriptionOld");
		BasicOperation basicOperationNew = new BasicOperation(new ObjectId().toString(), "nameNew", "descriptionNew");
		addBasicOperationToDB(basicOperationOld);

		// Exercise
		basicOperationMongoRepository.update(basicOperationNew);

		// Verify
		assertThat(readAllBasicOperatoinFromDB().get(0)).isNotEqualTo(basicOperationNew);
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
