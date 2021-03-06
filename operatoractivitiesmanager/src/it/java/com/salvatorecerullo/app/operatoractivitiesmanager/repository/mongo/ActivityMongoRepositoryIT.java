package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
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
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;

import de.bwaldvogel.mongo.bson.ObjectId;

public class ActivityMongoRepositoryIT {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	private MongoClient mongoClient;
	private ActivityMongoRepository activityMongoRepository;
	private MongoCollection<Document> activityCollection;
	private Date startTime;
	private Date endTime;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
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
	public void testFindAllActivities() {
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

	@Test
	public void testSaveActivity() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);

		// Exercise
		activityMongoRepository.save(activity1);

		// Verify
		assertThat(readAllActivityFromDB()).containsExactly(activity1);
	}

	@Test
	public void testFindByID() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), "operatorMatricola2", "basicOperationID2",
				startTime, endTime);
		addActivityToDB(activity1);
		addActivityToDB(activity2);

		// Exercise
		Activity activityRetrieved = activityMongoRepository.findById(activity2.getId());

		// Verify
		assertThat(activityRetrieved).isEqualTo(activity2);
	}

	@Test
	public void testDelete() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);
		addActivityToDB(activity1);

		// Exercise
		activityMongoRepository.delete(activity1.getId());

		// Verify
		assertThat(readAllActivityFromDB()).isEmpty();
	}

	@Test
	public void testFindActivityByOperatorMatricola() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID2",
				startTime, endTime);
		addActivityToDB(activity1);
		addActivityToDB(activity2);

		// Exercise
		List<Activity> activitiesRetrieved = activityMongoRepository
				.findByOperatorMatricola(activity2.getOperatorMatricola());

		// Verify
		assertThat(activitiesRetrieved).containsExactly(activity1, activity2);
	}

	@Test
	public void testFindActivityByBasicOperation() {
		// Setup
		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime, endTime);
		Activity activity2 = new Activity(new ObjectId().toString(), "operatorMatricola2", "basicOperationID2",
				startTime, endTime);
		Activity activity3 = new Activity(new ObjectId().toString(), "operatorMatricola2", "basicOperationID1",
				startTime, endTime);
		addActivityToDB(activity1);
		addActivityToDB(activity2);
		addActivityToDB(activity3);

		// Exercise
		List<Activity> activitiesRetrieved = activityMongoRepository.findByBasicOperationId("basicOperationID1");

		// Verify
		assertThat(activitiesRetrieved).containsExactly(activity1, activity3);
	}

	@Test
	public void testFindActivityByDate() {
		// Setup
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 00, 00, 00);
		Date startTime1 = cal.getTime();
		cal.set(2021, 1, 1, 23, 59, 59);
		Date endTime1 = cal.getTime();

		Activity activity1 = new Activity(new ObjectId().toString(), "operatorMatricola1", "basicOperationID1",
				startTime1, endTime1);

		cal.set(2021, 1, 2, 00, 00, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 1, 2, 23, 59, 59);
		Date endTime2 = cal.getTime();

		Activity activity2 = new Activity(new ObjectId().toString(), "operatorMatricola2", "basicOperationID2",
				startTime2, endTime2);

		addActivityToDB(activity1);
		addActivityToDB(activity2);

		cal.set(2021, 1, 1, 12, 30, 30);
		Date dateToFind = cal.getTime();
		// Exercise
		List<Activity> activitiesRetrieved = activityMongoRepository.findByDay(dateToFind);

		// Verify
		assertThat(activitiesRetrieved.get(0)).isEqualTo(activity1);
	}

	@Test
	public void testUpdate() {
		// Setup
		Activity activityOld = new Activity(new ObjectId().toString(), "operatorMatricolaOld", "basicOperationIDOld",
				startTime, endTime);
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 21);
		Date startTime2 = cal.getTime();
		cal.set(2021, 1, 21);
		Date endTime2 = cal.getTime();
		Activity activityNew = new Activity(activityOld.getId(), "operatorMatricolaNew", "basicOperationIDNew",
				startTime2, endTime2);
		addActivityToDB(activityOld);

		// Exercise
		activityMongoRepository.update(activityNew);

		// Verify
		assertThat(readAllActivityFromDB()).containsExactly(activityNew);
	}
	
	// Utility

	private void addActivityToDB(Activity activity) {
		activityCollection.insertOne(new Document().append("_id", activity.getId())
				.append("operatorMatricola", activity.getOperatorMatricola())
				.append("operationID", activity.getOperationId()).append("startTime", activity.getStartTime())
				.append("endTime", activity.getEndTime()));
	}

	private List<Activity> readAllActivityFromDB() {
		return StreamSupport.stream(activityCollection.find().spliterator(), false).map(this::fromDocumentToActivity)
				.collect(Collectors.toList());
	}

	private Activity fromDocumentToActivity(Document document) {
		return new Activity(document.getString("_id"), document.getString("operatorMatricola"),
				document.getString("operationID"), document.getDate("startTime"), document.getDate("endTime"));
	}
}
