package client.gui;

import static common.ModelWrapper.Operation.EDIT_QUESTION;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Question;
import models.User;

/**
 * QuestionEditController class handle question edit screen in the teacher menu
 *
 */
public class QuestionEditController implements EventHandler<WindowEvent>, Initializable {

	@FXML
	private TextArea taQD;

	@FXML
	private TextArea taA1;

	@FXML
	private TextArea taA2;

	@FXML
	private TextArea taA3;

	@FXML
	private TextArea taA4;

	@FXML
	private Label labelCorrect;

	@FXML
	private JFXButton btnExit;

	@FXML
	private JFXButton btnUpdate;

	@FXML
	private Label errorLabel;

	@FXML
	private JFXComboBox<Integer> cbCoorect;

	private static Question question;
	private static boolean isWindowOpend;

	/**
	 * @return question
	 */
	public static Question getQuestion() {
		return question;
	}

	/**
	 * set question
	 * @param question
	 */
	public static void setQuestion(Question question) {
		QuestionEditController.question = question;
	}

	/**
	 * @return if window is open or not
	 */
	public static boolean isWindowOpend() {
		return isWindowOpend;
	}

	/**
	 * set if window is open or not
	 * @param isWindowOpend
	 */
	public static void setWindowOpend(boolean isWindowOpend) {
		QuestionEditController.isWindowOpend = isWindowOpend;
	}

	/**
	 * exit on click
	 * @param event
	 */
	@FXML
	void onClickExit(ActionEvent event) {
		Node n = (Node) event.getSource();
		Stage s = (Stage) n.getScene().getWindow();
		setWindowOpend(false);
		s.close();

	}

	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		Stage stage = new Stage();
		AnchorPane mainPane;
		try {
			mainPane = (AnchorPane) FXMLLoader.load(getClass().getResource("QuestionEdit.fxml"));
			Scene scene = new Scene(mainPane, 450, 600);
			stage.setScene(scene);
			stage.setTitle("Question Details");
			stage.show();
			stage.setOnHidden(this);
			setWindowOpend(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void handle(WindowEvent arg0) {
		setWindowOpend(false);

	}

	/**
	 * Update the edited data and send to server
	 * @param event
	 */
	@FXML
	void onClickUpdate(ActionEvent event) {
		User teacherUser = ClientController.getClientUI().getUser();
		String details = taQD.getText();
		String answer1 = taA1.getText();
		String answer2 = taA2.getText();
		String answer3 = taA3.getText();
		String answer4 = taA4.getText();
		String teacherName = teacherUser.getFirstName() + " " + teacherUser.getLastName();
		String subject = question.getSubject();
		String questionID = question.getQuestionID();

		int correctAnswer;

		if (cbCoorect.getSelectionModel().getSelectedItem() == null)
			correctAnswer = -1;
		else
			correctAnswer = cbCoorect.getSelectionModel().getSelectedItem();

		if (details.isEmpty()) {
			errorLabel.setText("You must write question description");
		} else {
			errorLabel.setText("");
		}

		if (answer1.isEmpty()) {
			errorLabel.setText("answer can't be empty");
		} else {
			errorLabel.setText("");
		}

		if (answer2.isEmpty()) {
			errorLabel.setText("answer can't be empty");
		} else {
			errorLabel.setText("");
		}

		if (answer3.isEmpty()) {
			errorLabel.setText("answer can't be empty");
		} else {
			errorLabel.setText("");
		}

		if (answer4.isEmpty()) {
			errorLabel.setText("answer can't be empty");
		} else {
			errorLabel.setText("");
		}

		if (correctAnswer == -1) {
			errorLabel.setText("You must choose one correct answer");
			errorLabel.setStyle("-fx-text-fill: RED;");
		} else {
			errorLabel.setText("");
		}

		if (!details.isEmpty() && !answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty() && !answer4.isEmpty()
				&& correctAnswer != -1) {

			Question editedQuestion = new Question(questionID, teacherName, subject, details, answer1, answer2, answer3,
					answer4, correctAnswer, null);

			ModelWrapper<Question> modelWrapper = new ModelWrapper<>(editedQuestion, EDIT_QUESTION);
			ClientUI.getClientController().sendClientUIRequest(modelWrapper);
			errorLabel.setStyle("-fx-text-fill: GREEN;");
			errorLabel.setText(Client.getServerMessages());
		}

	}

	/**
	 * Set question data on init
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		taQD.setText(question.getDetails());
		taA1.setText(question.getAnswer1());
		taA2.setText(question.getAnswer2());
		taA3.setText(question.getAnswer3());
		taA4.setText(question.getAnswer4());
		cbCoorect.getItems().add(1);
		cbCoorect.getItems().add(2);
		cbCoorect.getItems().add(3);
		cbCoorect.getItems().add(4);
		int correctAnswer = question.getCorrectAnswer();
		cbCoorect.getSelectionModel().select(correctAnswer - 1);

	}

}
