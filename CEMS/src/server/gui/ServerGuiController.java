package server.gui;

import java.io.IOException;
//
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Database;
import server.Server;
import server.ServerEventListener;

/**
 * FXML controller class for server screen in javaFX graphic user interface, let
 * the user start new server by enter correct server and database details, this
 * class handle creating new server instance that's listening for clients
 * requests. every event that occurred on server displayed on server log.
 *
 */
public class ServerGuiController implements Initializable, ServerEventListener {

	@FXML
	private TextField tfIp;

	@FXML
	private TextField tfPort;

	@FXML
	private TextField tfScheme;

	@FXML
	private TextField tfUserName;

	@FXML
	private PasswordField tfPassword;

	@FXML
	private TextField tfPortServer;

	@FXML
	private Button btnStart;

	@FXML
	private TextArea taLogs;

	/** Server instance for client-server communication */
	private Server server;

	/**
	 * Getting the application primary stage and set new server screen graphic
	 * interface scene, with database,port details fields and button in order to
	 * create the server and start listening.
	 * 
	 * @param primaryStage of javaFX application
	 */
	public void start(Stage primaryStage) {
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("ServerGui.fxml"));
			Scene scene = new Scene(root, 700, 550);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("CEMS-Server");
			primaryStage.getIcons().add(new Image("server/gui/icon.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle "Start" button, by creating new server instance and start listening,if
	 * server already connected call method to disconnect from server.
	 * 
	 * @param event window event handler
	 */
	@FXML
	void onClickStartServer(ActionEvent event) {

		if (Server.isConnected() == false) {
			btnStart.setText("Starting Server..");
			createServer();
			try {
				server.listen();
			} catch (Exception ex) {
				printToLog("ERROR - Could not listen to clients!");
			}
		} else {
			btnStart.setText("Disconnecting..");
			disconnectFromServer();
		}

	}

	/**
	 * Called by javaFX controller system, initialize all appropriate variable this
	 * in case fill all text fields in default value.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillDefaultDetails();
	}

	/**
	 * Method to collect all server and database information, create new server
	 * instance to implement server-client communication and transfer database
	 * details into server in order to create database controller.
	 */
	private void createServer() {
		String ip = tfIp.getText();
		String port = tfPort.getText();
		String scheme = tfScheme.getText();
		String username = tfUserName.getText();
		String password = tfPassword.getText();
		String serverPort = tfPortServer.getText();
		Database database = new Database(ip, port, scheme, username, password);
		server = new Server(this, database, serverPort);
	}

	/**
	 * Called when disconnect button has been clicked, close server and stop
	 * listening.
	 */
	public void disconnectFromServer() {
		try {
			server.close();
			printToLog("Disconnected from server");
		} catch (IOException e) {
			printToLog("Critical error disconnecting");
			e.printStackTrace();
		}
	}

	/**
	 * Change all text fields to default values
	 */
	private void fillDefaultDetails() {
		tfIp.setText("127.0.0.1");
		tfPort.setText("3306");
		tfScheme.setText("cems");
		tfUserName.setText("root");
		tfPassword.setText("1234");
		tfPortServer.setText("5555");
	}

	@Override
	public void printToLog(String logs) {
		taLogs.appendText(logs + "\n");
	}

	/**
	 * Method to handle button clicking events, change the button status
	 * {"connect","disconnect"} when server has been connected/disconnected.
	 */
	@Override
	public void changeButtonStatus(boolean status) {
		Thread btnChangeToDisconnect = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (Exception ex) {
					taLogs.appendText("Problem Occured");
				}
				Platform.runLater(() -> {
					if (status)
						btnStart.setText("Disconnect");
					else
						btnStart.setText("Connect");
				});
			}
		});

		btnChangeToDisconnect.setDaemon(true);
		btnChangeToDisconnect.start();
	}

}
