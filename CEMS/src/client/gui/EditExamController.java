package client.gui;

import static common.ModelWrapper.Operation.GET_QUESTION_LIST;
import static common.ModelWrapper.Operation.GET_QUESTION_LIST_BY_SUBJECT;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.Client;
import client.ClientController;
import client.ClientUI;
import common.ModelWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.Exam;
import models.ExamQuestion;
import models.Question;
import sun.applet.Main;

/**
 * CreateExamController class handle the screen of create exam by teacher. The
 * teacher can add questions that already made and watch their details. In
 * addition, there is option to filter questions by subject. The teacher need to
 * choose subject, course and exam duration. Optionally, the teacher can add
 * note to the exam.
 *
 */
public class EditExamController implements Initializable {

	@FXML
	public TableView<Question> tvQuestionPool;

	@FXML
	private TableColumn<Question, String> tcIdPool;

	@FXML
	private TableColumn<Question, String> tcSubjectPool;

	@FXML
	private TableColumn<Question, String> tcTeacherPool;

	@FXML
	private TableColumn<Question, JFXButton> tcDetailsPool;

	@FXML
	private TableView<ExamQuestion> tvSelectedQuestion;

	@FXML
	private TableColumn<Question, JFXButton> tcAddPool;

	@FXML
	private TableColumn<ExamQuestion, String> tcIdSelected;

	@FXML
	private TableColumn<ExamQuestion, TextField> tcPointsSelected;

	@FXML
	private TableColumn<ExamQuestion, String> tcSubjectSelected;

	@FXML
	private TableColumn<ExamQuestion, String> tcTeacherSelected;

	@FXML
	private TableColumn<ExamQuestion, JFXButton> tcRemove;

	@FXML
	private TableColumn<ExamQuestion, JFXButton> tcDetailsSelected;

	@FXML
	private JFXButton btnSearch;

	@FXML
	private JFXButton btnNote;

	@FXML
	private JFXTextField tfDuration;

	@FXML
	private JFXButton btnContinue;

	@FXML
	private JFXComboBox<String> cbQuestionSubject;

	@FXML
	private JFXComboBox<String> cbExamCourse;

	@FXML
	private Label messageLabel;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXButton btnStudentNote;

	@FXML
	private JFXButton btnTeacherNote;

	@FXML
	private JFXComboBox<String> cbExamSubject;

	private static String teacherNote;

	private static String studentNote;

	private static Exam oldExam;

	enum Operation {
		REMOVE, ADD
	}

	public EditExamController() {

	}

	public EditExamController(Exam oldExam) {
		EditExamController.oldExam = oldExam;
	}

