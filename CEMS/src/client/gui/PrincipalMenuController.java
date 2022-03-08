package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.Client;
import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Sidebar for navigation for "Administrator" user
 *
 */
public class PrincipalMenuController implements Initializable {

	@FXML
	private JFXButton btnExamPool;

	@FXML
	private JFXButton btnQuestionPool;

	@FXML
	private JFXButton btnOverallStatistics;

	@FXML
	private JFXButton btnTimeExtensionRequest;

	@FXML
	private JFXButton btnLogout;

	@FXML
	private Label labelWelcome;

	enum Buttons {
		OVERALL_STATISTICS, TIME_EXTENSION_REQUEST, EXAM_POOL, QUESTION_POOL
	}

	/**
	 * This method show the screen Exam Pool
	 * 
	 * @param event
	 */
	@FXML
	void onClickExamPool(ActionEvent event) {
		paintSelectedButton(Buttons.EXAM_POOL);
		MainGuiController.getMenuHandler().setExamPoolScreen();
	}

	/**
	 * This method show the screen Question Pool
	 * 
	 * @param event
	 */
	@FXML
	void onClickQuestionPool(ActionEvent event) {
		paintSelectedButton(Buttons.QUESTION_POOL);
		MainGuiController.getMenuHandler().setQuestionPoolScreen();
	}

	/**
	 * This method show the screen Overall Statistics
	 * 
	 * @param event
	 */
	@FXML
	void onClickOverallStatistics(ActionEvent event) {
		paintSelectedButton(Buttons.OVERALL_STATISTICS);
		MainGuiController.getMenuHandler().setOverallStatisticsScreen();
	}

	/**
	 * This method show the screen Time Extension Requests
	 * 
	 * @param event
	 */
	@FXML
	void onClickTimeExtensionRequest(ActionEvent event) {
		paintSelectedButton(Buttons.TIME_EXTENSION_REQUEST);
		MainGuiController.getMenuHandler().setTimeExtensionRequestsScreen();
	}

	/**
	 * This method show the screen Login
	 * 
	 * @param event
	 */
	@FXML
	void onClickLogout(ActionEvent event) {
		MainGuiController.getMenuHandler().setLoginMenu();
		Client.logOutClient();
	}

	/**
	 * painting the selected button
	 * 
	 * @param button
	 */
	private void paintSelectedButton(Buttons button) {

		switch (button) {

		case EXAM_POOL:
			btnExamPool.setStyle("-fx-background-color:#48a832");
			btnQuestionPool.setStyle("-fx-background-color:#333333");
			btnOverallStatistics.setStyle("-fx-background-color:#333333");
			btnTimeExtensionRequest.setStyle("-fx-background-color:#333333");
			break;

		case QUESTION_POOL:
			btnExamPool.setStyle("-fx-background-color:#333333");
			btnQuestionPool.setStyle("-fx-background-color:#48a832");
			btnOverallStatistics.setStyle("-fx-background-color:#333333");
			btnTimeExtensionRequest.setStyle("-fx-background-color:#333333");
			break;

		case OVERALL_STATISTICS:
			btnExamPool.setStyle("-fx-background-color:#333333");
			btnQuestionPool.setStyle("-fx-background-color:#333333");
			btnOverallStatistics.setStyle("-fx-background-color:#48a832");
			btnTimeExtensionRequest.setStyle("-fx-background-color:#333333");
			break;

		case TIME_EXTENSION_REQUEST:
			btnExamPool.setStyle("-fx-background-color:#333333");
			btnQuestionPool.setStyle("-fx-background-color:#333333");
			btnOverallStatistics.setStyle("-fx-background-color:#333333");
			btnTimeExtensionRequest.setStyle("-fx-background-color:#48a832");
			break;

		}

	}

	/**
	 * Set the name label
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String name = ClientController.getClientUI().getUser().getFirstName() + " "
				+ ClientController.getClientUI().getUser().getLastName();
		String LabelPrint = "Welcome, " + name;
		labelWelcome.setText(LabelPrint);

	}

	/**
	 * Click on logo show menu
	 * 
	 * @param event
	 */
	@FXML
	void onLogoClicked(MouseEvent event) {

		MainGuiController.getMenuHandler().setPrincipalMenu();

	}

}
