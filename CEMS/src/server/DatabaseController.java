package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.SubjectCourseCollection;
import models.Database;
import models.Exam;
import models.ExamProcess;
import models.ExamQuestion;
import models.ExecutedExam;
import models.Question;
import models.StudentExecutedExam;
import models.StudentInExam;
import models.User;
import models.User.ErrorType;
import models.User.UserType;
import models.WordFile;

/**
 * The DatabaseController Class handle all the operations sent by client, it
 * communicates with server side, and send queries to SQL database.
 *
 */
public class DatabaseController {

	/**
	 * Create connection instance to SQL Database
	 */
	private Connection conn;

	/**
	 * Database details instance, in order to store all SQL connection details
	 */
	private Database database;

	/**
	 * Server event listener in order to handle events that occurred by database,
	 * send it back to server log.
	 */
	private ServerEventListener logListener;

	/**
	 * @param database    store database details instance{ip,port,etc..} on class
	 * @param logListener store event listener on class
	 */
	public DatabaseController(Database database, ServerEventListener logListener) {
		super();
		this.database = database;
		this.logListener = logListener;
	}

	/**
	 * Creating new connection to SQL database driver by using database details
	 * instance. In case of connection problem terminate the connection.
	 * 
	 * @throws SQLException in case of connectivity problem.
	 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			logListener.printToLog("Driver definition succeed");
		} catch (Exception ex) {
			logListener.printToLog("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + database.getIp() + ":" + database.getPort() + "/"
					+ database.getScheme() + "?serverTimezone=IST", database.getUserName(), database.getPassword());
			logListener.printToLog("SQL connection succeed");
		} catch (SQLException ex) {
			logListener.printToLog("SQLException: " + ex.getMessage());
			logListener.printToLog("SQLState: " + ex.getSQLState());
			logListener.printToLog("VendorError: " + ex.getErrorCode());
			throw new SQLException();
		}

	}

	/**
	 * Saving new test on database using appropriate query by prepared statement.
	 * 
	 * @param exam test that's needed to be save on database
	 * @return boolean value{true = test saved successfully,false = can't save test}
	 */
	public boolean saveExam(Exam exam) {
		PreparedStatement prepareStatement;
		int examID;
		String subjectID = Server.getSubjectCollection().getSubjectMap().get(exam.getSubject());
		String courseID = Server.getSubjectCollection().getCourseMap().get(exam.getCourse());

		String examLastID = getExamLastId(exam.getSubject(), exam.getCourse());

		if (examLastID == null) {
			examID = 0;
		} else {
			examID = Integer.valueOf(examLastID);
		}
		String finalExamID = subjectID + courseID + String.format("%02d", ++examID);

		try {
			prepareStatement = conn.prepareStatement("INSERT INTO Exam VALUES (?,?,?,?,?,?,?,?);");
			prepareStatement.setString(1, finalExamID);
			prepareStatement.setString(2, exam.getTeacherID());
			prepareStatement.setString(3, exam.getTeacherName());
			prepareStatement.setString(4, exam.getSubject());
			prepareStatement.setString(5, exam.getCourse());
			prepareStatement.setString(6, exam.getDuration());
			prepareStatement.setString(7, exam.getTeacherNote());
			prepareStatement.setString(8, exam.getStudentNote());

			int resultSet = prepareStatement.executeUpdate();
			if (resultSet == 1) {
				for (ExamQuestion question : exam.getExamQuestions()) {
					saveExamQuestion(question, finalExamID);
				}
				System.out.print("Exam Saved Succuessfully");
				return true;
			}

		} catch (SQLException e) {
			System.err.print("Error occurred, Exam has not been saved ");
			return false;
		}

		return true;
	}

