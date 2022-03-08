package client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import models.ComputerizedTestReport;


/**
 * This class is used as controller for student computerized test report screen.
 * The screen show the details of the selected test from the previous screen:
 * Show selected answer with the correct one, points, teacher comment and final grade.
 */
public class StudentComputerizedTestReportController implements Initializable {

	@FXML
	private TableView<ComputerizedTestReport> tvQuestions;

	@FXML
	private TableColumn<ComputerizedTestReport, Integer> tcQuestionNumber;

	@FXML
	private TableColumn<ComputerizedTestReport, String> tcSelectedAnswer;

	@FXML
	private TableColumn<ComputerizedTestReport, String> tcCorrectAnswer;

	@FXML
	private TableColumn<ComputerizedTestReport, String> tcQuestionPoints;

	@FXML
	private TableColumn<ComputerizedTestReport, ImageView> tcCorrect;

	@FXML
	private TableColumn<ComputerizedTestReport, JFXButton> tcDetails;

	@FXML
	private Label lblQuestions;

	@FXML
	private Label lblSelectedQuestion;

	@FXML
	private TextArea taSelectedQuestion;

	@FXML
	private Label lblPossibleAnswers1;

	@FXML
	private JFXRadioButton radio1;

	@FXML
	private ToggleGroup AnswersGroup;

	@FXML
	private JFXRadioButton radio2;

	@FXML
	private JFXRadioButton radio3;

	@FXML
	private JFXRadioButton radio4;

	@FXML
	private TextField tfComment;

	@FXML
	private Label lblNote;

	@FXML
	private TextField lbl_Course;

	@FXML
	private Label lblPossibleAnswers11;

	@FXML
	private Label lbl_grade;

	@FXML
	private TextField lbl_Subject;

	@FXML
	private JFXButton btnBack;

	private static List<ComputerizedTestReport> exam;

	private static String subject = "";

	private static String course = "";

	private static String grade;

	private static String comment;

	public StudentComputerizedTestReportController() {
	}

	/**
	 * Constructor for StudentComputerizedTestReportController class
	 * @param exam
	 * @param subject
	 * @param course
	 * @param grade
	 * @param comment
	 */
	public StudentComputerizedTestReportController(List<ComputerizedTestReport> exam, String subject, String course,
			String grade, String comment) {
		StudentComputerizedTestReportController.exam = exam;
		StudentComputerizedTestReportController.subject = subject;
		StudentComputerizedTestReportController.course = course;
		StudentComputerizedTestReportController.grade = grade;
		StudentComputerizedTestReportController.comment = comment;
	}

	
	
	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		try {
			Pane studentComputerizedTestReport = (Pane) FXMLLoader
					.load(getClass().getResource("StudentComputerizedTestReport.fxml"));
			MainGuiController.getMenuHandler().getMainFrame().setCenter(studentComputerizedTestReport);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * This method load the table and set the data
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		set_labels();

		// Setting the table
		ObservableList<ComputerizedTestReport> questions = FXCollections.observableArrayList();

		questions.addAll(exam);
		tvQuestions.setItems((ObservableList<ComputerizedTestReport>) questions);

		tcQuestionNumber.setCellValueFactory(
				new Callback<CellDataFeatures<ComputerizedTestReport, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(CellDataFeatures<ComputerizedTestReport, Integer> p) {
						return new ReadOnlyObjectWrapper(tvQuestions.getItems().indexOf(p.getValue()) + 1 + "");
					}
				});
		tcSelectedAnswer
				.setCellValueFactory(new PropertyValueFactory<ComputerizedTestReport, String>("selectedAnswer"));
		tcCorrectAnswer.setCellValueFactory(new PropertyValueFactory<ComputerizedTestReport, String>("correctAnswer"));
		tcQuestionPoints.setCellValueFactory(new PropertyValueFactory<ComputerizedTestReport, String>("points"));
		tcCorrect.setCellValueFactory(new PropertyValueFactory<ComputerizedTestReport, ImageView>("correctImg"));

		for (ComputerizedTestReport report : exam) {
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
						QuestionDetailsController.setQuestion(report.getQuestion());
						questionDetailsController.start();
					}
				}

			});

			report.setDetailsBtn(detailsButton);
		}

		tcDetails.setCellValueFactory(new PropertyValueFactory<ComputerizedTestReport, JFXButton>("detailsBtn"));

	}

	/**
	 * This method set the labels
	 */
	public void set_labels() {
		lbl_Subject.setText(subject);
		lbl_Course.setText(course);
		lbl_grade.setText(grade);
		tfComment.setText(comment);
	}

	/**
	 * Method for back button: set the previous screen(ExecutedExams)
	 * @param event
	 */
	@FXML
	void onClickBack(ActionEvent event) {
		MainGuiController.getMenuHandler().setExecutedExamsScreen();
	}
}
