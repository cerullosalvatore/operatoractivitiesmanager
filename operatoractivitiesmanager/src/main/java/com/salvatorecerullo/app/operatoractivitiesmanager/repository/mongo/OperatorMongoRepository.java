package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;

public class OperatorMongoRepository implements OperatorRepository {
	private static final String ID = "_id";
	private static final String NAME = "name";
	private static final String SURNAME = "surname";

	private MongoCollection<Document> operatorCollection;

	public OperatorMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		operatorCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	@Override
	public List<Operator> findAll() {
		return StreamSupport.stream(operatorCollection.find().spliterator(), false).map(this::fromDocumentToOperator)
				.collect(Collectors.toList());
	}

	@Override
	public void save(Operator operator) {
		if (operatorCollection.find(Filters.eq(ID, operator.getMatricola())).first() == null) {
			operatorCollection.insertOne(new Document().append(ID, operator.getMatricola())
					.append(NAME, operator.getName()).append(SURNAME, operator.getSurname()));
		}
	}

	@Override
	public Operator findByMatricola(String matricola) {
		Document documentRetrivied = operatorCollection.find(Filters.eq(ID, matricola)).first();
		if (documentRetrivied != null) {
			return fromDocumentToOperator(documentRetrivied);
		} else {
			return null;
		}
	}

	@Override
	public void delete(String matricola) {
		operatorCollection.deleteOne(Filters.eq(ID, matricola));
	}

	@Override
	public void update(Operator newOperator) {
		Bson update1 = Updates.set(NAME, newOperator.getName());
		Bson update2 = Updates.set(SURNAME, newOperator.getSurname());
		Bson updates = Updates.combine(update1, update2);
		operatorCollection.updateOne(Filters.eq(ID, newOperator.getMatricola()), updates);
	}

	private Operator fromDocumentToOperator(Document document) {
		return new Operator(document.getString(ID), document.getString(NAME), document.getString(SURNAME));
	}

}
