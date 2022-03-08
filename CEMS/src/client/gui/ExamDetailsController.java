package client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.Exam;
import models.ExamQuestion;

/**
 * ExamDetailsController class handle the screen of exam details that used in teacher and principal
 *
 */
public class ExamDetailsController implements Initializable {

	@FXML
	private TableView<ExamQuestion> tvQuestions;

	@FXML
	private TableColumn<ExamQuestion, String> tcID;

	@FXML
	private TableColumn<ExamQuestion, String> tcSubject;

	@FXML
	private TableColumn<ExamQuestion, String> tcTeacher;

	@FXML
	private TableColumn<ExamQuestion, Integer> tcPoints;

	@FXML
	private TableColumn<ExamQuestion, JFXButton> tcDetails;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXTabPane tpNote;

	@FXML
	private Tab tabTeacherNote;

	@FXML
	private Tab tabStudentNote;

	private static Exam exam;

	private static String backClassName;

	public ExamDetailsController() {

	}

	/**
	 * Constructor for ExamDetailsController
	 * @param exam
	 * @param backClassName
	 */
	public ExamDetailsController(Exam exam, String backClassName) {
		ExamDetailsController.exam = exam;
		ExamDetailsController.backClassName = backClassName;
	}

	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		try {
			Pane questionListPane = (Pane) FXMLLoader.load(getClass().getResource("ExamDetails.fxml"));
			MainGuiController.getMenuHandler().getMainFrame().setCenter(questionListPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for back button: set the previous screen
	 * @param event
	 */
	@FXML
	void onClickBack(ActionEvent event) {
		switch (backClassName) {
		case "ExamStatisticController":
			MainGuiController.getMenuHandler().setExamStatisticScreen();
			break;

		case "ComputerizedExamController":
			MainGuiController.getMenuHandler().setComputerizedScreen();
			break;

		case "TeacherExamPoolController":
			MainGuiController.getMenuHandler().setTeacherExamPoolScreen();
			break;
		}

	}

	/**
	 * Setting the table and insert data
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tcID.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("questionID"));
		tcSubject.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("subject"));
		tcTeacher.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("teacherName"));
		tcPoints.setCellValueFactory(new PropertyValueFactory<ExamQuestion, Integer>("points"));
		tcDetails.setCellValueFactory(new PropertyValueFactory<ExamQuestion, JFXButton>("detailsButton"));

		TextArea taTeacher = (TextArea) tabTeacherNote.getContent();
		taTeacher.setText(exam.getTeacherNote());

		TextArea taStudent = (TextArea) tabStudentNote.getContent();
		taStudent.setText(exam.getStudentNote());

		List<ExamQuestion> examQuestionList = exam.getExamQuestions();
		setQuestionDetailButtons(examQuestionList);

		ObservableList<ExamQuestion> examQuestions = FXCollections.observableArrayList();
		examQuestions.addAll(examQuestionList);
		tvQuestions.setItems(examQuestions);

	}

	/**
	 * Adding buttons to objects
	 * @param questions
	 */
	public void setQuestionDetailButtons(List<ExamQuestion> questions) {
		for (ExamQuestion question : questions) {
			JFXButton detailsButton = new JFXButton();
			detailsButton.setPrefSize(90, 15);
			detailsButton
					.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
			detailsButton.setText("Details");
			detailsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!QuestionDetailsController.isWindowOpend()) {
						QuestionDetailsController questionDetailsController = new QuestionDetailsController();
						QuestionDetailsController.setQuestion(question);
						questionDetailsController.start();
					}
				}

			});
			question.setDetailsButton(detailsButton);
		}
	}

}
