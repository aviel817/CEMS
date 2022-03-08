package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;

/**
 * Class that represent report of one question in an exam that has been already executed
 * by a student as a Computerized test.
 *
 */
public class ComputerizedTestReport implements Serializable {

	private static final long serialVersionUID = 1L;
	private String selectedAnswer;
	private String correctAnswer;
	private String points;
	private ImageView correctImg;
	private ExamQuestion question;
	private JFXButton detailsBtn;
	

	/**
	 * Constructor for ComputerizedTestReport
	 * @param selectedAnswer
	 * @param correctAnswer
	 * @param points
	 * @param correctImg
	 * @param question
	 */
	public ComputerizedTestReport(String selectedAnswer, String correctAnswer, String points, ImageView correctImg,
			ExamQuestion question) {
		super();
		if (selectedAnswer.equals("9")) {
			this.selectedAnswer = "NONE";
		} else {
			this.selectedAnswer = selectedAnswer;
		}
		this.correctAnswer = correctAnswer;
		this.points = points;
		this.correctImg = correctImg;
		this.question = question;
	}


	/**
	 * @return selected answer
	 */
	public String getSelectedAnswer() {
		return selectedAnswer;
	}

	/**
	 * set selected answer
	 * @param selectedAnswer
	 */
	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	/**
	 * @return correct answer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * set correct answer
	 * @param correctAnswer
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	/**
	 * @return points
	 */
	public String getPoints() {
		return points;
	}

	/**
	 * set points
	 * @param points
	 */
	public void setPoints(String points) {
		this.points = points;
	}

	/**
	 * @return correct/wrong image
	 */
	public ImageView getCorrectImg() {
		return correctImg;
	}

	/**
	 * set correct/wrong image
	 * @param correctImg
	 */
	public void setCorrectImg(ImageView correctImg) {
		this.correctImg = correctImg;
	}

	/**
	 * @return question
	 */
	public ExamQuestion getQuestion() {
		return question;
	}

	/**
	 * set question
	 * @param question
	 */
	public void setQuestion(ExamQuestion question) {
		this.question = question;
	}

	/**
	 * @return details button
	 */
	public JFXButton getDetailsBtn() {
		return detailsBtn;
	}

	/**
	 * set details button
	 * @param detailsBtn
	 */
	public void setDetailsBtn(JFXButton detailsBtn) {
		this.detailsBtn = detailsBtn;
	}

}
