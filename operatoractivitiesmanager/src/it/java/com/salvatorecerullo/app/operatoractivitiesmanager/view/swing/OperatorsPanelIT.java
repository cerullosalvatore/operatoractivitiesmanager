package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

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

import static org.assertj.core.api.Assertions.assertThat;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;

@RunWith(GUITestRunner.class)
public class OperatorsPanelIT extends AssertJSwingJUnitTestCase {
	private static final String THEOPERATOR = "The Operator: ";
	private static final String NOTEXIST = " does not exist.";

	private JFrame jFrame;
	private OperatorsPanel operatorsPanel;
	private FrameFixture frameFixture;

	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	private OperatorController operatorController;
	private OperatorRepository operatorRepository;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);

		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			operatorsPanel = new OperatorsPanel();
			operatorController = new OperatorController(operatorRepository, operatorsPanel);
			operatorsPanel.setOperatorController(operatorController);
			jFrame.add(operatorsPanel);
			return jFrame;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		robot.settings().delayBetweenEvents(100);
		frameFixture = new FrameFixture(robot, jFrame);
		frameFixture.show(); // shows the frame to test
	}

	@Before
	public void setup() {
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		operatorRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		operatorController = new OperatorController(operatorRepository, operatorsPanel);
	}

	@Test
	@GUITest
	public void testAllOperators() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		Operator operator2 = new Operator("matricolaTest2", "nameTest2", "surnameTest2");
		operatorRepository.save(operator1);
		operatorRepository.save(operator2);

		// Exercise
		GuiActionRunner.execute(() -> {
			operatorController.allOperators();
		});

		// Verify
		JListFixture listOperators = frameFixture.panel("listOperatorsPanel").list("listOperators");
		assertThat(listOperators.contents()).containsExactly(operator1.toString(), operator2.toString());
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		operatorRepository.save(operator1);
		Operator operatorNew = new Operator("matricolaTestNew", "nameTestNew", "surnameTestNew");

		// Exercise
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		formOperatorPanel.textBox("textFieldMatricola").enterText(operatorNew.getMatricola());
		formOperatorPanel.textBox("textFieldName").enterText(operatorNew.getName());
		formOperatorPanel.textBox("textFieldSurname").enterText(operatorNew.getSurname());

		buttonsFormOperatorPanel.button("btnAddOperator").click();

		// Verify
		JListFixture listOperators = frameFixture.panel("listOperatorsPanel").list("listOperators");
		assertThat(listOperators.contents()).containsExactly(operator1.toString(), operatorNew.toString());

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operatorNew.getMatricola() + " has been added.");
	}

	@Test
	@GUITest
	public void testAddButtonError() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		operatorRepository.save(operator1);
		Operator operatorNew = new Operator("matricolaTest1", "nameTestNew", "surnameTestNew");

		// Exercise
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		formOperatorPanel.textBox("textFieldMatricola").enterText(operatorNew.getMatricola());
		formOperatorPanel.textBox("textFieldName").enterText(operatorNew.getName());
		formOperatorPanel.textBox("textFieldSurname").enterText(operatorNew.getSurname());

		buttonsFormOperatorPanel.button("btnAddOperator").click();

		// Verify
		JListFixture listOperators = frameFixture.panel("listOperatorsPanel").list("listOperators");
		assertThat(listOperators.contents()).containsExactly(operator1.toString());

		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operatorNew.getMatricola() + " already exist.");
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		Operator operator2 = new Operator("matricolaTest2", "nameTest2", "surnameTest2");

		// Exercise
		GuiActionRunner.execute(() -> {
			operatorController.addOperator(operator1);
			operatorController.addOperator(operator2);
		});

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").selectItem(0);

		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnDeleteOperator").click();

		// Verify
		assertThat(listOperatorsPanel.list("listOperators").contents()).containsExactly(operator2.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operator1.getMatricola() + " has been removed.");
	}

	@Test
	@GUITest
	public void testDeleteButtonError() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		Operator operator2 = new Operator("matricolaTest2", "nameTest2", "surnameTest2");
		Operator operatorNotDB = new Operator("matricolaTestNotDB", "nameTestNotDB", "surnameTestNotDB");

		// Exercise
		GuiActionRunner.execute(() -> {
			operatorController.addOperator(operator1);
			operatorController.addOperator(operator2);
			operatorsPanel.getListOperatorsModel().addElement(operatorNotDB);
		});

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").selectItem(2);

		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnDeleteOperator").click();

		// Verify
		assertThat(listOperatorsPanel.list("listOperators").contents()).containsExactly(operator1.toString(),
				operator2.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operatorNotDB.getMatricola() + NOTEXIST);
	}

	@Test
	@GUITest
	public void testUpdateButtonSuccess() {
		// Setup
		Operator operatorOld = new Operator("matricolaTestOld", "nameTestOld", "surnameTestOld");
		Operator operatorUpdate = new Operator("matricolaTestOld", "nameTestNew", "surnameTestNew");

		// Exercise
		GuiActionRunner.execute(() -> {
			operatorController.addOperator(operatorOld);
		});

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").selectItem(0);

		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyOperator").click();

		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		formOperatorPanel.textBox("textFieldName").doubleClick().deleteText();
		formOperatorPanel.textBox("textFieldName").enterText(operatorUpdate.getName());
		formOperatorPanel.textBox("textFieldSurname").doubleClick().deleteText();
		formOperatorPanel.textBox("textFieldSurname").enterText(operatorUpdate.getSurname());

		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel");
		buttonsFormOperatorPanel.button("btnUpdateOperator").click();

		// Verify
		assertThat(listOperatorsPanel.list("listOperators").contents()).containsExactly(operatorUpdate.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operatorOld.getMatricola() + " has been updated.");
	}

	@Test
	@GUITest
	public void testUpdateButtonError() {
		// Setup
		Operator operator1 = new Operator("matricolaTest1", "nameTest1", "surnameTest1");
		Operator operatorNotDB = new Operator("matricolaTestNotDB", "nameTestNotDB", "surnameTestNotDB");

		// Exercise
		GuiActionRunner.execute(() -> {
			operatorController.addOperator(operator1);
			operatorsPanel.getListOperatorsModel().addElement(operatorNotDB);
		});

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").selectItem(1);

		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyOperator").click();

		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel");
		buttonsFormOperatorPanel.button("btnUpdateOperator").click();

		// Verify
		assertThat(listOperatorsPanel.list("listOperators").contents()).containsExactly(operator1.toString());
		listBottomMenuPanel.label("lblMessageStatus")
				.requireText(THEOPERATOR + operatorNotDB.getMatricola() + NOTEXIST);
	}

}
