package client.gui;

import static common.ModelWrapper.Operation.GET_EXAM_BY_EXAM_ID;
import static common.ModelWrapper.Operation.GET_EXECUTED_EXAM_LIST_BY_CREATOR;
import static common.ModelWrapper.Operation.GET_EXECUTED_EXAM_STUDENT_LIST;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Exam;
import models.ExecutedExam;
import models.StudentExecutedExam;

/**
 * ExamStatisticController class handle exam statistic screen in the teacher menu
 *
 */
public class ExamStatisticController implements Initializable {

	@FXML
	private JFXComboBox<String> cbExamCourse;

	@FXML
	private JFXComboBox<String> cbExamSubject;

	@FXML
	private TableView<ExecutedExam> tvExecutedExams;

	@FXML
	private TableColumn<ExecutedExam, String> tcID;

	@FXML
	private TableColumn<ExecutedExam, String> tcSubject;

	@FXML
	private TableColumn<ExecutedExam, String> tcCourse;

	@FXML
	private TableColumn<ExecutedExam, String> tcDate;

	@FXML
	private TableColumn<ExecutedExam, JFXButton> tcDetails;

	@FXML
	private TableColumn<ExecutedExam, String> tcTeacher;

	@FXML
	private Label avgLabel;

	@FXML
	private Label medLabel;

	@FXML
	private Label labelTotalStudents;

	@FXML
	private Label labelFinishedStudents;

	@FXML
	private Label labelUnfinishedStudents;

	@FXML
	private BarChart<String, Integer> bcExamStatistic;

	@FXML
	void onClickExamCourse(ActionEvent event) {

	}

	@FXML
	void onClickExamSubject(ActionEvent event) {

	}

	/**
	 * Setting the table and insert data
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String loggendInTeacherID = ClientController.getClientUI().getUser().getUserID();
		ModelWrapper<String> modelWrapperExecutedExams = new ModelWrapper<>(loggendInTeacherID,
				GET_EXECUTED_EXAM_LIST_BY_CREATOR);
		ClientUI.getClientController().sendClientUIRequest(modelWrapperExecutedExams);

		tcID.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("id"));
		tcTeacher.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("executorTeacherName"));
		tcSubject.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("subject"));
		tcCourse.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("course"));
		tcDate.setCellValueFactory(new PropertyValueFactory<ExecutedExam, String>("execDate"));
		tcDetails.setCellValueFactory(new PropertyValueFactory<ExecutedExam, JFXButton>("questionList"));

		ObservableList<ExecutedExam> executedExam = FXCollections.observableArrayList();
		List<ExecutedExam> executedExamList = setExecutedExamsListUI(Client.getExecExams());
		executedExam.addAll(executedExamList);
		tvExecutedExams.setItems(executedExam);

		tvExecutedExams.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						bcExamStatistic.setAnimated(false);
						bcExamStatistic.getData().clear();

						String examID = newSelection.getId();
						String date = newSelection.getExecDate();
						String teacherID = newSelection.getTeacherID();
						String finishedStudentCount = newSelection.getFinishedStudentsCount();
						List<String> parameters = Arrays.asList(examID, date, teacherID);
						String avg = String.valueOf(newSelection.getAvg());
						String median = String.valueOf(newSelection.getMedian());
						avgLabel.setText(avg);
						medLabel.setText(median);
						ModelWrapper<String> modelWrapper = new ModelWrapper<>(parameters,
								GET_EXECUTED_EXAM_STUDENT_LIST);
						ClientUI.getClientController().sendClientUIRequest(modelWrapper);
						List<StudentExecutedExam> studentList = Client.getExecutedExamStudentList();

						XYChart.Series<String, Integer> newStats = new XYChart.Series<>();
						newStats.setName(newSelection.getSubject() + " " + newSelection.getCourse());

						for (StudentExecutedExam sutdent : studentList) {
							newStats.getData().add(
									new XYChart.Data<>(sutdent.getStudentName(), Integer.parseInt(sutdent.getGrade())));
						}

						String numOfStudents = String.valueOf(studentList.size());
						labelTotalStudents.setText(numOfStudents);
						labelFinishedStudents.setText(finishedStudentCount);
						int totalStudentCount = studentList.size();
						int finishedStudentSize = Integer.valueOf(finishedStudentCount);
						int unfinishedStudentCount = totalStudentCount - finishedStudentSize;
						labelUnfinishedStudents.setText(String.valueOf(unfinishedStudentCount));

						bcExamStatistic.getData().add(newStats);
					}
				});
			}
		});
	}

	/**
	 * Adding buttons to objects
	 * @param executedExamsList
	 * @return list of ExecutedExam with buttons
	 */
	private List<ExecutedExam> setExecutedExamsListUI(List<ExecutedExam> executedExamsList) {
		List<ExecutedExam> approvedExams = new ArrayList<>();
		for (ExecutedExam executedExam : executedExamsList) {
			if (executedExam.isApproved()) {
				JFXButton questionListButton = new JFXButton();
				questionListButton.setPrefSize(90, 15);
				questionListButton.setStyle(
						"-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
				questionListButton.setText("Details");
				questionListButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						String examID = executedExam.getId();
						ModelWrapper<String> modelWrapper = new ModelWrapper<>(examID, GET_EXAM_BY_EXAM_ID);
						ClientUI.getClientController().sendClientUIRequest(modelWrapper);

						Exam exam = Client.getExam();

						MainGuiController.getMenuHandler().setExamDetailstScreen(exam, "ExamStatisticController");

					}
				});
				executedExam.setQuestionList(questionListButton);
				approvedExams.add(executedExam);
			}

		}

		return approvedExams;
	}

}
