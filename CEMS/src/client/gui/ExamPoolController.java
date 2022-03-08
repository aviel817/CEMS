package client.gui;

import static common.ModelWrapper.Operation.GET_EXAMS_LIST;
import static common.ModelWrapper.Operation.GET_EXAMS_LIST_BY_COURSE;
import static common.ModelWrapper.Operation.GET_EXAMS_LIST_BY_SUBJECT;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.Client;
import client.ClientUI;
import common.ModelWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Exam;

/**
 * ExamPoolController class handle exam pool screen in teacher menu
 *
 */
public class ExamPoolController implements Initializable {

	@FXML
	private JFXComboBox<String> cbExamCourse;

	@FXML
	private JFXComboBox<String> cbExamSubject;

	@FXML
	private TableView<Exam> tvExamPool;

	@FXML
	private TableColumn<Exam, String> tcID;

	@FXML
	private TableColumn<Exam, String> tcTeacher;

	@FXML
	private TableColumn<Exam, String> tcSubject;

	@FXML
	private TableColumn<Exam, String> tcCourse;

	@FXML
	private TableColumn<Exam, String> tcDuration;

	@FXML
	private TableColumn<Exam, JFXButton> tcDetails;

	/**
	 * Filter by subject
	 * @param event
	 */
	@FXML
	void onClickExamSubject(ActionEvent event) {
		String subjectSelected = cbExamSubject.getSelectionModel().getSelectedItem();
		cbExamCourse.getItems().clear();
		cbExamCourse.getItems().addAll(Client.getSubjectCollection().getCourseListBySubject(subjectSelected));

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(subjectSelected, GET_EXAMS_LIST_BY_SUBJECT);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		ObservableList<Exam> exams = FXCollections.observableArrayList();
		exams.addAll(Client.getExams());
		tvExamPool.setItems(exams);
		setExamQuestioListButtons(Client.getExams());

	}

	/**
	 * Adding buttons to objects
	 * @param exams
	 * @return list of Exam with buttons
	 */
	private List<Exam> setExamQuestioListButtons(List<Exam> exams) {

		for (Exam exam : exams) {
			JFXButton examDetailsButton = new JFXButton();
			examDetailsButton.setPrefSize(90, 15);
			examDetailsButton
					.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
			examDetailsButton.setText("Details");
			examDetailsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					MainGuiController.getMenuHandler().setExamDetailstScreen(exam, "ComputerizedExamController");
				}

			});

			exam.setQuestionListButton(examDetailsButton);
		}
		return exams;
	}

	/**
	 * Filter by course
	 * @param event
	 */
	@FXML
	void onClickExamCourse(ActionEvent event) {
		String courseSlected = cbExamCourse.getSelectionModel().getSelectedItem();

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(courseSlected, GET_EXAMS_LIST_BY_COURSE);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		ObservableList<Exam> exams = FXCollections.observableArrayList();
		exams.addAll(Client.getExams());
		tvExamPool.setItems(exams);
		setExamQuestioListButtons(Client.getExams());

	}

	/**
	 * Setting all table column and creating new data set from client request that
	 * has been sent to database.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbExamSubject.getItems().addAll(Client.getSubjectCollection().getSubjects());

		tcID.setCellValueFactory(new PropertyValueFactory<Exam, String>("id"));
		tcTeacher.setCellValueFactory(new PropertyValueFactory<Exam, String>("teacherName"));
		tcSubject.setCellValueFactory(new PropertyValueFactory<Exam, String>("subject"));
		tcCourse.setCellValueFactory(new PropertyValueFactory<Exam, String>("course"));
		tcDuration.setCellValueFactory(new PropertyValueFactory<Exam, String>("duration"));
		tcDetails.setCellValueFactory(new PropertyValueFactory<Exam, JFXButton>("questionListButton"));

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(GET_EXAMS_LIST);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);
		ObservableList<Exam> exams = FXCollections.observableArrayList();
		List<Exam> examList = Client.getExams();
		examList = setExamQuestioListButtons(examList);
		exams.addAll(examList);
		tvExamPool.setItems(exams);
		tvExamPool.getSelectionModel().select(0);
	}

}
