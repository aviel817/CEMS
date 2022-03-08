package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML controller class for Student homepage in javaFX graphic user interface.
 * 
 */

public class StudentHomePageController implements Initializable {

	@FXML
	private Label labelName;

	/**
	 * This method show the username firstname and lastname on the screen
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String name = ClientController.getClientUI().getUser().getFirstName() + " "
				+ ClientController.getClientUI().getUser().getLastName();
		labelName.setText("Hey, " + name);

	}

}
