package operatoractivitiesmanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;
import java.util.Calendar;
import java.util.Date;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.swing.OperatorActivitiesManagerView;

@RunWith(GUITestRunner.class)
public class OperatorActivitiesManagerSwingAppE2E extends AssertJSwingJUnitTestCase {
	private static final String MONGO_CLIENT_HOST = "localhost";

	private static final String DB_NAME = "operatoractivities";

	private static final String COLLECTION_NAME_ACTIVITY = "activity";
	private static final String COLLECTION_NAME_OPERATOR = "operator";
	private static final String COLLECTION_NAME_BASICOPERATION = "basicoperation";

	private static final String OPERATOR_1_ID = "matricolaTest1";
	private static final String OPERATOR_1_NAME = "nameTest1";
	private static final String OPERATOR_1_SURNAME = "surnameTest1";
	private static final String OPERATOR_2_ID = "matricolaTest2";
	private static final String OPERATOR_2_NAME = "nameTest2";
	private static final String OPERATOR_2_SURNAME = "surnameTest2";
	
	private MongoClient mongoClient;

	private FrameFixture frameFixture;

	private Date startTime1;
	private Date endTime1;
	private Date startTime2;
	private Date endTime2;

	@Override
	protected void onSetUp() throws Exception {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		mongoClient = new MongoClient(new ServerAddress(MONGO_CLIENT_HOST, mongoPort));

		// Empty DB
		mongoClient.getDatabase(DB_NAME).drop();

		// Adding element to DB
		addOperatorToDB("matricolaTest1", "nameTest1", "surnameTest1");
		addOperatorToDB("matricolaTest2", "nameTest2", "surnameTest2");
		addBasicOperationToDB("IDTest1", "nameTest1", "descriptionTest1");
		addBasicOperationToDB("IDTest2", "nameTest2", "descriptionTest2");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		startTime1 = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		endTime1 = cal.getTime();
		cal.set(2019, 4, 5, 13, 0, 00);
		startTime2 = cal.getTime();
		cal.set(2021, 5, 1, 17, 00, 00);
		endTime2 = cal.getTime();
		addActivityToDB("IDTest1", "matricolaOperatorTest1", "idBasicOperationTest1", startTime1, endTime1);
		addActivityToDB("IDTest2", "matricolaOperatorTest2", "idBasicOperationTest2", startTime2, endTime2);
		application("com.salvatorecerullo.app.operatoractivitiesmanager.app.swing.OperatorActivitiesManagerSwingApp")
				.withArgs("--mongo-host=" + MONGO_CLIENT_HOST, "--mongo-port=" + mongoPort, "--db-name=" + DB_NAME,
						"--db-collection-operators=" + COLLECTION_NAME_OPERATOR,
						"--db-collection-basicoperations=" + COLLECTION_NAME_BASICOPERATION,
						"--db-collection-activities=" + COLLECTION_NAME_ACTIVITY)
				.start();

		frameFixture = WindowFinder
				.findFrame(new GenericTypeMatcher<OperatorActivitiesManagerView>(OperatorActivitiesManagerView.class) {
					@Override
					protected boolean isMatching(OperatorActivitiesManagerView frame) {
						return "Operator Activities Manager".equals(frame.getTitle()) && frame.isShowing();
					}
				}).using(robot());

	}

	private void addOperatorToDB(String matricola, String name, String surname) {
		Document documentOperator = new Document().append("_id", matricola).append("name", name).append("surname",
				surname);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_OPERATOR).insertOne(documentOperator);
	}

	private void addBasicOperationToDB(String id, String name, String description) {
		Document documentBasicOperation = new Document().append("_id", id).append("name", name).append("description",
				description);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_BASICOPERATION)
				.insertOne(documentBasicOperation);
	}

	private void addActivityToDB(String id, String matricolaOperator, String idBasicOperation, Date startTime,
			Date endTime) {
		Document documentActivity = new Document().append("_id", id).append("operatorMatricola", matricolaOperator)
				.append("operationID", idBasicOperation).append("startTime", startTime).append("endTime", endTime);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_ACTIVITY).insertOne(documentActivity);
	}

	@Test
	@GUITest
	public void testOnStartAllActivitiesOnActivitiesTabAreShown() {
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "matricolaOperatorTest1", "idBasicOperationTest1",
						startTime1.toString(), endTime1.toString()))
				.anySatisfy(e -> assertThat(e).contains("IDTest2", "matricolaOperatorTest2", "idBasicOperationTest2",
						startTime2.toString(), endTime2.toString()));
	}

	@Test
	@GUITest
	public void testOnStartAllOperatorsOnActivitiesTabAreShown() {
		assertThat(frameFixture.comboBox("comboBoxOperatorActivity").contents())
				.anySatisfy(e -> assertThat(e).contains("matricolaTest1", "nameTest1", "surnameTest1"))
				.anySatisfy(e -> assertThat(e).contains("matricolaTest2", "nameTest2", "surnameTest2"));
	}
	
	@Test
	@GUITest
	public void testOnStartAllBasicOperationsOnActivitiesTabAreShown() {
		assertThat(frameFixture.comboBox("comboBoxBasicOperationActivity").contents())
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "nameTest1", "descriptionTest1"))
				.anySatisfy(e -> assertThat(e).contains("IDTest2", "nameTest2", "descriptionTest2"));
	}
	
	@Test
	@GUITest
	public void testOnStartAllOperatorsOnOperatorsTabAreShown() {
		frameFixture.tabbedPane().focus().selectTab("Operators");
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("matricolaTest1", "nameTest1", "surnameTest1"))
				.anySatisfy(e -> assertThat(e).contains("matricolaTest2", "nameTest2", "surnameTest2"));

	}
	
	@Test
	@GUITest
	public void testOnStartAllBasicOperationOnBasicOperationTabAreShown() {
		frameFixture.tabbedPane().focus().selectTab("Basic Operations");
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "nameTest1", "descriptionTest1"))
				.anySatisfy(e -> assertThat(e).contains("IDTest2", "nameTest2", "descriptionTest2"));

	}
}
