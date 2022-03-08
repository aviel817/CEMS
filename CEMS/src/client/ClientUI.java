package client;

import java.io.IOException;

import client.gui.MainGuiController;
import javafx.application.Application;
import javafx.stage.Stage;
import models.User;

/**
 * Main user interface class that's starting the client application, and all of
 * the graphic user interface component.
 * 
 */
public class ClientUI extends Application {

	private static ClientController clientController;
	private static boolean serverStatus = false;

	/** Value that hold the user, use to login and and open appropriate menu */
	private User user;

	/**
	 * launch JavaFX
	 * 
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Function that's starting when all of javaFX component is ready
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			setClientController(new ClientController("localhost", 5555, this));
			setServerStatus(true);
		} catch (IOException e1) {
		}

		MainGuiController clientMainGuiController = new MainGuiController();
		clientMainGuiController.start(primaryStage);

	}

	/**
	 * @return the client controller
	 */
	public static ClientController getClientController() {
		return clientController;
	}

	/**
	 * set the client controller
	 * 
	 * @param clientController
	 */
	public static void setClientController(ClientController clientController) {
		ClientUI.clientController = clientController;
	}

	/**
	 * @return server status
	 */
	public static boolean isServerStatus() {
		return serverStatus;
	}

	/**
	 * set server status
	 * 
	 * @param serverStatus
	 */
	public static void setServerStatus(boolean serverStatus) {
		ClientUI.serverStatus = serverStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
