package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.assertj.swing.junit.runner.GUITestRunner;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

@RunWith(GUITestRunner.class)
public class OperatorsPanelTest extends AssertJSwingJUnitTestCase {
	private JFrame jFrame;

	private OperatorsPanel operatorsPanel;

	private FrameFixture frameFixture;

	@Mock
	private OperatorController operatorController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			operatorsPanel = new OperatorsPanel();
			operatorsPanel.setOperatorController(operatorController);
			jFrame.add(operatorsPanel);
			return jFrame;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		robot.settings().eventPostingDelay(500);
		frameFixture = new FrameFixture(robot, jFrame);
		frameFixture.show(); // shows the frame to test
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
			operatorsPanel.getListOperatorsModel().addElement(newOperator);
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
			operatorsPanel.getListOperatorsModel().addElement(newOperator);
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
		formOperatorPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();

		// Exercise
		formOperatorPanel.textBox("textFieldName").enterText("TestName");
		formOperatorPanel.textBox("textFieldSurname").deleteText();

		// Verify
		buttonsFormOperatorPanel.button("btnUpdateOperator").requireDisabled();

	}

	@Test
	@GUITest
	public void testUpdateOperatorIsPressedListEnabledAndInputReset() {
		// Setup
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator newOperator = new Operator("MatricolaTest", "NameTest", "SurnameTest");
		GuiActionRunner.execute(() -> {
			operatorsPanel.getListOperatorsModel().addElement(newOperator);
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
		
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		GuiActionRunner.execute(() -> {
			operatorsPanel.getListOperatorsModel().addElement(operator1);
			operatorsPanel.getListOperatorsModel().addElement(operator2);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		// Verify
		formOperatorPanel.textBox("textFieldMatricola").requireText("MatricolaTest1");
		formOperatorPanel.textBox("textFieldName").requireText("NameTest1");
		formOperatorPanel.textBox("textFieldSurname").requireText("SurnameTest1");
	}

	@Test
	@GUITest
	public void testTextFieldMatricolaShouldBeNotEditabledWhenModifyButtonIsPressed() {
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		GuiActionRunner.execute(() -> {
			operatorsPanel.getListOperatorsModel().addElement(operator1);
			operatorsPanel.getListOperatorsModel().addElement(operator2);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		formOperatorPanel.textBox("textFieldMatricola").requireNotEditable();
		
		// Exercise
		buttonsFormOperatorPanel.button("btnUpdateOperator").click();

		// Verify
		formOperatorPanel.textBox("textFieldMatricola").requireText("").requireEditable();
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
			operatorsPanel.showAllOperators(operators);
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
			operatorsPanel.showSuccessfull("Successfull Message.");
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
			operatorsPanel.showError("Error Message.");
		});

		verify(operatorController).allOperators();
		listOperatorsPanel.label("lblMessageStatus").requireText("Error Message.");
	}

	// TEST INTERACTION BETWEEN BUTTON AND CONTROLLERS
	@Test
	@GUITest
	public void testAddButtonShouldDelegateToOperatorControllerNewOperator() throws ParseException {
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		Operator newOperator = new Operator("MatricolaTest", "NameTest", "SurnameTest");

		// Exercise
		formOperatorPanel.textBox("textFieldMatricola").enterText("MatricolaTest");
		formOperatorPanel.textBox("textFieldName").enterText("NameTest");
		formOperatorPanel.textBox("textFieldSurname").enterText("SurnameTest");

		buttonsFormOperatorPanel.button("btnAddOperator").click();

		// Verify
		verify(operatorController).addOperator(newOperator);
	}

	@Test
	@GUITest
	public void testDeletedButtonShouldDelegateToOperatorControllerRemoveOperator() throws ParseException {
		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator operator1 = new Operator("MatricolaTest1", "NameTest1", "SurnameTest1");
		Operator operator2 = new Operator("MatricolaTest2", "NameTest2", "SurnameTest2");

		GuiActionRunner.execute(() -> {
			operatorsPanel.getListOperatorsModel().addElement(operator1);
			operatorsPanel.getListOperatorsModel().addElement(operator2);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnDeleteOperator").click();

		// Verify
		verify(operatorController).removeOperator(operator1);

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(1);
		listBottomMenuPanel.button("btnDeleteOperator").click();

		// Verify
		verify(operatorController).removeOperator(operator2);
	}

	@Test
	@GUITest
	public void testUpdateButtonShouldDelegateToOperatorControllerUpdateOperator() throws ParseException {
		JPanelFixture formOperatorPanel = frameFixture.panel("newOperatorPanel").panel("formOperatorPanel");
		JPanelFixture buttonsFormOperatorPanel = frameFixture.panel("newOperatorPanel")
				.panel("buttonsFormOperatorPanel");

		JPanelFixture listOperatorsPanel = frameFixture.panel("listOperatorsPanel");
		JPanelFixture listBottomMenuPanel = listOperatorsPanel.panel("listBottomMenuPanel");

		Operator operatorOld = new Operator("MatricolaTestOld", "NameTestOld", "SurnameTestOld");

		GuiActionRunner.execute(() -> {
			operatorsPanel.getListOperatorsModel().addElement(operatorOld);
		});

		// Exercise
		listOperatorsPanel.list("listOperators").selectItem(0);
		listBottomMenuPanel.button("btnModifyOperator").click();

		formOperatorPanel.textBox("textFieldName").deleteText().enterText("NameTestUpdated");
		formOperatorPanel.textBox("textFieldSurname").deleteText().enterText("SurnameTestUpdated");
		buttonsFormOperatorPanel.button("btnUpdateOperator").click();

		// Verify
		Operator operatorUpdated = new Operator(operatorOld.getMatricola(), "NameTestUpdated", "SurnameTestUpdated");
		verify(operatorController).updateOperator(operatorUpdated);
	}
}
