package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

@RunWith(GUITestRunner.class)
public class ActivitiesPanelTest extends AssertJSwingJUnitTestCase {
	private JFrame jFrame;

	private ActivitiesPanel activitiesPanel;

	@Mock
	private ActivityController activityController;

	private FrameFixture frameFixture;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			activitiesPanel = new ActivitiesPanel();
			activitiesPanel.setActivityController(activityController);
			jFrame.add(activitiesPanel);
			return jFrame;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		frameFixture = new FrameFixture(robot, jFrame);
		frameFixture.show(); // shows the frame to test
	}

	@Test
	@GUITest
	public void testActivitiesControlsArePresentAndInitialStates() {
		// Verify
		frameFixture.panel("newActivityPanel").label("labelNewActivity").requireVisible();
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.label("labelId").requireVisible();
		formActivityPanel.textBox("textFieldIdActivity").requireVisible().requireNotEditable()
				.requireText(activitiesPanel.getActivityIdTemp());
		formActivityPanel.label("labelOperatorActivity").requireVisible();
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireVisible().requireEnabled().requireNoSelection();
		formActivityPanel.label("labelBasicOperationActivity").requireVisible();
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireVisible().requireEnabled()
				.requireNoSelection();
		formActivityPanel.label("labelStartDataActivity").requireVisible();
		formActivityPanel.textBox("textFieldStartDataActivity").requireVisible().requireEnabled().requireEmpty();
		formActivityPanel.label("labelStartHourActivity").requireVisible();
		formActivityPanel.textBox("textFieldStartHourActivity").requireVisible().requireEnabled().requireEmpty();
		formActivityPanel.label("labelEndDataActivity").requireVisible();
		formActivityPanel.textBox("textFieldEndDataActivity").requireVisible().requireEnabled().requireEmpty();
		formActivityPanel.label("labelEndHourActivity").requireVisible();
		formActivityPanel.textBox("textFieldEndHourActivity").requireVisible().requireEnabled().requireEmpty();
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireVisible().requireDisabled();
		buttonsFormActivityPanel.button("btnUpdateActivity").requireVisible().requireDisabled();
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.list("listActivities").requireVisible().requireEnabled().requireNoSelection();
		JPanelFixture listTopMenuPanel = frameFixture.panel("listTopMenuPanel");
		listTopMenuPanel.button("btnShowAll").requireVisible().requireEnabled();
		listTopMenuPanel.button("btnFindByOperator").requireVisible().requireDisabled();
		listTopMenuPanel.button("btnFindByBasicOperation").requireVisible().requireDisabled();
		listTopMenuPanel.button("btnFindByData").requireVisible().requireDisabled();
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity").requireVisible().requireDisabled();
		listBottomMenuPanel.button("btnDeleteActivity").requireDisabled().requireDisabled();
		listActivitiesPanel.label("lblMessageStatus").requireVisible().requireText("");
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonEnabledOnlyWhenAllInputIsCompiled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireEnabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonEnabledOnlyWhenDateIsValid() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireEnabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("");
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("");
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartHourActivity").setText("");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldEndDataActivity").setText("");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("errorInput");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonDisabledWhenAllInputAreEmpty() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("");

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonDisabledWhenOneInputTextIsNotCompiled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonDisabledWhenOneComboBoxIsNotSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel().removeAllElements();
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();

		// Setup
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().removeAllElements();
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testFindByOperatorButtonShouldBeEnabledOnlyWhenOperatorIsSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
		});

		// Exercise
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);

		// Verify
		formTopMenuPanel.button("btnFindByOperator").requireEnabled();

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().removeAllElements();
		});

		// Verify
		formTopMenuPanel.button("btnFindByOperator").requireDisabled();
	}

	@Test
	@GUITest
	public void testFindByBasicOperationButtonShouldBeEnabledOnlyWhenBasicOperationIsSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperationsModel().addElement(new BasicOperation("IdBasicOperationTest",
					"Name Operation Test", "Basic Operation Description  Test"));
		});

		// Exercise
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		formTopMenuPanel.button("btnFindByBasicOperation").requireEnabled();

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperationsModel().removeAllElements();
		});

		// Verify
		formTopMenuPanel.button("btnFindByBasicOperation").requireDisabled();
	}

	@Test
	@GUITest
	public void testFindByDateButtonShouldBeEnabledOnlyWhenStartDataIsValid() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		// Execute
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");

		// Verify
		formTopMenuPanel.button("btnFindByData").requireEnabled();

		// Execute
		formActivityPanel.textBox("textFieldStartDataActivity").setText("");
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("errorInput");

		// Verify
		formTopMenuPanel.button("btnFindByData").requireDisabled();
	}

	@Test
	@GUITest
	public void testDeleteButtonAndModifyShouldBeEnabledOnlyWhenAnActivityIsSelected() {
		// Setup
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		GuiActionRunner.execute(() -> {
			activitiesPanel.getListActivitiesModel()
					.addElement(new Activity("ActivityId", "OperatorMatricola", "OperationId", startTime, endTime));
		});

		// Exercise
		listActivitiesPanel.list("listActivities").selectItem(0);

		// Verify
		listBottomMenuPanel.button("btnDeleteActivity").requireEnabled();
		listBottomMenuPanel.button("btnModifyActivity").requireEnabled();

		// Exercise
		listActivitiesPanel.list("listActivities").clearSelection();

		// Verify
		listBottomMenuPanel.button("btnDeleteActivity").requireDisabled();
		listBottomMenuPanel.button("btnModifyActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonWasPressedAndFieldWasCompiledCorrectly() throws ParseException {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		Operator operator = new Operator("MatricolaTest", "Name Test", "Surname Test");
		BasicOperation basicOperation = new BasicOperation("OperationId", "Name Operation", "Description Operation");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation);
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("02");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("02/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("09:").enterText("00");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		formActivityPanel.textBox("textFieldStartDataActivity").requireText("");
		formActivityPanel.textBox("textFieldStartHourActivity").requireText("");
		formActivityPanel.textBox("textFieldEndDataActivity").requireText("");
		formActivityPanel.textBox("textFieldEndHourActivity").requireText("");
		formActivityPanel.textBox("textFieldIdActivity").requireText(activitiesPanel.getActivityIdTemp());
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireSelection(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireSelection(0);
		formTopMenuPanel.button("btnFindByData").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedListDisabledAndUpdateButtonIsEnabled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
			activitiesPanel.getListActivitiesModel()
					.addElement(new Activity("ActivityId", "MatricolaTest", "OperationId", startTime, endTime));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnModifyActivity").click();

		// Verify
		listActivitiesPanel.list("listActivities").requireDisabled();
		buttonsFormActivityPanel.button("btnUpdateActivity").requireEnabled();
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
		formTopMenuPanel.button("btnFindByData").requireEnabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").doubleClick().deleteText();
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").doubleClick().deleteText();

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedFieldWasCompiledCorrectly() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");

		// Setting 2 different activity
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 00, 00, 00);
		Date startTime1 = cal.getTime();
		cal.set(2021, 1, 1, 23, 59, 00);
		Date endTime1 = cal.getTime();

		cal.set(2022, 2, 2, 10, 00, 00);
		Date startTime2 = cal.getTime();
		cal.set(2022, 2, 2, 10, 39, 00);
		Date endTime2 = cal.getTime();

		Operator operator1 = new Operator("MatricolaTest1", "Name Test1", "Surname Test1");
		Operator operator2 = new Operator("MatricolaTest2", "Name Test2", "Surname Test2");
		BasicOperation basicOperation1 = new BasicOperation("OperationId1", "Name Operation1",
				"Description Operation1");
		BasicOperation basicOperation2 = new BasicOperation("OperationId2", "Name Operation2",
				"Description Operation2");
		Activity activity1 = new Activity("ActivityId1", "MatricolaTest1", "OperationId1", startTime1, endTime1);
		Activity activity2 = new Activity("ActivityId2", "MatricolaTest2", "OperationId2", startTime2, endTime2);

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator1);
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator2);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation1);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation2);
			activitiesPanel.getListActivitiesModel().addElement(activity1);
			activitiesPanel.getListActivitiesModel().addElement(activity2);
		});

		// Exercise
		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnModifyActivity").click();

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime1);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime1);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime1);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime1);

		// Verify
		formActivityPanel.textBox("textFieldIdActivity").requireText(activity1.getId());
		formActivityPanel.textBox("textFieldStartDataActivity").requireText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").requireText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").requireText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").requireText(formattedEndHour);
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireSelection(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireSelection(0);

		// Exercise
		buttonsFormActivityPanel.button("btnUpdateActivity").click();
		listActivitiesPanel.list("listActivities").selectItem(1);
		listBottomMenuPanel.button("btnModifyActivity").click();

		formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime2);
		formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime2);
		formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime2);
		formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime2);

		// Verify
		formActivityPanel.textBox("textFieldIdActivity").requireText(activity2.getId());
		formActivityPanel.textBox("textFieldStartDataActivity").requireText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").requireText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").requireText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").requireText(formattedEndHour);
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireSelection(1);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireSelection(1);

		// Setup
		GuiActionRunner.execute(() -> {
			activitiesPanel.getListActivitiesModel().addElement(new Activity("ActivityIdTesting",
					"MatricolaTestNotPresent", "OperationIdNotPresent", startTime2, endTime2));
		});

		// Exercise
		buttonsFormActivityPanel.button("btnUpdateActivity").click();
		listActivitiesPanel.list("listActivities").selectItem(2);
		listBottomMenuPanel.button("btnModifyActivity").click();

		// Verify
		formActivityPanel.textBox("textFieldStartDataActivity").requireText(formattedStartDate);
		formActivityPanel.textBox("textFieldStartHourActivity").requireText(formattedStartHour);
		formActivityPanel.textBox("textFieldEndDataActivity").requireText(formattedEndDate);
		formActivityPanel.textBox("textFieldEndHourActivity").requireText(formattedEndHour);
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireSelection(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireSelection(0);
	}

	@Test
	@GUITest
	public void testUpdateActivityIsPressedListEnabledAndInputReset() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
			activitiesPanel.getListActivitiesModel()
					.addElement(new Activity("ActivityId", "MatricolaTest", "OperationId", startTime, endTime));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnModifyActivity").click();

		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		listActivitiesPanel.list("listActivities").isEnabled();
		formActivityPanel.textBox("textFieldIdActivity").requireText(activitiesPanel.getActivityIdTemp());
		formActivityPanel.comboBox("comboBoxOperatorActivity").requireSelection(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").requireSelection(0);
		formActivityPanel.textBox("textFieldStartDataActivity").requireEmpty();
		formActivityPanel.textBox("textFieldStartHourActivity").requireEmpty();
		formActivityPanel.textBox("textFieldEndDataActivity").requireEmpty();
		formActivityPanel.textBox("textFieldEndHourActivity").requireEmpty();
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testUpdateActivityButtonEnabledWhenDateIsValid() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
			activitiesPanel.getListActivitiesModel()
					.addElement(new Activity("ActivityId", "MatricolaTest", "OperationId", startTime, endTime));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("10:").enterText("10");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnModifyActivity").click();

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireEnabled();

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("");
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise and Verify
		formActivityPanel.textBox("textFieldStartDataActivity").setText("");
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise and Verify
		formActivityPanel.textBox("textFieldStartHourActivity").setText("");
		formActivityPanel.textBox("textFieldStartHourActivity").setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("errorInput");

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();

		// Exercise and Verify
		formActivityPanel.textBox("textFieldEndDataActivity").setText("");
		formActivityPanel.textBox("textFieldEndDataActivity").setText("01/01/").enterText("2001");
		formActivityPanel.textBox("textFieldEndHourActivity").setText("");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("errorInput");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		buttonsFormActivityPanel.button("btnUpdateActivity").requireDisabled();
	}

	// Tests for the interaction between our buttons and the controllers:
	@Test
	@GUITest
	public void testAddButtonShouldDelegateToActivityControllerNewActivity() throws ParseException {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 00, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		Operator operator = new Operator("MatricolaTest", "Name Test", "Surname Test");
		BasicOperation basicOperation = new BasicOperation("OperationId", "Name Operation", "Description Operation");
		String activityIdTemp = activitiesPanel.getActivityIdTemp();
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator);
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation);
		});

		String formattedStartDate = new SimpleDateFormat("dd/MM/yyyy").format(startTime);
		String formattedStartHour = new SimpleDateFormat("HH:mm").format(startTime);
		String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(endTime);
		String formattedEndHour = new SimpleDateFormat("HH:mm").format(endTime);

		Date parsedStartDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.parse(formattedStartDate + " " + formattedStartHour + ":00");

		Date parsedEndDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.parse(formattedEndDate + " " + formattedEndHour + ":00");

		Activity activityNew = new Activity(activityIdTemp, operator.getMatricola(), basicOperation.getId(),
				parsedStartDate, parsedEndDate);

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText(formattedStartDate.substring(0, 6)).enterText(formattedStartDate.substring(6, 10));
		formActivityPanel.textBox("textFieldStartHourActivity").setText(formattedStartHour.substring(0, 3)).enterText(formattedStartHour.substring(3, 5));
		formActivityPanel.textBox("textFieldEndDataActivity").setText(formattedEndDate.substring(0, 6)).enterText(formattedEndDate.substring(6, 10));
		formActivityPanel.textBox("textFieldEndHourActivity").setText(formattedEndHour.substring(0, 3)).enterText(formattedEndHour.substring(3, 5));
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		buttonsFormActivityPanel.button("btnAddActivity").click();

		// Verify
		verify(activityController).addActivity(activityNew);
	}

	@Test
	@GUITest
	public void testDeleteButtonShouldDelegateToActivityControllerRemoveActivity() throws ParseException {
		// Setup
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");

		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 00, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		Activity activity1 = new Activity("ActivityId1", "operator1", "operation1", startTime, endTime);
		Activity activity2 = new Activity("ActivityId2", "operator2", "operation2", startTime, endTime);

		GuiActionRunner.execute(() -> {
			activitiesPanel.getListActivitiesModel().addElement(activity1);
			activitiesPanel.getListActivitiesModel().addElement(activity2);
		});

		// Exercise
		listActivitiesPanel.list("listActivities").selectItem(1);
		listBottomMenuPanel.button("btnDeleteActivity").click();

		// Verify
		verify(activityController).removeActivity(activity2);

		// Exercise
		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnDeleteActivity").click();

		// Verify
		verify(activityController).removeActivity(activity1);
	}

	@Test
	@GUITest
	public void testShowAllButtonShouldDelegateToActivityControllers() {
		// Setup
		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		// Exercise
		listTopMenuPanel.button("btnShowAll").click();

		// Verify
		verify(activityController).allActivities();
		verify(activityController).allOperators();
		verify(activityController).allBasicOperation();
	}

	@Test
	@GUITest
	public void testFindByOperatorButtonShouldDelegateToActivityControllerFindByOperatorActivities() {
		// Setup
		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		Operator operator1 = new Operator("IdOperator", "Name", "Surname");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel().addElement(operator1);
		});

		// Exercise
		listTopMenuPanel.button("btnFindByOperator").click();

		// Verify
		verify(activityController).findByOperator("IdOperator");

	}

	@Test
	@GUITest
	public void testFindByBasicOperationButtonShouldDelegateToActivityControllerFindByBasicOperationActivities() {
		// Setup
		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");
		BasicOperation basicOperation = new BasicOperation("IdOperation", "Name", "Description");

		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperationsModel().addElement(basicOperation);
		});

		// Exercise
		listTopMenuPanel.button("btnFindByBasicOperation").click();

		// Verify
		verify(activityController).findByBasicOperation(basicOperation.getId());

	}

	@Test
	@GUITest
	public void testFindByDateButtonShouldDelegateToActivityControllerFindByDayActivities() throws ParseException {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture listTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").setText("01/01/").enterText("2021");
		Date startData = new SimpleDateFormat("dd/MM/yyyy")
				.parse(formActivityPanel.textBox("textFieldStartDataActivity").text());
		listTopMenuPanel.button("btnFindByData").click();

		// Verify
		verify(activityController).findByDay(startData);

	}

	@Test
	@GUITest
	public void testUpdateButtonShouldDelegateToActivityControllerNewActivity() throws ParseException {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listBottomMenuPanel");
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();
		GuiActionRunner.execute(() -> {
			activitiesPanel.getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			activitiesPanel.getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
			activitiesPanel.getListActivitiesModel()
					.addElement(new Activity("ActivityId", "MatricolaTest", "OperationId", startTime, endTime));
		});

		// Exercise
		listActivitiesPanel.list("listActivities").selectItem(0);
		listBottomMenuPanel.button("btnModifyActivity").click();
		formActivityPanel.textBox("textFieldStartDataActivity").doubleClick().deleteText().setText("02/02/").enterText("2002");
		formActivityPanel.textBox("textFieldStartHourActivity").doubleClick().deleteText().setText("00:").enterText("00");
		formActivityPanel.textBox("textFieldEndDataActivity").doubleClick().deleteText().setText("02/02/").enterText("2002");
		formActivityPanel.textBox("textFieldEndHourActivity").doubleClick().deleteText().setText("10:").enterText("00");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);
		buttonsFormActivityPanel.button("btnUpdateActivity").click();

		// Verify
		Date parsedStartDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("02/02/2002 00:00:00");
		Date parsedEndDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("02/02/2002 10:00:00");
		Activity activityUpdated = new Activity("ActivityId", "MatricolaTest", "OperationId", parsedStartDate,
				parsedEndDate);

		verify(activityController).updadeActivity(activityUpdated);
	}

	// TEST INTERFACE METHODS IMPLEMENTED
	@Test
	@GUITest
	public void testShowActivitiesShouldAddActivitiesToTheList() {
		// Setup
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		Calendar cal = Calendar.getInstance();
		cal.set(2021, 1, 1, 8, 0, 00);
		Date startTime = cal.getTime();
		cal.set(2021, 1, 1, 16, 00, 00);
		Date endTime = cal.getTime();

		Activity activity1 = new Activity("ActivityId1", "MatricolaTest1", "OperationId1", startTime, endTime);
		Activity activity2 = new Activity("ActivityId2", "MatricolaTest2", "OperationId2", startTime, endTime);

		List<Activity> activities = new ArrayList<Activity>();

		activities.add(activity1);
		activities.add(activity2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.showActivities(activities);
		});

		// Very
		String[] listContents = listActivitiesPanel.list("listActivities").contents();
		assertThat(listContents).containsExactly(activity1.toString(), activity2.toString());
		listActivitiesPanel.label("lblMessageStatus").requireText("");
	}

	@Test
	@GUITest
	public void testShowSuccessfullShouldAddAllActivitiesToTheListAndShowSuccessMessage() {
		// Setup
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.list("listActivities").requireVisible().requireEnabled().requireNoSelection();

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.showSuccessfull("Successfull Message.");
		});

		// Verify
		verify(activityController, atLeast(1)).allActivities();
		verify(activityController, atMost(2)).allActivities();
		listActivitiesPanel.label("lblMessageStatus").requireText("Successfull Message.");
	}

	@Test
	@GUITest
	public void testShowErrorShouldAddActivitiesToTheListAndShowErroMessage() {
		// Setup
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.showError("Error Message.");
		});

		// Verify
		verify(activityController, atLeast(1)).allActivities();
		verify(activityController, atMost(2)).allActivities();
		listActivitiesPanel.label("lblMessageStatus").requireText("Error Message.");
	}

	@Test
	@GUITest
	public void testShowOperatorsShouldUpdateOperatorsComboBox() {
		// Setup
		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		List<Operator> operators = new ArrayList<Operator>();

		operators.add(operator1);
		operators.add(operator2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.showOperators(operators);
		});

		// Verify
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		String[] operatorsComboContents = formActivityPanel.comboBox("comboBoxOperatorActivity").contents();
		assertThat(operatorsComboContents).containsExactly(operator1.toString(), operator2.toString());
	}

	@Test
	@GUITest
	public void testShowOperatorsShouldUpdateBasicOperationsComboBox() {
		// Setup
		BasicOperation basicOperation1 = new BasicOperation("IdTest1", "NameTest1", "DescriptionTest1");
		BasicOperation basicOperation2 = new BasicOperation("IdTest2", "NameTest2", "DescriptionTest2");

		List<BasicOperation> operations = new ArrayList<BasicOperation>();

		operations.add(basicOperation1);
		operations.add(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			activitiesPanel.showBasicOperation(operations);
		});

		// Verify
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		String[] basicoOperationComboContents = formActivityPanel.comboBox("comboBoxBasicOperationActivity").contents();
		assertThat(basicoOperationComboContents).containsExactly(basicOperation1.toString(),
				basicOperation2.toString());
	}
}