	/**
	 * Saving question to the given exam using appropriate query by prepared
	 * statement.
	 * 
	 * @param examQuestion questions of exam
	 * @param examID       exam id
	 */
	private void saveExamQuestion(ExamQuestion examQuestion, String examID) {
		PreparedStatement prepareStatement;
		try {
			prepareStatement = conn.prepareStatement("INSERT INTO ExamQuestion VALUES (?,?,?);");
			prepareStatement.setString(1, examQuestion.getQuestionID());
			prepareStatement.setInt(2, examQuestion.getPoints());
			prepareStatement.setString(3, examID);
			int resultSet = prepareStatement.executeUpdate();
			if (resultSet == 1) {
				System.out.print("Question Saved Succuessfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.print("Error occurred, Question has not been saved ");
		}

	}

	/**
	 * Saving new question on database using appropriate query by prepared
	 * statement.
	 * 
	 * @param question that's needed to be save on database
	 * @return boolean value{true = question saved successfully,false = can't save
	 *         test}
	 */
	public boolean saveQuestion(Question question) {
		int questionID;
		String subjectID = Server.getSubjectCollection().getSubjectMap().get(question.getSubject());
		String questionLastID = getQuestionLastId(question.getSubject());
		if (questionLastID == null) {
			questionID = 0;
		} else {
			questionID = Integer.valueOf(questionLastID);
		}
		String finalID = subjectID + String.format("%03d", ++questionID);
		PreparedStatement prepareStatement;
		try {
			prepareStatement = conn.prepareStatement("INSERT INTO Question VALUES (?,?,?,?,?,?,?,?,?);");
			prepareStatement.setString(1, finalID);
			prepareStatement.setString(2, question.getSubject());
			prepareStatement.setString(3, question.getDetails());
			prepareStatement.setString(4, question.getAnswer1());
			prepareStatement.setString(5, question.getAnswer2());
			prepareStatement.setString(6, question.getAnswer3());
			prepareStatement.setString(7, question.getAnswer4());
			prepareStatement.setInt(8, question.getCorrectAnswer());
			prepareStatement.setString(9, question.getTeacherName());
			int resultSet = prepareStatement.executeUpdate();
			if (resultSet == 1) {
				System.out.print("Question Saved Succuessfully");
				return true;
			}

		} catch (SQLException e) {
			System.err.print("Error occurred, Question has not been saved ");
		}
		return false;
	}

	/**
	 * Get the last digits of the question using appropriate query by prepared
	 * statement.
	 * 
	 * @param subject, get the last digits of the question by subject given.
	 * @return String {return last digits of the question} Else {return error -
	 *         can't find last digits of the question }.
	 */
	private String getQuestionLastId(String subject) {
		try {
			Statement statement = conn.createStatement();
			String sql = ("SELECT SUBSTRING(questionID, 3, 5) questionID FROM question WHERE Subject=\"" + subject
					+ "\" ORDER BY questionID DESC LIMIT 1;");
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String id = resultSet.getString("questionID");
				return id;
			}
		} catch (SQLException e) {
			return null;
		}

		return null;
	}

	/**
	 * Get the last digits of the examID using appropriate query by prepared
	 * statement.
	 * 
	 * @param subject subject
	 * @param course  course
	 * @return exam last id
	 */
	private String getExamLastId(String subject, String course) {
		try {
			Statement statement = conn.createStatement();
			String sql = "SELECT SUBSTRING(examID, 5, 6) examID FROM exam WHERE Subject=" + "\"" + subject + "\""
					+ " AND Course=" + "\"" + course + "\"" + " ORDER BY examID DESC LIMIT 1;";
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				String id = resultSet.getString("examID");
				return id;
			}
		} catch (SQLException e) {
			return null;
		}

		return null;
	}

	/**
	 * * Get specific user by given user name and password from database test table,
	 * using appropriate query and SQL statement.
	 * 
	 * @param userID   user id
	 * @param password password
	 * @return user
	 */
	public User getUser(String userID, String password) {
		User user = null;

		if (userID == null || password == null)
			return null;

		try {
			Statement statement = conn.createStatement();
			String sql = ("SELECT * FROM User WHERE userID=" + userID + ";");
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String email = resultSet.getString("email");
				String passwordDB = resultSet.getString("password");
				UserType type = UserType.valueOf(resultSet.getString("type"));

				if (password.equals(passwordDB))
					user = new User(userID, password, firstName, lastName, email, type);
				else
					user = new User(ErrorType.WRONG_PASSWORD);

			} else
				user = new User(ErrorType.WRONG_ID);

		} catch (SQLException e) {
			System.err.println("User not found");
			return user;
		}