	/**
	 * Update the questions by subject
	 * 
	 * @param event
	 */
	@FXML
	void onSubjectSelected(ActionEvent event) {
		String subjectSelected = cbQuestionSubject.getSelectionModel().getSelectedItem();
		ModelWrapper<String> modelWrapper = new ModelWrapper<>(subjectSelected, GET_QUESTION_LIST_BY_SUBJECT);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);
		addQuestionList();
	}

	@FXML
	void onClickBack(ActionEvent event) {
		MainGuiController.getMenuHandler().setTeacherExamPoolScreen();
	}

	@FXML
	void onClickEditStudentNote(ActionEvent event) {
		String editNote = getStudentNote();
		AddNoteController addNoteController = new AddNoteController(editNote, "Students");
		addNoteController.start();
	}

	@FXML
	void onClickEditTeacherNote(ActionEvent event) {
		String editNote = getTeacherNote();
		AddNoteController addNoteController = new AddNoteController(editNote, "Teachers");
		addNoteController.start();
	}

	/**
	 * Adding questions to the table
	 */
	private void addQuestionList() {
		ObservableList<Question> questions = FXCollections.observableArrayList();
		questions.addAll(Client.getQuestions());
		tvQuestionPool.setItems(questions);

		for (Question question : Client.getQuestions()) {
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

			JFXButton addButton = new JFXButton();
			addButton.setPrefSize(70, 15);
			addButton.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
			addButton.setText("Add");
			addButton.setOnAction(new AddRemoveEvenetHandler(Operation.ADD, question));
			question.setAddRemoveButton(addButton);
		}
	}

	/**
	 * Add note to exam
	 * 
	 * @param event
	 */
	@FXML
	void onClickAddNote(ActionEvent event) {
		AddNoteController addNoteWindow = new AddNoteController();
		addNoteWindow.start();
	}

	/**
	 * Continue to the next step in creating exam
	 * 
	 * @param event
	 */
	@FXML
	void onClickContinue(ActionEvent event) {
		String duration;
		boolean wrongInput = false;
		for (ExamQuestion question : tvSelectedQuestion.getItems()) {

			if (question.getTfPoints().getText().isEmpty())
				wrongInput = true;

			if (!isNumeric(question.getTfPoints().getText()))
				wrongInput = true;

		}

		duration = tfDuration.getText();
		List<ExamQuestion> examQuestions = createRegularList(tvSelectedQuestion.getItems());
		messageLabel.setStyle("-fx-text-fill: RED;");

		if (examQuestions.isEmpty()) {
			messageLabel.setText("Question list is empty, please insert question");
		} else if (duration.isEmpty()) {
			messageLabel.setText("Please insert exam duration");
		} else if (!isNumeric(duration)) {
			messageLabel.setText("Duration must to be number value");
		} else if (wrongInput) {
			messageLabel.setText("Please insert numric points for every question");
		}

		if (!duration.isEmpty() && examQuestions != null && isNumeric(duration) && !examQuestions.isEmpty()
				&& !wrongInput) {
			examQuestions = addAllPoints(examQuestions);
			String teacherID = ClientController.getClientUI().getUser().getUserID();
			String teacherNote = getTeacherNote();
			String studentNote = getStudentNote();
			String examID = oldExam.getId();
			String subject = oldExam.getSubject();
			String course = oldExam.getCourse();

			Exam newExam = new Exam(examID, subject, teacherID, course, duration, teacherNote, studentNote,
					examQuestions);
			newExam.setTeacherName(ClientController.getClientUI().getUser().getFirstName() + " "
					+ ClientController.getClientUI().getUser().getLastName());

			ConfirmExamController confirmPage = new ConfirmExamController(oldExam, newExam, "Edit");

			confirmPage.start();
		}

	}

	/**
	 * Add questions to the table
	 * 
	 * @param observableList
	 * @return list of exam questions
	 */
	private List<ExamQuestion> createRegularList(ObservableList<ExamQuestion> observableList) {
		List<ExamQuestion> examQuestions = new ArrayList<>();
		examQuestions.addAll(observableList);
		return examQuestions;
	}

	/**
	 * The method add points to each question
	 * 
	 * @param examQuestions
	 * @return list of exam questions
	 */
	private List<ExamQuestion> addAllPoints(List<ExamQuestion> examQuestions) {

		for (ExamQuestion examQuestion : examQuestions) {
			String finalPoint = examQuestion.getTfPoints().getText();
			examQuestion.setPoints(Integer.valueOf(finalPoint));
		}

		return examQuestions;
	}

	/**
	 * This method filters courses on subject click.
	 * 
	 * @param event
	 */
	@FXML
	void onClickExamSubject(ActionEvent event) {
		String subject = cbExamSubject.getSelectionModel().getSelectedItem();
		cbExamCourse.getItems().clear();
		cbExamCourse.getItems().addAll(Client.getSubjectCollection().getCourseListBySubject(subject));

	}

	/**
	 * Setting all table column and creating new data set from client request that
	 * has been sent to database.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ModelWrapper<Question> modelWrapper = new ModelWrapper<>(GET_QUESTION_LIST);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);
		addQuestionList();

		tvSelectedQuestion.setPlaceholder(new Label("No question were added to the list"));
		cbQuestionSubject.getItems().addAll(Client.getSubjectCollection().getSubjects());

		tcIdPool.setCellValueFactory(new PropertyValueFactory<Question, String>("questionID"));
		tcSubjectPool.setCellValueFactory(new PropertyValueFactory<Question, String>("subject"));
		tcTeacherPool.setCellValueFactory(new PropertyValueFactory<Question, String>("teacherName"));
		tcAddPool.setCellValueFactory(new PropertyValueFactory<Question, JFXButton>("addRemoveButton"));
		tcDetailsPool.setCellValueFactory(new PropertyValueFactory<Question, JFXButton>("detailsButton"));

		tcIdSelected.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("questionID"));
		tcSubjectSelected.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("subject"));
		tcTeacherSelected.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("teacherName"));
		tcPointsSelected.setCellValueFactory(new PropertyValueFactory<ExamQuestion, TextField>("tfPoints"));
		tcRemove.setCellValueFactory(new PropertyValueFactory<ExamQuestion, JFXButton>("addRemoveButton"));
		tcDetailsSelected.setCellValueFactory(new PropertyValueFactory<ExamQuestion, JFXButton>("detailsButton"));

		ObservableList<ExamQuestion> examQuestions = FXCollections.observableArrayList();
		InitializeSelectedQuestionPool();
		examQuestions.addAll(oldExam.getExamQuestions());
		tvSelectedQuestion.setItems(examQuestions);

		setStudentNote(oldExam.getStudentNote());
		setTeacherNote(oldExam.getTeacherNote());

		tfDuration.setText(oldExam.getDuration());

	}

	private void InitializeSelectedQuestionPool() {

		for (ExamQuestion examQuestion : oldExam.getExamQuestions()) {
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
						QuestionDetailsController.setQuestion(examQuestion);
						questionDetailsController.start();
					}
				}

			});

			examQuestion.setDetailsButton(detailsButton);

			JFXButton remove = new JFXButton();
			remove.setPrefSize(90, 15);
			remove.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
			remove.setText("Remove");
			remove.setOnAction(new AddRemoveEvenetHandler(Operation.REMOVE, examQuestion));
			examQuestion.setAddRemoveButton(remove);
			String points = String.valueOf(examQuestion.getPoints());
			TextField tfPoints = new TextField(points);
			examQuestion.setTfPoints(tfPoints);
			tvSelectedQuestion.getItems().add(examQuestion);
			removeFromQuestionPool(examQuestion);
		}

	}

	private void removeFromQuestionPool(ExamQuestion examQuestion) {
		List<Question> questionPool = tvQuestionPool.getItems();
		Question questionToDelete = null;
		for (Question question : questionPool) {
			if (question.getQuestionID().equals(examQuestion.getQuestionID())) {
				questionToDelete = question;
			}
		}

		if (questionToDelete != null)
			questionPool.remove(questionToDelete);

	}

	/**
	 * check if the input is numeric or not
	 * 
	 * @param strNum
	 * @return true if numeric else false
	 */
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * This class take care of adding and removing questions from/to table
	 *
	 */
	public class AddRemoveEvenetHandler implements EventHandler<ActionEvent> {

		private Operation operation;
		private Question question;
		private ExamQuestion examQuestion;

		public AddRemoveEvenetHandler(Operation operation, Question question) {
			this.operation = operation;
			this.question = question;
		}

		public AddRemoveEvenetHandler(Operation operation, ExamQuestion examQuestion) {
			this.operation = operation;
			this.examQuestion = examQuestion;
		}

		@Override
		public void handle(ActionEvent event) {

			switch (operation) {

			case ADD:
				tvQuestionPool.getItems().remove(question);
				examQuestion = new ExamQuestion(question);
				JFXButton remove = new JFXButton();
				remove.setPrefSize(90, 15);
				remove.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
				remove.setText("Remove");
				remove.setOnAction(new AddRemoveEvenetHandler(Operation.REMOVE, examQuestion));
				examQuestion.setAddRemoveButton(remove);
				TextField tfPoints = new TextField();
				examQuestion.setTfPoints(tfPoints);
				tvSelectedQuestion.getItems().add(examQuestion);
				break;

			case REMOVE:
				tvSelectedQuestion.getItems().remove(examQuestion);
				question = new Question(examQuestion);
				JFXButton add = new JFXButton();
				add.setPrefSize(70, 15);
				add.setStyle("-fx-background-color:#616161;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
				add.setText("Add");
				add.setOnAction(new AddRemoveEvenetHandler(Operation.ADD, question));
				question.setAddRemoveButton(add);
				tvQuestionPool.getItems().add(question);
				break;
			}

		}

	}

	/**
	 * @return teacher note
	 */
	public static String getTeacherNote() {
		return teacherNote;
	}

	/**
	 * set teacher note
	 * 
	 * @param teacherNote
	 */
	public static void setTeacherNote(String teacherNote) {
		EditExamController.teacherNote = teacherNote;
	}

	/**
	 * @return student note
	 */
	public static String getStudentNote() {
		return studentNote;
	}

	/**
	 * set student note
	 * 
	 * @param studentNote
	 */
	public static void setStudentNote(String studentNote) {
		EditExamController.studentNote = studentNote;
	}

	public void start() {
		try {
			Pane enterExamPane = (Pane) FXMLLoader.load(getClass().getResource("EditExam.fxml"));
			MainGuiController.getMenuHandler().getMainFrame().setCenter(enterExamPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
