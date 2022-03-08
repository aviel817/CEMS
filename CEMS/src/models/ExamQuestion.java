package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Class that extends Question class and represent additional information about
 * the questions.
 *
 */
public class ExamQuestion extends Question implements Serializable {

	private static final long serialVersionUID = 1L;
	private String studentNote;
	private String teacherNotes;
	private int points;
	private JFXButton noteDetails;
	private JFXButton removeButton;
	private TextField tfPoints;
	private ImageView checkImage;


	/**
	 * Constructor for ExamQuestion
	 * @param question
	 */
	public ExamQuestion(Question question) {
		super(question.getQuestionID(), question.getTeacherName(), question.getSubject(), question.getDetails(),
				question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4(),
				question.getCorrectAnswer(), question.getDetailsButton());
	}

	/**
	 * Constructor2 for ExamQuestion
	 * @param question
	 * @param points
	 */
	public ExamQuestion(Question question, int points) {
		super(question.getQuestionID(), question.getTeacherName(), question.getSubject(), question.getDetails(),
				question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4(),
				question.getCorrectAnswer(), question.getDetailsButton());
		this.points = points;
	}


	/**
	 * @return points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * set points
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @return note details button
	 */
	public JFXButton getNoteDetails() {
		return noteDetails;
	}

	/**
	 * set note details button
	 * @param noteDetails
	 */
	public void setNoteDetails(JFXButton noteDetails) {
		this.noteDetails = noteDetails;
	}

	/**
	 * @return ImageView of check image
	 */
	public ImageView getCheckImage() {
		return checkImage;
	}

	/**
	 * set ImageView of check image
	 * @param checkImage
	 */
	public void setCheckImage(ImageView checkImage) {
		this.checkImage = checkImage;
	}

	/**
	 * set visibility of image to true
	 */
	public void setVisibleImage() {
		this.checkImage.setVisible(true);
	}

	/**
	 * @return remove button
	 */
	public JFXButton getRemoveButton() {
		return removeButton;
	}

	/**
	 * set remove button
	 * @param removeButton
	 */
	public void setRemoveButton(JFXButton removeButton) {
		this.removeButton = removeButton;
	}

	/**
	 * @return student note
	 */
	public String getStudentNote() {
		return studentNote;
	}

	/**
	 * set student note
	 * @param studentNote
	 */
	public void setStudentNote(String studentNote) {
		this.studentNote = studentNote;
	}

	/**
	 * @return teacher notes
	 */
	public String getTeacherNotes() {
		return teacherNotes;
	}

	/**
	 * set teacher notes
	 * @param teacherNotes
	 */
	public void setTeacherNotes(String teacherNotes) {
		this.teacherNotes = teacherNotes;
	}

	/**
	 * @return textField of points
	 */
	public TextField getTfPoints() {
		return tfPoints;
	}

	/**
	 * set textField of points
	 * @param tfPoints
	 */
	public void setTfPoints(TextField tfPoints) {
		this.tfPoints = tfPoints;
	}
	
}
