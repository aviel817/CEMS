package client.gui;

import static common.ModelWrapper.Operation.GET_EXAM_BY_CODE;
import static common.ModelWrapper.Operation.GET_QUESTION_LIST_BY_CODE;
import static common.ModelWrapper.Operation.INSERT_FINISHED_STUDENT;
import static common.ModelWrapper.Operation.INSERT_STUDENT_TO_EXAM;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import models.Exam;
import models.ExamQuestion;
import models.StudentInExam;

/**
 * This class is used as controller for Execute Computerized Test screen. The
 * screen show the test to the student so he could answer the questions and in
 * the end sumbit the test and get grade. The test is limited by time which
 * showed on the screen. In addition, there is 2 min alert and locking of the
 * menu while doing the exam.
 */

public class ExecuteComputerizedExamController implements Initializable {

	@FXML
	private TableView<ExamQuestion> tvQuestions;

	@FXML
	private TableColumn<ExamQuestion, Integer> tcQuestionNumber;

	@FXML
	private TableColumn<ExamQuestion, Integer> tcQuestionPoints;

	@FXML
	private TableColumn<ExamQuestion, String> tcQuestionContent;

	@FXML
	private TableColumn<ExamQuestion, ImageView> tcFilled;

	@FXML
	private Label lblQuestions;

	@FXML
	private Label lblSelectedQuestion;

	@FXML
	private JFXButton btnSaveAnswer;

	@FXML
	private TextArea taSelectedQuestion;

	@FXML
	private JFXButton btnSubmitTest;

	@FXML
	private Label lblPossibleAnswers;

	@FXML
	private Label lblRemainingTime;

	@FXML
	private TextField tfRemainingTime;

	@FXML
	private JFXRadioButton radio1;

	@FXML
	private JFXRadioButton radio2;

	@FXML
	private JFXRadioButton radio3;

	@FXML
	private JFXRadioButton radio4;

	@FXML
	private ToggleGroup AnswersGroup;

	@FXML
	private TextField tfNote;

	@FXML
	private Label lblNote;

	@FXML
	private Label timeLabel;

	@FXML
	private Label lblTimeExtension;

	private StudentStopwatch sw;

	private Exam exam;

	private String[] answersArr;

	private Integer selectedRadio;

	private static String code;

	private volatile boolean shutdown = false;

	public ExecuteComputerizedExamController() {
	}