		return user;
	}

	public SubjectCourseCollection updateSubjectCollection(SubjectCourseCollection subjectCollection) {
		subjectCollection = new SubjectCourseCollection();
		Map<String, String> subjectCodeMap = new HashMap<>();
		Map<String, String> courseCodeMap = new HashMap<>();
		Map<String, List<String>> courseListMap = new HashMap<>();
		List<String> subjectList = new ArrayList<>();
		List<String> courseList = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String subjectSQL = "SELECT * FROM Subject";
			ResultSet resultSetSubject = statement.executeQuery(subjectSQL);

			while (resultSetSubject.next()) {
				String subjectID = resultSetSubject.getString("subjectID");
				String subject = resultSetSubject.getString("subjectName");
				subjectCodeMap.put(subject, subjectID);
				subjectList.add(subject);
				String courseSQL = "SELECT * FROM Course WHERE subjectID=" + subjectID + ";";
				Statement statement2 = conn.createStatement();
				ResultSet resultSetCourse = statement2.executeQuery(courseSQL);
				List<String> subjectCourseList = new ArrayList<>();

				while (resultSetCourse.next()) {
					String courseID = resultSetCourse.getString("courseID");
					String course = resultSetCourse.getString("courseName");
					courseCodeMap.put(course, courseID);
					subjectCourseList.add(course);
					courseList.add(course);
				}
				courseListMap.put(subject, subjectCourseList);
			}
			subjectCollection.setSubjects(subjectList);
			subjectCollection.setCourses(courseList);
			subjectCollection.setCourseListMap(courseListMap);
			subjectCollection.setSubjectMap(subjectCodeMap);
			subjectCollection.setCourseMap(courseCodeMap);

		} catch (SQLException e) {
			System.err.println("Can't collect list");
			return subjectCollection;
		}

		return subjectCollection;
	}

	/**
	 * Get all questions which is related wit a specific subject using appropriate
	 * query by prepared statement.
	 * 
	 * @param subject subject
	 * @return list of Question
	 */
	public List<Question> getQuestionList(String subject) {
		List<Question> questionList = new ArrayList<>();
		String courseQuery;
		if (subject == null) {
			courseQuery = "SELECT * FROM Question;";
		} else {
			courseQuery = "SELECT * FROM Question WHERE Subject=\"" + subject + "\";";
		}

		try {
			Statement statement = conn.createStatement();

			ResultSet rsQuestionOfCourse = statement.executeQuery(courseQuery);
			while (rsQuestionOfCourse.next()) {
				String questionID = rsQuestionOfCourse.getString("questionID");
				String teacherName = rsQuestionOfCourse.getString("TeacherName");
				String subjectRet = rsQuestionOfCourse.getString("Subject");
				String details = rsQuestionOfCourse.getString("Details");
				String answer1 = rsQuestionOfCourse.getString("Answer1");
				String answer2 = rsQuestionOfCourse.getString("Answer2");
				String answer3 = rsQuestionOfCourse.getString("Answer3");
				String answer4 = rsQuestionOfCourse.getString("Answer4");
				int correctAnswer = rsQuestionOfCourse.getInt("CorrectAnswer");
				Question q = new Question(questionID, teacherName, subjectRet, details, answer1, answer2, answer3,
						answer4, correctAnswer);
				questionList.add(q);
			}

		} catch (SQLException e) {
			System.err.println("ERROR #22132 - ERROR LOADING QUSETION FROM DATABASE");
		}

		return questionList;
	}

	// Belong to overall statisti - Statistic by course:
	// (i should change to ll 3 statuse by enum operation)
	public List<ExecutedExam> getGradesForStatisticByCourse(String course_select) {
		List<ExecutedExam> set = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String courseStatistic = "select *\r\n" + "from executedexam\r\n" + "where course = \"" + course_select
					+ "\";";
			ResultSet rsGetStatisticByCourse = statement.executeQuery(courseStatistic);
			while (rsGetStatisticByCourse.next()) {
				String examID = rsGetStatisticByCourse.getString("examID");
				String executeTeacherID = rsGetStatisticByCourse.getString("executeTeacherID");
				String teacherName = getUserName(executeTeacherID);
				String date = rsGetStatisticByCourse.getString("executeDate");
				String time = rsGetStatisticByCourse.getString("executeTime");
				double avg = rsGetStatisticByCourse.getDouble("avg");
				double median = rsGetStatisticByCourse.getDouble("median");
				ExecutedExam ExamListByCourse = new ExecutedExam(examID, executeTeacherID, teacherName, date, time, avg,
						median);
				set.add(ExamListByCourse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #23142 - ERROR LOADING EXAM FROM DATABASE");
		}
		return set;
	}

	public List<StudentExecutedExam> getGradeStatisticByStudent(String studentID_select) {
		List<StudentExecutedExam> examListByStudent = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String studentStatistic = "SELECT * FROM executedexambystudent where studentID = \"" + studentID_select
					+ "\";";
			ResultSet rsGtadeStatisticByStudent = statement.executeQuery(studentStatistic);
			while (rsGtadeStatisticByStudent.next()) {
				String Course = rsGtadeStatisticByStudent.getString("Course");
				String Grade = rsGtadeStatisticByStudent.getString("Grade");
				String execDate = rsGtadeStatisticByStudent.getString("ExecDate");

				StudentExecutedExam student = new StudentExecutedExam(Course, Grade, execDate);
				examListByStudent.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #23143 - ERROR LOADING StudenExactutedExam FROM DATABASE");
		}
		return examListByStudent;
	}

	/**
	 * Get all exams who's has a specific subject using appropriate query by
	 * prepared statement.
	 * 
	 * @param subject subject
	 * @return list of Exam
	 */
	public List<Exam> getExamListBySubject(String subject) {
		List<Exam> examList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			String courseQuery = "SELECT * FROM Exam WHERE Subject=\"" + subject + "\";";
			ResultSet rsQuestionOfCourse = statement.executeQuery(courseQuery);
			while (rsQuestionOfCourse.next()) {
				String examID = rsQuestionOfCourse.getString("examID");
				String teacherID = rsQuestionOfCourse.getString("teacherID");
				String teacherName = rsQuestionOfCourse.getString("teacherName");
				String course = rsQuestionOfCourse.getString("Course");
				String duration = rsQuestionOfCourse.getString("Duration");
				String studentNote = rsQuestionOfCourse.getString("StudentNote");
				String teacherNote = rsQuestionOfCourse.getString("TeacherNote");

				List<ExamQuestion> questionsList = getExamQuestionsList(examID);
				Exam exam = new Exam(examID, teacherID, subject, course, duration, questionsList, studentNote,
						teacherNote);
				exam.setTeacherName(teacherName);
				examList.add(exam);
			}

		} catch (SQLException e) {
			System.err.println("ERROR #22132 - ERROR LOADING EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Get all exams who's has a specific course using appropriate query by prepared
	 * statement.
	 * 
	 * @param course course
	 * @return list of Exam
	 */
	public List<Exam> getExamListByCourse(String course) {
		List<Exam> examList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			String courseQuery = "SELECT * FROM Exam WHERE Course=\"" + course + "\";";
			ResultSet rsQuestionOfCourse = statement.executeQuery(courseQuery);
			while (rsQuestionOfCourse.next()) {
				String examID = rsQuestionOfCourse.getString("examID");
				String teacherID = rsQuestionOfCourse.getString("teacherID");
				String teacherName = rsQuestionOfCourse.getString("teacherName");
				String subject = rsQuestionOfCourse.getString("Subject");
				String duration = rsQuestionOfCourse.getString("Duration");
				String studentNote = rsQuestionOfCourse.getString("StudentNote");
				String teacherNote = rsQuestionOfCourse.getString("TeacherNote");

				List<ExamQuestion> questionsList = getExamQuestionsList(examID);
				Exam exam = new Exam(examID, teacherID, subject, course, duration, questionsList, studentNote,
						teacherNote);
				exam.setTeacherName(teacherName);
				examList.add(exam);
			}

		} catch (SQLException e) {
			System.err.println("ERROR #223637 - ERROR LOADING EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Get all the questions in a specific exam using appropriate query by prepared
	 * statement.
	 * 
	 * @param examID exam id
	 * @return list of ExamQuestion
	 */
	public List<ExamQuestion> getExamQuestionsList(String examID) {

		List<ExamQuestion> examQuestionsList = new ArrayList<>();
		try {
			Statement statement = conn.createStatement();
			String examQuestionQuery = "SELECT * FROM examquestion WHERE examID=\"" + examID + "\";";
			ResultSet rsexamQuestion = statement.executeQuery(examQuestionQuery);
			while (rsexamQuestion.next()) {
				String questionID = rsexamQuestion.getString("questionID");
				int points = rsexamQuestion.getInt("point");

				Statement statement2 = conn.createStatement();
				String questionQuery = "SELECT * FROM question WHERE questionID=\"" + questionID + "\";";
				ResultSet rsQuestion = statement2.executeQuery(questionQuery);
				if (rsQuestion.next()) {
					String subject = rsQuestion.getString("Subject");
					String details = rsQuestion.getString("Details");
					String answer1 = rsQuestion.getString("Answer1");
					String answer2 = rsQuestion.getString("Answer2");
					String answer3 = rsQuestion.getString("Answer3");
					String answer4 = rsQuestion.getString("Answer4");
					int correctAnswer = rsQuestion.getInt("CorrectAnswer");
					String teacherName = rsQuestion.getString("TeacherName");

					Question question = new Question(questionID, teacherName, subject, details, answer1, answer2,
							answer3, answer4, correctAnswer);
					ExamQuestion examQuestion = new ExamQuestion(question, points);
					examQuestionsList.add(examQuestion);
				}

			}

		} catch (SQLException e) {
			System.err.println("ERROR #22642 - ERROR LOADING QUESTION FROM DATABASE");
		}
		return examQuestionsList;
	}

	/**
	 * Get all the exams using appropriate query by prepared statement.
	 * 
	 * @return list of exam
	 */
	public List<Exam> getExamList() {
		List<Exam> examList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			String courseQuery = "SELECT * FROM Exam;";
			ResultSet rsQuestionOfCourse = statement.executeQuery(courseQuery);
			while (rsQuestionOfCourse.next()) {
				String examID = rsQuestionOfCourse.getString("examID");
				String teacherID = rsQuestionOfCourse.getString("teacherID");
				String teacherName = rsQuestionOfCourse.getString("teacherName");
				String subject = rsQuestionOfCourse.getString("Subject");
				String duration = rsQuestionOfCourse.getString("Duration");
				String course = rsQuestionOfCourse.getString("Course");
				String studentNote = rsQuestionOfCourse.getString("StudentNote");
				String teacherNote = rsQuestionOfCourse.getString("TeacherNote");
				List<ExamQuestion> questionsList = getExamQuestionsList(examID);
				Exam exam = new Exam(examID, teacherID, subject, course, duration, questionsList, studentNote,
						teacherNote);
				exam.setTeacherName(teacherName);
				examList.add(exam);
			}

		} catch (SQLException e) {
			System.err.println("ERROR #223637 - ERROR LOADING EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Get all the executed exams by the specific student using appropriate query by
	 * prepared statement.
	 * 
	 * @param studentID student id
	 * @return list of StudentExecutedExam
	 */
	public List<StudentExecutedExam> getExecutedExamListByStudentID(String studentID) {
		List<StudentExecutedExam> examList = new ArrayList<>();
		InputStream copyFile = null;
		WordFile checked_file = new WordFile();
		try {
			Statement statement = conn.createStatement();
			String courseQuery = "SELECT * FROM executedexambystudent WHERE studentID=\"" + studentID + "\";";
			ResultSet rsQuestionOfCourse = statement.executeQuery(courseQuery);
			while (rsQuestionOfCourse.next()) {
				String examID = rsQuestionOfCourse.getString("ExamID");
				String teacherID = rsQuestionOfCourse.getString("teacherID");
				String subject = rsQuestionOfCourse.getString("Subject");
				String course = rsQuestionOfCourse.getString("Course");
				String execDate = rsQuestionOfCourse.getString("ExecDate");
				String testType = rsQuestionOfCourse.getString("TestType");
				String grade = rsQuestionOfCourse.getString("Grade");
				String comment = rsQuestionOfCourse.getString("comment");
				boolean approval = rsQuestionOfCourse.getBoolean("Approved");
				Blob copy = rsQuestionOfCourse.getBlob("Copy");
				String alert = rsQuestionOfCourse.getString("Alert");

				if (testType != "Manual" || copy.length() == 0) {
					checked_file.setSize(0);
				} else {
					copyFile = copy.getBinaryStream();

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					int nRead;
					byte[] data = new byte[16384];

					try {
						while ((nRead = copyFile.read(data, 0, data.length)) != -1) {
							buffer.write(data, 0, nRead);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					byte[] arr = buffer.toByteArray();

					checked_file.initArray(arr.length);
					checked_file.setSize(arr.length);
					checked_file.setMybytearray(arr);
				}
				String studentName = getUserName(studentID);
				StudentExecutedExam exam = new StudentExecutedExam(examID, studentID, studentName, teacherID, subject,
						course, execDate, testType, grade, alert, approval, comment);

				examList.add(exam);
			}

		} catch (SQLException e) {
			System.err.println("ERROR #223688 - ERROR LOADING EXECUTED EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Saving the word file of specific student who's uploaded word file appropriate
	 * query by prepared statement.
	 * 
	 * @param studentExam student exam
	 * @param file        file
	 */
	public void UploadFile(StudentExecutedExam studentExam, WordFile file) {
		String sql = "UPDATE ExecutedExamByStudent SET Copy = ? WHERE studentID = ? AND examID = ? AND execDate = ?;";

		try {
			InputStream targetStream = new ByteArrayInputStream(file.getMybytearray());
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setBlob(1, targetStream);
			stmt.setString(2, studentExam.getStudentID());
			stmt.setString(3, studentExam.getExamID());
			stmt.setString(4, studentExam.getExecDate());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Saving specific student and exam information which has entered to the
	 * specific exam using appropriate query by prepared statement.
	 * 
	 * @param studentID student id
	 * @param exam      exam
	 * @return true if inserted else false
	 */
	public boolean insertToExecutedExamByStudent(String studentID, ExamProcess exam) {
		String sql = "INSERT INTO executedexambystudent VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, studentID);
			stmt.setString(2, exam.getExamId());
			stmt.setString(3, exam.getTeacherID());
			stmt.setString(4, exam.getSubject());
			stmt.setString(5, exam.getCourse());
			stmt.setString(6, exam.getDate());
			stmt.setString(7, exam.getType().toString());
			stmt.setString(8, "0");
			stmt.setString(9, null);
			stmt.setString(10, "0");
			stmt.setBoolean(11, false);
			stmt.setBoolean(12, false);
			stmt.setString(13, null);
			stmt.setString(14, null);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #223980 - ERROR INSERTING STUDENT TO EXAM IN DATABASE");
		}
		return false;
	}

	/**
	 * Get specific examID by the user Code using appropriate query by prepared
	 * statement.
	 * 
	 * @param userCode we want to get the examID with the user code.
	 * @return String {get the examID by the specific userCode} Else {return error -
	 *         can't find the examID in DB}
	 */
	public String GetExamID(String userCode) {
		String sql = "SELECT examID FROM examprocess WHERE code = " + userCode;
		String examID = "";
		try {
			Statement statement = conn.createStatement();
			ResultSet examID_RS = statement.executeQuery(sql);

			if (examID_RS.next()) {
				examID = examID_RS.getString("ExamID");
			}
			return examID;

		} catch (SQLException e) {
			System.err.println("ERROR #223982 - EXAM ID NOT EXIST FOR CODE " + userCode);
		}
		return null;
	}

	/**
	 * Get specific exam by the examID using appropriate query by prepared
	 * statement.
	 * 
	 * @param examID we want to get the Exam with the examID.
	 * @return Exam {get the exactly exam with the specific examID} Else {return
	 *         error - can't find the examID in DB}
	 */
	public Exam GetExamByExamID(String examID) {
		Exam exam;

		try {
			Statement statement = conn.createStatement();
			String Query = "SELECT * FROM exam WHERE examID=\"" + examID + "\";";
			ResultSet rs = statement.executeQuery(Query);
			if (rs.next()) {
				String teacherID = rs.getString("teacherID");
				String subject = rs.getString("Subject");
				String course = rs.getString("Course");
				String duration = rs.getString("Duration");
				String teacherName = rs.getString("teacherName");
				String studentNote = rs.getString("StudentNote");
				String teacherNote = rs.getString("TeacherNote");
				List<ExamQuestion> questionsList = getExamQuestionsList(examID);
				exam = new Exam(examID, teacherID, subject, course, duration, questionsList, studentNote, teacherNote);
				exam.setTeacherName(teacherName);
				return exam;
			}

		} catch (SQLException e) {
			System.err.println("ERROR #223688 - ERROR LOADING EXECUTED EXAM FROM DATABASE");
		}

		return null;
	}

	/**
	 * Saving student grade to specific student in a specific exam using appropriate
	 * query by prepared statement.
	 * 
	 * @param studentID    student id
	 * @param examID       exam id
	 * @param teacherID    teacher id
	 * @param grade        grade
	 * @param execDuration exec duration
	 */
	public void insertFinishedStudent(String studentID, String examID, String teacherID, String grade,
			String execDuration) {
		String sql = "UPDATE ExecutedExamByStudent SET Grade = ? , ExecDuration = ? WHERE studentID = ? AND examID = ? AND ExecDate = ?;";
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDateTime now = LocalDateTime.now();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, grade);
			stmt.setString(2, execDuration);
			stmt.setString(3, studentID);
			stmt.setString(4, examID);
			stmt.setString(5, dtf.format(now));
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Saving executed exam with the exam which has been executed using appropriate
	 * query by prepared statement.
	 * 
	 * @param exam to save the exam which has been executed.
	 * @return boolean value{true = Exam saved successfully, false = can't save
	 *         exam}
	 */
	public boolean saveExecutedExam(ExamProcess exam) {
		PreparedStatement prepareStatement;
		String creatorTeacherID = getCreatorTeacherID(exam.getExamId());
		creatorTeacherID = creatorTeacherID != null ? creatorTeacherID : "";
		try {
			prepareStatement = conn.prepareStatement("INSERT INTO ExecutedExam VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");
			prepareStatement.setString(1, exam.getExamId());
			prepareStatement.setString(2, exam.getTeacherID());
			prepareStatement.setString(3, creatorTeacherID);
			prepareStatement.setString(4, exam.getSubject());
			prepareStatement.setString(5, exam.getCourse());
			prepareStatement.setString(6, exam.getDuration());
			prepareStatement.setString(7, exam.getDate());
			prepareStatement.setString(8, exam.getTime());
			prepareStatement.setString(9, exam.getFinishedStudentsCount());
			prepareStatement.setBoolean(10, false);
			prepareStatement.setDouble(11, 0);
			prepareStatement.setDouble(12, 0);
			prepareStatement.setString(13, exam.getType().toString());
			int resultSet = prepareStatement.executeUpdate();
			if (resultSet == 1) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.print("Error occurred, Exam has not been saved ");
			return false;
		}

		return true;

	}

	/**
	 * Get all the executed exams by the specific executor teacher using appropriate
	 * query by prepared statement.
	 * 
	 * @param loggedInTeacherId the id which we want to find all the executed exams
	 * @return list of ExecutedExam
	 */
	public List<ExecutedExam> getExecutedExamListByExecutorTeacherID(String loggedInTeacherId) {
		List<ExecutedExam> examList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			String executedExamQuery = "SELECT * FROM executedExam WHERE executeTeacherID=\"" + loggedInTeacherId
					+ "\";";
			ResultSet rsExecutedExam = statement.executeQuery(executedExamQuery);
			while (rsExecutedExam.next()) {
				String examID = rsExecutedExam.getString("ExamID");
				String subject = rsExecutedExam.getString("subject");
				String course = rsExecutedExam.getString("course");
				String executeDate = rsExecutedExam.getString("executeDate");
				String executeTime = rsExecutedExam.getString("executeTime");
				String type = rsExecutedExam.getString("type");
				String executeTeacherID = rsExecutedExam.getString("executeTeacherID");
				String teacherName = getUserName(executeTeacherID);
				double avg = rsExecutedExam.getDouble("avg");
				boolean approved = rsExecutedExam.getBoolean("Approved");

				ExecutedExam executedExam = new ExecutedExam(examID, teacherName, executeTeacherID, subject, course,
						executeDate, executeTime, type, approved, avg);
				examList.add(executedExam);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #223688 - ERROR LOADING EXECUTED EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Get all the executed exams by the specific teacher who's created the exams
	 * using appropriate query by prepared statement.
	 * 
	 * @param loggedInTeacherId the id which we want to find all the executed exams
	 * @return list of ExecutedExam
	 */
	public List<ExecutedExam> getExecutedExamListByCreatorTeacherID(String loggedInTeacherId) {
		List<ExecutedExam> examList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			String executedExamQuery = "SELECT * FROM executedExam WHERE creatorTeacherID=\"" + loggedInTeacherId
					+ "\";";
			ResultSet rsExecutedExam = statement.executeQuery(executedExamQuery);
			while (rsExecutedExam.next()) {
				String examID = rsExecutedExam.getString("ExamID");
				String subject = rsExecutedExam.getString("subject");
				String course = rsExecutedExam.getString("course");
				String executeDate = rsExecutedExam.getString("executeDate");
				String executeTime = rsExecutedExam.getString("executeTime");
				String type = rsExecutedExam.getString("type");
				String executeTeacherID = rsExecutedExam.getString("executeTeacherID");
				String finishedStudentsCount = rsExecutedExam.getString("finishedStudentsCount");
				double avg = rsExecutedExam.getDouble("avg");
				double median = rsExecutedExam.getDouble("median");
				String teacherName = getUserName(executeTeacherID);
				boolean approved = rsExecutedExam.getBoolean("Approved");

				if (approved) {

					ExecutedExam executedExam = new ExecutedExam(examID, subject, course, executeTeacherID, teacherName,
							executeDate, executeTime, finishedStudentsCount, type, avg, median, approved);

					examList.add(executedExam);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #223688 - ERROR LOADING EXECUTED EXAM FROM DATABASE");
		}

		return examList;
	}

	/**
	 * Get teacherID who's created specific Exam using appropriate query by prepared
	 * statement.
	 * 
	 * @param examID we want to get the teacherID who's created by.
	 * @return String {TeacherID} Else {return error - can't find the exam in DB}
	 */
	private String getCreatorTeacherID(String examID) {
		try {
			Statement statement = conn.createStatement();
			String examQuery = "SELECT * FROM Exam WHERE examID=\"" + examID + "\";";
			ResultSet rsExam = statement.executeQuery(examQuery);
			if (rsExam.next()) {
				String teacherID = rsExam.getString("teacherID");
				return teacherID;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #223688 - ERROR LOADING EXAM FROM DATABASE");
		}
		return null;
	}

	/**
	 * Get user first name + last name using appropriate query by prepared
	 * statement.
	 * 
	 * @param userID user id
	 * @return user name
	 */
	private String getUserName(String userID) {
		try {
			Statement statement = conn.createStatement();
			String examQuery = "SELECT * FROM User WHERE userID=\"" + userID + "\";";
			ResultSet rsExam = statement.executeQuery(examQuery);
			if (rsExam.next()) {
				String firstName = rsExam.getString("FirstName");
				String lastName = rsExam.getString("LastName");
				return firstName + " " + lastName;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #2245688 - ERROR LOADING USER INFO FROM DATABASE");
		}
		return null;
	}

	/**
	 * Get all the students who's executed a specific exam using appropriate query
	 * by prepared statement.
	 * 
	 * @param examID          exam id
	 * @param date            date
	 * @param executerTeacher executor teacher
	 * @return list of StudentExecutedExam
	 */
	public List<StudentExecutedExam> getExecutedExamStudentList(String examID, String date, String executerTeacher) {
		List<StudentExecutedExam> studentList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();

			String studentListQuery = "SELECT * FROM ExecutedExamByStudent WHERE examID=\"" + examID
					+ "\" AND ExecDate=\"" + date + "\" AND teacherID=\"" + executerTeacher + "\";";
			ResultSet rs = statement.executeQuery(studentListQuery);
			while (rs.next()) {
				String studentID = rs.getString("studentID");
				String teacherID = rs.getString("teacherID");
				String subject = rs.getString("Subject");
				String course = rs.getString("Course");
				String execDate = rs.getString("ExecDate");
				String testType = rs.getString("TestType");
				String grade = rs.getString("Grade");
				String comment = rs.getString("comment");
				// Blob blob = rs.getBlob("Copy");
				// InputStream inputStream = blob.getBinaryStream();
				// WordFile copy = new WordFile();
				boolean approved = rs.getBoolean("Approved");
				String alert = rs.getString("Alert");
				String studentName = getUserName(studentID);

				StudentExecutedExam executedStudent = new StudentExecutedExam(examID, studentID, studentName, teacherID,
						subject, course, execDate, testType, grade, alert, approved, comment);

				studentList.add(executedStudent);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #2245688 - ERROR LOADING USER INFO FROM DATABASE");
		}

		return studentList;
	}

	/**
	 * Update alert chance to a specific student in specific exam using appropriate
	 * query by prepared statement.
	 * 
	 * @param studentID    student id
	 * @param examID       exam id
	 * @param AlertPercent alert percentage
	 */
	public void updateAlertValue(String studentID, String examID, String AlertPercent) {
		String sql = "UPDATE ExecutedExamByStudent SET Alert = ? WHERE studentID = ? AND examID = ? AND ExecDate = ?;";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, AlertPercent);
			stmt.setString(2, studentID);
			stmt.setString(3, examID);
			stmt.setString(4, dtf.format(now));
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method update the approved column on list of executed exams
	 * 
	 * @param approvedStudents students list to approve
	 * @return true on success else false
	 */
	public boolean saveApprovedStudents(List<StudentExecutedExam> approvedStudents) {

		String query = "UPDATE executedexambystudent SET Approved = ?, Grade = ? ,comment = ? WHERE studentID = ? AND examID = ? AND ExecDate= ?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			for (StudentExecutedExam student : approvedStudents) {
				stmt.setBoolean(1, student.isApproved());
				stmt.setString(2, student.getGrade());
				stmt.setString(3, student.getComment());
				stmt.setString(4, student.getStudentID());
				stmt.setString(5, student.getExamID());
				stmt.setString(6, student.getExecDate());
				stmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * This method update statistic data in the exam row in the DB
	 * 
	 * @param executedExam The exam object
	 */
	public void updateStatistic(ExecutedExam executedExam) {
		String query = "UPDATE executedexam SET avg = ?, median = ? , Approved = ? WHERE examID = ? AND executeDate = ? AND executeTeacherID= ?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDouble(1, executedExam.getAvg());
			stmt.setDouble(2, executedExam.getMedian());
			stmt.setBoolean(3, true);
			stmt.setString(4, executedExam.getId());
			stmt.setString(5, executedExam.getExecDate());
			stmt.setString(6, executedExam.getTeacherID());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method insert student exam answers into the DB
	 * 
	 * @param studentList The list of students in the exam
	 * @param code        The code of the exam
	 * @return true on success else false
	 */
	public boolean insertStudentAnswers(List<StudentInExam> studentList, String code) {
		if (studentList != null) {
			PreparedStatement prepareStatement;
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDateTime now = LocalDateTime.now();
				for (StudentInExam student : studentList) {
					if (student.getSolution() != null) {
						prepareStatement = conn
								.prepareStatement("INSERT INTO StudentComputerizedAnswers VALUES (?,?,?,?);");
						prepareStatement.setString(1, student.getStudentID());
						prepareStatement.setString(2, Server.getExamsInProcess().get(code).getExamId());
						prepareStatement.setString(3, dtf.format(now));
						StringBuilder stringBuilder = new StringBuilder();
						for (int i = 0; i < student.getSolution().length; i++) {
							stringBuilder.append(student.getSolution()[i]);
						}
						prepareStatement.setString(4, stringBuilder.toString());
						prepareStatement.executeUpdate();
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Error occurred, Answers has not been saved ");
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * This method get specific exam questions answers of student
	 * 
	 * @param studentID The ID of the student
	 * @param examID    The exam id of the exam
	 * @param date      The date the exam occurred on
	 * @return selected answers of student exam
	 */
	public String getSelectedAnswers(String studentID, String examID, String date) {
		String selectedAnswers = null;
		try {
			Statement statement = conn.createStatement();

			String studentTestReport = "SELECT * FROM StudentComputerizedAnswers WHERE studentID = \"" + studentID
					+ "\" AND examID=\"" + examID + "\" AND ExecDate=\"" + date + "\";";
			ResultSet rs = statement.executeQuery(studentTestReport);
			while (rs.next()) {
				selectedAnswers = rs.getString("Answers");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("ERROR #5555 - ERROR LOADING USER INFO FROM DATABASE");
		}

		return selectedAnswers;
	}

	/*
	 * questionID varchar(255), Subject varchar(255), Details varchar(255), Answer1
	 * varchar(255), Answer2 varchar(255), Answer3 varchar(255), Answer4
	 * varchar(255), CorrectAnswer int, TeacherName varchar(255), PRIMARY
	 * KEY(questionID)
	 */

	public void updateQuestion(Question editedQuestion) {
		String query = "UPDATE Question SET Details = ? , Answer1 = ? , Answer2 = ? , Answer3 = ? , Answer4 = ? , CorrectAnswer = ? WHERE questionID = ?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, editedQuestion.getDetails());
			stmt.setString(2, editedQuestion.getAnswer1());
			stmt.setString(3, editedQuestion.getAnswer2());
			stmt.setString(4, editedQuestion.getAnswer3());
			stmt.setString(5, editedQuestion.getAnswer4());
			stmt.setInt(6, editedQuestion.getCorrectAnswer());
			stmt.setString(7, editedQuestion.getQuestionID());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateExam(Exam oldExam, Exam newExam) {

		String query = "UPDATE Exam SET Duration = ? , TeacherNote = ? , StudentNote = ? WHERE examID = ?;";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, newExam.getDuration());
			stmt.setString(2, newExam.getTeacherNote());
			stmt.setString(3, newExam.getStudentNote());
			stmt.setString(4, newExam.getId());
			stmt.executeUpdate();

			updateExamQuestionList(oldExam, newExam);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateExamQuestionList(Exam oldExam, Exam newExam) {
		String query = "DELETE FROM ExamQuestion WHERE examID = ? AND questionID = ?;";
		try {
			PreparedStatement stmt;
			for (ExamQuestion question : oldExam.getExamQuestions()) {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, oldExam.getId());
				stmt.setString(2, question.getQuestionID());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (ExamQuestion question : newExam.getExamQuestions()) {
			saveExamQuestion(question, newExam.getId());
		}
	}

}
