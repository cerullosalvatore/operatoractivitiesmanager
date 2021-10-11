package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorControllerTest {

	@Mock
	private OperatorRepository operatorRepository;

	@Mock
	private OperatorView operatorView;

	@InjectMocks
	private OperatorController operatorController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAllOperators() {
		List<Operator> operators = new ArrayList<Operator>();
		operators.add(new Operator());
		when(operatorRepository.findAll()).thenReturn(operators);
		operatorController.allOperators();
		verify(operatorView).showAllOperators(operators);
	}
	
	@Test 
	public void testAddOperator() {
		Operator newOperator = new Operator("matricola0000", "testName", "testSurname");
		operatorController.addOperator(newOperator);
		InOrder inOrder = Mockito.inOrder(operatorRepository, operatorView);
		inOrder.verify(operatorRepository).save(newOperator);
		inOrder.verify(operatorView).operatorAdded();
	}
}
