package com.salvatorecerullo.app.operatoractivitiesmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.ActivityRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.BasicOperationRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.repository.OperatorRepository;
import com.salvatorecerullo.app.operatoractivitiesmanager.view.ActivityView;

public class ActivityTest {
	@Mock
	private ActivityRepository activityRepository;

	@Mock
	private ActivityView activityView;

	@Mock
	private OperatorRepository operatorRepository;

	@Mock
	private BasicOperationRepository basicOperationRepository;

	@InjectMocks
	private ActivityController activityController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAllActivities() {
		// Setup
		List<Activity> activities = new ArrayList<Activity>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		activities.add(new Activity(0, "matricolaTest", 0, startTime, endTime));
		when(activityRepository.findAll()).thenReturn(activities);

		// Exercise
		activityController.allActivities();

		// Verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testAddActivitySuccessfull() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(operation.getId())).thenReturn(operation);
		// Exercise
		activityController.addActivity(newActivity);
		// verify
		InOrder inOrder = Mockito.inOrder(activityRepository, activityView);
		inOrder.verify(activityRepository).save(newActivity);
		inOrder.verify(activityView).activityAdded();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testAddActivityExceptionId() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		Activity oldActivity = new Activity(0, "OldOperatorTestMatricola", 2, startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(oldActivity);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError("The Activity Id: " + newActivity.getId() + " already exist.");
	}

	@Test
	public void testAddActivityExceptionOperator() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(null);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError("The Operator Matricola: " + operator.getMatricola() + " does not exist.");
	}

	@Test
	public void testAddActivityExceptionOperation() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(operation.getId())).thenReturn(null);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError("The BasicOperation Id: " + operation.getId() + " does not exist.");
	}

	@Test
	public void testAddActivityExceptionStartTimeAfterStopTime() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(operation.getId())).thenReturn(operation);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError(
				"The Start Date: " + newActivity.getStartTime() + "follow the End Date: " + newActivity.getEndTime());
	}

	@Test
	public void testRemoveActivitySuccessfull() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity oldActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(oldActivity.getId())).thenReturn(oldActivity);
		// Exercise
		activityController.removeActivity(oldActivity);
		// verify
		InOrder inOrder = Mockito.inOrder(activityRepository, activityView);
		inOrder.verify(activityRepository).delete(oldActivity.getId());
		inOrder.verify(activityView)
				.showActivityRemoved("The activity with Id: " + oldActivity.getId() + " has been removed");
	}

	@Test
	public void testRemoveActivityException() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity newActivity = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		// Exercise
		activityController.removeActivity(newActivity);
		// verify
		verify(activityView).showError("The activity with Id: " + newActivity.getId() + " not exist.");
	}

	@Test
	public void testSearchActivityByOperator() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity activity1 = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		Activity activity2 = new Activity(1, operator.getMatricola(), operation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByOperator(operator.getMatricola())).thenReturn(activities);
		// Exercise
		activityController.findByOperator(operator.getMatricola());
		// verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testSearchActivityByBasicOperation() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity activity1 = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		Activity activity2 = new Activity(1, operator.getMatricola(), operation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByBasicOperation(operation.getId())).thenReturn(activities);
		// Exercise
		activityController.findByBasicOperation(operation.getId());
		// verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testSearchActivityByStartDaySuccesfull() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation operation = new BasicOperation(0, "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 00);
		Date startTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 00);
		Date endTime = cal.getTime();
		Activity activity1 = new Activity(0, operator.getMatricola(), operation.getId(), startTime, endTime);
		Activity activity2 = new Activity(1, operator.getMatricola(), operation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByDay(activity1.getStartTime())).thenReturn(activities);
		// Exercise
		activityController.findByDay(activity1.getStartTime());
		// verify
		verify(activityView).showActivities(activities);
	}

}
