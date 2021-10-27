package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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
	public void testActivitiesControlsArePresent() {
		// Verify
		frameFixture.panel("newActivityPanel").label("labelNewActivity");
		JPanelFixture formActivityPanel = frameFixture.panel("newActivityPanel").panel("formActivityPanel");
		formActivityPanel.label("labelOperatorActivity");
		formActivityPanel.comboBox("comboBoxOperatorActivity");
		formActivityPanel.label("labelBasicOperationActivity");
		formActivityPanel.comboBox("comboBoxBasicOperationActivity");
		formActivityPanel.label("labelStartDataActivity");
		formActivityPanel.textBox("textFieldStartDataActivity");
		formActivityPanel.label("labelStartHourActivity");
		formActivityPanel.textBox("textFieldStartHourActivity");
		formActivityPanel.label("labelEndDataActivity");
		formActivityPanel.textBox("textFieldEndDataActivity");
		formActivityPanel.label("labelEndHourActivity");
		formActivityPanel.textBox("textFieldEndHourActivity");
		frameFixture.panel("newActivityPanel").button("btnAddActivity");
		JPanelFixture listActivitiesPanel = frameFixture.panel("listActivitiesPanel");
		listActivitiesPanel.list("listActivities");
		JPanelFixture listTopMenuPanel = frameFixture.panel("listTopMenuPanel");
		listTopMenuPanel.toggleButton("tglbtnShowAll");
		listTopMenuPanel.toggleButton("tglbtnFindByOperator");
		listTopMenuPanel.toggleButton("tglbtnFindByBasicOperation");
		listActivitiesPanel.comboBox("comboBoxSelectorSearch");
		JPanelFixture listBottomMenuPanel = frameFixture.panel("listBottomMenuPanel");
		listBottomMenuPanel.button("btnModifyActivity");
		listBottomMenuPanel.button("btnDeleteActivity");
	}


}
