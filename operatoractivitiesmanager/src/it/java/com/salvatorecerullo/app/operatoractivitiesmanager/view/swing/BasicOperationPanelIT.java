package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.BasicOperationMongoRepository;

import de.bwaldvogel.mongo.bson.ObjectId;

@RunWith(GUITestRunner.class)
public class BasicOperationPanelIT  extends AssertJSwingJUnitTestCase {
	private static final String BASICOPERATION = "The BasicOperation with ID: ";
	private static final String NOTEXIST = " does not exist.";
	
	private JFrame jFrame;
	private BasicOperationPanel basicOperationPanel;
	private FrameFixture frameFixture;

	private BasicOperationRepository basicOperationRepository;
	private BasicOperationController basicOperationController;
	
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "basicoperation";
	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		basicOperationRepository = new BasicOperationMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			basicOperationPanel = new BasicOperationPanel();
			basicOperationController = new BasicOperationController(basicOperationRepository, basicOperationPanel);
			basicOperationPanel.setBasicOperationController(basicOperationController);
			jFrame.add(basicOperationPanel);
			return jFrame;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		frameFixture = new FrameFixture(robot(), jFrame);
		frameFixture.show(); // shows the frame to test
	}

	@Test
	@GUITest
	public void testAllBasicOperations() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation(new ObjectId().toString(), "name1", "description1");
		BasicOperation basicOperation2 = new BasicOperation(new ObjectId().toString(), "name2", "description2");
		basicOperationRepository.save(basicOperation1);
		basicOperationRepository.save(basicOperation2);
		
		// Exercise
		GuiActionRunner.execute(() -> {
			basicOperationController.allBasicOperations();
		});

		// Verify
		JListFixture listBasicOperations = frameFixture.panel("listBasicOperationsPanel").list("listBasicOperations");
		assertThat(listBasicOperations.contents()).containsExactly(basicOperation1.toString(), basicOperation2.toString());
	}

}
