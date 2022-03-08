package client.gui;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.ComputerizedTestReport;
import models.Exam;
import models.ExecutedExam;

/**
 * The MenuHandler class handle the JavaFX load of screens and set the screen
 * according to the function.
 *
 */
public class MenuHandler {

	private BorderPane mainFrame;

	public MenuHandler(BorderPane mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void setLoginMenu() {
		try {
			Pane loginMenuPane = (Pane) FXMLLoader.load(getClass().getResource("LoginMenu.fxml"));
			Pane loginLogoPane = (Pane) FXMLLoader.load(getClass().getResource("LoginLogo.fxml"));
			mainFrame.setLeft(loginMenuPane);
			mainFrame.setCenter(loginLogoPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTeacherMenu() {
		try {
			Pane teacherMenuPane = (Pane) FXMLLoader.load(getClass().getResource("TeacherMenu.fxml"));
			Pane teacherHomePage = (Pane) FXMLLoader.load(getClass().getResource("TeacherLoginHomePage.fxml"));
			mainFrame.setLeft(teacherMenuPane);
			mainFrame.setCenter(teacherHomePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPrincipalMenu() {
		try {
			Pane principalMenuPane = (Pane) FXMLLoader.load(getClass().getResource("PrincipalMenu.fxml"));
			Pane principalHomePage = (Pane) FXMLLoader.load(getClass().getResource("PrincipalHomePage.fxml"));

			mainFrame.setLeft(principalMenuPane);
			mainFrame.setCenter(principalHomePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStudentlMenu() {
		try {
			Pane sutdentMenuPane = (Pane) FXMLLoader.load(getClass().getResource("StudentMenu.fxml"));
			Pane studentHomePage = (Pane) FXMLLoader.load(getClass().getResource("StudentHomePage.fxml"));
			mainFrame.setLeft(sutdentMenuPane);
			mainFrame.setCenter(studentHomePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCreateExamScreen() {
		try {
			Pane createExamPane = (Pane) FXMLLoader.load(getClass().getResource("CreateExam.fxml"));
			mainFrame.setCenter(createExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCreateQuestionScreen() {
		try {
			Pane createQuestionPane = (Pane) FXMLLoader.load(getClass().getResource("CreateQuestion.fxml"));
			mainFrame.setCenter(createQuestionPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStartExamScreen() {
		try {
			Pane startExamPane = (Pane) FXMLLoader.load(getClass().getResource("StartExam.fxml"));
			mainFrame.setCenter(startExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setComputerizedScreen() {
		try {
			Pane computerizedPane = (Pane) FXMLLoader.load(getClass().getResource("ComputerizedExam.fxml"));
			mainFrame.setCenter(computerizedPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setManualScreen() {
		try {
			Pane manualPane = (Pane) FXMLLoader.load(getClass().getResource("ManualExam.fxml"));
			mainFrame.setCenter(manualPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExamStatisticScreen() {
		try {
			Pane examStatisticPane = (Pane) FXMLLoader.load(getClass().getResource("ExamStatistic.fxml"));
			mainFrame.setCenter(examStatisticPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setGradeApprovalScreen() {
		try {
			Pane gradeApprovalPane = (Pane) FXMLLoader.load(getClass().getResource("GradeApproval.fxml"));
			mainFrame.setCenter(gradeApprovalPane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExecutedExamsScreen() {
		try {
			Pane executedExamsPane = (Pane) FXMLLoader.load(getClass().getResource("ExecutedExams.fxml"));
			mainFrame.setCenter(executedExamsPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStudentListsScreen(ExecutedExam executedExam) {
		ComputerizedGradeApproveStudentListController studentListController = new ComputerizedGradeApproveStudentListController(
				executedExam);
		studentListController.start();
	}

	public void setManualTestScreen(String code) {
		ExecuteManualExamController studentExecuteManualExam = new ExecuteManualExamController(code);
		studentExecuteManualExam.start();

	}

	public void setComputerizedTestScreen(String code) {
		ExecuteComputerizedExamController studentExecuteComputerizedExam = new ExecuteComputerizedExamController(code);
		studentExecuteComputerizedExam.start();
	}

	public void setEnterExamScreen() {
		try {
			Pane enterExamPane = (Pane) FXMLLoader.load(getClass().getResource("EnterExam.fxml"));
			mainFrame.setCenter(enterExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExamPoolScreen() {
		try {
			Pane examPoolPane = (Pane) FXMLLoader.load(getClass().getResource("ExamPool.fxml"));
			mainFrame.setCenter(examPoolPane);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setQuestionPoolScreen() {
		try {
			Pane questionPoolPane = (Pane) FXMLLoader.load(getClass().getResource("QuestionPool.fxml"));
			mainFrame.setCenter(questionPoolPane);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setCreateExamSucceeded() {
		try {
			Pane examCreateSucceeded = (Pane) FXMLLoader.load(getClass().getResource("ExamCreateSucceeded.fxml"));
			mainFrame.setCenter(examCreateSucceeded);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setStudentComputerizedTestReportScreen(List<ComputerizedTestReport> exam, String subject, String course,
			String grade, String comment) {
		StudentComputerizedTestReportController studentComputerizedTestReportController = new StudentComputerizedTestReportController(
				exam, subject, course, grade, comment);
		studentComputerizedTestReportController.start();
	}

	public void setOverallStatisticsScreen() {
		try {
			Pane overallStatisticsPane = (Pane) FXMLLoader.load(getClass().getResource("OverallStatistics.fxml"));
			mainFrame.setCenter(overallStatisticsPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTimeExtensionRequestsScreen() {
		try {
			Pane timeExtensionRequestsPane = (Pane) FXMLLoader.load(getClass().getResource("TimeExtension.fxml"));
			mainFrame.setCenter(timeExtensionRequestsPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMainScreen() {
		try {
			Pane mainPane = (Pane) FXMLLoader.load(getClass().getResource("LoginLogo.fxml"));
			mainFrame.setCenter(mainPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExamDetailstScreen(Exam exam, String backClassName) {
		ExamDetailsController examDetailsController = new ExamDetailsController(exam, backClassName);
		examDetailsController.start();
	}

	public BorderPane getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(BorderPane mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void setEditQuestionScreen() {
		try {
			Pane enterExamPane = (Pane) FXMLLoader.load(getClass().getResource("EditQuestion.fxml"));
			mainFrame.setCenter(enterExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTeacherQuestionPoolScreen() {
		try {
			Pane enterExamPane = (Pane) FXMLLoader.load(getClass().getResource("TeacherQuestionPool.fxml"));
			mainFrame.setCenter(enterExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTeacherExamPoolScreen() {
		try {
			Pane enterExamPane = (Pane) FXMLLoader.load(getClass().getResource("TeacherExamPool.fxml"));
			mainFrame.setCenter(enterExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setEditExamScreen(Exam exam) {
		EditExamController editExamController = new EditExamController(exam);
		editExamController.start();
		
		
	}

}
