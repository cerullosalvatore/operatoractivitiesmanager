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
import com.salvatorecerullo.app.operatoractivitiesmanager.view.swing.OperatorActivitiesManagerView;

@RunWith(GUITestRunner.class)
public class OperatorActivitiesManagerSwingAppE2E extends AssertJSwingJUnitTestCase {
	private static final String MONGO_CLIENT_HOST = "localhost";

	private static final String DB_NAME = "operatoractivities";

	private static final String COLLECTION_NAME_ACTIVITY = "activity";
	private static final String COLLECTION_NAME_OPERATOR = "operator";
	private static final String COLLECTION_NAME_BASICOPERATION = "basicoperation";

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
		addOperatorToDB("matricolaOperatorTest1", "nameTest1", "surnameTest1");
		addOperatorToDB("matricolaOperatorTest2", "nameTest2", "surnameTest2");
		addBasicOperationToDB("idBasicOperationTest1", "nameTest1", "descriptionTest1");
		addBasicOperationToDB("idBasicOperationTest2", "nameTest2", "descriptionTest2");

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
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest1", "nameTest1", "surnameTest1"))
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest2", "nameTest2", "surnameTest2"));
	}

	@Test
	@GUITest
	public void testOnStartAllBasicOperationsOnActivitiesTabAreShown() {
		assertThat(frameFixture.comboBox("comboBoxBasicOperationActivity").contents())
				.anySatisfy(e -> assertThat(e).contains("idBasicOperationTest1", "nameTest1", "descriptionTest1"))
				.anySatisfy(e -> assertThat(e).contains("idBasicOperationTest2", "nameTest2", "descriptionTest2"));
	}

	@Test
	@GUITest
	public void testOnStartAllOperatorsOnOperatorsTabAreShown() {
		frameFixture.tabbedPane().focus().selectTab("Operators");
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest1", "nameTest1", "surnameTest1"))
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest2", "nameTest2", "surnameTest2"));

	}

	@Test
	@GUITest
	public void testOnStartAllBasicOperationOnBasicOperationTabAreShown() {
		frameFixture.tabbedPane().focus().selectTab("Basic Operations");
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("idBasicOperationTest1", "nameTest1", "descriptionTest1"))
				.anySatisfy(e -> assertThat(e).contains("idBasicOperationTest2", "nameTest2", "descriptionTest2"));

	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		Calendar cal = Calendar.getInstance();
		cal.set(2020, 0, 21);
		Date startTime = settingDate(cal.getTime(), 12, 11);
		cal.set(2021, 1, 11);
		Date endTime = settingDate(cal.getTime(), 11, 12);

		String tempId = frameFixture.textBox("textFieldIdActivity").text();
		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").enterText("11/02/2021");
		frameFixture.textBox("textFieldEndHourActivity").enterText("11:12");
		
		frameFixture.button("btnAddActivity").click();

		
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains(tempId, "matricolaOperatorTest1", "idBasicOperationTest2",
						startTime.toString(), endTime.toString()));
	}

	private Date settingDate(Date date, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}
}
