package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Before;
import org.junit.Test;
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
		MockitoAnnotations.initMocks(this);
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
		frameFixture = new FrameFixture(robot(), jFrame);
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
		activityRepository = new ActivityMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_ACTIVITY);
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_OPERATOR);
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME,
				COLLECTION_NAME_BASICOPERATION);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		activityController = new ActivityController(activityRepository, operatorRepository, basicOperationRepository,
				activitiesPanel);
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
		formActivityPanel.textBox("textFieldStartDataActivity").enterText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").enterText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").enterText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").enterText(formattedEndHour);

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
		formActivityPanel.textBox("textFieldStartDataActivity").enterText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").enterText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").enterText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").enterText(formattedEndHour);

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
		formActivityPanel.textBox("textFieldStartDataActivity").enterText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").enterText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").enterText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").enterText(formattedEndHour);

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
		formActivityPanel.textBox("textFieldStartDataActivity").enterText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").enterText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").enterText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").enterText(formattedEndHour);

		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		JListFixture listActivities = frameFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly();

		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.label("lblMessageStatus")
				.requireText(BASICOPERATION + activityNew.getOperationId() + NOTEXIST);
	}
}