	/**
	 * Constructor for ExecuteComputerizedExamController class
	 */
	public ExecuteComputerizedExamController(String code) {
		ExecuteComputerizedExamController.code = code;

	}

	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		try {
			Pane computerizedTestPane = (Pane) FXMLLoader.load(getClass().getResource("ComputerizedTest.fxml"));
			MainGuiController.getMenuHandler().getMainFrame().setCenter(computerizedTestPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method get the questions and the exam details and insert it into the
	 * table, and then insert the student into the exam. It also set the timer,
	 * toggle listener and table click function.
	 */
	public void initialize(URL location, ResourceBundle resources) {

		StudentMenuController.setLocked(true);
		shutdown = false;
		// Get questions
		ModelWrapper<String> modelWrapperQuestionList = new ModelWrapper<>(code, GET_QUESTION_LIST_BY_CODE);
		ClientUI.getClientController().sendClientUIRequest(modelWrapperQuestionList);

		// Get exam
		ModelWrapper<String> modelWrapperEaxmDetails = new ModelWrapper<>(code, GET_EXAM_BY_CODE);
		ClientUI.getClientController().sendClientUIRequest(modelWrapperEaxmDetails);

		exam = Client.getExam();

		tfNote.setText(exam.getStudentNote());
		String userID = ClientController.getClientUI().getUser().getUserID();
		answersArr = new String[exam.getExamQuestions().size()];

		for (int i = 0; i < answersArr.length; i++)
			answersArr[i] = "9";

		StudentInExam newStudent = new StudentInExam(userID, code, "0", "0", answersArr);

		ModelWrapper<StudentInExam> modelWrapperInsertStudent = new ModelWrapper<>(newStudent, INSERT_STUDENT_TO_EXAM);

		ClientUI.getClientController().sendClientUIRequest(modelWrapperInsertStudent);

		String teacherTime = Client.getExamProcess().getTime();
		Date date = new Date();
		SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss");
		String currentTime = timeformat.format(date).toString();

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date1 = format.parse(currentTime);
			Date date2 = format.parse(teacherTime);
			long difference = date1.getTime() - date2.getTime();
			long examDuration = TimeUnit.MINUTES.toSeconds(Long.parseLong(Client.getExam().getDuration()));
			long durationInSecond = examDuration - TimeUnit.MILLISECONDS.toSeconds(difference);
			int minutes = (int) durationInSecond / 60;
			int second = (int) durationInSecond % 60;
			sw = new StudentStopwatch(minutes, second, timeLabel);
			sw.startTime();
			set2MinutesLeft();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Setting the table
		ObservableList<ExamQuestion> questions = FXCollections.observableArrayList();

		for (ExamQuestion q : exam.getExamQuestions()) {
			final ImageView imageview = new ImageView(new Image(getClass().getResource("check.jpeg").toExternalForm()));
			imageview.setFitHeight(30);
			imageview.setFitWidth(30);
			imageview.setVisible(false);
			q.setCheckImage(imageview);
		}

		questions.addAll(exam.getExamQuestions());
		tvQuestions.setItems((ObservableList<ExamQuestion>) questions);

		tcQuestionNumber
				.setCellValueFactory(new Callback<CellDataFeatures<ExamQuestion, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(CellDataFeatures<ExamQuestion, Integer> p) {
						return new ReadOnlyObjectWrapper(tvQuestions.getItems().indexOf(p.getValue()) + 1 + "");
					}
				});

		tcQuestionPoints.setCellValueFactory(new PropertyValueFactory<ExamQuestion, Integer>("points"));
		tcQuestionContent.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("details"));
		tcFilled.setCellValueFactory(new PropertyValueFactory<ExamQuestion, ImageView>("checkImage"));

		// Disable sort columns
		tcQuestionNumber.setSortable(false);
		tcQuestionPoints.setSortable(false);
		tcQuestionContent.setSortable(false);
		tcFilled.setSortable(false);

		// Adding listener to toggle group
		AnswersGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldToggle, Toggle newToggle) {
				selectedRadio = Character.getNumericValue((((JFXRadioButton) newToggle).getId().charAt(5)));
			}
		});

		// Setting start selection on first row
		tvQuestions.getSelectionModel().select(0);

		// Loading answers for first question
		onRowClick();

		// Loading answers on click
		tvQuestions.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() > 0) {
				onRowClick();
			}
		});

		taSelectedQuestion.setEditable(false);
		tfNote.setEditable(false);

	}

	/**
	 * Set the question information within row click from the table
	 */
	public void onRowClick() {
		// check the table's selected item and get selected item
		if (tvQuestions.getSelectionModel().getSelectedItem() != null) {
			ExamQuestion selectedRow = tvQuestions.getSelectionModel().getSelectedItem();
			int selectedRowIndex = tvQuestions.getSelectionModel().getSelectedIndex();
			taSelectedQuestion.setText(selectedRow.getDetails());
			Integer currentSelectedAnswer = null;
			if (answersArr[selectedRowIndex] != null)
				currentSelectedAnswer = Integer.parseInt(answersArr[selectedRowIndex]);
			if (currentSelectedAnswer != null) {
				switch (currentSelectedAnswer) {
				case 1:
					AnswersGroup.selectToggle(radio1);
					break;
				case 2:
					AnswersGroup.selectToggle(radio2);
					break;
				case 3:
					AnswersGroup.selectToggle(radio3);
					break;
				case 4:
					AnswersGroup.selectToggle(radio4);
					break;
				}
			}
			radio1.setText(selectedRow.getAnswer1());
			radio2.setText(selectedRow.getAnswer2());
			radio3.setText(selectedRow.getAnswer3());
			radio4.setText(selectedRow.getAnswer4());

		}
	}

	/**
	 * Save the student answer
	 * 
	 * @param event
	 */
	@FXML
	public void onSaveClick(ActionEvent event) {
		if (AnswersGroup.getSelectedToggle() != null) {
			ExamQuestion selectedRow = tvQuestions.getSelectionModel().getSelectedItem();
			int selectedQuestion = tvQuestions.getSelectionModel().getSelectedIndex();
			answersArr[selectedQuestion] = (String) selectedRadio.toString();
			int selectedRowIndex = tvQuestions.getSelectionModel().getSelectedIndex();
			tvQuestions.getSelectionModel().select(selectedRowIndex + 1);
			selectedRow.setVisibleImage();
			selectedRow = tvQuestions.getSelectionModel().getSelectedItem();
			radio1.setText(selectedRow.getAnswer1());
			radio2.setText(selectedRow.getAnswer2());
			radio3.setText(selectedRow.getAnswer3());
			radio4.setText(selectedRow.getAnswer4());
		}
	}

	/**
	 * Calculate the student grade by adding the correct answers points for every
	 * question
	 * 
	 * @param event
	 */
	@FXML
	void onClickSubmit(ActionEvent event) {
		StudentMenuController.setLocked(false);
		shutdown = true;
		sw.stopTime();
		Integer grade = 0;
		for (int i = 0; i < exam.getExamQuestions().size(); i++) {
			if (answersArr[i] != null) {
				if (Integer.parseInt(answersArr[i]) == exam.getExamQuestions().get(i).getCorrectAnswer()) {
					grade += exam.getExamQuestions().get(i).getPoints();
				}
			}
		}

		String userID = ClientController.getClientUI().getUser().getUserID();
		String finalGrade = grade.toString();
		int examDuration = Integer.parseInt(Client.getExam().getDuration());
		int minutes = sw.getMin();
		int execDuration = examDuration - minutes;
		StudentInExam finishedStudent = new StudentInExam(userID, code, finalGrade, String.valueOf(execDuration),
				answersArr);
		ModelWrapper<StudentInExam> modelWrapper = new ModelWrapper<>(finishedStudent, INSERT_FINISHED_STUDENT);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);
		MainGuiController.getMenuHandler().setStudentlMenu();

	}

	/**
	 * Set a alert for a student within 2 minutes left into the exam
	 */
	public void set2MinutesLeft() {
		Thread timerThread = new Thread(() -> {
			while (!shutdown) {
				if (sw.getMin() == 2 && sw.getSec() == 0) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// Creating a dialog
							Dialog<String> dialog = new Dialog<String>();
							// Setting the title
							dialog.setTitle("Warning");
							ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
							// Setting the content of the dialog
							dialog.setContentText("Warning: You have 2 minutes left!");
							// Adding buttons to the dialog pane
							dialog.getDialogPane().getButtonTypes().add(type);

							dialog.showAndWait();
						}
					});

					break;
				}
			}
		});
		timerThread.start();
	}

	/**
	 * set an dialog for an exam that has been frozen by the teacher
	 */
	public void setFreezePopup() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				MainGuiController.getMenuHandler().setMainScreen();
				// Creating a dialog
				Dialog<String> dialog = new Dialog<String>();
				// Setting the title
				dialog.setTitle("Exam closed");
				ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
				// Setting the content of the dialog
				dialog.setContentText("The exam has been closed by your teacher");
				// Adding buttons to the dialog pane
				dialog.getDialogPane().getButtonTypes().add(type);

				dialog.showAndWait();
			}
		});

	}

	/**
	 * class that define a stop watch for a student into the exam
	 */
	public class StudentStopwatch {
		private int min;
		private int sec;
		private Timer timer;
		private Label label;

		/**
		 * Constructor for StudentStopwatch class
		 * 
		 * @param min
		 * @param sec
		 * @param label
		 */
		public StudentStopwatch(int min, int sec, Label label) {
			this.min = min;
			this.sec = sec;
			this.label = label;
		}

		/**
		 * Method for starting the timer thread
		 */
		public void startTime() {
			int delay = 1000;
			int period = 1000;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {

				public void run() {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							long timeExtension = Client.getTimeExtension();
							if (StudentMenuController.isClosed()) {
								StudentMenuController.setLocked(false);
								timer.cancel();
								shutdown = true;
								StudentMenuController.setClosed(false);
								return;
							}

							if (timeExtension == -1) {
								setFreezePopup();
								shutdown = true;
								timer.cancel();
								Client.setTimeExtension(0);
								StudentMenuController.setLocked(false);
								StudentMenuController.setClosed(true);
								return;
							} else if (timeExtension != 0) {
								min += (int) timeExtension;
								lblTimeExtension.setText("*Time extended by " + timeExtension);
								Client.setTimeExtension(0);
							}

							label.setText(String.format("%02d:%02d\n", min, sec));

							if (min == 0 && sec == 0) {
								timer.cancel();
								shutdown = true;
								MainGuiController.getMenuHandler().setMainScreen();
								StudentMenuController.setClosed(true);

							} else if (sec == 0) {
								min--;
								sec = 59;
							} else {
								sec--;
							}
						}
					});
				}
			}, delay, period);
		}

		/**
		 * @return Timer minutes
		 */
		public int getMin() {
			return min;
		}

		/**
		 * @return Timer seconds
		 */
		public int getSec() {
			return sec;
		}

		/**
		 * Method for stopping the timer
		 */
		public void stopTime() {
			timer.cancel();
		}

	}

}
