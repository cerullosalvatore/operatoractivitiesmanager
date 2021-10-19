package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository.ActivityMongoRepository;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import de.bwaldvogel.mongo.bson.ObjectId;

public class ActivityMongoRepositoryTest {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "activities";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	private MongoClient mongoClient;
	private ActivityMongoRepository activityMongoRepository;
	private MongoCollection<Document> activityCollection;

	private Date startTime;
	private Date endTime;

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
		activityMongoRepository = new ActivityMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		activityCollection = database.getCollection(COLLECTION_NAME);
	}

	@Before
	public void setupDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		endTime = cal.getTime();
	}

	@Test
	public void testFindAllDBEmpty() {
		assertThat(activityMongoRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllDBNotEmpty() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), "operatorMatricola2", "basicOperationID2",
				startTime, endTime);
		addActivityToDB(activity1);
		addActivityToDB(activity2);

		// Exercise
		List<Activity> activities = activityMongoRepository.findAll();

		// Verify
		assertThat(activities).containsExactly(activity1, activity2);
	}

	private void addActivityToDB(Activity activity) {
		activityCollection.insertOne(new Document().append("_id", activity.getId())
				.append("operatorMatricola", activity.getOperatorMatricola())
				.append("operationID", activity.getOperationId()).append("startTime", activity.getStartTime())
				.append("endTime", activity.getEndTime()));

	}
}
