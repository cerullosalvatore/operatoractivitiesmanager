package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
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
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
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
public class OperatorActivitiesManagerViewIT extends AssertJSwingJUnitTestCase {
	private OperatorActivitiesManagerView operatorActivitiesManagerView;

	private ActivityController activityController;
	private OperatorController operatorController;
	private BasicOperationController basicOperationController;

	private static final String DB_NAME = "operatoractivities";

	private static final String COLLECTION_NAME_ACTIVITY = "activity";
	private static final String COLLECTION_NAME_OPERATOR = "operator";
	private static final String COLLECTION_NAME_BASICOPERATION = "basicoperation";

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	private ActivityRepository activityRepository;
	private OperatorRepository operatorRepository;
	private BasicOperationRepository basicOperationRepository;

	private FrameFixture frameFixture;

	private Date startTime;
	private Date endTime;

	@Override
	protected void onSetUp() throws Exception {
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		activityRepository = new ActivityMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_ACTIVITY);
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME_OPERATOR);
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME,
				COLLECTION_NAME_BASICOPERATION);

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
			activityController = new ActivityController(activityRepository, operatorRepository,
					basicOperationRepository, operatorActivitiesManagerView.getActivitiesPanel());
			operatorActivitiesManagerView.getActivitiesPanel().setActivityController(activityController);

			operatorController = new OperatorController(operatorRepository,
					operatorActivitiesManagerView.getOperatorsPanel());
			operatorActivitiesManagerView.getOperatorsPanel().setOperatorController(operatorController);

			basicOperationController = new BasicOperationController(basicOperationRepository,
					operatorActivitiesManagerView.getBasicOperationPanel());
			operatorActivitiesManagerView.getBasicOperationPanel()
					.setBasicOperationController(basicOperationController);

			return operatorActivitiesManagerView;
		});

		Robot robot = robot();
		robot.settings().eventPostingDelay(500);
		frameFixture = new FrameFixture(robot, operatorActivitiesManagerView);
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
	public void testOperatorsSelectionTab() {
		// Setup
		Operator operator1 = new Operator("Matricola1", "Name1", "Surname1");
		Operator operator2 = new Operator("Matricola2", "Name2", "Surname2");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		// Exercise
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();

		// Verify
		JPanelFixture operatorPanelFixture = frameFixture.panel("contentPane").panel("operatorsPanel");
		JListFixture listOperators = operatorPanelFixture.panel("listOperatorsPanel").list("listOperators");
		assertThat(listOperators.contents()).containsExactly(operator1.toString(), operator2.toString());
	}

	@Test
	@GUITest
	public void testBasicOperationSelectionTab() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation("BasicOperationId1", "Name1", "Description1");
		BasicOperation basicOperation2 = new BasicOperation("BasicOperationId2", "Name2", "Description2");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);

		// Exercise
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Basic Operations").requireVisible().requireEnabled();

		// Verify
		JPanelFixture basicOperationPanelFixture = frameFixture.panel("contentPane").panel("basicOperationPanel");
		JListFixture listBasicOperations = basicOperationPanelFixture.panel("listBasicOperationsPanel")
				.list("listBasicOperations");
		assertThat(listBasicOperations.contents()).containsExactly(basicOperation1.toString(),
				basicOperation2.toString());
	}

	@Test
	@GUITest
	public void testActivitiesSelectionTab() {
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
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		tabbedPaneFixture.focus().selectTab("Activities").requireVisible().requireEnabled();

		// Verify
		JPanelFixture activityPanelFixture = frameFixture.panel("contentPane").panel("activitiesPanel");
		JListFixture listActivities = activityPanelFixture.panel("listActivitiesPanel").list("listActivities");
		assertThat(listActivities.contents()).containsExactly(activity1.toString(), activity2.toString());

		JPanelFixture formActivityPanel = activityPanelFixture.panel("newActivityPanel").panel("formActivityPanel");
		JComboBoxFixture comboBoxOperatorFixture = formActivityPanel.comboBox("comboBoxOperatorActivity");

		assertThat(comboBoxOperatorFixture.contents()).containsExactly(operator1.toString(), operator2.toString());

		JComboBoxFixture comboBoxBasicOperationsFixture = formActivityPanel.comboBox("comboBoxBasicOperationActivity");

		assertThat(comboBoxBasicOperationsFixture.contents()).containsExactly(basicOperation1.toString(),
				basicOperation2.toString());
	}

}
