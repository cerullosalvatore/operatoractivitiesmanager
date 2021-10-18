package com.salvatorecerullo.app.operatoractivitiesmanager.repository.mongorepository;

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
		if (operatorCollection.find(Filters.eq("_id", operator.getMatricola())).first() == null) {
			operatorCollection.insertOne(new Document().append("_id", operator.getMatricola())
					.append("name", operator.getName()).append("surname", operator.getSurname()));
		}
	}

	@Override
	public Operator findByMatricola(String matricola) {
		Document documentRetrivied = operatorCollection.find(Filters.eq("_id", matricola)).first();
		if (documentRetrivied != null) {
			return fromDocumentToOperator(documentRetrivied);
		} else {
			return null;
		}
	}

	@Override
	public void delete(String matricola) {
		operatorCollection.deleteOne(Filters.eq("_id", matricola));
	}

	@Override
	public void update(Operator newOperator) {
		Bson update1 = Updates.set("name", newOperator.getName());
		Bson update2 = Updates.set("surname", newOperator.getSurname());
		Bson updates = Updates.combine(update1, update2);
		operatorCollection.updateOne(Filters.eq("_id", newOperator.getMatricola()), updates);
	}

	private Operator fromDocumentToOperator(Document document) {
		return new Operator(document.getString("_id"), document.getString("name"), document.getString("surname"));
	}

}
