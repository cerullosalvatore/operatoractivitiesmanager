package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

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
import org.bson.Document;

public class OperatorMongoRepositoryTest {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	private MongoClient mongoClient;
	private OperatorMongoRepository operatorMongoRepository;
	private MongoCollection<Document> operatorCollection;

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
		operatorMongoRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		operatorCollection = database.getCollection(COLLECTION_NAME);
	}
	
	@Test
	public void testFindAllDBEmpty() {
		assertThat(operatorMongoRepository.findAll()).isEmpty();
	}

}
