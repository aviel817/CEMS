package models;

import java.io.Serializable;
import java.util.List;

import com.jfoenix.controls.JFXButton;

/**
 * Class that describe test on project system, store all necessary attributes
 * inside this model.
 * 
 */
public class Exam implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String teacherID;
	private String subject;
	private String course;
	private String duration;
	private String teacherName;
	private String studentNote;
	private String teacherNote;
	private List<ExamQuestion> examQuestions;
	private JFXButton questionListButton;
	private JFXButton editExamButton;


	/**
	 * Constructor for Exam
	 * @param id
	 * @param teacherID
	 * @param subject
	 * @param course
	 * @param duration
	 * @param examQuestions
	 * @param studentNote
	 * @param teacherNote
	 */
	public Exam(String id, String teacherID, String subject, String course, String duration,
			List<ExamQuestion> examQuestions, String studentNote, String teacherNote) {
		super();
		this.teacherID = teacherID;
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.examQuestions = examQuestions;
		this.studentNote = studentNote;
		this.teacherNote = teacherNote;
	}

	/**
	 * Constructor2 for Exam
	 * @param id
	 * @param subject
	 * @param course
	 * @param duration
	 * @param examQuestions
	 * @param questionListButton
	 */
	public Exam(String id, String subject, String course, String duration, List<ExamQuestion> examQuestions,
			JFXButton questionListButton) {
		super();
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.examQuestions = examQuestions;
		this.questionListButton = questionListButton;
	}

	/**
	 * Constructor3 for Exam
	 * @param subject
	 * @param teacherID
	 * @param course
	 * @param duration
	 * @param teacherNote
	 * @param studentNote
	 * @param examQuestions
	 */
	public Exam(String subject, String teacherID, String course, String duration, String teacherNote,
			String studentNote, List<ExamQuestion> examQuestions) {
		super();
		this.teacherID = teacherID;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.studentNote = studentNote;
		this.teacherNote = teacherNote;
		this.examQuestions = examQuestions;
	}

	/**
	 * Constructor4 for Exam
	 * @param examID
	 * @param subject
	 * @param teacherID
	 * @param course
	 * @param duration
	 * @param teacherNote
	 * @param studentNote
	 * @param examQuestions
	 */
	public Exam(String examID, String subject, String teacherID, String course, String duration, String teacherNote,
			String studentNote, List<ExamQuestion> examQuestions) {
		super();
		this.id = examID;
		this.teacherID = teacherID;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
		this.studentNote = studentNote;
		this.teacherNote = teacherNote;
		this.examQuestions = examQuestions;
	}

	/**
	 * Constructor5 for Exam
	 * @param id
	 * @param subject
	 * @param course
	 * @param duration
	 */
	public Exam(String id, String subject, String course, String duration) {
		super();
		this.id = id;
		this.subject = subject;
		this.course = course;
		this.duration = duration;
	}


	/**
	 * @return exam id
	 */
	public String getId() {
		return id;
	}

	/**
	 * set exam id
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
	 * @return duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * set duration
	 * @param duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return Exam questions
	 */
	public List<ExamQuestion> getExamQuestions() {
		return examQuestions;
	}

	/**
	 * set exam questions
	 * @param examQuestions
	 */
	public void setExamQuestions(List<ExamQuestion> examQuestions) {
		this.examQuestions = examQuestions;
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
	 * @return question list button
	 */
	public JFXButton getQuestionListButton() {
		return questionListButton;
	}

	/**
	 * set question list button
	 * @param questionListButton
	 */
	public void setQuestionListButton(JFXButton questionListButton) {
		this.questionListButton = questionListButton;
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
	 * @return teacher note
	 */
	public String getTeacherNote() {
		return teacherNote;
	}

	/**
	 * set teacher note
	 * @param teacherNote
	 */
	public void setTeacherNote(String teacherNote) {
		this.teacherNote = teacherNote;
	}

	/**
	 * @return edit exam button
	 */
	public JFXButton getEditExamButton() {
		return editExamButton;
	}

	/**
	 * set edit exam button
	 * @param editExamButton
	 */
	public void setEditExamButton(JFXButton editExamButton) {
		this.editExamButton = editExamButton;
	}

}
