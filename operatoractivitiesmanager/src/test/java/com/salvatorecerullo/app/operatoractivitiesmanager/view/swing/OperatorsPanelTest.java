package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.Operator;

public class OperatorsPanelTest extends AssertJSwingJUnitTestCase {
	private OperatorActivitiesManagerView operatorActivitiesManagerView;
	private FrameFixture frameFixture;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
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

}
