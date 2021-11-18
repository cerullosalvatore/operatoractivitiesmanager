package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import static org.mockito.Mockito.times;
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
	protected void onSetUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			operatorActivitiesManagerView = new OperatorActivitiesManagerView();
			operatorActivitiesManagerView.setActivitiesController(activityController);
			operatorActivitiesManagerView.setOperatorController(operatorController);
			operatorActivitiesManagerView.setBasicOperationController(basicOperationController);

			return operatorActivitiesManagerView;
		});
		// FrameFixture will then be used to interact with our view’s controls (labels,
		// text fields, buttons, etc.).
		Robot robot = robot();
		robot.settings().eventPostingDelay(500);
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
		// Verify
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Activities").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("activitiesPanel");

		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("operatorsPanel");

		tabbedPaneFixture.focus().selectTab("Basic Operations").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("basicOperationPanel");
	}

	@Test
	@GUITest
	public void testActivitiesTabInitialStates() {
		// Verify
		frameFixture.panel("contentPane").panel("activitiesPanel").requireEnabled().requireVisible();
		verify(activityController).allActivities();
		verify(activityController).allOperators();
		verify(activityController).allBasicOperation();
	}

	@Test
	@GUITest
	public void testTabChangeToActivities() {
		// Verify
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		tabbedPaneFixture.focus().selectTab("Activities").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("activitiesPanel").requireVisible().requireEnabled();

		verify(activityController, times(2)).allActivities();
		verify(activityController, times(2)).allOperators();
		verify(activityController, times(2)).allBasicOperation();
	}

	@Test
	@GUITest
	public void testTabChangeToOperators() {
		// Verify
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Operators").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("operatorsPanel").requireVisible().requireEnabled();
		verify(operatorController).allOperators();
	}

	@Test
	@GUITest
	public void testTabChangeToBasicOperation() {
		// Verify
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.focus().selectTab("Basic Operations").requireVisible().requireEnabled();
		frameFixture.panel("contentPane").panel("basicOperationPanel");
		verify(basicOperationController).allBasicOperations();
	}
}
