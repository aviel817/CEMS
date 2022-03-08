package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * TeacherLoginHomePageController class handle teacher homepage screen
 *
 */
public class TeacherLoginHomePageController implements Initializable {

	@FXML
	private Label labelTeacherName;

	/**
	 * Setting name label on init
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String name = ClientController.getClientUI().getUser().getFirstName() + " "
				+ ClientController.getClientUI().getUser().getLastName();
		labelTeacherName.setText("Hey, " + name);

	}

}
