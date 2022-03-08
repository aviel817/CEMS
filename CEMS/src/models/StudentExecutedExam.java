package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.TextField;

/**
 * Class that  represent a student who's executed an specific exam.
 * 
 *
 */
public class StudentExecutedExam implements Serializable, Comparable<StudentExecutedExam> {
	
	private static final long serialVersionUID = 1L;
	private String examID;
	private String studentID;
	private String studentName;
	private String TeacherId;
	private String subject;
	private String course;
	private String execDate;
	private String testType;
	private String grade;
	private String code;
	private String alert;
	private String execDuration;
	private String comment;
	private boolean finished;
	private boolean approved;
	private WordFile copy;
	private TextField tfGrade;
	private TextField tfComment;
	private JFXButton getCopy;

	


	/**
	 * Compare with 2 students
	 * @param StudentExecutedExam
	 * @return integer
	 */
	public int compareTo(StudentExecutedExam other) { 
		return Integer.compare(Integer.parseInt(this.grade), Integer.parseInt(other.grade));
	}


	/**
	 * Constructor
	 * @param course
	 * @param grade
	 * @param execDate
	 */
	public StudentExecutedExam(String course, String grade, String execDate) {
		super();
		this.course = course;
		this.grade = grade;
		this.execDate = execDate;
	}

	/**
	 * Constructor
	 * @param executedStudentExam
	 * @param newGrade
	 */
	public StudentExecutedExam(StudentExecutedExam executedStudentExam, String newGrade) {
		super();
		examID = executedStudentExam.getExamID();
		studentID = executedStudentExam.getStudentID();
		studentName = executedStudentExam.getStudentName();
		TeacherId = executedStudentExam.getTeacherId();
		subject = executedStudentExam.getSubject();
		course = executedStudentExam.getCourse();
		execDate = executedStudentExam.getExecDate();
		testType = executedStudentExam.getTestType();
		grade = executedStudentExam.getGrade();
		approved = executedStudentExam.isApproved();
		grade = newGrade;
	}


	/**
	 * Constructor
	 * @param examID
	 * @param studentID
	 * @param studentName
	 * @param teacherId
	 * @param subject
	 * @param course
	 * @param execDate
	 * @param testType
	 * @param grade
	 * @param alert
	 * @param approved
	 * @param comment
	 */
	public StudentExecutedExam(String examID, String studentID, String studentName, String teacherId, String subject,
			String course, String execDate, String testType, String grade, String alert, boolean approved,
			String comment) {
		super();
		this.examID = examID;
		this.studentID = studentID;
		this.studentName = studentName;
		TeacherId = teacherId;
		this.subject = subject;
		this.course = course;
		this.execDate = execDate;
		this.testType = testType;
		this.grade = grade;
		this.approved = approved;
		this.alert = alert;
		this.comment = comment;
	}

	/**
	 * Constructor
	 * @param examID
	 * @param studentID
	 * @param code
	 * @param execDate
	 */
	public StudentExecutedExam(String examID, String studentID, String code, String execDate) {
		super();
		this.examID = examID;
		this.studentID = studentID;
		this.execDate = execDate;
		this.code = code;
	}


	/**
	 * @return examID
	 */
	public String getExamID() {
		return examID;
	}

	/**
	 * set examID
	 * @param examID
	 */
	public void setExamID(String examID) {
		this.examID = examID;
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
	 * @return TeacherId
	 */
	public String getTeacherId() {
		return TeacherId;
	}

	/**
	 * set teacherId
	 * @param teacherId
	 */
	public void setTeacherId(String teacherId) {
		TeacherId = teacherId;
	}

	/**
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * set subject
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return course
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * set course
	 * @param course
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 * @return execDate
	 */
	public String getExecDate() {
		return execDate;
	}

	/**
	 * set execDate
	 * @param execDate
	 */
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}

	/**
	 * @return testType
	 */
	public String getTestType() {
		return testType;
	}

	/**
	 * set testType
	 * @param testType
	 */
	public void setTestType(String testType) {
		this.testType = testType;
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
	 * @return studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * set studentName
	 * @param studentName
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	/**
	 * @return approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * set approved
	 * @param approved
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return copy
	 */
	public WordFile getCopy() {
		return copy;
	}

	/**
	 * set copy
	 * @param copy
	 */
	public void setCopy(WordFile copy) {
		this.copy = copy;
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
	 * @return alert
	 */
	public String getAlert() {
		return alert;
	}

	/**
	 * set alert
	 * @param alert
	 */
	public void setAlert(String alert) {
		this.alert = alert;
	}

	/**
	 * @return tfGrade
	 */
	public TextField getTfGrade() {
		return tfGrade;
	}

	/**
	 * set TfGrade
	 * @param tfGrade
	 */
	public void setTfGrade(TextField tfGrade) {
		this.tfGrade = tfGrade;
	}

	/**
	 * @return getCopy
	 */
	public JFXButton getGetCopy() {
		return getCopy;
	}

	/**
	 * set getcopy
	 * @param getCopy
	 */
	public void setGetCopy(JFXButton getCopy) {
		this.getCopy = getCopy;
	}

	/**
	 * @return execDuration
	 */
	public String getExecDuration() {
		return execDuration;
	}

	/**
	 * set execDuration
	 * @param execDuration
	 */
	public void setExecDuration(String execDuration) {
		this.execDuration = execDuration;
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
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * set comment
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return tfComment
	 */
	public TextField getTfComment() {
		return tfComment;
	}

	/**
	 * set tfComment
	 * @param tfComment
	 */
	public void setTfComment(TextField tfComment) {
		this.tfComment = tfComment;
	}

	/**
	 *toString
	 */
	@Override
	public String toString() {
		return "StudentExecutedExam [examID=" + examID + ", studentID=" + studentID + ", studentName=" + studentName
				+ ", TeacherId=" + TeacherId + ", subject=" + subject + ", course=" + course + ", execDate=" + execDate
				+ ", testType=" + testType + ", grade=" + grade + ", approved=" + approved + "]";
	}

}
