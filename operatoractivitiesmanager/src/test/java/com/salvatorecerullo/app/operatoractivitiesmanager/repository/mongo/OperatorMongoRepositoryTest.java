package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.bson.Document;

public class OperatorMongoRepositoryTest {
	private static final String DB_NAME = "operatoractivities";
	private static final String COLLECTION_NAME = "operator";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	private MongoClient mongoClient;
	private OperatorMongoRepository operatorMongoRepository;
	private MongoCollection<Document> operatorCollection;

	@BeforeClass
	public static void setupServer() {
		mongoServer = new MongoServer(new MemoryBackend());
		serverAddress = mongoServer.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		mongoServer.shutdown();
	}

	@Before
	public void setup() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		operatorMongoRepository = new OperatorMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		operatorCollection = database.getCollection(COLLECTION_NAME);
	}

	@Test
	public void testFindAllDBEmpty() {
		assertThat(operatorMongoRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllDBNotEmpty() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		Operator operator2 = new Operator("matricola2", "name2", "surname2");
		addOperatorToDB(operator1);
		addOperatorToDB(operator2);

		// Exercise
		List<Operator> operators = operatorMongoRepository.findAll();

		// Verify
		assertThat(operators).containsExactly(operator1, operator2);
	}

	@Test
	public void testSaveOperatorSuccessfull() {
		// Setup
		Operator newOperator = new Operator("newMatricola", "newName", "newSurname");

		// Exercise
		operatorMongoRepository.save(newOperator);

		// Verify
		assertThat(operatorMongoRepository.findAll()).containsExactly(newOperator);
	}

	@Test
	public void testSaveOperatorErrorDuplicateMatricola() {
		// Setup
		Operator oldOperator = new Operator("oldMatricola", "oldName", "oldSurname");
		Operator newOperator = new Operator("oldMatricola", "newName", "newSurname");
		addOperatorToDB(oldOperator);

		// Exercise
		operatorMongoRepository.save(newOperator);

		// Verify
		assertThat(operatorMongoRepository.findAll()).containsExactly(oldOperator);
	}

	@Test
	public void testFindByMatricolaSuccess() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		Operator operator2 = new Operator("matricola2", "name2", "surname2");
		addOperatorToDB(operator1);
		addOperatorToDB(operator2);

		// Exercise
		Operator operatorRetrivied = operatorMongoRepository.findByMatricola(operator1.getMatricola());

		// Verify
		assertThat(operatorRetrivied).isEqualTo(operator1);
	}

	@Test
	public void testFindByMatricolaErrorNotFound() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		Operator operator2 = new Operator("matricola2", "name2", "surname2");
		addOperatorToDB(operator1);

		// Exercise
		Operator operatorRetrivied = operatorMongoRepository.findByMatricola(operator2.getMatricola());

		// Verify
		assertThat(operatorRetrivied).isNull();
	}

	@Test
	public void testDeleteOperatorSuccessfull() {
		// Setup
		Operator operator1 = new Operator("matricola1", "name1", "surname1");
		addOperatorToDB(operator1);

		// Exercise
		operatorMongoRepository.delete(operator1.getMatricola());

		// Verify
		assertThat(readAllOperatorsFromDB()).isEmpty();
	}

	@Test
	public void testUpdateOperatorSuccessfull() {
		// Setup
		Operator operatorOld = new Operator("oldMatricola", "oldNmae", "oldSurname");
		Operator operatorNew = new Operator("oldMatricola", "newName", "newSurname");
		addOperatorToDB(operatorOld);

		// Exercise
		operatorMongoRepository.update(operatorNew);

		// Verify
		assertThat(readAllOperatorsFromDB().get(0)).isEqualTo(operatorNew);
	}

	@Test
	public void testUpdateOperatorError() {
		// Setup
		Operator operatorOld = new Operator("oldMatricola", "oldNmae", "oldSurname");
		Operator operatorNew = new Operator("newMatricola", "newName", "newSurname");
		addOperatorToDB(operatorOld);

		// Exercise
		operatorMongoRepository.update(operatorNew);

		// Verify
		assertThat(readAllOperatorsFromDB().get(0)).isNotEqualTo(operatorNew);
	}

	private void addOperatorToDB(Operator operator) {
		operatorCollection.insertOne(new Document().append("_id", operator.getMatricola())
				.append("name", operator.getName()).append("surname", operator.getSurname()));
	}

	private List<Operator> readAllOperatorsFromDB() {
		return StreamSupport.stream(operatorCollection.find().spliterator(), false).map(this::fromDocumentToOperator)
				.collect(Collectors.toList());
	}

	private Operator fromDocumentToOperator(Document document) {
		return new Operator(document.getString("_id"), document.getString("name"), document.getString("surname"));
	}

}
