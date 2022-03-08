package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

/**
 * Class that  represent all the information after executed an exam 
 * (happens after the teacher freeze - close the exam or at the end of time given).
 *
 */
public class ExecutedExam implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String subject;
	private String course;
	private String teacherID;
	private String executorTeacherName;
	private String execDate;
	private String execTime;
	private String testType;
	private String finishedStudentsCount;
	private boolean approved;
	private double avg;
	private double median;
	private JFXButton questionList;
	private JFXButton gradeApproval;


	/**
	 * Constructor for ExecutedExam 
	 * @param id
	 * @param subject
	 * @param course
	 * @param teacherID
	 * @param executorTeacherName
	 * @param execDate
	 * @param execTime
	 * @param finishedStudentsCount
	 * @param testType
	 * @param avg
	 * @param median
	 * @param approved
	 */
	public ExecutedExam(String id, String subject, String course, String teacherID, String executorTeacherName,
			String execDate, String execTime, String finishedStudentsCount, String testType, double avg, double median,
			boolean approved) {
		super();
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.teacherID = teacherID;
		this.executorTeacherName = executorTeacherName;
		this.execDate = execDate;
		this.execTime = execTime;
		this.finishedStudentsCount = finishedStudentsCount;
		this.testType = testType;
		this.avg = avg;
		this.median = median;
		this.approved = approved;

	}

	/**
	 * Constructor2 for ExecutedExam 
	 * @param id
	 * @param subject
	 * @param course
	 * @param execDate
	 * @param testType
	 * @param grade
	 */
	public ExecutedExam(String id, String subject, String course, String execDate, String testType, int grade) {
		super();
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.execDate = execDate;
		this.testType = testType;
	}

	/**
	 * Constructor3 for ExecutedExam 
	 * @param id
	 * @param executorTeacherName
	 * @param teacherID
	 * @param subject
	 * @param course
	 * @param execDate
	 * @param execTime
	 * @param testType
	 * @param approved
	 * @param avg
	 */
	public ExecutedExam(String id, String executorTeacherName, String teacherID, String subject, String course,
			String execDate, String execTime, String testType, boolean approved, double avg) {
		super();
		this.executorTeacherName = executorTeacherName;
		this.id = id;
		this.subject = subject;
		this.teacherID = teacherID;
		this.course = course;
		this.execDate = execDate;
		this.execTime = execTime;
		this.testType = testType;
		this.approved = approved;
		this.avg = avg;
	}

	/**
	 * Constructor4 for ExecutedExam 
	 * @param id
	 * @param teacherID
	 * @param executorTeacherName
	 * @param execDate
	 * @param execTime
	 * @param avg
	 * @param median
	 */
	public ExecutedExam(String id, String teacherID, String executorTeacherName, String execDate, String execTime,
			double avg, double median) {
		this.id = id;
		this.teacherID = teacherID;
		this.avg = avg;
		this.execDate = execDate;
		this.execTime = execTime;
		this.executorTeacherName = executorTeacherName;
		this.median = median;
	}

	/**
	 * Constructor5 for ExecutedExam 
	 * @param id
	 * @param subject
	 * @param course
	 * @param teacherID
	 * @param execDate
	 * @param testType
	 * @param avg
	 * @param median
	 * @param approved
	 */
	public ExecutedExam(String id, String subject, String course, String teacherID, String execDate, String testType,
			double avg, double median, boolean approved) {
		super();
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.teacherID = teacherID;
		this.execDate = execDate;
		this.testType = testType;
		this.avg = avg;
		this.median = median;
		this.approved = approved;
	}


	/**
	 * @return average
	 */
	public double getAvg() {
		return avg;
	}

	/**
	 * set average
	 * @param avg
	 */
	public void setAvg(double avg) {
		this.avg = avg;
	}

	/**
	 * @return median
	 */
	public double getMedian() {
		return median;
	}

	/**
	 * set median
	 * @param median
	 */
	public void setMedian(double median) {
		this.median = median;
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * set id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return execution date
	 */
	public String getExecDate() {
		return execDate;
	}

	/**
	 * set execution date
	 * @param execDate
	 */
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}

	/**
	 * @return test type
	 */
	public String getTestType() {
		return testType;
	}

	/**
	 * set test type 
	 * @param testType
	 */
	public void setTestType(String testType) {
		this.testType = testType;
	}

	/**
	 * @return serial version
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return executor teacher name
	 */
	public String getExecutorTeacherName() {
		return executorTeacherName;
	}

	/**
	 * set executor teacher name
	 * @param executorTeacherName
	 */
	public void setExecutorTeacherName(String executorTeacherName) {
		this.executorTeacherName = executorTeacherName;
	}

	/**
	 * @return total execution time
	 */
	public String getExecTime() {
		return execTime;
	}

	/**
	 * set total execution time
	 * @param execTime
	 */
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	/**
	 * @return question list button
	 */
	public JFXButton getQuestionList() {
		return questionList;
	}

	/**
	 * set question list button
	 * @param questionList
	 */
	public void setQuestionList(JFXButton questionList) {
		this.questionList = questionList;
	}

	/**
	 * @return button of grade approval
	 */
	public JFXButton getGradeApproval() {
		return gradeApproval;
	}

	/**
	 * set button of grade approval
	 * @param gradeApproval
	 */
	public void setGradeApproval(JFXButton gradeApproval) {
		this.gradeApproval = gradeApproval;
	}

	/**
	 * @return count of finished students
	 */
	public String getFinishedStudentsCount() {
		return finishedStudentsCount;
	}

	/**
	 * set count of finished students
	 * @param finishedStudentsCount
	 */
	public void setFinishedStudentsCount(String finishedStudentsCount) {
		this.finishedStudentsCount = finishedStudentsCount;
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
	 * To string function
	 */
	@Override
	public String toString() {
		return "ExecutedExam [id=" + id + ", subject=" + subject + ", course=" + course + ", teacherID=" + teacherID
				+ ", executorTeacherName=" + executorTeacherName + ", execDate=" + execDate + ", execTime=" + execTime
				+ ", testType=" + testType + ", finishedStudentsCount=" + finishedStudentsCount + ", avg=" + avg
				+ ", median=" + median + ", questionList=" + questionList + ", gradeApproval=" + gradeApproval + "]";
	}

}
