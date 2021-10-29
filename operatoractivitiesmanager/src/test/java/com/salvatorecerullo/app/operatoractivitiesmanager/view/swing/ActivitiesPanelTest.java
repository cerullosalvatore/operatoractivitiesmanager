package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import java.util.Calendar;
import java.util.Date;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.Activity;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public class ActivitiesPanelTest extends AssertJSwingJUnitTestCase {

	@InjectMocks
	private OperatorActivitiesManagerView operatorActivitiesManagerView;

	private FrameFixture frameFixture;

	@Override
	protected void onSetUp() throws Exception {
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
			return operatorActivitiesManagerView;
		});

		MockitoAnnotations.initMocks(this);
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		frameFixture = new FrameFixture(robot(), operatorActivitiesManagerView);
		frameFixture.show(); // shows the frame to test
	}

	@Test
	@GUITest
	public void testActivitiesControlsArePrestentAndInitialStates() {
		// Verify
		frameFixture.panel("newActivityPanel").label("labelNewActivity").requireVisible();
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
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
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonInitialStatesEnabledWhenAllInputIsCompiled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise and Verify
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("TestInput1");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("TestInput2");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("TestInput3");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("TestInput4");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireEnabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonInitialStatesDisabledWhenAllInputAreNotCompiled() {
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
	public void testActivitiesAddButtonInitialStatesDisabledWhenOneInputTextIsNotCompiled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("TestInput2");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("TestInput3");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("TestInput4");
		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonInitialStatesDisabledWhenOperatorIsNotSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperationsModel()
					.addElement(new BasicOperation("OperationId", "Name Operation", "Description Operation"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("Operator2");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("TestInput2");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("TestInput3");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("TestInput4");
		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testActivitiesAddButtonInitialStatesDisabledWhenBasicOperationIsNotSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
		});

		// Exercise
		formActivityPanel.textBox("textFieldStartDataActivity").enterText("");
		formActivityPanel.textBox("textFieldStartHourActivity").enterText("TestInput2");
		formActivityPanel.textBox("textFieldEndDataActivity").enterText("TestInput3");
		formActivityPanel.textBox("textFieldEndHourActivity").enterText("TestInput4");

		// Verify
		JPanelFixture buttonsFormActivityPanel = frameFixture.panel("newActivityPanel")
				.panel("buttonsFormActivityPanel");
		buttonsFormActivityPanel.button("btnAddActivity").requireDisabled();
	}

	@Test
	@GUITest
	public void testFindByOperatorButtonShouldBeEnabledOnlyWhenOperatorIsSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperatorsModel()
					.addElement(new Operator("MatricolaTest", "Name Test", "Surname Test"));
		});

		formActivityPanel.comboBox("comboBoxOperatorActivity").selectItem(0);

		formTopMenuPanel.button("btnFindByOperator").requireEnabled();
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperatorsModel().removeAllElements();
		});
		formTopMenuPanel.button("btnFindByOperator").requireDisabled();
	}
	
	@Test
	@GUITest
	public void testFindByBasicOperationButtonShouldBeEnabledOnlyWhenBasicOperationIsSelected() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperationsModel()
					.addElement(new BasicOperation("IdBasicOperationTest", "Name Operation Test", "Basic Operation Description  Test"));
		});

		formActivityPanel.comboBox("comboBoxBasicOperationActivity").selectItem(0);

		formTopMenuPanel.button("btnFindByBasicOperation").requireEnabled();
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getActivitiesPanel().getComboBoxOperationsModel().removeAllElements();
		});
		formTopMenuPanel.button("btnFindByBasicOperation").requireDisabled();
	}

	@Test
	@GUITest
	public void testFindByDateButtonShouldBeEnabledOnlyWhenStartDataIsCompiled() {
		// Setup
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		JPanelFixture formTopMenuPanel = frameFixture.panel("listActivitiesPanel").panel("listTopMenuPanel");

		formActivityPanel.textBox("textFieldStartDataActivity").enterText("InputDataTest");
		
		// Verify
		formTopMenuPanel.button("btnFindByData").requireEnabled();
		
		//Setup
		formActivityPanel.textBox("textFieldStartDataActivity").deleteText();

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
			operatorActivitiesManagerView.getActivitiesPanel().getListActivitiesModel()
					.addElement(new Activity("ActivityId", "OperatorMatricola", "OperationId", startTime, endTime));
		});

		listActivitiesPanel.list("listActivities").selectItem(0);

		listBottomMenuPanel.button("btnDeleteActivity").requireEnabled();
		listBottomMenuPanel.button("btnModifyActivity").requireEnabled();

		listActivitiesPanel.list("listActivities").clearSelection();
		
		listBottomMenuPanel.button("btnDeleteActivity").requireDisabled();
		listBottomMenuPanel.button("btnModifyActivity").requireDisabled();
	}
	
	
}
