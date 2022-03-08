package client.gui;

import static common.ModelWrapper.Operation.CLOSE_EXAM;
import static common.ModelWrapper.Operation.EXTENSION_REQUEST;

import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.ExamExtension;
import models.ExamProcess;

/**
 * ExamManagementWindow class handle exam management screen for a teacher that
 * started an exam.
 *
 */
public class ExamManagementWindow {

	private Stopwatch sw;
	private JFXButton freezeExam;
	private JFXButton askForExstension;
	private JFXButton teacherNoteButton;
	private TextArea taCause;
	private Label codeLabel;
	private Label timerLabel;
	private HBox requestSection;
	private boolean requestFlag;
	private boolean isClosed = false;
	private static ExamProcess examProcess;

	public ExamManagementWindow(ExamProcess examProcess) {
		ExamManagementWindow.examProcess = examProcess;
	}

	/**
	 * open the window
	 */
	public void open() {
		try {
			VBox examManagement = new VBox();
			setVBoxComponents(examManagement);
			Scene scene = new Scene(examManagement, 600, 600);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Exam Management");
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					stopExam();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Setting components of the screen
	 * 
	 * @param examManagement
	 */
	private void setVBoxComponents(VBox examManagement) {
		String duration = examProcess.getDuration();
		String code = examProcess.getCode();
		timerLabel = new Label(String.format("%02d:%02d\n", Integer.valueOf(duration), 0));
		timerLabel.setFont(new Font("Agency FB", 100));
		timerLabel.setPrefSize(600, 128);
		timerLabel.setStyle("-fx-background-color:#333333;" + "-fx-text-fill:white;");
		timerLabel.setAlignment(Pos.CENTER);

		codeLabel = new Label("Exam code: " + code);
		codeLabel.setFont(new Font(30));
		codeLabel.setStyle("-fx-text-fill:#333333;");

		examManagement.setAlignment(Pos.CENTER);
		examManagement.setSpacing(10);
		freezeExam = new JFXButton();
		freezeExam.setPrefSize(200, 30);
		freezeExam.setStyle("-fx-background-color:#48a832;" + "-fx-background-radius:10;" + "-fx-text-fill:white;"
				+ "-jfx-disable-visual-focus: true;");
		freezeExam.setText("Freeze exam");
		freezeExam.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				stopExam();

				if (requestFlag)
					requestSection.setVisible(false);

			}

		});

		askForExstension = new JFXButton();
		askForExstension.setPrefSize(200, 30);
		askForExstension
				.setStyle("-fx-background-color:#48a832;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
		askForExstension.setText("Ask for time extension");
		askForExstension.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!requestFlag) {
					requestSection.setVisible(true);
					taCause.setVisible(true);
				} else {
					requestSection.setVisible(false);
					taCause.setVisible(false);
				}
				requestFlag = !requestFlag;
			}

		});

		teacherNoteButton = new JFXButton();
		teacherNoteButton.setPrefSize(200, 30);
		teacherNoteButton
				.setStyle("-fx-background-color:#48a832;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
		teacherNoteButton.setText("Teacher's note");
		teacherNoteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String note = examProcess.getTeacherNote();

				try {
					VBox noteWindow = new VBox();
					TextArea taNote = new TextArea(note);
					noteWindow.getChildren().add(taNote);
					Scene scene = new Scene(noteWindow, 300, 300);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Teacher note");
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		taCause = new TextArea();
		VBox.setVgrow(taCause, Priority.NEVER);
		taCause.setMaxSize(250, 125);
		taCause.setVisible(false);
		taCause.setPromptText("Cause");

		JFXTextField tfTime = new JFXTextField();
		tfTime.setPromptText("time");
		JFXButton sendRequest = new JFXButton("Send");

		sendRequest.setPrefSize(100, 30);
		sendRequest.setStyle("-fx-background-color:#48a832;" + "-fx-background-radius:10;" + "-fx-text-fill:white;"
				+ "-jfx-disable-visual-focus: true;");
		sendRequest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String examID = examProcess.getExamId();
				String code = examProcess.getCode();
				String teacherID = ClientController.getClientUI().getUser().getUserID();
				String teacherName = ClientController.getClientUI().getUser().getFirstName() + " "
						+ ClientController.getClientUI().getUser().getLastName();
				String timeExtension = ((JFXTextField) requestSection.getChildren().get(0)).getText();
				String examDuration = examProcess.getDuration();
				String cause = taCause.getText();

				ExamExtension extesnion = new ExamExtension(examID, code, teacherID, teacherName, timeExtension,
						examDuration, cause);
				ModelWrapper<ExamExtension> modelWrapper = new ModelWrapper<>(extesnion, EXTENSION_REQUEST);
				ClientUI.getClientController().sendClientUIRequest(modelWrapper);
			}

		});

		requestSection = new HBox();
		requestSection.setVisible(false);
		requestSection.setSpacing(5);
		requestSection.getChildren().add(tfTime);
		requestSection.getChildren().add(sendRequest);
		requestSection.setAlignment(Pos.CENTER);

		examManagement.getChildren().add(codeLabel);
		examManagement.getChildren().add(timerLabel);
		examManagement.getChildren().add(freezeExam);
		examManagement.getChildren().add(teacherNoteButton);
		examManagement.getChildren().add(askForExstension);
		examManagement.getChildren().add(taCause);
		examManagement.getChildren().add(requestSection);

		sw = new Stopwatch(Integer.valueOf(duration), timerLabel);
		sw.startTime();

	}

	/**
	 * Stopping the exam
	 */
	public void stopExam() {
		if (!isClosed) {
			String code = examProcess.getCode();
			ModelWrapper<String> modelWrapper = new ModelWrapper<>(code, CLOSE_EXAM);
			ClientUI.getClientController().sendClientUIRequest(modelWrapper);
			sw.stopTime();
			freezeExam.setVisible(false);
			askForExstension.setVisible(false);
			timerLabel.setFont(new Font(50));
			timerLabel.setText("Exam Finished");
			teacherNoteButton.setVisible(false);
			taCause.setVisible(false);
			isClosed = true;
		}
	}

	/**
	 * class that define a stop watch for a student into the exam
	 */
	public class Stopwatch {
		private int min;
		private int sec;
		private Timer timer;
		private Label label;

		/**
		 * Constructor for Stopwatch
		 * 
		 * @param min
		 * @param label
		 */
		public Stopwatch(int min, Label label) {
			this.min = min;
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
							if (timeExtension != 0) {
								min += (int) timeExtension;
								Client.setTimeExtension(0);
							}
							label.setText(String.format("%02d:%02d\n", min, sec));
							if (min == 0 && sec == 0) {
								stopExam();

								if (requestFlag)
									requestSection.setVisible(false);

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
		 * Method for stopping the timer
		 */
		public void stopTime() {
			timer.cancel();
		}

	}

}
