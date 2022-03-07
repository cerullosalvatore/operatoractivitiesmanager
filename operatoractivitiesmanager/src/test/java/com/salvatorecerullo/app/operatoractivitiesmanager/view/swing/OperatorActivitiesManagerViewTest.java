package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.mockito.Mockito.verify;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.ActivityController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.controller.OperatorController;

@RunWith(GUITestRunner.class)
public class OperatorActivitiesManagerViewTest extends AssertJSwingJUnitTestCase {
	private OperatorActivitiesManagerView operatorActivitiesManagerView;

	@Mock
	private ActivityController activityController;

	@Mock
	private OperatorController operatorController;

	@Mock
	private BasicOperationController basicOperationController;

	private FrameFixture frameFixture;

	@Override
	protected void onSetUp(){
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
			operatorActivitiesManagerView.getActivitiesPanel().setActivityController(activityController);
			operatorActivitiesManagerView.getOperatorsPanel().setOperatorController(operatorController);
			operatorActivitiesManagerView.getBasicOperationPanel()
					.setBasicOperationController(basicOperationController);

			return operatorActivitiesManagerView;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		frameFixture = new FrameFixture(robot, operatorActivitiesManagerView);
		frameFixture.show(); // shows the frame to test
	}

	@Test
	@GUITest
	public void testTabbedPaneIsPresent() {
		// Verify
		frameFixture.requireTitle("Operator Activities Manager");
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.requireTabTitles("Activities", "Operators", "Basic Operations");
	}

	@Test
	@GUITest
	public void testAllTabsInitialStates() {
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		// Exercise
		tabbedPaneFixture.focus().selectTab("Activities").requireVisible().requireEnabled();
		// Verify
		frameFixture.panel("contentPane").panel("activitiesPanel");

		// Exercise
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		// Verify
		frameFixture.panel("contentPane").panel("operatorsPanel");

		// Exercise
		tabbedPaneFixture.focus().selectTab("Basic Operations").requireVisible().requireEnabled();
		// Verify
		frameFixture.panel("contentPane").panel("basicOperationPanel");
	}

	@Test
	@GUITest
	public void testTabChangeToActivities() {
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");

		// Exercise
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		tabbedPaneFixture.focus().selectTab("Activities").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("activitiesPanel").requireVisible().requireEnabled();

		// Verify
		verify(activityController).allActivities();
		verify(activityController).allOperators();
		verify(activityController).allBasicOperation();
	}

	@Test
	@GUITest
	public void testTabChangeToOperators() {
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		// Exercise
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		// Verify
		frameFixture.panel("contentPane").panel("operatorsPanel").requireVisible().requireEnabled();
		verify(operatorController).allOperators();
	}

	@Test
	@GUITest
	public void testTabChangeToBasicOperation() {
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		// Exercise
		tabbedPaneFixture.focus().selectTab("Basic Operations").requireVisible().requireEnabled();
		// Verify
		frameFixture.panel("contentPane").panel("basicOperationPanel");
		verify(basicOperationController).allBasicOperations();
	}
}
