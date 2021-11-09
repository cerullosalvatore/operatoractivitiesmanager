package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

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
		JPanelFixture formOBasicOperationPanel = frameFixture.panel("newBasicOperationPanel").panel("formBasicOperationPanel");
		formOBasicOperationPanel.label("lblId").requireVisible();
		formOBasicOperationPanel.textBox("textFieldId").requireVisible().requireNotEditable().requireText(operatorActivitiesManagerView.getBasicOperationPanel().getBasicOperationIdTemp());
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
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnModify").requireVisible()
				.requireDisabled();
		listBottomMenuPanel.panel("listBottomMenuPanelButton").button("btnDelete").requireDisabled()
				.requireDisabled();
		listBottomMenuPanel.label("lblMessageStatus").requireVisible().requireText("");
	}
}
