package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

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
}
