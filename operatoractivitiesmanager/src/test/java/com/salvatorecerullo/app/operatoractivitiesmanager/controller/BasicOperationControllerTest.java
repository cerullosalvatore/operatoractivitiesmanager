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

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.BasicOperationView;

public class BasicOperationControllerTest {
	@Mock
	private BasicOperationRepository basicOperationRepository;

	@Mock
	private BasicOperationView basicOperationView;

	@InjectMocks
	private BasicOperationController basicOperationController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAllBasicOperations() {
		// Setup
		List<BasicOperation> basicOperations = new ArrayList<BasicOperation>();
		basicOperations.add(new BasicOperation(0, "NameOperationTest", "DescriptionTest"));
		when(basicOperationRepository.findAll()).thenReturn(basicOperations);
		// Excercise
		basicOperationController.allBasicOperations();
		// Verify
		verify(basicOperationView).showAllBasicOperations(basicOperations);
	}

	@Test
	public void addBasicOperationSuccessfull() {
		// Setup
		BasicOperation newBasicOperation = new BasicOperation(0, "NameOperationTest", "DescriptionTest");
		when(basicOperationRepository.findById(0)).thenReturn(null);
		//Exercise
		basicOperationController.addBasicOperation(newBasicOperation);
		//Verify
		InOrder inOrder = Mockito.inOrder(basicOperationRepository, basicOperationView);
		inOrder.verify(basicOperationRepository).save(newBasicOperation);
		inOrder.verify(basicOperationView).basicOperationAdded();
		inOrder.verifyNoMoreInteractions();		
	}
	
	@Test
	public void addBasicOperationException() {
		//Setup
		BasicOperation newBasicOperation = new BasicOperation(0, "NameOperationTest", "DescriptionTest");
		BasicOperation oldBasicOperation = new BasicOperation(0, "NameOperationTestOld", "DescriptionTestOld");
		when(basicOperationRepository.findById(0)).thenReturn(oldBasicOperation);
		//Exercise
		basicOperationController.addBasicOperation(newBasicOperation);
		//Verify
		verify(basicOperationView).showError("The BasicOperation with ID: 0 already exist.");
	}
	
	

}
