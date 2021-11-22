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
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salvatorecerullo.app.operatoractivitiesmanager.controller.BasicOperationController;
import com.salvatorecerullo.app.operatoractivitiesmanager.model.BasicOperation;

@RunWith(GUITestRunner.class)
public class BasicOperationPanelTest extends AssertJSwingJUnitTestCase {
	private JFrame jFrame;

	private FrameFixture frameFixture;

	private BasicOperationPanel basicOperationPanel;

	@Mock
	private BasicOperationController basicOperationController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		// Our frame and the fixture will be recreated for each test method so that we
		// always start with a fresh user interface.
		GuiActionRunner.execute(() -> {
			jFrame = new JFrame();
			basicOperationPanel = new BasicOperationPanel();
			basicOperationPanel.setBasicOperationController(basicOperationController);
			jFrame.add(basicOperationPanel);
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
	public void testBasicOperationControlsArePrestentAndInitialStates() {
		// Verify
		frameFixture.panel("newBasicOperationPanel").label("lblOperationDetails").requireVisible();
		JPanelFixture formOBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		formOBasicOperationPanel.label("lblId").requireVisible();
		formOBasicOperationPanel.textBox("textFieldId").requireVisible().requireNotEditable()
				.requireText(basicOperationPanel.getBasicOperationIdTemp());
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
			basicOperationPanel.getListBasicOpeationModel().addElement(newBasicOperation);
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
			basicOperationPanel.getListBasicOpeationModel().addElement(newBasicOperation);
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
			basicOperationPanel.getListBasicOpeationModel().addElement(newBasicOperation);
		});

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("TestName");
		formOperationPanel.textBox("textAreaDescription").enterText("TestDescription");

		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnModify").click();

		buttonsFormBasicOperationPanel.button("btnUpdateOperation").click();

		// Verify
		listBasicOperationsPanel.list("listBasicOperations").isEnabled();
		formOperationPanel.textBox("textFieldId").requireText(basicOperationPanel.getBasicOperationIdTemp());
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
			basicOperationPanel.getListBasicOpeationModel().addElement(basicOperation1);
			basicOperationPanel.getListBasicOpeationModel().addElement(basicOperation2);
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

	// TEST INTERFACE METHODS
	@Test
	@GUITest
	public void testShowAllOperatorsShouldAddBasicOperationsToTheList() {
		// Setup
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");

		BasicOperation basicOperation1 = new BasicOperation("IdOperation1", "NameOperation1", "TestDescription1");
		BasicOperation basicOperation2 = new BasicOperation("IdOperation2", "NameOperation2", "TestDescription2");

		List<BasicOperation> basicOpeations = new ArrayList<BasicOperation>();

		basicOpeations.add(basicOperation1);
		basicOpeations.add(basicOperation2);

		// Exercise
		GuiActionRunner.execute(() -> {
			basicOperationPanel.showAllBasicOperations(basicOpeations);
		});

		// Verify
		String[] listContents = listBasicOperationsPanel.list("listBasicOperations").contents();
		assertThat(listContents).containsExactly(basicOperation1.toString(), basicOperation2.toString());
		listBasicOperationsPanel.label("lblMessageStatus").requireText("");
	}

	@Test
	@GUITest
	public void testShowSuccessfullShouldAddBasicOperationsToTheListAndShowSuccessMessage() {
		// Setup
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		listBasicOperationsPanel.list("listBasicOperations").requireVisible().requireEnabled().requireNoSelection();

		// Exercise
		GuiActionRunner.execute(() -> {
			basicOperationPanel.showSuccessfull("Successfull Message.");
		});

		// Verify
		verify(basicOperationController).allBasicOperations();
		listBasicOperationsPanel.label("lblMessageStatus").requireText("Successfull Message.");
	}

	@Test
	@GUITest
	public void testShowErrorShouldAddOperatorsToTheListAndShowErrorMessage() {
		// Setup
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		listBasicOperationsPanel.list("listBasicOperations").requireVisible().requireEnabled().requireNoSelection();

		// Exercise
		GuiActionRunner.execute(() -> {
			basicOperationPanel.showError("Error Message.");
		});

		// Verify
		verify(basicOperationController).allBasicOperations();
		listBasicOperationsPanel.label("lblMessageStatus").requireText("Error Message.");
	}

	// TEST INTERACTION BETWEEN BUTTON AND CONTROLLERS
	@Test
	@GUITest
	public void testAddButtonShouldDelegateToBasicOperationControllerNewBasicOperation() throws ParseException {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		String idTemp = basicOperationPanel.getBasicOperationIdTemp();
		BasicOperation basicOperation = new BasicOperation(idTemp, "NameOperation", "TestDescription");

		// Exercise
		formOperationPanel.textBox("textFieldName").enterText("NameOperation");
		formOperationPanel.textBox("textAreaDescription").enterText("TestDescription");

		buttonsFormBasicOperationPanel.button("btnAddOperation").click();

		// Verify
		verify(basicOperationController).addBasicOperation(basicOperation);
		formOperationPanel.textBox("textFieldId").requireText(basicOperationPanel.getBasicOperationIdTemp());

	}

	@Test
	@GUITest
	public void testDeletedButtonShouldDelegateToBasicOperationControllerRemoveBasicOperation() throws ParseException {
		// Setup
		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation basicOperation1 = new BasicOperation("IdOperation1", "NameOperation1", "TestDescription1");
		BasicOperation basicOperation2 = new BasicOperation("IdOperation2", "NameOperation2", "TestDescription2");

		GuiActionRunner.execute(() -> {
			basicOperationPanel.getListBasicOpeationModel().addElement(basicOperation1);
			basicOperationPanel.getListBasicOpeationModel().addElement(basicOperation2);
		});

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnDelete").click();

		// Verify
		verify(basicOperationController).removeBasicOperation(basicOperation1);

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").selectItem(1);
		listBottomMenuPanel.button("btnDelete").click();

		// Verify
		verify(basicOperationController).removeBasicOperation(basicOperation2);
	}

	@Test
	@GUITest
	public void testUpdateButtonShouldDelegateToOperatorControllerUpdateOperator() throws ParseException {
		// Setup
		JPanelFixture formOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("formBasicOperationPanel");
		JPanelFixture buttonsFormBasicOperationPanel = frameFixture.panel("newBasicOperationPanel")
				.panel("buttonsFormBasicOperationPanel");

		JPanelFixture listBasicOperationsPanel = frameFixture.panel("listBasicOperationsPanel");
		JPanelFixture listBottomMenuPanel = listBasicOperationsPanel.panel("listBottomMenuPanel");

		BasicOperation basicOperatioOld = new BasicOperation("idOld", "NameOld", "DescriptionOld");

		GuiActionRunner.execute(() -> {
			basicOperationPanel.getListBasicOpeationModel().addElement(basicOperatioOld);
		});

		// Exercise
		listBasicOperationsPanel.list("listBasicOperations").selectItem(0);
		listBottomMenuPanel.button("btnModify").click();

		formOperationPanel.textBox("textFieldName").deleteText().enterText("NameTestUpdated");
		formOperationPanel.textBox("textAreaDescription").deleteText().enterText("DescriptionTestUpdated");
		buttonsFormBasicOperationPanel.button("btnUpdateOperation").click();

		// Verify
		BasicOperation basicOperationUpdated = new BasicOperation(basicOperatioOld.getId(), "NameTestUpdated",
				"DescriptionTestUpdated");
		verify(basicOperationController).updateBasicOperation(basicOperationUpdated);
		formOperationPanel.textBox("textFieldId").requireText(basicOperationPanel.getBasicOperationIdTemp());

	}

}
