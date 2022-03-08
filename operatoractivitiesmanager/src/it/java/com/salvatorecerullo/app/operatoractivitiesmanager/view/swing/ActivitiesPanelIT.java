package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.ActivityMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.BasicOperationMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;

@RunWith(GUITestRunner.class)
public class ActivitiesPanelIT extends AssertJSwingJUnitTestCase {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME_ACTIVITY = "activity";
	private static final String COLLECTION_NAME_OPERATOR = "operator";
	private static final String COLLECTION_NAME_BASICOPERATION = "basicoperation";

	private static final String THEACTIVITY = "The Activity: ";
	private static final String THEOPERATOR = "The Operator: ";
	private static final String NOTEXIST = " does not exist.";
	private static final String BASICOPERATION = "The BasicOperation with ID: ";

	private Date startTime;
	private Date endTime;

	private JFrame jFrame;
	private ActivitiesPanel activitiesPanel;
	private FrameFixture frameFixture;

	private ActivityController activityController;
	private ActivityRepository activityRepository;
	private OperatorRepository operatorRepository;
	private BasicOperationRepository basicOperationRepository;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Override
	protected void onSetUp() {
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		activityRepository = new ActivityMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_ACTIVITY);
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_OPERATOR);
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME,
				COLLECTION_NAME_BASICOPERATION);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			activitiesPanel = new ActivitiesPanel();
			activityController = new ActivityController(activityRepository, operatorRepository,
					basicOperationRepository, activitiesPanel);
			activitiesPanel.setActivityController(activityController);
			jFrame.add(activitiesPanel);
			return jFrame;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		frameFixture = new FrameFixture(robot, jFrame);
		frameFixture.show(); // shows the frame to test
	}

	@Before
	public void setup() {
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		endTime = cal.getTime();
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
	}

	@Test
	@GUITest
	public void testAllActivities() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID2", "basicOperationID2", startTime, endTime);
		activityRepository.save(activity1);
		activityRepository.save(activity2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.allActivities();
		});

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());
	}

	@Test
	@GUITest
	public void testAllOperators() {
		// Setup
		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.allOperators();
		});

		// Verify
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		String[] operatorComboContents = formActivityPanel.comboBox("comboBoxOperatorActivity").contents();
		assertThat(operatorComboContents).containsExactly(operator1.toString(), operator2.toString());
	}

	@Test
	@GUITest
	public void testAllBasicOperations() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation("IdTest1", "NameTest1", "DescriptionTest1");
		BasicOperation basicOperation2 = new BasicOperation("IdTest2", "NameTest2", "DescriptionTest2");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.allBasicOperation();
		});

		// Verify
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		String[] basicOperationComboContents = formActivityPanel.comboBox("comboBoxBasicOperationActivity").contents();
		assertThat(basicOperationComboContents).containsExactly(basicOperation1.toString(), basicOperation2.toString());
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		// Setup
		Operator operatorNew = new Operator("MatricolaNew", "NameNew", "SurnameNew");
		operatorRepository.save(operatorNew);
		BasicOperation basicOperationNew = new BasicOperation("BasicOperationIdNew", "NameNew", "DescriptionNew");
		basicOperationRepository.save(basicOperationNew);

		String idTemp = activitiesPanel.getActivityIdTemp();
		Activity activityNew = new Activity(idTemp, "MatricolaNew", "BasicOperationIdNew", startTime, endTime);

		// Exercise
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorNew);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationNew);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activityNew.toString());

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus")
				.requireText(THEACTIVITY + activityNew.getId() + " has been added.");
	}

	@Test
	@GUITest
	public void testAddButtonErrorActivityAlreadyExist() {
		// Setup
		Operator operatorNew = new Operator("MatricolaNew", "NameNew", "SurnameNew");
		operatorRepository.save(operatorNew);

		BasicOperation basicOperationNew = new BasicOperation("BasicOperationIdNew", "NameNew", "DescriptionNew");
		basicOperationRepository.save(basicOperationNew);

		String idTemp = activitiesPanel.getActivityIdTemp();
		Activity activityNew = new Activity(idTemp, "MatricolaNew", "BasicOperationIdNew", startTime, endTime);
		activityRepository.save(activityNew);

		// Exercise
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorNew);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationNew);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activityNew.toString());

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus")
				.requireText(THEACTIVITY + activityNew.getId() + " already exist.");
	}

	@Test
	@GUITest
	public void testAddButtonErrorOperatorNotExist() {
		// Setup
		Operator operatorNew = new Operator("MatricolaNew", "NameNew", "SurnameNew");
		BasicOperation basicOperationNew = new BasicOperation("BasicOperationIdNew", "NameNew", "DescriptionNew");
		basicOperationRepository.save(basicOperationNew);

		String idTemp = activitiesPanel.getActivityIdTemp();
		Activity activityNew = new Activity(idTemp, "MatricolaNew", "BasicOperationIdNew", startTime, endTime);

		// Exercise
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorNew);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationNew);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		

		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly();

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + activityNew.getOperatorMatricola() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testAddButtonErrorBasicOperationNotExist() {
		// Setup
		Operator operatorNew = new Operator("MatricolaNew", "NameNew", "SurnameNew");
		operatorRepository.save(operatorNew);
		BasicOperation basicOperationNew = new BasicOperation("BasicOperationIdNew", "NameNew", "DescriptionNew");

		String idTemp = activitiesPanel.getActivityIdTemp();
		Activity activityNew = new Activity(idTemp, "MatricolaNew", "BasicOperationIdNew", startTime, endTime);

		// Exercise
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorNew);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationNew);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly();

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus")
				.requireText(BASICOPERATION + activityNew.getOperationId() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testAddButtonErrorStartDateFollowEndDate() {
		// Setup
		Operator operatorNew = new Operator("MatricolaNew", "NameNew", "SurnameNew");
		operatorRepository.save(operatorNew);

		BasicOperation basicOperationNew = new BasicOperation("BasicOperationIdNew", "NameNew", "DescriptionNew");
		basicOperationRepository.save(basicOperationNew);

		String idTemp = activitiesPanel.getActivityIdTemp();
		Activity activityNew = new Activity(idTemp, "MatricolaNew", "BasicOperationIdNew", endTime, startTime);

		// Exercise
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorNew);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationNew);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly();

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus").requireText(
				"The Start Date: " + activityNew.getStartTime() + "follow the End Date: " + activityNew.getEndTime());
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID", "basicOperationID", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID", "basicOperationID", startTime, endTime);

		Operator operator = new Operator("operatorID", "Name", "Surname");
		operatorRepository.save(operator);

		BasicOperation basicOperation = new BasicOperation("basicOperationID", "Name", "Description");
		basicOperationRepository.save(basicOperation);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnDeleteActivity").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activity2.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEACTIVITY + activity1.getId() + " has been removed.");
	}

	@Test
	@GUITest
	public void testDeleteButtonError() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID", "basicOperationID", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID", "basicOperationID", startTime, endTime);
		Activity activityNotDB = new Activity("activityIdNotDB", "operatorID", "basicOperationID", startTime, endTime);

		Operator operator = new Operator("operatorID", "Name", "Surname");
		operatorRepository.save(operator);

		BasicOperation basicOperation = new BasicOperation("basicOperationID", "Name", "Description");
		basicOperationRepository.save(basicOperation);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activitiesPanel.getListActivitiesModel().addElement(activityNotDB);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(2);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnDeleteActivity").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());
		listBottomMenuPanel.label("lblMessageStatus").requireText(THEACTIVITY + activityNotDB.getId() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testFindByOperatorSuccess() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID2", "basicOperationID2", startTime, endTime);
		Activity activity3 = new Activity("activityId3", "operatorID1", "basicOperationID1", startTime, endTime);

		Operator operator1 = new Operator("operatorID1", "Name", "Surname");
		Operator operator2 = new Operator("operatorID2", "Name", "Surname");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("basicOperationID1", "Name", "Description");
		BasicOperation basicOperation2 = new BasicOperation("basicOperationID2", "Name", "Description");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activityController.addActivity(activity3);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator1);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator2);
		});

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);

		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnFindByOperator").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");

		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity3.toString());

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);

		listTopMenuPanel.button("btnFindByOperator").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activity2.toString());

	}

	@Test
	@GUITest
	public void testFindByOperatorError() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId3", "operatorID1", "basicOperationID1", startTime, endTime);

		Operator operator1 = new Operator("operatorID1", "Name", "Surname");
		Operator operator2 = new Operator("operatorID2", "Name", "Surname");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("basicOperationID1", "Name", "Description");
		BasicOperation basicOperation2 = new BasicOperation("basicOperationID2", "Name", "Description");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator1);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator2);
		});

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);

		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnFindByOperator").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.label("lblMessageStatus").requireText(THEOPERATOR + operator2.getMatricola() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testFindByBasicOperationSuccess() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "operationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID2", "operationID2", startTime, endTime);
		Activity activity3 = new Activity("activityId3", "operatorID1", "operationID1", startTime, endTime);

		Operator operator1 = new Operator("operatorID1", "Name", "Surname");
		Operator operator2 = new Operator("operatorID2", "Name", "Surname");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("operationID1", "Name", "Description");
		BasicOperation basicOperation2 = new BasicOperation("operationID2", "Name", "Description");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation1);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation2);
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activityController.addActivity(activity3);
		});

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnFindByBasicOperation").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");

		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity3.toString());

		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);

		listTopMenuPanel.button("btnFindByBasicOperation").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activity2.toString());

	}

	@Test
	@GUITest
	public void testFindByBasicOperationError() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID1", "basicOperationID1", startTime, endTime);

		Operator operator1 = new Operator("operatorID1", "Name", "Surname");
		Operator operator2 = new Operator("operatorID2", "Name", "Surname");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("basicOperationID1", "Name", "Description");
		BasicOperation basicOperation2 = new BasicOperation("basicOperationID2", "Name", "Description");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation1);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation2);
		});

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);

		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnFindByBasicOperation").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.label("lblMessageStatus").requireText(BASICOPERATION + basicOperation2.getId() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testFindByDay() {
		// Setup
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 9, 10, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 10, 9, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID1", "basicOperationID1", startTime2, endTime2);
		Activity activity3 = new Activity("activityId3", "operatorID1", "basicOperationID1", startTime, endTime);

		Operator operator1 = new Operator("operatorID1", "Name", "Surname");
		Operator operator2 = new Operator("operatorID2", "Name", "Surname");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("basicOperationID1", "Name", "Description");
		BasicOperation basicOperation2 = new BasicOperation("basicOperationID2", "Name", "Description");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activity1);
			activityController.addActivity(activity2);
			activityController.addActivity(activity3);
		});

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/02/").enterText("2021");

		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnFindByData").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity3.toString());

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").doubleClick().deleteText();
		formActivityPanel.textBox("textFieldStartDataActivity").setText("10/10/").enterText("2021");

		listTopMenuPanel.button("btnFindByData").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activity2.toString());
	}

	@Test
	@GUITest
	public void testShowAllActivitiesButton() {
		// Setup
		Activity activity1 = new Activity("activityId1", "operatorID1", "basicOperationID1", startTime, endTime);
		Activity activity2 = new Activity("activityId2", "operatorID2", "basicOperationID2", startTime, endTime);
		activityRepository.save(activity1);
		activityRepository.save(activity2);

		Operator operator1 = new Operator("Matricola1", "Name1", "Surname1");
		Operator operator2 = new Operator("Matricola2", "Name2", "Surname2");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		BasicOperation basicOperation1 = new BasicOperation("BasicOperationId1", "Name1", "Description1");
		BasicOperation basicOperation2 = new BasicOperation("BasicOperationId2", "Name2", "Description2");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		listTopMenuPanel.button("btnShowAll").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		String[] operatorComboContents = formActivityPanel.comboBox("comboBoxOperatorActivity").contents();
		assertThat(operatorComboContents).containsExactly(operator1.toString(), operator2.toString());

		String[] basicOperationComboContents = formActivityPanel.comboBox("comboBoxBasicOperationActivity").contents();
		assertThat(basicOperationComboContents).containsExactly(basicOperation1.toString(), basicOperation2.toString());
	}

	@Test
	@GUITest
	public void testUpdateActivitiesButtonSuccess() {
		// Setup
		Activity activityOld = new Activity("activityIdOld", "matricolaOld", "idOld", startTime, endTime);
		Operator operatorOld = new Operator("matricolaOld", "nameOld", "surnameOld");
		BasicOperation basicOperationOld = new BasicOperation("idOld", "nameOld", "descriptionOld");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 9, 10, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 10, 9, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Activity activityUpdate = new Activity("activityIdOld", "matricolaUpdate", "idUpdate", startTime2, endTime2);
		Operator operatorUpdate = new Operator("matricolaUpdate", "nameUpdate", "surnameUpdate");
		BasicOperation basicOperationUpdate = new BasicOperation("idUpdate", "nameUpdate", "descriptionUpdate");

		operatorRepository.save(operatorOld);
		operatorRepository.save(operatorUpdate);
		basicOperationRepository.save(basicOperationOld);
		basicOperationRepository.save(basicOperationUpdate);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activityOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationUpdate);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorOld);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorUpdate);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activityUpdate.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEACTIVITY + activityUpdate.getId() + " has been updated.");

	}

	@Test
	@GUITest
	public void testUpdateActivitiesButtonActivityDoesNotExistError() {
		// Setup
		Activity activityOld = new Activity("activityIdOld", "matricolaOld", "idOld", startTime, endTime);
		Operator operatorOld = new Operator("matricolaOld", "nameOld", "surnameOld");
		BasicOperation basicOperationOld = new BasicOperation("idOld", "nameOld", "descriptionOld");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 9, 10, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 10, 9, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Operator operatorUpdate = new Operator("matricolaUpdate", "nameUpdate", "surnameUpdate");
		BasicOperation basicOperationUpdate = new BasicOperation("idUpdate", "nameUpdate", "descriptionUpdate");

		operatorRepository.save(operatorOld);
		operatorRepository.save(operatorUpdate);
		basicOperationRepository.save(basicOperationOld);
		basicOperationRepository.save(basicOperationUpdate);

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.getListActivitiesModel().addElement(activityOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationUpdate);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorOld);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorUpdate);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		
		// Verify
		assertThat(listActivities.contents()).containsExactly();
		listBottomMenuPanel.label("lblMessageStatus").requireText(THEACTIVITY + activityOld.getId() + NOTEXIST);

	}

	@Test
	@GUITest
	public void testUpdateActivitiesButtonNewOperatorDoesNotExistError() {
		// Setup
		Activity activityOld = new Activity("activityIdOld", "matricolaOld", "idOld", startTime, endTime);
		Operator operatorOld = new Operator("matricolaOld", "nameOld", "surnameOld");
		BasicOperation basicOperationOld = new BasicOperation("idOld", "nameOld", "descriptionOld");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 9, 10, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 10, 9, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Activity activityUpdate = new Activity("activityIdOld", "matricolaUpdate", "idUpdate", startTime2, endTime2);
		Operator operatorUpdate = new Operator("matricolaUpdate", "nameUpdate", "surnameUpdate");
		BasicOperation basicOperationUpdate = new BasicOperation("idUpdate", "nameUpdate", "descriptionUpdate");

		operatorRepository.save(operatorOld);
		basicOperationRepository.save(basicOperationOld);
		basicOperationRepository.save(basicOperationUpdate);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activityOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationUpdate);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorOld);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorUpdate);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + activityUpdate.getOperatorMatricola() + NOTEXIST);
		assertThat(listActivities.contents()).containsExactly(activityOld.toString());
	}

	@Test
	@GUITest
	public void testUpdateActivitiesButtonNewBasicOperationDoesNotExistError() {
		// Setup
		Activity activityOld = new Activity("activityIdOld", "matricolaOld", "idOld", startTime, endTime);
		Operator operatorOld = new Operator("matricolaOld", "nameOld", "surnameOld");
		BasicOperation basicOperationOld = new BasicOperation("idOld", "nameOld", "descriptionOld");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 9, 10, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 10, 9, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Activity activityUpdate = new Activity("activityIdOld", "matricolaUpdate", "idUpdate", startTime2, endTime2);
		Operator operatorUpdate = new Operator("matricolaUpdate", "nameUpdate", "surnameUpdate");
		BasicOperation basicOperationUpdate = new BasicOperation("idUpdate", "nameUpdate", "descriptionUpdate");

		operatorRepository.save(operatorOld);
		operatorRepository.save(operatorUpdate);
		basicOperationRepository.save(basicOperationOld);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activityOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationUpdate);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorOld);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorUpdate);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activityOld.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(BASICOPERATION + activityUpdate.getOperationId() + NOTEXIST);

	}

	@Test
	@GUITest
	public void testUpdateActivitiesButtonStartDateFollowEndDateError() {
		// Setup
		Activity activityOld = new Activity("activityIdOld", "matricolaOld", "idOld", startTime, endTime);
		Operator operatorOld = new Operator("matricolaOld", "nameOld", "surnameOld");
		BasicOperation basicOperationOld = new BasicOperation("idOld", "nameOld", "descriptionOld");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 10, 9, 8, 0, 00);
		Date startTime2 = cal.getTime();
		cal.set(2021, 9, 10, 16, 00, 00);
		Date endTime2 = cal.getTime();

		Activity activityUpdate = new Activity("activityIdOld", "matricolaUpdate", "idUpdate", startTime2, endTime2);
		Operator operatorUpdate = new Operator("matricolaUpdate", "nameUpdate", "surnameUpdate");
		BasicOperation basicOperationUpdate = new BasicOperation("idUpdate", "nameUpdate", "descriptionUpdate");

		operatorRepository.save(operatorOld);
		operatorRepository.save(operatorUpdate);
		basicOperationRepository.save(basicOperationOld);
		basicOperationRepository.save(basicOperationUpdate);

		// Exercise
		GuiActionRunner.execute(() -> {
			activityController.addActivity(activityOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationOld);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperationUpdate);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorOld);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operatorUpdate);
		});

		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		listActivities.selectItem(0);

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(1);
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		assertThat(listActivities.contents()).containsExactly(activityOld.toString());
		listBottomMenuPanel.label("lblMessageStatus").requireText("The Start Date: " + activityUpdate.getStartTime()
				+ "follow the End Date: " + activityUpdate.getEndTime());
	}
}
