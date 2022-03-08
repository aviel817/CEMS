package client.gui;

import static common.ModelWrapper.Operation.CREATE_QUESTION;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.Question;
import models.User;

/**
 * CreateQuestionController class handle create question by teacher screen It
 * allows the teacher to add the question, the 4 optional answers and the right
 * answer In addition, the teacher need to choose subject.
 */
public class CreateQuestionController implements Initializable {

	@FXML
	private JFXComboBox<String> cbSubject;

	@FXML
	private JFXComboBox<Integer> cbCorrectAnswer;

	@FXML
	private TextArea taQuestion;

	@FXML
	private JFXButton btnCreateQuestion;

	@FXML
	private TextArea taAnswer1;

	@FXML
	private TextArea taAnswer2;

	@FXML
	private TextArea taAnswer3;

	@FXML
	private TextArea taAnswer4;

	@FXML
	private Label subjectError;

	@FXML
	private Label descriptionError;

	@FXML
	private Label answer1Error;

	@FXML
	private Label answer2Error;

	@FXML
	private Label answer3Error;

	@FXML
	private Label answer4Error;

	@FXML
	private Label correctAnswerError;

	@FXML
	private Label message;

	@FXML
	private JFXButton btnBack;

	@FXML
	void cbCorrect(ActionEvent event) {

	}

	/**
	 * Method for create question button, checking the input and if success sending
	 * add question request to the server
	 * 
	 * @param event
	 */
	@FXML
	void onClickCreateQuestion(ActionEvent event) {
		
		
		User teacherUser = ClientController.getClientUI().getUser();
		String details = taQuestion.getText();
		String answer1 = taAnswer1.getText();
		String answer2 = taAnswer2.getText();
		String answer3 = taAnswer3.getText();
		String answer4 = taAnswer4.getText();
		String teacherName = teacherUser.getFirstName() + " " + teacherUser.getLastName();
		String subject;

		if (cbSubject.getSelectionModel().getSelectedItem() == null)
			subject = "";
		else
			subject = cbSubject.getSelectionModel().getSelectedItem();

		int correctAnswer;

		if (cbCorrectAnswer.getSelectionModel().getSelectedItem() == null)
			correctAnswer = -1;
		else
			correctAnswer = cbCorrectAnswer.getSelectionModel().getSelectedItem();

		if (subject.isEmpty()) {
			subjectError.setText("You must choose subject from subject list");
		} else {
			subjectError.setText("");
		}

		if (details.isEmpty()) {
			descriptionError.setText("You must write question description");
		} else {
			descriptionError.setText("");
		}

		if (answer1.isEmpty()) {
			answer1Error.setText("answer can't be empty");
		} else {
			answer1Error.setText("");
		}

		if (answer2.isEmpty()) {
			answer2Error.setText("answer can't be empty");
		} else {
			answer2Error.setText("");
		}

		if (answer3.isEmpty()) {
			answer3Error.setText("answer can't be empty");
		} else {
			answer3Error.setText("");
		}

		if (answer4.isEmpty()) {
			answer4Error.setText("answer can't be empty");
		} else {
			answer4Error.setText("");
		}

		if (correctAnswer == -1) {
			message.setText("You must choose one correct answer");
			message.setStyle("-fx-text-fill: RED;");
		} else {
			message.setText("");
		}

		if (!details.isEmpty() && !answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty() && !answer4.isEmpty()
				&& !subject.isEmpty() && correctAnswer != -1) {
			Question question = new Question(teacherName, subject, details, answer1, answer2, answer3, answer4,
					correctAnswer);
			ModelWrapper<Question> modelWrapper = new ModelWrapper<>(question, CREATE_QUESTION);
			ClientUI.getClientController().sendClientUIRequest(modelWrapper);
			message.setStyle("-fx-text-fill: GREEN;");
			message.setText(Client.getServerMessages());
		}

	}

	@FXML
	void onClickBack(ActionEvent event) {
		MainGuiController.getMenuHandler().setTeacherQuestionPoolScreen();
	}

	/**
	 * Adding 4 options combo box on initialize
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbSubject.getItems().addAll(Client.getSubjectCollection().getSubjects());
		cbCorrectAnswer.getItems().add(1);
		cbCorrectAnswer.getItems().add(2);
		cbCorrectAnswer.getItems().add(3);
		cbCorrectAnswer.getItems().add(4);
	}

}
