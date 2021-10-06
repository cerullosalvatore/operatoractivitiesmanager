package com.salvatorecerullo.app.operatoractivitiesmanager.controller;


import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
	
	@Test
	public void testAllOperators(){
		List<Operator> operators = Arrays.asList(new Operator());
		when(operatorRepository.findAll()).thenReturn(operators);
	}
}
