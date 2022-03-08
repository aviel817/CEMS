package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.gui.ServerGuiController;

/**
 * Main user interface class that's starting the server application, and all of
 * the graphic user interface component.
 * 
 */
public class ServerUI extends Application {

	/**
	 * launch JavaFX
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Function that's starting when all of javaFX component is ready terminate the
	 * server if the user close the window.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				System.exit(0);
			}
		});
		ServerGuiController serverGui = new ServerGuiController();
		serverGui.start(primaryStage);
	}

}
