package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

public class BasicOperationPanelTest extends AssertJSwingJUnitTestCase {
	private OperatorActivitiesManagerView operatorActivitiesManagerView;

	private FrameFixture frameFixture;

	@Override
	protected void onSetUp() {
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
		tabbedPaneFixture.selectTab("Basic Operations");
	}

	@Test
	@GUITest
	public void testBasicOperationControlsArePrestentAndInitialStates() {
		// Verify
		frameFixture.panel("newBasicOperationPanel").label("lblOperationDetails").requireVisible();
		JPanelFixture formOBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		formOBasicOperationPanel.label("lblId").requireVisible();
		formOBasicOperationPanel.textBox("textFieldId").requireVisible().requireNotEditable()
				.requireText(operatorActivitiesManagerView.getBasicOperationPanel().getBasicOperationIdTemp());
		formOBasicOperationPanel.label("lblName").requireVisible();
		formOBasicOperationPanel.textBox("textFieldName").requireVisible().requireEnabled().requireEmpty();
		formOBasicOperationPanel.label("lblDescription").requireVisible();
		formOBasicOperationPanel.textBox("textAreaDescription").requireVisible().requireEnabled().requireEmpty();
		JPanelFixture buttonFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");
		buttonFormBasicOperationPanel.button("btnAddOperation").requireVisible().requireDisabled();
		buttonFormBasicOperationPanel.button("btnUpdateOperation").requireVisible().requireDisabled();
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		listBasicOperationsPanel.list("listBasicOperations").requireVisible().requireEnabled().requireNoSelection();
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnModify").requireVisible().requireDisabled();
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnDelete").requireDisabled().requireDisabled();
		listBottomMenuPanel.label("lblMessageStatus").requireVisible().requireText("");
	}

	@Test
	@GUITest
	public void testAddOperationButtonEnabledOnlyWhenAllInputIsCompiled() {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").enterText("TestDescription");

		// Verify
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireEnabled();

		// Exercise
		formOperationPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireDisabled();

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").deleteText();

		// Verify
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireDisabled();

		// Exercise
		formOperationPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireDisabled();
	}

	@Test
	@GUITest
	public void testDeleteButtonAndModifyShouldBeEnabledOnlyWhenABasicOperationIsSelected() {
		// Setup
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation newBasicOperation = new BasicOperation("IdOperation", "NameOperation", "TestDescription");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getBasicOperationPanel().getListBasicOpeationModel()
					.addElement(newBasicOperation);
		});

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);

		// Verify
		listBottomMenuPanel.button("btnModify").requireEnabled();
		listBottomMenuPanel.button("btnDelete").requireEnabled();

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").clearSelection();

		// Verify
		listBottomMenuPanel.button("btnModify").requireDisabled();
		listBottomMenuPanel.button("btnDelete").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedListDisabledAndUpdateButtonIsEnabled() {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation newBasicOperation = new BasicOperation("IdOperation", "NameOperation", "TestDescription");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getBasicOperationPanel().getListBasicOpeationModel()
					.addElement(newBasicOperation);
		});

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").enterText("TestDescription");

		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnModify").click();

		// Verify
		listBasicOperationsPanel.list("listBasicOperations").requireDisabled();
		buttonsFormBasicOperationPanel.button("btnUpdateOperation").requireEnabled();
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireDisabled();

		// Exercise
		formOperationPanel.textBox("textFieldName").deleteText();

		// Verify
		buttonsFormBasicOperationPanel.button("btnUpdateOperation").requireDisabled();

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").deleteText();

		// Verify
		buttonsFormBasicOperationPanel.button("btnUpdateOperation").requireDisabled();

	}

	@Test
	@GUITest
	public void testUpdateOperationIsPressedListEnabledAndInputReset() {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation newBasicOperation = new BasicOperation("IdOperation", "NameOperation", "TestDescription");
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getBasicOperationPanel().getListBasicOpeationModel()
					.addElement(newBasicOperation);
		});

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").enterText("TestDescription");

		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnModify").click();

		buttonsFormBasicOperationPanel.button("btnUpdateOperation").click();

		// Verify
		listBasicOperationsPanel.list("listBasicOperations").isEnabled();
		formOperationPanel.textBox("textFieldId")
				.requireText(operatorActivitiesManagerView.getBasicOperationPanel().getBasicOperationIdTemp());
		formOperationPanel.textBox("textFieldName").requireEmpty();
		formOperationPanel.textBox("textAreaDescription").requireEmpty();

		buttonsFormBasicOperationPanel.button("btnUpdateOperation").requireDisabled();
		buttonsFormBasicOperationPanel.button("btnAddOperation").requireDisabled();
	}

	@Test
	@GUITest
	public void testModifyButtonIsPressedFieldWasCompiledCorrectly() {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation basicOperation1 = new BasicOperation("IdOperation1", "NameOperation1", "TestDescription1");
		BasicOperation basicOperation2 = new BasicOperation("IdOperation2", "NameOperation2", "TestDescription2");

		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView.getBasicOperationPanel().getListBasicOpeationModel()
					.addElement(basicOperation1);
			operatorActivitiesManagerView.getBasicOperationPanel().getListBasicOpeationModel()
					.addElement(basicOperation2);
		});

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnModify").click();

		// Verify
		formOperationPanel.textBox("textFieldId").requireText(basicOperation1.getId());
		formOperationPanel.textBox("textFieldName").requireText(basicOperation1.getName());
		formOperationPanel.textBox("textAreaDescription").requireText(basicOperation1.getDescription());

		// Exercise
		buttonsFormBasicOperationPanel.button("btnUpdateOperation").click();
		listBasicOperationsPanel.list("listBasicOperations").selectItem(1);
		listBottomMenuPanel.button("btnModify").click();

		// Verify
		formOperationPanel.textBox("textFieldId").requireText(basicOperation2.getId());
		formOperationPanel.textBox("textFieldName").requireText(basicOperation2.getName());
		formOperationPanel.textBox("textAreaDescription").requireText(basicOperation2.getDescription());
	}

	
}
