package operatoractivitiesmanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;
import static org.awaitility.Awaitility.*;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.model.Filters;
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

		Robot robot = robot();
		robot.settings().eventPostingDelay(500);
		frameFixture = WindowFinder
				.findFrame(new GenericTypeMatcher<OperatorActivitiesManagerView>(OperatorActivitiesManagerView.class) {
					@Override
					protected boolean isMatching(OperatorActivitiesManagerView frame) {
						return "Operator Activities Manager".equals(frame.getTitle()) && frame.isShowing();
					}
				}).using(robot);

	}

	// Utility
	
	private void addOperatorToDB(String matricola, String name, String surname) {
		Document documentOperator = new Document().append("_id", matricola).append("name", name).append("surname",
				surname);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_OPERATOR).insertOne(documentOperator);
	}

	private void removeOperatorFromDB(String matricola) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_OPERATOR)
				.deleteOne(Filters.eq("_id", matricola));
	}

	private void addBasicOperationToDB(String id, String name, String description) {
		Document documentBasicOperation = new Document().append("_id", id).append("name", name).append("description",
				description);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_BASICOPERATION)
				.insertOne(documentBasicOperation);
	}

	private void removeBasicOperationFromDB(String id) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_BASICOPERATION).deleteOne(Filters.eq("_id", id));
	}

	private void addActivityToDB(String id, String matricolaOperator, String idBasicOperation, Date startTime,
			Date endTime) {
		Document documentActivity = new Document().append("_id", id).append("operatorMatricola", matricolaOperator)
				.append("operationID", idBasicOperation).append("startTime", startTime).append("endTime", endTime);
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_ACTIVITY).insertOne(documentActivity);
	}

	private void removeActivityFromDB(String id) {
		await().atMost(2, TimeUnit.SECONDS).until(() -> {
			try {
				mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME_ACTIVITY)
						.deleteOne(Filters.eq("_id", id));
				return true;
			} catch (Exception e) {
				return false;
			}
		});
	}

	private Date settingDate(Date date, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}
	
	// TEST INITIAL STATES TAB

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

	// Activity Panel Test
	@Test
	@GUITest
	public void testAddActivityButtonSuccess() {
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
		assertThat(frameFixture.label("lblMessageStatus").text()).contains(tempId, " has been added.");
		assertThat(frameFixture.list().contents()).anySatisfy(e -> assertThat(e).contains(tempId,
				"matricolaOperatorTest1", "idBasicOperationTest2", startTime.toString(), endTime.toString()));
	}

	@Test
	@GUITest
	public void testAddActivityButtonErrorActivityAlreadyExist() {
		// Setup

		// Insert into db activity with the actual tempId
		String tempId = frameFixture.textBox("textFieldIdActivity").text();
		addActivityToDB(tempId, "matricolaOperatorTest1", "idBasicOperationTest1", startTime1, endTime1);

		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").enterText("11/02/2021");
		frameFixture.textBox("textFieldEndHourActivity").enterText("11:12");

		frameFixture.button("btnAddActivity").click();

		assertThat(frameFixture.label("lblMessageStatus").text()).contains(tempId, " already exist.");
	}

	@Test
	@GUITest
	public void testAddActivityButtonErrorOperatorNotExist() {
		// Setup
		// Remove from db the operator
		removeOperatorFromDB("matricolaOperatorTest1");

		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").enterText("11/02/2021");
		frameFixture.textBox("textFieldEndHourActivity").enterText("11:12");

		frameFixture.button("btnAddActivity").click();

		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" does not exist.");
	}

	@Test
	@GUITest
	public void testAddActivityButtonErrorBasicOperationNotExist() {
		// Setup
		// Remove from db the basic operation
		removeBasicOperationFromDB("idBasicOperationTest2");

		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").enterText("11/02/2021");
		frameFixture.textBox("textFieldEndHourActivity").enterText("11:12");

		frameFixture.button("btnAddActivity").click();

		assertThat(frameFixture.label("lblMessageStatus").text()).contains("idBasicOperationTest2", " does not exist.");
	}

	@Test
	@GUITest
	public void testAddActivityButtonErrorEndDateBeforeStartDate() {
		// Setup
		Calendar cal = Calendar.getInstance();
		cal.set(2020, 0, 21);
		Date startTime = settingDate(cal.getTime(), 12, 11);
		cal.set(2020, 0, 11);
		Date endTime = settingDate(cal.getTime(), 11, 12);

		// Insert into db activity with the actual tempId
		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").enterText("11/01/2020");
		frameFixture.textBox("textFieldEndHourActivity").enterText("11:12");

		frameFixture.button("btnAddActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains(startTime.toString(), endTime.toString());
	}

	@Test
	@GUITest
	public void testDeleteActivityButtonSuccess() {
		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest1" + ".*"));
		frameFixture.button("btnDeleteActivity").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("IDTest1"));
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("IDTest1", " has been removed.");
	}

	@Test
	@GUITest
	public void testDeleteActivityButtonError() {
		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest1" + ".*"));

		// remove the activity from the DB
		removeActivityFromDB("IDTest1");

		frameFixture.button("btnDeleteActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("IDTest1", " does not exist.");
	}

	@Test
	@GUITest
	public void testShowAllActivityButton() {
		// remove the activity from the DB
		removeActivityFromDB("IDTest1");
		frameFixture.button("btnShowAll").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("IDTest1"));
	}

	@Test
	@GUITest
	public void testFindActivitiesByOperatorButtonSuccess() {
		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(1);

		frameFixture.button("btnFindByOperator").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("IDTest1"))
				.anySatisfy(e -> assertThat(e).contains("IDTest2", "matricolaOperatorTest2", "idBasicOperationTest2",
						startTime2.toString(), endTime2.toString()));
	}

	@Test
	@GUITest
	public void testFindActivitiesByOperatorButtonError() {
		// remove the activity from the DB
		removeActivityFromDB("IDTest1");

		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);

		frameFixture.button("btnFindByOperator").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" does not exist.");
	}

	@Test
	@GUITest
	public void testFindActivitiesByBasicOperationButtonSuccess() {
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		frameFixture.button("btnFindByBasicOperation").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("IDTest2"))
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "matricolaOperatorTest1", "idBasicOperationTest1",
						startTime1.toString(), endTime1.toString()));
	}

	@Test
	@GUITest
	public void testFindActivitiesByBasicOperationButtonError() {
		// remove the activity from the DB
		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest2" + ".*"));
		frameFixture.button("btnDeleteActivity").click();

		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);

		frameFixture.button("btnFindByBasicOperation").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("idBasicOperationTest2", " does not exist.");
	}

	@Test
	@GUITest
	public void testFindActivitiesByDayButton() {
		frameFixture.textBox("textFieldStartDataActivity").enterText("01/02/2021");

		frameFixture.button("btnFindByData").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("IDTest2"))
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "matricolaOperatorTest1", "idBasicOperationTest1",
						startTime1.toString(), endTime1.toString()));

	}

	@Test
	@GUITest
	public void testUpdateActivityButtonSuccess() {
		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest1" + ".*"));
		frameFixture.button("btnModifyActivity").click();

		Calendar cal = Calendar.getInstance();
		cal.set(2020, 0, 21);
		Date startTime = settingDate(cal.getTime(), 12, 11);
		cal.set(2021, 1, 11);
		Date endTime = settingDate(cal.getTime(), 11, 12);

		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(1);
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		frameFixture.textBox("textFieldStartDataActivity").doubleClick().deleteText().enterText("21/01/2020");
		frameFixture.textBox("textFieldStartHourActivity").doubleClick().deleteText().enterText("12:11");
		frameFixture.textBox("textFieldEndDataActivity").doubleClick().deleteText().enterText("11/02/2021");
		frameFixture.textBox("textFieldEndHourActivity").doubleClick().deleteText().enterText("11:12");

		frameFixture.button("btnUpdateActivity").click();
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("IDTest1", "matricolaOperatorTest2", "idBasicOperationTest2",
						startTime.toString(), endTime.toString()))
				.anySatisfy(e -> assertThat(e).contains("IDTest2", "matricolaOperatorTest2", "idBasicOperationTest2",
						startTime2.toString(), endTime2.toString()));
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("IDTest1", " has been updated.");
	}

	@Test
	@GUITest
	public void testUpdateActivityButtonErrorActivityNotExist() {
		removeActivityFromDB("IDTest1");

		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest1" + ".*"));
		frameFixture.button("btnModifyActivity").click();

		frameFixture.button("btnUpdateActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("IDTest1", " does not exist.");
	}

	@Test
	@GUITest
	public void testUpdateActivityButtonErrorOperatorNotExist() {
		removeOperatorFromDB("matricolaOperatorTest1");

		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest2" + ".*"));
		frameFixture.button("btnModifyActivity").click();
		frameFixture.comboBox("comboBoxOperatorActivity").selectItem(0);

		frameFixture.button("btnUpdateActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" does not exist.");
	}

	@Test
	@GUITest
	public void testUpdateActivityButtonErrorBasicOperationNotExist() {
		removeBasicOperationFromDB("idBasicOperationTest1");

		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest2" + ".*"));
		frameFixture.button("btnModifyActivity").click();
		frameFixture.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		frameFixture.button("btnUpdateActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("idBasicOperationTest1", " does not exist.");
	}

	@Test
	@GUITest
	public void testUpdateActivityButtonErrorEndDateBeforeStartDate() {
		// Setup
		Calendar cal = Calendar.getInstance();
		cal.set(2020, 0, 21);
		Date startTime = settingDate(cal.getTime(), 8, 00);
		cal.set(2020, 0, 11);
		Date endTime = settingDate(cal.getTime(), 16, 00);

		frameFixture.list().selectItem(Pattern.compile(".*" + "IDTest1" + ".*"));
		frameFixture.button("btnModifyActivity").click();

		frameFixture.textBox("textFieldStartDataActivity").doubleClick().deleteText().enterText("21/01/2020");
		frameFixture.textBox("textFieldEndDataActivity").doubleClick().deleteText().enterText("11/01/2020");

		frameFixture.button("btnUpdateActivity").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains(startTime.toString(), endTime.toString());
	}

	// OPERATORS PANEL

	@Test
	@GUITest
	public void testAddOperatorButtonSuccess() {
		frameFixture.tabbedPane().focus().selectTab("Operators");

		frameFixture.textBox("textFieldMatricola").enterText("testMatricolaNew");
		frameFixture.textBox("textFieldName").enterText("testNameNew");
		frameFixture.textBox("textFieldSurname").enterText("testSurnameNew");

		frameFixture.button("btnAddOperator").click();

		assertThat(frameFixture.label("lblMessageStatus").text()).contains("testMatricolaNew");
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("testMatricolaNew", "testNameNew", "testSurnameNew"));
	}

	@Test
	@GUITest
	public void testAddOperatorButtonErrorAlreadyExist() {
		addOperatorToDB("testMatricolaNew", "testNameNew", "testSurnameNew");

		frameFixture.tabbedPane().focus().selectTab("Operators");

		frameFixture.textBox("textFieldMatricola").enterText("testMatricolaNew");
		frameFixture.textBox("textFieldName").enterText("testNameNew");
		frameFixture.textBox("textFieldSurname").enterText("testSurnameNew");

		frameFixture.button("btnAddOperator").click();

		assertThat(frameFixture.label("lblMessageStatus").text()).contains("testMatricolaNew", " already exist.");
	}

	@Test
	@GUITest
	public void testRemoveOperatorButtonSuccess() {
		frameFixture.tabbedPane().focus().selectTab("Operators");
		frameFixture.list().selectItem(Pattern.compile(".*" + "matricolaOperatorTest1" + ".*"));
		frameFixture.button("btnDeleteOperator").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("matricolaOperatorTest1"));
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" has been removed.");
	}

	@Test
	@GUITest
	public void testRemoveOperatorButtonError() {
		frameFixture.tabbedPane().focus().selectTab("Operators");
		removeOperatorFromDB("matricolaOperatorTest1");
		frameFixture.list().selectItem(Pattern.compile(".*" + "matricolaOperatorTest1" + ".*"));
		frameFixture.button("btnDeleteOperator").click();
		assertThat(frameFixture.list().contents()).noneMatch(e -> e.contains("matricolaOperatorTest1"));
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" does not exist.");
	}

	@Test
	@GUITest
	public void testUpdateOperatorButtonSuccess() {
		frameFixture.tabbedPane().focus().selectTab("Operators");
		frameFixture.list().selectItem(Pattern.compile(".*" + "matricolaOperatorTest1" + ".*"));
		frameFixture.button("btnModifyOperator").click();

		frameFixture.textBox("textFieldMatricola").requireNotEditable();
		frameFixture.textBox("textFieldName").doubleClick().deleteText().enterText("nameUpdated");
		frameFixture.textBox("textFieldSurname").doubleClick().deleteText().enterText("surnameUpdated");

		frameFixture.button("btnUpdateOperator").click();
		assertThat(frameFixture.list().contents())
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest1", "nameUpdated", "surnameUpdated"))
				.anySatisfy(e -> assertThat(e).contains("matricolaOperatorTest2", "nameTest2", "surnameTest2"));
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" has been updated.");
	}

	@Test
	@GUITest
	public void testUpdateOperatorButtonError() {
		frameFixture.tabbedPane().focus().selectTab("Operators");

		removeOperatorFromDB("matricolaOperatorTest1");

		frameFixture.list().selectItem(Pattern.compile(".*" + "matricolaOperatorTest1" + ".*"));
		frameFixture.button("btnModifyOperator").click();

		frameFixture.button("btnUpdateOperator").click();
		assertThat(frameFixture.label("lblMessageStatus").text()).contains("matricolaOperatorTest1",
				" does not exist.");
	}

	// BASIC OPERATION PANEL

}
