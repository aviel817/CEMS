package models;

import java.io.Serializable;

/**
 * Class that  represent all the statistics of an specific exam / specific teacher.
 */
public class Statistics implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String examID = null;
	private String executeTeacherID = null;
	private String avg = null;
	private String median = null;
	
	/**
	 * Constructor for Statistics. 
	 * @param userID
	 * @param FirstName
	 * @param LastName
	 * @param Grade
	 */
	public Statistics(String userID, String FirstName, String LastName, String Grade) {
		super();
		this.examID = userID;
		this.executeTeacherID = FirstName;
		this.avg = LastName;
		this.median = Grade;
	}

	/**
	 * getter for examID
	 * @return examID
	 */
	public String getExamID() {
		return examID;
	}

	/**setter for examID
	 * @param examID
	 */
	public void setExamID(String examID) {
		this.examID = examID;
	}

	/**
	 * getter for executeTeacherID
	 * @return executeTeacherID
	 */
	public String getExecuteTeacherID() {
		return executeTeacherID;
	}

	/**
	 * setter for executeTeacherID
	 * @param executeTeacherID
	 */
	public void setExecuteTeacherID(String executeTeacherID) {
		this.executeTeacherID = executeTeacherID;
	}

	/**
	 * getter for average
	 * @return average
	 */
	public String getAvg() {
		return avg;
	}

	/**
	 * setter for average
	 * @param avg
	 */
	public void setAvg(String avg) {
		this.avg = avg;
	}

	/**
	 * getter for the median's exams
	 * @return median
	 */
	public String getMedian() {
		return median;
	}

	/**
	 * setter for the median's exams
	 * @param median
	 */
	public void setMedian(String median) {
		this.median = median;
	}

	/**
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}