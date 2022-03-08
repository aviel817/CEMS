package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

/**
 * Class that represent Question within all the information about it, happens
 * when a teacher decide to create an question.
 */

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	private String questionID;
	private String teacherName;
	private String subject;
	private String details;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private int correctAnswer;
	private JFXButton detailsButton;
	private JFXButton addRemoveButton;
	private JFXButton editButton;

	/**
	 * Constructor for the class Question
	 * Fills in the private parameters: questionID, teacherName, subject, details, answer1,
	 *  answer2, answer3, answer4, correctAnswer, detailsButton.
	 * @param question - object of ExamQuestion
	 */
	public Question(ExamQuestion question) {
		super();
		questionID = question.getQuestionID();
		teacherName = question.getTeacherName();
		subject = question.getSubject();
		details = question.getDetails();
		answer1 = question.getAnswer1();
		answer2 = question.getAnswer2();
		answer3 = question.getAnswer3();
		answer4 = question.getAnswer4();
		correctAnswer = question.getCorrectAnswer();
		detailsButton = question.getDetailsButton();
	}

	/**
	 * Constructor for the class Question
	 * Fills in the private parameters: questionID, teacherName, subject, details, answer1,
	 *  answer2, answer3, answer4, correctAnswer, detailsButton, addRemoveButton.
	 */
	public Question(String questionID, String teacherName, String subject, String details, String answer1,
			String answer2, String answer3, String answer4, int correctAnswer, JFXButton detailsButton,
			JFXButton addRemoveButton) {
		super();
		this.questionID = questionID;
		this.teacherName = teacherName;
		this.subject = subject;
		this.details = details;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.detailsButton = detailsButton;
		this.addRemoveButton = addRemoveButton;
	}

	/**
	 * Constructor for the class Question
	 * Fills in the private parameters: questionID, teacherName, subject, details, answer1,
	 *  answer2, answer3, answer4, correctAnswer, detailsButton.
	 */
	public Question(String questionID, String teacherName, String subject, String details, String answer1,
			String answer2, String answer3, String answer4, int correctAnswer, JFXButton detailsButton) {
		super();
		this.questionID = questionID;
		this.teacherName = teacherName;
		this.subject = subject;
		this.details = details;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.detailsButton = detailsButton;
	}
	/**
	 * Constructor for the class Question
	 * Fills in the private parameters: teacherName, subject, details, answer1,
	 *  answer2, answer3, answer4, correctAnswer.
	 */
	public Question(String teacherName, String subject, String details, String answer1, String answer2, String answer3,
			String answer4, int correctAnswer) {
		super();
		this.teacherName = teacherName;
		this.subject = subject;
		this.details = details;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
	}
	
	/**
	 * Constructor for the class Question
	 * Fills in the private parameters: questionID, teacherName, subject, details, answer1,
	 *  answer2, answer3, answer4, correctAnswer.
	 */
	public Question(String questionID, String teacherName, String subject, String details, String answer1,
			String answer2, String answer3, String answer4, int correctAnswer) {
		super();
		this.questionID = questionID;
		this.teacherName = teacherName;
		this.subject = subject;
		this.details = details;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
	}


	/**
	 * getter for questionID.
	 * @return questionID
	 */
	public String getQuestionID() {
		return questionID;
	}

	
	/**
	 * setter for questionID
	 * @param questionID
	 */
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	/**
	 * getter for teacherName.
	 * @return teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	
	/**
	 * setter for teacherName.
	 * @param teacherName
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	
	/**
	 * getter for subject.
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * setter for subject.
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * getter for details
	 * @return details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * setter for details.
	 * @param details
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * getter for answer1.
	 * @return answer1
	 */
	public String getAnswer1() {
		return answer1;
	}

	/**
	 * setter for answer1.
	 * @param answer1
	 */
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	/**
	 * getter for answer2.
	 * @return answer2
	 */
	public String getAnswer2() {
		return answer2;
	}

	/**
	 * setter for answer2.
	 * @param answer2
	 */
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	/**
	 * getter for answer3.
	 * @return answer3
	 */
	public String getAnswer3() {
		return answer3;
	}

	/**
	 * setter for answer3.
	 * @param answer3
	 */
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	/**
	 * getter for answer4.
	 * @return answer4
	 */
	public String getAnswer4() {
		return answer4;
	}

	/**
	 * setter for answer4.
	 * @param answer4
	 */
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	/**
	 * getter for correctAnswer.
	 * @return correctAnswer
	 */
	public int getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * setter for correctAnswer.
	 * @param correctAnswer
	 */
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	/**
	 * getter for detailsButton.
	 * @return detailsButton
	 */
	public JFXButton getDetailsButton() {
		return detailsButton;
	}

	/**
	 * setter for detailsButton.
	 * @param detailsButton
	 */
	public void setDetailsButton(JFXButton detailsButton) {
		this.detailsButton = detailsButton;
	}

	/**
	 * getter for addRemoveButton.
	 * @return addRemoveButton
	 */
	public JFXButton getAddRemoveButton() {
		return addRemoveButton;
	}

	/**
	 * setter for addRemoveButton.
	 * @param addRemoveButton
	 */
	public void setAddRemoveButton(JFXButton addRemoveButton) {
		this.addRemoveButton = addRemoveButton;
	}
	
	/**
	 * getter for editButton.
	 * @return editButton
	 */
	public JFXButton getEditButton() {
		return editButton;
	}

	/**
	 * setter for editButton.
	 * @param editButton
	 */
	public void setEditButton(JFXButton editButton) {
		this.editButton = editButton;
	}

	/**
	 *print the all parameter of Question. 
	 */
	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", teacherName=" + teacherName + ", subject=" + subject
				+ ", details=" + details + ", answer1=" + answer1 + ", answer2=" + answer2 + ", answer3=" + answer3
				+ ", answer4=" + answer4 + ", correctAnswer=" + correctAnswer + "]";
	}

}
