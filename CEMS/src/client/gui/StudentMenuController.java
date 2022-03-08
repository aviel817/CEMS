package client.gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.Client;
import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML controller class for Student pages in javaFX graphic user interface, 
 * handle and set the page by what the student clicked. 
 *
 */

public class StudentMenuController implements Initializable {

	@FXML
	private JFXButton btnExecutedExams;

	@FXML
	private JFXButton btnEnterExam;

	@FXML
	private JFXButton btnLogout;

	@FXML
	private Label labelWelcome;

	private static boolean locked;
	
	private static boolean closed = false;

	enum Buttons {
		EXECUTED_EXAMS, ENTER_EXAM
	}

	/**
	 * Set Executed Exams window on click
	 * @param event
	 */
	@FXML
	void onClickExecutedExams(ActionEvent event) {
		if (locked)
		{
			if (setConfirmationPopup())
			{
				paintSelectedButton(Buttons.EXECUTED_EXAMS);
				MainGuiController.getMenuHandler().setExecutedExamsScreen();
				locked = false;
				closed = true;
			}
		}
		
		if (!locked) {
			paintSelectedButton(Buttons.EXECUTED_EXAMS);
			MainGuiController.getMenuHandler().setExecutedExamsScreen();
		}

	}

	/**
	 * Set EnterExam window on click
	 * @param event
	 */
	@FXML
	void onClickEnterExam(ActionEvent event) {
		if (locked)
		{
			if (setConfirmationPopup())
			{
				paintSelectedButton(Buttons.ENTER_EXAM);
				MainGuiController.getMenuHandler().setEnterExamScreen();
				locked = false;
				closed = true;
			}
		}
		
		
		if (!locked) {
			paintSelectedButton(Buttons.ENTER_EXAM);
			MainGuiController.getMenuHandler().setEnterExamScreen();
		}
	}

	/**
	 * Set logout window on click
	 * @param event
	 */
	@FXML
	void onClickLogout(ActionEvent event) {
		if (locked)
		{
			if (setConfirmationPopup())
			{
				MainGuiController.getMenuHandler().setLoginMenu();
				Client.logOutClient();
				locked = false;
				closed = true;
			}
		}
		
		if (!locked) {
			MainGuiController.getMenuHandler().setLoginMenu();
			Client.logOutClient();
		}
	}

	
	/**
	 * Sets the given button bold by painting
	 * @param button The button to bold
	 */
	private void paintSelectedButton(Buttons button) {

		switch (button) {
		case EXECUTED_EXAMS:
			btnExecutedExams.setStyle("-fx-background-color:#48a832");
			btnEnterExam.setStyle("-fx-background-color:#333333");
			break;

		case ENTER_EXAM:
			btnExecutedExams.setStyle("-fx-background-color:#333333");
			btnEnterExam.setStyle("-fx-background-color:#48a832");
			break;
		}

	}
	
	/**
	 * Set a alert dialog on click menu
	 * @return true if user clicked ok else false
	 */
	public boolean setConfirmationPopup()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("You are trying to exit exam");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    return true;
		} else {
		   return false;
		}
	}

	/**
	 *	set welcome label with username firstname and lastname
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String name = ClientController.getClientUI().getUser().getFirstName() + " " + ClientController.getClientUI().getUser().getLastName();
		String LabelPrint = "Welcome, " + name;
		labelWelcome.setText(LabelPrint);
	}

	/**
	 * Go to student menu on logo click
	 * @param event
	 */
	@FXML
	void onLogoClicked(MouseEvent event) {
		MainGuiController.getMenuHandler().setStudentlMenu();
	}

	
	/**
	 * @return true if menu is locked else false
	 */
	public static boolean isLocked() {
		return locked;
	}

	/**
	 * set locked menu boolean according to the status
	 * @param locked
	 */
	public static void setLocked(boolean locked) {
		StudentMenuController.locked = locked;
	}

	/**
	 * @return true if test is closed else false
	 */
	public static boolean isClosed() {
		return closed;
	}

	
	/**
	 * Set closed boolean according to the status
	 * @param closed
	 */
	public static void setClosed(boolean closed) {
		StudentMenuController.closed = closed;
	}
	
	

}
