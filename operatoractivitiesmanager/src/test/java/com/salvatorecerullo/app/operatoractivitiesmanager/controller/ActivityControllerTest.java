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

public class ActivityControllerTest {
	private static final String THEOPERATOR = "The Operator: ";
	private static final String BASICOPERATION = "The BasicOperation with ID: ";
	private static final String THEACTIVITY = "The Activity: ";
	private static final String NOTEXIST = " does not exist.";

	private Date startTime;
	private Date endTime;

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
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		endTime = cal.getTime();
	}

	@Test
	public void testAllActivities() {
		// Setup
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("0", "matricolaTest1", "0", startTime, endTime));
		activities.add(new Activity("1", "matricolaTest2", "1", startTime, endTime));
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
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(basicOperation.getId())).thenReturn(basicOperation);
		// Exercise
		activityController.addActivity(newActivity);
		// verify
		InOrder inOrder = Mockito.inOrder(activityRepository, activityView);
		inOrder.verify(activityRepository).save(newActivity);
		inOrder.verify(activityView)
				.showSuccessfull(THEACTIVITY + newActivity.getId() + " has been added.");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testAddActivityErrorId() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity oldActivity = new Activity("0", "OldOperatorTestMatricola", "2", startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(oldActivity);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError(THEACTIVITY + newActivity.getId() + " already exist.");
	}

	@Test
	public void testAddActivityErrorOperator() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(null);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError(THEOPERATOR + operator.getMatricola() + NOTEXIST);
	}

	@Test
	public void testAddActivityErrorBasicOperation() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(basicOperation.getId())).thenReturn(null);
		// Exercise
		activityController.addActivity(newActivity);
		// Verify
		verify(activityView).showError(BASICOPERATION + basicOperation.getId() + NOTEXIST);
	}

	@Test
	public void testAddActivityErrorStartTimeAfterStopTime() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 16, 0, 00);
		startTime = cal.getTime();
		cal.set(2021, 1, 1, 8, 00, 00);
		endTime = cal.getTime();
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		when(operatorRepository.findByMatricola(operator.getMatricola())).thenReturn(operator);
		when(basicOperationRepository.findById(basicOperation.getId())).thenReturn(basicOperation);
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
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity oldActivity = new Activity("=", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(oldActivity.getId())).thenReturn(oldActivity);
		// Exercise
		activityController.removeActivity(oldActivity);
		// verify
		InOrder inOrder = Mockito.inOrder(activityRepository, activityView);
		inOrder.verify(activityRepository).delete(oldActivity.getId());
		inOrder.verify(activityView)
				.showSuccessfull(THEACTIVITY + oldActivity.getId() + " has been removed.");
	}

	@Test
	public void testRemoveActivityException() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity newActivity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		when(activityRepository.findById(newActivity.getId())).thenReturn(null);
		// Exercise
		activityController.removeActivity(newActivity);
		// verify
		verify(activityView).showError(THEACTIVITY + newActivity.getId() + NOTEXIST);
	}

	@Test
	public void testSearchActivityByOperator() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity activity1 = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity("1", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByOperatorMatricola(operator.getMatricola())).thenReturn(activities);
		// Exercise
		activityController.findByOperator(operator.getMatricola());
		// verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testSearchActivityByOperatorError() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity activity = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity);
		when(activityRepository.findByOperatorMatricola(operator.getMatricola())).thenReturn(null);
		// Exercise
		activityController.findByOperator(operator.getMatricola());
		// verify
		verify(activityView).showError(THEOPERATOR + operator.getMatricola() + NOTEXIST);
	}

	@Test
	public void testSearchActivityByBasicOperation() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity activity1 = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity("1", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByBasicOperationId(basicOperation.getId())).thenReturn(activities);
		// Exercise
		activityController.findByBasicOperation(basicOperation.getId());
		// verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testSearchActivityByBasicOperationError() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity activity1 = new Activity("1", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		when(activityRepository.findByBasicOperationId(basicOperation.getId())).thenReturn(null);
		// Exercise
		activityController.findByBasicOperation(basicOperation.getId());
		// verify
		verify(activityView).showError(BASICOPERATION + basicOperation.getId() + NOTEXIST);
	}

	@Test
	public void testSearchActivityByStartDaySuccesfull() {
		// Setup
		Operator operator = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperation = new BasicOperation("0", "testName", "testDescription");
		Activity activity1 = new Activity("1", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		Activity activity2 = new Activity("0", operator.getMatricola(), basicOperation.getId(), startTime, endTime);
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(activity1);
		activities.add(activity2);
		when(activityRepository.findByDay(activity1.getStartTime())).thenReturn(activities);
		// Exercise
		activityController.findByDay(activity1.getStartTime());
		// verify
		verify(activityView).showActivities(activities);
	}

	@Test
	public void testUpdateActivitySuccessfull() {
		// Setup
		Operator operatorOld = new Operator("testMatricolaOld", "testNameOld", "testSurnameOld");
		BasicOperation basicOperationOld = new BasicOperation("0", "testNameOld", "testDescriptionOld");
		Activity activityOld = new Activity("0", operatorOld.getMatricola(), basicOperationOld.getId(), startTime,
				endTime);
		Operator operatorUpdated = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperationUpdated = new BasicOperation("0", "testName", "testDescription");
		Activity activityUpdated = new Activity("0", operatorUpdated.getMatricola(), basicOperationUpdated.getId(),
				startTime, endTime);
		when(activityRepository.findById(activityUpdated.getId())).thenReturn(activityOld);
		when(operatorRepository.findByMatricola(operatorUpdated.getMatricola())).thenReturn(operatorOld);
		when(basicOperationRepository.findById(basicOperationUpdated.getId())).thenReturn(basicOperationOld);
		// Exercise
		activityController.updadeActivity(activityUpdated);
		// verify
		InOrder inOrder = Mockito.inOrder(activityRepository, activityView);
		inOrder.verify(activityRepository).update(activityUpdated);
		inOrder.verify(activityView).showSuccessfull(THEACTIVITY + activityUpdated.getId() + " has been updated.");
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testUpdateActivityErrorId() {
		// Setup
		Operator operatorUpdated = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperationUpdated = new BasicOperation("0", "testName", "testDescription");
		Activity activityUpdated = new Activity("0", operatorUpdated.getMatricola(), basicOperationUpdated.getId(),
				startTime, endTime);
		when(activityRepository.findById(activityUpdated.getId())).thenReturn(null);
		// Exercise
		activityController.updadeActivity(activityUpdated);
		// verify
		verify(activityView).showError(THEACTIVITY + activityUpdated.getId() + NOTEXIST);
	}

	@Test
	public void testUpdateActivityErrorOperator() {
		// Setup
		Operator operatorOld = new Operator("testMatricolaOld", "testNameOld", "testSurnameOld");
		BasicOperation basicOperationOld = new BasicOperation("0", "testNameOld", "testDescriptionOld");
		Activity activityOld = new Activity("0", operatorOld.getMatricola(), basicOperationOld.getId(), startTime,
				endTime);
		Operator operatorUpdated = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperationUpdated = new BasicOperation("0", "testName", "testDescription");
		Activity activityUpdated = new Activity("0", operatorUpdated.getMatricola(), basicOperationUpdated.getId(),
				startTime, endTime);
		when(activityRepository.findById(activityUpdated.getId())).thenReturn(activityOld);
		when(operatorRepository.findByMatricola(operatorUpdated.getMatricola())).thenReturn(null);
		// Exercise
		activityController.updadeActivity(activityUpdated);
		// verify
		verify(activityView).showError(THEOPERATOR + activityUpdated.getOperatorMatricola() + NOTEXIST);
	}

	@Test
	public void testUpdateActivityErrorBasicOperation() {
		// Setup
		Operator operatorOld = new Operator("testMatricolaOld", "testNameOld", "testSurnameOld");
		BasicOperation basicOperationOld = new BasicOperation("0", "testNameOld", "testDescriptionOld");
		Activity activityOld = new Activity("0", operatorOld.getMatricola(), basicOperationOld.getId(), startTime,
				endTime);
		Operator operatorUpdated = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperationUpdated = new BasicOperation("0", "testName", "testDescription");
		Activity activityUpdated = new Activity("0", operatorUpdated.getMatricola(), basicOperationUpdated.getId(),
				startTime, endTime);
		when(activityRepository.findById(activityUpdated.getId())).thenReturn(activityOld);
		when(operatorRepository.findByMatricola(operatorUpdated.getMatricola())).thenReturn(operatorOld);
		when(basicOperationRepository.findById(basicOperationUpdated.getId())).thenReturn(null);
		// Exercise
		activityController.updadeActivity(activityUpdated);
		// verify
		verify(activityView).showError(BASICOPERATION + activityUpdated.getOperationId() + NOTEXIST);
	}

	@Test
	public void testUpdateActivityErrorStartTimeAfterStopTime() {
		// Setup
		Operator operatorOld = new Operator("testMatricolaOld", "testNameOld", "testSurnameOld");
		BasicOperation basicOperationOld = new BasicOperation("0", "testNameOld", "testDescriptionOld");
		Activity activityOld = new Activity("0", operatorOld.getMatricola(), basicOperationOld.getId(), startTime,
				endTime);
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 16, 0, 00);
		Date startTimeUpdated = cal.getTime();
		cal.set(2021, 1, 1, 8, 00, 00);
		Date endTimeUpdated = cal.getTime();
		Operator operatorUpdated = new Operator("testMatricola", "testName", "testSurname");
		BasicOperation basicOperationUpdated = new BasicOperation("0", "testName", "testDescription");
		Activity activityUpdated = new Activity("0", operatorUpdated.getMatricola(), basicOperationUpdated.getId(),
				startTimeUpdated, endTimeUpdated);
		when(activityRepository.findById(activityUpdated.getId())).thenReturn(activityOld);
		when(operatorRepository.findByMatricola(operatorUpdated.getMatricola())).thenReturn(operatorOld);
		when(basicOperationRepository.findById(basicOperationUpdated.getId())).thenReturn(basicOperationOld);
		// Exercise
		activityController.updadeActivity(activityUpdated);
		// verify
		verify(activityView).showError("The Start Date: " + activityUpdated.getStartTime() + "follow the End Date: "
				+ activityUpdated.getEndTime());
	}
}
