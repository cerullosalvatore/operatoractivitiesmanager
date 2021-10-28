package com.salvatorecerullo.app.operatoractivitiesmanager.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTabbedPaneFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class OperatorActivitiesManagerViewTest extends AssertJSwingJUnitTestCase {

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
	public void testTabbedPaneIsPresent() {
		// Verify
		frameFixture.requireTitle("Operator Activities Manager");
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.panel("contentPane").tabbedPane("tabbedPane");
		tabbedPaneFixture.requireTabTitles("Activities", "Operators", "Basic Operations");
	}

	@Test
	@GUITest
	public void testActivitiesTabControlsArePresent() {
		// Verify
		frameFixture.panel("contentPane").panel("newActivityPanel");
	}

}
