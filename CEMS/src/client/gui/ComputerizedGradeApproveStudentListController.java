package client.gui;

import static common.ModelWrapper.Operation.GET_EXECUTED_EXAM_STUDENT_LIST;
import static common.ModelWrapper.Operation.SAVE_APPROVED_STUDENTS;
import static common.ModelWrapper.Operation.UPDATE_EXAM_STATISTIC;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.Client;
import client.ClientUI;
import common.ModelWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.ExecutedExam;
import models.StudentExecutedExam;

/**
 * ComputerizedGradeApproveStudentListController class hande the grade approval
 * screen of the Teacher. It shows the teacher the grade and alert value and let
 * the teacher change the grade and add comment for each student.
 */
public class ComputerizedGradeApproveStudentListController implements Initializable {

	@FXML
	private TableView<StudentExecutedExam> tvSutdents;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcExamId;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcStudent;

	@FXML
	private TableColumn<StudentExecutedExam, TextField> tcGrade;

	@FXML
	private TableColumn<StudentExecutedExam, String> tcCopyAlert;

	@FXML
	private TableColumn<StudentExecutedExam, TextField> tcComment;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXButton btnSave;

	@FXML
	private Label messageLabel;

	private static ExecutedExam executedExam;

	private static List<StudentExecutedExam> executedExamStudentList;

	public ComputerizedGradeApproveStudentListController() {
	}

	/**
	 * Constructor for ComputerizedGradeApproveStudentListController class
	 * 
	 * @param executedExam
	 */
	public ComputerizedGradeApproveStudentListController(ExecutedExam executedExam) {
		ComputerizedGradeApproveStudentListController.executedExam = executedExam;
	}

	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		try {
			Pane studentListPane = (Pane) FXMLLoader
					.load(getClass().getResource("ComputerizedGradeApprovalStudentList.fxml"));
			MainGuiController.getMenuHandler().getMainFrame().setCenter(studentListPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for back button: set the previous screen(setGradeApprovalScreen)
	 * 
	 * @param event
	 */
	@FXML
	void onClickBack(ActionEvent event) {
		MainGuiController.getMenuHandler().setGradeApprovalScreen();
	}

	/**
	 * Method for save button, save the data and submit to server
	 * 
	 * @param event
	 */
	@FXML
	void onClickSave(ActionEvent event) {

		if (executedExamStudentList.size() != 0) {

			setNewGradeChanges();

			double[] avgAndMedian = getAvarageAndMedian(executedExamStudentList);
			StudentExecutedExam sampleExecutedExamStudent = executedExamStudentList.get(0);
			String examID = sampleExecutedExamStudent.getExamID();
			String subject = sampleExecutedExamStudent.getSubject();
			String course = sampleExecutedExamStudent.getCourse();
			String teacherID = sampleExecutedExamStudent.getTeacherId();
			String execDate = sampleExecutedExamStudent.getExecDate();
			String testType = sampleExecutedExamStudent.getTestType();

			double avg = avgAndMedian[0];
			double median = avgAndMedian[1];

			ExecutedExam executedExam = new ExecutedExam(examID, subject, course, teacherID, execDate, testType, avg,
					median, true);

			ModelWrapper<ExecutedExam> modelWrapper1 = new ModelWrapper<>(executedExam, UPDATE_EXAM_STATISTIC);
			ClientUI.getClientController().sendClientUIRequest(modelWrapper1);
		}

		for (StudentExecutedExam executedExamStudent : executedExamStudentList) {
			String comment = executedExamStudent.getTfComment().getText();
			executedExamStudent.setComment(comment);
			executedExamStudent.setApproved(true);
			executedExamStudent.setGetCopy(null);
			executedExamStudent.setTfGrade(null);
			executedExamStudent.setTfComment(null);

		}

		ModelWrapper<StudentExecutedExam> modelWrapper2 = new ModelWrapper<>(executedExamStudentList,
				SAVE_APPROVED_STUDENTS);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper2);

		String serverMessage = Client.getServerMessages();
		messageLabel.setStyle("-fx-text-fill: GREEN;");
		messageLabel.setText(serverMessage);
	}

	/**
	 * Method that get the input grade value and set it in the object
	 */
	private void setNewGradeChanges() {
		for (StudentExecutedExam student : executedExamStudentList) {
			String newGrade = student.getTfGrade().getText();
			student.setGrade(newGrade);
		}
	}

	/**
	 * @return average and median
	 */
	public double[] getAvarageAndMedian(List<StudentExecutedExam> studentList) {
		List<Integer> studentsGrade = new ArrayList<>();
		int sum = 0;

		for (StudentExecutedExam student : studentList) {
			int studentGrade = Integer.valueOf(student.getGrade());
			studentsGrade.add(studentGrade);
			sum += studentGrade;
		}

		studentsGrade.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		if (studentsGrade.isEmpty()) {
			double[] avgAndMedian = { 0.0, 0.0 };
			return avgAndMedian;
		}

		double avg = sum / studentsGrade.size();
		double median;

		int n = studentsGrade.size();
		if (n % 2 == 0) {
			int firstStudentGrade = studentsGrade.get((n / 2) - 1);
			int secondStudentGrade = studentsGrade.get((n / 2));
			median = firstStudentGrade + secondStudentGrade;
			median /= 2;
		} else {
			median = studentsGrade.get(((n + 1) / 2) - 1);
		}
		double[] avgAndMedian = { avg, median };
		return avgAndMedian;
	}

	/**
	 * Set the table and insert data
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String examID = executedExam.getId();
		String date = executedExam.getExecDate();
		String teacherID = executedExam.getTeacherID();
		List<String> parameters = Arrays.asList(examID, date, teacherID);

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(parameters, GET_EXECUTED_EXAM_STUDENT_LIST);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		tcExamId.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("examID"));
		tcStudent.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("studentName"));
		tcGrade.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, TextField>("tfGrade"));
		tcCopyAlert.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, String>("alert"));
		tcComment.setCellValueFactory(new PropertyValueFactory<StudentExecutedExam, TextField>("tfComment"));

		ObservableList<StudentExecutedExam> executedExam = FXCollections.observableArrayList();
		executedExamStudentList = addApproveButton(Client.getExecutedExamStudentList());
		executedExam.addAll(executedExamStudentList);
		tvSutdents.setItems(executedExam);

	}

	/**
	 * Update student grade
	 * 
	 * @param executedExamStudentList list of student executed exams
	 * @return the list after update
	 */
	private List<StudentExecutedExam> addApproveButton(List<StudentExecutedExam> executedExamStudentList) {
		for (StudentExecutedExam executedStudentExam : executedExamStudentList) {
			TextField tfGrade = new TextField(executedStudentExam.getGrade());
			TextField tfComment = new TextField();
			executedStudentExam.setTfComment(tfComment);
			executedStudentExam.setTfGrade(tfGrade);
		}

		return executedExamStudentList;
	}

}
