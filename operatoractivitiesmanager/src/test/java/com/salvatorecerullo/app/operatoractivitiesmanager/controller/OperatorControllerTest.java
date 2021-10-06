package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.OperatorView;

public class OperatorControllerTest {

	@Mock
	private OperatorRepository operatorRepository;
	
	@Mock
	private OperatorView operatorView;
	
	@InjectMocks
	private OperatorController operatorController;
	
}
