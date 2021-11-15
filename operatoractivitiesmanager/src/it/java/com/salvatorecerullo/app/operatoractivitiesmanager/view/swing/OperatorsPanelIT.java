package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;

public class OperatorsPanelIT extends AssertJSwingJUnitTestCase {
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

		// Remove all operator from DB
		for (Operator operator : operatorRepository.findAll()) {
			operatorRepository.delete(operator.getMatricola());
		}
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
		frameFixture = new FrameFixture(robot(), jFrame);
		frameFixture.show(); // shows the frame to test
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
	
	
}
