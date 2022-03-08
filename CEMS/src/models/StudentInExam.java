package models;

import java.io.Serializable;
import java.util.Arrays;

import ocsf.server.ConnectionToClient;

/**
 * Class that  represent a specific Student in a specific exam.
 *
 */

public class StudentInExam implements Serializable {

	private static final long serialVersionUID = 1L;
	private String studentID;
	private String code;
	private String grade;
	private String execDuration;
	private ConnectionToClient client;
	private String[] solution;
	private boolean finished;

	
	/**
	 * Constructor
	 * @param studentID
	 * @param code
	 * @param grade
	 * @param execDuration
	 * @param solution
	 */
	public StudentInExam(String studentID, String code, String grade, String execDuration, String[] solution) {
		super();
		this.studentID = studentID;
		this.code = code;
		this.grade = grade;
		this.execDuration = execDuration;
		this.solution = solution;
	}

	/**
	 * Constructor
	 * @param studentID
	 * @param client
	 */
	public StudentInExam(String studentID, ConnectionToClient client) {
		super();
		this.studentID = studentID;
		this.client = client;
	}


	/**
	 * @return studentID
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * set studentID
	 * @param studentID
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return client
	 */
	public ConnectionToClient getClient() {
		return client;
	}

	/**
	 * set Client
	 * @param client
	 */
	public void setClient(ConnectionToClient client) {
		this.client = client;
	}

	/**
	 * @return solutions
	 */
	public String[] getSolution() {
		return solution;
	}

	/**
	 * set Solutions
	 * @param solution
	 */
	public void setSolution(String[] solution) {
		this.solution = solution;
	}

	/**
	 * @return finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * set finished
	 * @param finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * set code
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return grade
	 */
	public String getGrade() {
		return grade;
	}

	/** 
	 * set grade
	 * @param grade
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	/**
	 * @return execDuration
	 */
	public String getExecDuration() {
		return execDuration;
	}

	/**
	 * Set ExecDuration
	 * @param execDuration
	 */
	public void setExecDuration(String execDuration) {
		this.execDuration = execDuration;
	}

	/**
	 *toString
	 */
	@Override
	public String toString() {
		return "StudentInExam [studentID=" + studentID + ", code=" + code + ", grade=" + grade + ", client=" + client
				+ ", solution=" + Arrays.toString(solution) + ", finished=" + finished + "]";
	}

}
