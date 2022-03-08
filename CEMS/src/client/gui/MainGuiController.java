
package client.gui;

import client.Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML controller class for main screen in javaFX graphic user interface, let
 * the user interact with the main application graphic user interface.
 */
public class MainGuiController {

	/**
	 * Value for storing the main layout of the application, in order to change the
	 * sub layout inside it.
	 */
	private static BorderPane mainPane;

	private static MenuHandler menuHandler;

	@FXML
	private Button btnSearch;

	@FXML
	private TextField tfId;

	@FXML
	private Label labelStatus;

	/**
	 * Getting the application current stage and set new main screen scene.
	 * 
	 * @param stage of javaFX application
	 */
	public void start(Stage stage) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Client.logOutClient();
				System.exit(0);
			}
		});

		try {
			mainPane = (BorderPane) FXMLLoader.load(getClass().getResource("MainGui.fxml"));
			Scene scene = new Scene(mainPane, 1024, 768);
			stage.setScene(scene);
			stage.setTitle("CEMS");
			stage.getIcons().add(new Image("server/gui/icon.png"));
			stage.show();
			menuHandler = new MenuHandler(mainPane);
			menuHandler.setLoginMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return menu handler
	 */
	public static MenuHandler getMenuHandler() {
		return menuHandler;
	}

	/**
	 * set menu handler
	 * 
	 * @param menuHandler
	 */
	public static void setMenuHandler(MenuHandler menuHandler) {
		MainGuiController.menuHandler = menuHandler;
	}

}
