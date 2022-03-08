package client.gui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * FXML controller class for student enter a specific exam, manual nor
 * computerize in javaFX graphic user interface.
 *
 */
public class QuestionManagementController {

	@FXML
	private JFXButton btnCreateQuestion;

	@FXML
	private JFXButton btnEditQuestion;

	/**
	 * Set computerize exam screen
	 * 
	 * @param event
	 */
	@FXML
	void onClickCreateQuestion(ActionEvent event) {
		MainGuiController.getMenuHandler().setCreateQuestionScreen();

	}

	/**
	 * Set click manual screen
	 * 
	 * @param event
	 */
	@FXML
	void onClickEditQuestion(ActionEvent event) {
		MainGuiController.getMenuHandler().setEditQuestionScreen();
	}

}
