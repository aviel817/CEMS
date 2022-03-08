package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML controller class for login screen in javaFX graphic user interface, let
 * the user enter the main application displaying main graphic user interface,
 * and store the new client on client controller.
 * 
 */
public class ClientLoginGuiController implements Initializable {

	@FXML
	private TextField tfPort;

	@FXML
	private Button btnConnect;

	@FXML
	private Label labelStatus;

	/**
	 * Getting the application primary stage and set new login screen scene, with
	 * port text field and enter button by FXML.
	 * 
	 * @param primaryStage of javaFX application
	 */
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox) FXMLLoader.load(getClass().getResource("ClientLoginGui.fxml"));
			Scene scene = new Scene(root, 300, 400);
			primaryStage.setScene(scene);
			primaryStage.setTitle("CEMS");
			primaryStage.getIcons().add(new Image("/gui/icon.png"));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle "Connect" button, by creating new main application graphic user
	 * interface and connect to server by creating new instance of client and
	 * connect to port given by text field, if succeed close login window.
	 * 
	 * @param event window event handler
	 */
	@FXML
	void onConnect(ActionEvent event) {
		MainGuiController clientMainGuiController = new MainGuiController();

		labelStatus.setText("");
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		clientMainGuiController.start(stage);

		labelStatus.setText("ERROR--Worng Port");
		labelStatus.setTextFill(Color.color(1, 0, 0));
		labelStatus.setAlignment(Pos.CENTER);

	}

	/** Setting default port to "5555" */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfPort.setText("5555");
	}

}
