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

	private static final String THEOPERATOR = "The Operator: ";
	private static final String NOTEXIST = " does not exist.";

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
		// Setup
		List<Operator> operators = new ArrayList<Operator>();
		operators.add(new Operator("testMatricola", "testName", "testSurname"));
		when(operatorRepository.findAll()).thenReturn(operators);
		// Exercise
		operatorController.allOperators();
		// Verify
		verify(operatorView).showAllOperators(operators);
	}

	@Test
	public void testAddOperatorSuccessfull() {
		// Setup
		Operator newOperator = new Operator("testMatricola", "testName", "testSurname");
		when(operatorRepository.findByMatricola(newOperator.getMatricola())).thenReturn(null);
		// Exercise
		operatorController.addOperator(newOperator);
		// Verify
		InOrder inOrder = Mockito.inOrder(operatorRepository, operatorView);
		inOrder.verify(operatorRepository).save(newOperator);
		inOrder.verify(operatorView)
				.showSuccessfull(THEOPERATOR + newOperator.getMatricola() + " has been added.");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testAddOperatorError() {
		// Setup
		Operator newOperator = new Operator("testMatricola", "testName", "testSurname");
		Operator oldOperator = new Operator("testMatricola", "testNameOld", "testSurnameOld");
		when(operatorRepository.findByMatricola(newOperator.getMatricola())).thenReturn(oldOperator);
		// Exercise
		operatorController.addOperator(newOperator);
		// Verify
		verify(operatorView).showError(THEOPERATOR + newOperator.getMatricola() + " already exist.");
	}

	@Test
	public void testRemoveOperatorSuccessfull() {
		// Setup
		Operator oldOperator = new Operator("testMatricola", "testName", "testSurname");
		when(operatorRepository.findByMatricola(oldOperator.getMatricola())).thenReturn(oldOperator);
		// Exercise
		operatorController.removeOperator(oldOperator);
		// Verify
		InOrder inOrder = Mockito.inOrder(operatorRepository, operatorView);
		inOrder.verify(operatorRepository).delete(oldOperator.getMatricola());
		inOrder.verify(operatorView)
				.showSuccessfull(THEOPERATOR + oldOperator.getMatricola() + " has been removed.");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testRemoveOperatorError() {
		// Setup
		Operator newOperator = new Operator("testMatricola", "testName", "testSurname");
		when(operatorRepository.findByMatricola(newOperator.getMatricola())).thenReturn(null);
		// Exercise
		operatorController.removeOperator(newOperator);
		// Verify
		verify(operatorView).showError(THEOPERATOR + newOperator.getMatricola() + NOTEXIST);
	}

	@Test
	public void testUpdateOperatorSuccessfull() {
		// Setup
		Operator newOperator = new Operator("testMatricola", "testName", "testSurname");
		Operator oldOperator = new Operator("testMatricola", "testNameOld", "testSurnameOld");
		when(operatorRepository.findByMatricola(newOperator.getMatricola())).thenReturn(oldOperator);
		// Exercise
		operatorController.updateOperator(newOperator);
		// verify
		InOrder inOrder = Mockito.inOrder(operatorRepository, operatorView);
		inOrder.verify(operatorRepository).update(newOperator);
		inOrder.verify(operatorView)
				.showSuccessfull(THEOPERATOR + newOperator.getMatricola() + " has been updated.");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testUpdateOperatorError() {
		// Setup
		Operator newOperator = new Operator("testMatricola", "testName", "testSurname");
		when(operatorRepository.findByMatricola(newOperator.getMatricola())).thenReturn(null);
		// Exercise
		operatorController.updateOperator(newOperator);
		// verify
		verify(operatorView).showError(THEOPERATOR + newOperator.getMatricola() + NOTEXIST);
	}

}
