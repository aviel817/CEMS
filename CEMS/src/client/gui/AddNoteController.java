package client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ExamQuestion;
import models.Question;

/**
 * AddNoteController class handle the screen of adding note by teacher. The
 * teacher can add note to the student or to other teachers.
 */
public class AddNoteController implements Initializable {

	@FXML
	private JFXButton btnAddNote;

	@FXML
	private JFXTextField tfQuestionPoints;

	@FXML
	private JFXComboBox<String> cbDisplayedFor;

	@FXML
	private TextArea taNotes;

	@FXML
	private Label messageLabel;

	private static Question question;

	private static TableView<Question> tvQuestionPool;

	private static TableView<ExamQuestion> tvSelectedQuestion;

	private static String editNote;

	private static boolean edit;

	private static String editType;

	public AddNoteController() {
	}

	public AddNoteController(boolean edit) {
		AddNoteController.edit = edit;
	}

	public AddNoteController(String editNote, String editType) {
		AddNoteController.editNote = editNote;
		AddNoteController.edit = true;
		AddNoteController.editType = editType;
	}

	/**
	 * This method load the fxml and display to the screen
	 */
	public void start() {
		Stage stage = new Stage();
		Pane mainPane;
		try {
			String windowType;

			if (edit)
				windowType = "Edit note";
			else
				windowType = "Add note";

			mainPane = (Pane) FXMLLoader.load(getClass().getResource("AddNote.fxml"));
			Scene scene = new Scene(mainPane, 370, 280);
			stage.setScene(scene);
			stage.setTitle(windowType);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function that handle clicking "Add Note" button, adding the note into the
	 * EditExamController or CreateExamController.
	 * 
	 * @param event handler
	 */
	@FXML
	void onClickAddNote(ActionEvent event) {
		String note = taNotes.getText();

		messageLabel.setStyle("-fx-text-fill: RED;");

		if (edit) {

			if (note.isEmpty()) {
				messageLabel.setText("Note is empty");
			} else {
				if (editType == "Teachers") {
					EditExamController.setTeacherNote(note);
				} else {
					EditExamController.setStudentNote(note);
				}
				Node node = (Node) event.getSource();
				Stage stage = (Stage) node.getScene().getWindow();
				stage.close();
			}
		} else {
			if (note.isEmpty()) {
				messageLabel.setText("Note is empty");
			} else if (cbDisplayedFor.getSelectionModel().getSelectedItem() == null) {
				messageLabel.setText("You need to select group type");
			} else {
				if (cbDisplayedFor.getSelectionModel().getSelectedItem().equals("Teachers")) {
					CreateExamController.setTeacherNote(note);
				} else {
					CreateExamController.setStudentNote(note);
				}

				Node node = (Node) event.getSource();
				Stage stage = (Stage) node.getScene().getWindow();
				stage.close();
			}
		}

	}

	public static Question getQuestion() {
		return question;
	}

	public static void setQuestion(Question question) {
		AddNoteController.question = question;
	}

	public static TableView<Question> getTvQuestionPool() {
		return tvQuestionPool;
	}

	public static void setTvQuestionPull(TableView<Question> tvQuestionPull) {
		AddNoteController.tvQuestionPool = tvQuestionPull;
	}

	public static TableView<ExamQuestion> getTvSelectedQuestion() {
		return tvSelectedQuestion;
	}

	public static void setTvSelectedQuestion(TableView<ExamQuestion> tvSelectedQuestion) {
		AddNoteController.tvSelectedQuestion = tvSelectedQuestion;
	}

	/**
	 * Setting ComboBox and text area of editable note
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbDisplayedFor.getItems().add("Students");
		cbDisplayedFor.getItems().add("Teachers");

		if (edit) {
			taNotes.setText(editNote);
			cbDisplayedFor.setVisible(false);
			btnAddNote.setText("Edit");
		}

	}

}