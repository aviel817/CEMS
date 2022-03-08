package models;

import java.io.Serializable;

import ocsf.server.ConnectionToClient;

/**
 * Class that represent the exam process while a teacher start an exam.
 * 
 */

public class ExamProcess implements Serializable {

	private String examId;
	private String date;
	private String time;
	private String teacherID;
	private String code;
	private String subject;
	private String course;
	private String duration;
	private String finishedStudentsCount;
	private String teacherNote;
	private ConnectionToClient teacherClient;
	private WordFile manualFile;
	private ExamType type;

	public enum ExamType {
		Computerized, Manual
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for ExamProcess
	 * 
	 * @param examId
	 * @param date
	 * @param time
	 * @param teacherID
	 * @param code
	 * @param subject
	 * @param course
	 * @param duration
	 */
	public ExamProcess(String examId, String teacherNote, String date, String time, String teacherID, String code,
			String subject, String course, String duration) {
		super();
		this.examId = examId;
		this.teacherNote = teacherNote;
		this.date = date;
		this.time = time;
		this.teacherID = teacherID;
		this.code = code;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.type = ExamType.Computerized;
	}

	/**
	 * Constructor2 for ExamProcess
	 * 
	 * @param examId
	 * @param date
	 * @param time
	 * @param teacherID
	 * @param code
	 * @param subject
	 * @param course
	 * @param duration
	 * @param manualFile
	 */
	public ExamProcess(String examId, String date, String time, String teacherID, String code, String subject,
			String course, String duration, WordFile manualFile) {
		super();
		this.examId = examId;
		this.date = date;
		this.time = time;
		this.teacherID = teacherID;
		this.code = code;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.manualFile = manualFile;
		this.type = ExamType.Manual;
	}

	/**
	 * Constructor3 for ExamProcess
	 * 
	 * @param examProcess
	 */
	public ExamProcess(ExamProcess examProcess) {
		super();
		this.examId = examProcess.getExamId();
		this.date = examProcess.getDate();
		this.time = examProcess.getTime();
		this.teacherID = examProcess.getTeacherID();
		this.code = examProcess.getCode();
		this.subject = examProcess.getSubject();
		this.course = examProcess.getCourse();
		this.duration = examProcess.getDuration();
		this.manualFile = examProcess.getManualFile();
		this.type = examProcess.getType();
	}

	/**
	 * @return teacher id
	 */
	public String getTeacherID() {
		return teacherID;
	}

	/**
	 * set teacher id
	 * 
	 * @param teacherID
	 */
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	/**
	 * @return serial version
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * set date
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return exam code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * set exam code
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * set exam id
	 * 
	 * @param examId
	 */
	public void setexamId(String examId) {
		this.examId = examId;
	}

	/**
	 * @return exam type
	 */
	public ExamType getType() {
		return type;
	}

	/**
	 * set exam type
	 * 
	 * @param type
	 */
	public void setType(ExamType type) {
		this.type = type;
	}

	/**
	 * @return manual test file
	 */
	public WordFile getManualFile() {
		return manualFile;
	}

	/**
	 * set manual test file
	 * 
	 * @param manualFile
	 */
	public void setManualFile(WordFile manualFile) {
		this.manualFile = manualFile;
	}

	/**
	 * @return time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * set time
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return exam id
	 */
	public String getExamId() {
		return examId;
	}

	/**
	 * set exam id
	 * 
	 * @param examId
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}

	/**
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * set subject
	 * 
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
	 * 
	 * @param course
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 * @return duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * set duration
	 * 
	 * @param duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return teacher client
	 */
	public ConnectionToClient getTeacherClient() {
		return teacherClient;
	}

	/**
	 * set teacher client
	 * 
	 * @param teacherClient
	 */
	public void setTeacherClient(ConnectionToClient teacherClient) {
		this.teacherClient = teacherClient;
	}

	/**
	 * @return count of finished students
	 */
	public String getFinishedStudentsCount() {
		return finishedStudentsCount;
	}

	/**
	 * set count of finished students
	 * 
	 * @param finishedStudentsCount
	 */
	public void setFinishedStudentsCount(String finishedStudentsCount) {
		this.finishedStudentsCount = finishedStudentsCount;
	}
	
	public String getTeacherNote() {
		return teacherNote;
	}

	public void setTeacherNote(String teacherNote) {
		this.teacherNote = teacherNote;
	}

	/**
	 * To string function
	 */
	@Override
	public String toString() {
		return "ExamProcess [examId=" + examId + ", date=" + date + ", time=" + time + ", teacherID=" + teacherID
				+ ", code=" + code + ", subject=" + subject + ", course=" + course + ", duration=" + duration
				+ ", manualFile=" + manualFile + ", type=" + type + "]";
	}

}
