package client.gui;

import static common.ModelWrapper.Operation.GET_QUESTION_LIST;
import static common.ModelWrapper.Operation.GET_QUESTION_LIST_BY_SUBJECT;

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
import models.Question;

/**
 * QuestionPoolController class handle screen of question pool on principal menu
 *
 */
public class QuestionPoolController implements Initializable {

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
	private JFXComboBox<String> cbQuestionSubject;

	/**
	 * Filter by subject
	 * @param event
	 */
	@FXML
	void onSubjectSelected(ActionEvent event) {
		String subjectSelected = cbQuestionSubject.getSelectionModel().getSelectedItem();
		ModelWrapper<String> modelWrapper = new ModelWrapper<>(subjectSelected, GET_QUESTION_LIST_BY_SUBJECT);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		List<Question> questionList = Client.getQuestions();
		addDetailButtons(questionList);

		tvQuestionPool.getItems().clear();
		tvQuestionPool.getItems().addAll(questionList);

	}

	/**
	 * Adding details button to objects
	 * @param questionList
	 */
	private void addDetailButtons(List<Question> questionList) {

		for (Question question : questionList) {
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

	/**
	 * open add note screen
	 * @param event
	 */
	@FXML
	void onClickAddNote(ActionEvent event) {
		AddNoteController addNoteWindow = new AddNoteController();
		addNoteWindow.start();
	}

	/**
	 * Setting all table column and creating new data set from client request that
	 * has been sent to database.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ModelWrapper<Question> modelWrapper = new ModelWrapper<>(GET_QUESTION_LIST);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		ObservableList<Question> questions = FXCollections.observableArrayList();
		List<Question> questionList = Client.getQuestions();
		questions.addAll(questionList);
		tvQuestionPool.setItems(questions);

		addDetailButtons(questionList);

		cbQuestionSubject.getItems().addAll(Client.getSubjectCollection().getSubjects());

		tcIdPool.setCellValueFactory(new PropertyValueFactory<Question, String>("questionID"));
		tcSubjectPool.setCellValueFactory(new PropertyValueFactory<Question, String>("subject"));
		tcTeacherPool.setCellValueFactory(new PropertyValueFactory<Question, String>("teacherName"));
		tcDetailsPool.setCellValueFactory(new PropertyValueFactory<Question, JFXButton>("detailsButton"));

	}

	/**
	 * @param strNum
	 * @return if is numeric or not
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

}
