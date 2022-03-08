package client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

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

/**
 * QuestionDetailsController class handle screen with question details
 *
 */
public class QuestionDetailsController implements EventHandler<WindowEvent>, Initializable {

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
		QuestionDetailsController.question = question;
	}

	/**
	 * @return if the window is open or not
	 */
	public static boolean isWindowOpend() {
		return isWindowOpend;
	}

	/**
	 * set if the window is open or not
	 * @param isWindowOpend
	 */
	public static void setWindowOpend(boolean isWindowOpend) {
		QuestionDetailsController.isWindowOpend = isWindowOpend;
	}

	/**
	 * closing window on click exit
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
	 * opening the screen in javaFX
	 */
	public void start() {
		Stage stage = new Stage();
		AnchorPane mainPane;
		try {
			mainPane = (AnchorPane) FXMLLoader.load(getClass().getResource("QuestionDetails.fxml"));
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
	 * set data in labels
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		taQD.setText(question.getDetails());
		taQD.setEditable(false);
		taA1.setText(question.getAnswer1());
		taA1.setEditable(false);
		taA2.setText(question.getAnswer2());
		taA2.setEditable(false);
		taA3.setText(question.getAnswer3());
		taA3.setEditable(false);
		taA4.setText(question.getAnswer4());
		taA4.setEditable(false);
		labelCorrect.setText(question.getCorrectAnswer() + "");
	}

}
