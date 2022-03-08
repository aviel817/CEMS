package client.gui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * FXML controller class for student enter a specific exam, manual nor computerize in javaFX graphic user interface.
 *
 */
public class StartExamController {

	@FXML
	private JFXButton btnComputerized;

	@FXML
	private JFXButton btnManual;

	/**
	 * Set computerize exam screen
	 * @param event
	 */
	@FXML
	void onClickComputerized(ActionEvent event) {
		MainGuiController.getMenuHandler().setComputerizedScreen();
	}
	/**
	 * Set click manual screen
	 * @param event
	 */
	@FXML
	void onClickManual(ActionEvent event) {
		MainGuiController.getMenuHandler().setManualScreen();
	}

}
