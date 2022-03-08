package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;


/**
 * Class that represent exam extension while exam has been started by the teacher
 * of an specific exam.
 * 
 */

public class ExamExtension implements Serializable {


	private static final long serialVersionUID = 1L;
	private String examID;
	private String code;
	private String teacherID;
	private String teacherName;
	private String timeExtension;
	private String examDuration;
	private String casue;
	private JFXButton confirmButton;


	/**
	 * Constructor for ExamExtension
	 * @param examID
	 * @param code
	 * @param teacherID
	 * @param teacherName
	 * @param timeExtension
	 * @param examDuration
	 * @param casue
	 */
	public ExamExtension(String examID, String code, String teacherID, String teacherName, String timeExtension,
			String examDuration, String casue) {
		super();
		this.examID = examID;
		this.code = code;
		this.teacherID = teacherID;
		this.teacherName = teacherName;
		this.timeExtension = timeExtension;
		this.examDuration = examDuration;
		this.casue = casue;
	}


	/**
	 * @return exam code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * set exam code
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return teacher id
	 */
	public String getTeacherID() {
		return teacherID;
	}

	/**
	 * set teacher id
	 * @param teacherID
	 */
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	/**
	 * @return time extension
	 */
	public String getTimeExtension() {
		return timeExtension;
	}

	/**
	 * set time extension
	 * @param timeExtension
	 */
	public void setTimeExtension(String timeExtension) {
		this.timeExtension = timeExtension;
	}

	/**
	 * @return cause
	 */
	public String getCasue() {
		return casue;
	}

	/**
	 * set cause
	 * @param casue
	 */
	public void setCasue(String casue) {
		this.casue = casue;
	}

	/**
	 * @return teacher name
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * set teacher name
	 * @param teacherName
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @return exam duration
	 */
	public String getExamDuration() {
		return examDuration;
	}

	/**
	 * set exam duration
	 * @param examDuration
	 */
	public void setExamDuration(String examDuration) {
		this.examDuration = examDuration;
	}

	/**
	 * @return exam id
	 */
	public String getExamID() {
		return examID;
	}

	/**
	 * set exam id
	 * @param examID
	 */
	public void setExamID(String examID) {
		this.examID = examID;
	}
	
	/**
	 * @return confirmation button
	 */
	public JFXButton getConfirmButton() {
		return confirmButton;
	}

	/**
	 * set confirmation button
	 * @param confirmButton
	 */
	public void setConfirmButton(JFXButton confirmButton) {
		this.confirmButton = confirmButton;
	}

	/**
	 * To string function
	 */
	@Override
	public String toString() {
		return "ExamExtension [code=" + code + ", teacherID=" + teacherID + ", teacherName=" + teacherName
				+ ", timeExtension=" + timeExtension + ", casue=" + casue + "]";
	}

}
