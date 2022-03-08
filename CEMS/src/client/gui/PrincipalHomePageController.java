package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * "Administrator" user login screen.
 */
public class PrincipalHomePageController implements Initializable {

	@FXML
	private Label labelName;

	/**
	 * Set name label
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String name = ClientController.getClientUI().getUser().getFirstName() + " "
				+ ClientController.getClientUI().getUser().getLastName();
		labelName.setText("Hey, " + name);

	}

}
