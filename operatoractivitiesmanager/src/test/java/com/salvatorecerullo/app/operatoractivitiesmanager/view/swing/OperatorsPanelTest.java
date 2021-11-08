package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public class OperatorsPanelTest extends AssertJSwingJUnitTestCase {
	private OperatorActivitiesManagerView operatorActivitiesManagerView;

	private FrameFixture frameFixture;

	@Mock
	private OperatorController operatorController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
			operatorActivitiesManagerView.getOperatorsPanel().setOperatorController(operatorController);
			return operatorActivitiesManagerView;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		frameFixture = new FrameFixture(robot(), operatorActivitiesManagerView);
		frameFixture.show(); // shows the frame to test
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.selectTab("Operators");
	}

	@Test
	@GUITest
	public void testOperatorControlsArePrestentAndInitialStates() {
		// Verify
		frameFixture.panel("newOperatorPanel").label("lblOperatorDetails").requireVisible();
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		formOperatorPanel.label("lblMatricola").requireVisible();
		formOperatorPanel.textBox("textFieldMatricola").requireVisible().requireEnabled().requireEmpty();
		formOperatorPanel.label("lblName").requireVisible();
		formOperatorPanel.textBox("textFieldName").requireVisible().requireEnabled().requireEmpty();
		formOperatorPanel.label("lblSurname").requireVisible();
		formOperatorPanel.textBox("textFieldSurname").requireVisible().requireEnabled().requireEmpty();
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");
		buttonsFormOperatorPanel.button("btnAddOperator").requireVisible().requireDisabled();
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireVisible().requireDisabled();
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").requireVisible().requireEnabled().requireNoSelection();
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnModifyOperator").requireVisible()
				.requireDisabled();
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnDeleteOperator").requireDisabled()
				.requireDisabled();
		listBottomMenuPanel.label("lblMessageStatus").requireVisible().requireText("");
	}

	@Test
	@GUITest
	public void testOperatorAddButtonEnabledOnlyWhenAllInputIsCompiled() {
		// Setup
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("TestMatricola");
		formOperatorPanel.textBox("textFieldName").enterText("TestName");
		formOperatorPanel.textBox("textFieldSurname").enterText("TestSurname");

		// Verify
		buttonsFormOperatorPanel.button("btnAddOperator").requireEnabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("TestMatricola");
		formOperatorPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldName").enterText("TestName");
		formOperatorPanel.textBox("textFieldSurname").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").deleteText();
		formOperatorPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();
	}

	@Test
	@GUITest
	public void testDeleteButtonAndModifyShouldBeEnabledOnlyWhenAnOperatorIsSelected() {
		// Setup
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator newOperator = new Operator("MatricolaTest", "NameTest", "SurnameTest");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().getListOperatorsModel().addElement(newOperator);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);

		// Verify
		listBottomMenuPanel.button("btnModifyOperator").requireEnabled();
		listBottomMenuPanel.button("btnDeleteOperator").requireEnabled();

		// Exercise
		listOperatorsPanel.list("listOperators").clearSelection();

		// Verify
		listBottomMenuPanel.button("btnModifyOperator").requireDisabled();
		listBottomMenuPanel.button("btnDeleteOperator").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedListDisabledAndUpdateButtonIsEnabled() {
		// Setup
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator newOperator = new Operator("MatricolaTest", "NameTest", "SurnameTest");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().getListOperatorsModel().addElement(newOperator);
		});

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("MatricolaTest");
		formOperatorPanel.textBox("textFieldName").enterText("NameTest");
		formOperatorPanel.textBox("textFieldSurname").enterText("SurnameTest");

		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		// Verify
		listOperatorsPanel.list("listOperators").requireDisabled();
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireEnabled();
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("TestMatricola");
		formOperatorPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldName").enterText("TestName");
		formOperatorPanel.textBox("textFieldSurname").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").deleteText();
		formOperatorPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();
	}

	@Test
	@GUITest
	public void testUpdateActivityIsPressedListEnabledAndInputReset() {
		// Setup
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator newOperator = new Operator("MatricolaTest", "NameTest", "SurnameTest");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().getListOperatorsModel().addElement(newOperator);
		});

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("MatricolaTest");
		formOperatorPanel.textBox("textFieldName").enterText("NameTest");
		formOperatorPanel.textBox("textFieldSurname").enterText("SurnameTest");

		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		buttonsFormOperatorPanel.button("btnUpdateOperator").click();

		// Verify
		listOperatorsPanel.list("listOperators").isEnabled();
		formOperatorPanel.textBox("textFieldMatricola").requireEmpty();
		formOperatorPanel.textBox("textFieldName").requireEmpty();
		formOperatorPanel.textBox("textFieldSurname").requireEmpty();

		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();
		buttonsFormOperatorPanel.button("btnAddOperator").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedFieldWasCompiledCorrectly() {
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().getListOperatorsModel().addElement(operator1);
			operatorActivitiesManagerView.getOperatorsPanel().getListOperatorsModel().addElement(operator2);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		// VerifyactivityController
		formOperatorPanel.textBox("textFieldMatricola").requireText(operator1.getMatricola());
		formOperatorPanel.textBox("textFieldName").requireText(operator1.getName());
		formOperatorPanel.textBox("textFieldSurname").requireText(operator1.getSurname());

		// Exercise
		buttonsFormOperatorPanel.button("btnUpdateOperator").click();
		listOperatorsPanel.list("listOperators").selectItem(1);
		listBottomMenuPanel.button("btnModifyOperator").click();

		// Verify
		formOperatorPanel.textBox("textFieldMatricola").requireText(operator2.getMatricola());
		formOperatorPanel.textBox("textFieldName").requireText(operator2.getName());
		formOperatorPanel.textBox("textFieldSurname").requireText(operator2.getSurname());
	}

	// TEST INTERFACE METHODS
	@Test
	@GUITest
	public void testShowAllOperatorsShouldAddOperatorsToTheList() {
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");

		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		List<Operator> operators = new ArrayList<Operator>();

		operators.add(operator1);
		operators.add(operator2);

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().showAllOperators(operators);
		});

		String[] listContents = listOperatorsPanel.list("listOperators").contents();
		assertThat(listContents).containsExactly(operator1.toString(), operator2.toString());
		listOperatorsPanel.label("lblMessageStatus").requireText("");
	}

	@Test
	@GUITest
	public void testShowSuccessfullShouldAddOperatorsToTheListAndShowSuccessMessage() {
		// SETUP
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").requireVisible().requireEnabled().requireNoSelection();

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().showSuccessfull("Successfull Message.");
		});

		verify(operatorController).allOperators();
		listOperatorsPanel.label("lblMessageStatus").requireText("Successfull Message.");
	}

	@Test
	@GUITest
	public void testShowErrorShouldAddOperatorsToTheListAndShowErrorMessage() {
		// SETUP
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		listOperatorsPanel.list("listOperators").requireVisible().requireEnabled().requireNoSelection();

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getOperatorsPanel().showError("Error Message.");
		});

		verify(operatorController).allOperators();
		listOperatorsPanel.label("lblMessageStatus").requireText("Error Message.");
	}

}
