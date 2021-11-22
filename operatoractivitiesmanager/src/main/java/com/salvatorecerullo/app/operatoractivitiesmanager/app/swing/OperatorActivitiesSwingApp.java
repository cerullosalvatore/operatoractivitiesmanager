package com.salvatorecerullo.app.operatoractivitiesmanager.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.ActivityMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.BasicOperationMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo.OperatorMongoRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.swing.OperatorActivitiesManagerView;

@Command(mixinStandardHelpOptions = true)
public class OperatorActivitiesSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "operatoractivities";

	@Option(names = { "--db-collection-operators" }, description = "Collection name")
	private String collectionOperatorsName = "operator";

	@Option(names = { "--db-collection-basicoperations" }, description = "Collection name")
	private String collectionBasicOperationsName = "basicoperation";

	@Option(names = { "--db-collection-activities" }, description = "Collection name")
	private String collectionActiovitiesName = "activity";

	public static void main(String[] args) {
		new CommandLine(new OperatorActivitiesSwingApp()).execute(args);
	}
	
	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				ActivityRepository activityRepository = new ActivityMongoRepository(mongoClient, databaseName,
						collectionActiovitiesName);
				OperatorRepository operatorRepository = new OperatorMongoRepository(mongoClient, databaseName,
						collectionOperatorsName);
				BasicOperationRepository basicOperationRepository = new BasicOperationMongoRepository(mongoClient,
						databaseName, collectionBasicOperationsName);
				OperatorActivitiesManagerView operatorActivitiesManagerView = new OperatorActivitiesManagerView();

				ActivityController activityController = new ActivityController(activityRepository, operatorRepository,
						basicOperationRepository, operatorActivitiesManagerView.getActivitiesPanel());

				OperatorController operatorController = new OperatorController(operatorRepository,
						operatorActivitiesManagerView.getOperatorsPanel());

				BasicOperationController basicOperationController = new BasicOperationController(
						basicOperationRepository, operatorActivitiesManagerView.getBasicOperationPanel());

				operatorActivitiesManagerView.getActivitiesPanel().setActivityController(activityController);
				operatorActivitiesManagerView.getOperatorsPanel().setOperatorController(operatorController);
				operatorActivitiesManagerView.getBasicOperationPanel()
						.setBasicOperationController(basicOperationController);

				operatorActivitiesManagerView.setVisible(true);
				
				activityController.allActivities();
				activityController.allOperators();
				activityController.allBasicOperation();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

}
