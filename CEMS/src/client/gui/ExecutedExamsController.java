package client.gui;

import static common.ModelWrapper.Operation.EXAM_EXECUTE;
import static common.ModelWrapper.Operation.GET_QUESTION_LIST_BY_EXAM_ID;
import static common.ModelWrapper.Operation.GET_SELECTED_ANSWERS;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.Client;
import client.ClientController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.ComputerizedTestReport;
import models.StudentExecutedExam;

/**
 * This class is used as controller for Executed Exams screen.
 * The screen show the exectued exams by the student and his grades if the grade approved
 * All the details are inserted into the table and showed to the client
 */
public class ExecutedExamsController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<StudentExecutedExam> tvExExams;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcStudentID;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcExamID;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcSubject;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcCourse;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcExecDate;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcTestType;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcGrade;

	@FXML
	private TableColumn<StudentExecutedExam, JFXButton> tcGetTest;

	/**
	 * This method get the data and insert into the table
	 * Also it creates the details buttons which include StudentComputerizedTestReportController instance 
	 */
	public void initialize(URL location, ResourceBundle resources) {

		tcExamID.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("examID"));
		tcSubject.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("subject"));
		tcCourse.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("course"));
		tcExecDate.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("execDate"));
		tcTestType.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("testType"));
		tcGrade.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("grade"));
		tcGetTest.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, JFXButton>("getCopy"));

		String userID = ClientController.getClientUI().getUser().getUserID();

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(userID, EXAM_EXECUTE);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		if (!Client.getExecutedExamStudentList().isEmpty()) {
			ObservableList<StudentExecutedExam> exams = FXCollections.observableArrayList();
			List<StudentExecutedExam> studentList = addCopyButtons(Client.getExecutedExamStudentList());
			exams.addAll(studentList);
			tvExExams.setItems(exams);
		}

	}

	/**
	 * This method creates the list to insert into the table.
	 * It filters  the exams and return only the approved grades exams
	 * @param executedExamStudentList list of executued exams by student
	 * @return approved grades exams
	 */
	private List<StudentExecutedExam> addCopyButtons(List<StudentExecutedExam> executedExamStudentList) {
		List<StudentExecutedExam> approvedStudentList = new ArrayList<>();
		for (StudentExecutedExam studentExam : executedExamStudentList) {
			if (studentExam.isApproved()) {
				JFXButton getCopyButton = new JFXButton();
				getCopyButton.setPrefSize(90, 15);
				getCopyButton.setStyle(
						"-fx-background-color:#3399FF;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
				getCopyButton.setText("Get Results");
				getCopyButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						ModelWrapper<String> modelWrapper = new ModelWrapper<>(studentExam.getExamID(),
								GET_QUESTION_LIST_BY_EXAM_ID);
						ClientUI.getClientController().sendClientUIRequest(modelWrapper);

						List<String> elements = new ArrayList<>();
						elements.add(studentExam.getStudentID());
						elements.add(studentExam.getExamID());
						elements.add(studentExam.getExecDate());

						modelWrapper = new ModelWrapper<>(elements, GET_SELECTED_ANSWERS);
						ClientUI.getClientController().sendClientUIRequest(modelWrapper);

						List<ComputerizedTestReport> questionsReport = new ArrayList<>();
						int numOfQuestions = Client.getExamQuestions().size();
						if (Client.getSelectedAnswers() != null) {
							for (int i = 0; i < numOfQuestions; i++) {
								ComputerizedTestReport questionReport;
								if (Client.getSelectedAnswers().split("")[i].equals(
										Integer.toString(Client.getExamQuestions().get(i).getCorrectAnswer()))) {
									final ImageView imageview_correct = new ImageView(
											new Image(getClass().getResource("correct.png").toExternalForm()));
									imageview_correct.setFitHeight(30);
									imageview_correct.setFitWidth(30);
									questionReport = new ComputerizedTestReport(
											Client.getSelectedAnswers().split("")[i],
											Integer.toString(Client.getExamQuestions().get(i).getCorrectAnswer()),
											Integer.toString(Client.getExamQuestions().get(i).getPoints()),
											imageview_correct, Client.getExamQuestions().get(i));
								} else {
									final ImageView imageview_wrong = new ImageView(
											new Image(getClass().getResource("wrong.png").toExternalForm()));
									imageview_wrong.setFitHeight(30);
									imageview_wrong.setFitWidth(30);
									questionReport = new ComputerizedTestReport(
											Client.getSelectedAnswers().split("")[i],
											Integer.toString(Client.getExamQuestions().get(i).getCorrectAnswer()),
											Integer.toString(Client.getExamQuestions().get(i).getPoints()),
											imageview_wrong, Client.getExamQuestions().get(i));
								}
								questionsReport.add(questionReport);
							}
						}
						MainGuiController.getMenuHandler().setStudentComputerizedTestReportScreen(questionsReport,
								studentExam.getSubject(), studentExam.getCourse(), studentExam.getGrade(),
								studentExam.getComment());
					}

				});
				studentExam.setGetCopy(getCopyButton);
				approvedStudentList.add(studentExam);
			}
		}

		return approvedStudentList;
	}

}
