package server;

import static common.ModelWrapper.Operation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ModelWrapper;
import common.SubjectCourseCollection;
import models.Database;
import models.Exam;
import models.ExamExtension;
import models.ExamProcess;
import models.ExamProcess.ExamType;
import models.ExamQuestion;
import models.ExecutedExam;
import models.Question;
import models.StudentExecutedExam;
import models.StudentInExam;
import models.User;
import models.WordFile;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * The Server class handle all the server-client communication and transfer the
 * data to the DatabaseController for saving in DB
 * 
 * * @author -------Group 9-------- Arik Zagdon, Dvir ben simon, Aviel Turgeman,
 * Shenhav Hezi, Yaakov Shitrit
 */
public class Server extends AbstractServer {

	/**
	 * Server event listener in order to handle events that occurred by server, send
	 * to server user interface log.
	 */
	private ServerEventListener serverListener;

	/**
	 * Store database controller instance created by the constructor, in order to
	 * make transaction with database.
	 */
	private DatabaseController databaseController;

	/** Value that hold the user, use to login and and open appropriate menu */
	private static SubjectCourseCollection subjectCourseCollection;

	/** Map for exams in process */
	private static Map<String, ExamProcess> examsInProcess;

	/** Map for exam time extensions */
	private static Map<String, List<ExamExtension>> examsExtensions;

	/** map for students in exam */
	private static Map<String, List<StudentInExam>> studentInExam;

	/**
	 * Variable that hold all the connected users in the system
	 */
	private static List<String> connectedUsers;

	/** Indicate if the server is connected */
	private static boolean isConnected = false;

	/**
	 * Creating new server connection with port given by constructor
	 * 
	 * @param port number
	 */
	public Server(int port) {
		super(port);
	}

	/**
	 * @param logListener server event listener, in order to handle event and update
	 *                    the log
	 * @param database    details instance to create database instance
	 * @param serverPort
	 */
	public Server(ServerEventListener logListener, Database database, String serverPort) {
		super(Integer.parseInt(serverPort));
		this.serverListener = logListener;
		databaseController = new DatabaseController(database, logListener);
		examsInProcess = new HashMap<>();
		studentInExam = new HashMap<>();
		examsExtensions = new HashMap<>();
		connectedUsers = new ArrayList<>();
	}

	/**
	 * Function calls when client send message, the client send messages throw this
	 * function.
	 * 
	 * @param msg: get the returning message from client
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		ModelWrapper<?> modelWrapperFromClient = (ModelWrapper<?>) msg;
		ModelWrapper<?> modelWrapperToClient = null;

		switch (modelWrapperFromClient.getOperation()) {

		case GET_QUESTION_LIST:
			List<Question> questionList = databaseController.getQuestionList(null);
			modelWrapperToClient = new ModelWrapper<Question>(questionList, GET_QUESTION_LIST);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXTENSION_REQUESTS:
			List<ExamExtension> examExtensionList = new ArrayList<>();
			for (Map.Entry<String, List<ExamExtension>> entry : examsExtensions.entrySet()) {
				List<ExamExtension> examExtension = entry.getValue();
				for (ExamExtension exam : examExtension) {
					examExtensionList.add(exam);
				}
			}
			modelWrapperToClient = new ModelWrapper<>(examExtensionList, GET_EXTENSION_REQUESTS);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case EXTENSION_CONFIRM:
			ExamExtension extension = (ExamExtension) modelWrapperFromClient.getElement();
			try {
				List<StudentInExam> studentList = studentInExam.get(extension.getCode());
				modelWrapperToClient = new ModelWrapper<>(extension.getTimeExtension(), STUDENT_TIME_EXTENSION);
				if (studentList != null) {
					for (StudentInExam student : studentList) {
						ConnectionToClient studentClient = student.getClient();
						studentClient.sendToClient(modelWrapperToClient);
					}
				}
				ExamProcess examProcess = examsInProcess.get(extension.getCode());
				ConnectionToClient teacherClient = examProcess.getTeacherClient();
				teacherClient.sendToClient(modelWrapperToClient);
				client.sendToClient(modelWrapperFromClient);

				ExamExtension extensionToRemove = null;
				List<ExamExtension> extensionList = examsExtensions.get(extension.getCode());
				for (ExamExtension extensionFromList : extensionList) {
					if (extensionFromList.getCode().equals(extension.getCode())
							&& extensionFromList.getExamID().equals(extension.getExamID())) {
						extensionToRemove = extensionFromList;
					}
				}

				if (extensionToRemove != null)
					extensionList.remove(extensionToRemove);

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXECUTED_EXAM_LIST_BY_COURSE:
			String course_name = (String) modelWrapperFromClient.getElement();
			List<ExecutedExam> set = databaseController.getGradesForStatisticByCourse(course_name);
			modelWrapperToClient = new ModelWrapper<>(set, GET_EXECUTED_EXAM_LIST_BY_COURSE);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXECUTED_EXAM_LIST_BY_STUDENT:
			String student_name = (String) modelWrapperFromClient.getElement();
			List<StudentExecutedExam> set1 = databaseController.getGradeStatisticByStudent(student_name);
			modelWrapperToClient = new ModelWrapper<>(set1, GET_EXECUTED_EXAM_LIST_BY_STUDENT);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXECUTED_EXAM_LIST_BY_TEACHER:
			String teacherID = (String) modelWrapperFromClient.getElement();
			List<ExecutedExam> examListByTeacher = databaseController.getExecutedExamListByExecutorTeacherID(teacherID);
			modelWrapperToClient = new ModelWrapper<>(examListByTeacher, GET_EXECUTED_EXAM_LIST_BY_TEACHER);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_QUESTION_LIST_BY_EXAM_ID:
			String examID = (String) modelWrapperFromClient.getElement();
			List<ExamQuestion> questionListByExamID = databaseController.getExamQuestionsList(examID);
			modelWrapperToClient = new ModelWrapper<ExamQuestion>(questionListByExamID, GET_QUESTION_LIST_BY_EXAM_ID);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_QUESTION_LIST_BY_CODE:
			String examCode = (String) modelWrapperFromClient.getElement();
			ExamProcess examInProcess = examsInProcess.get(examCode);

			questionListByExamID = databaseController.getExamQuestionsList(examInProcess.getExamId());
			modelWrapperToClient = new ModelWrapper<ExamQuestion>(questionListByExamID, GET_QUESTION_LIST_BY_CODE);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXECUTED_EXAM_LIST_BY_EXECUTOR:
			String currentTeacherID = (String) modelWrapperFromClient.getElement();
			List<ExecutedExam> executedExamListByExecutor = databaseController
					.getExecutedExamListByExecutorTeacherID(currentTeacherID);
			modelWrapperToClient = new ModelWrapper<>(executedExamListByExecutor, GET_EXECUTED_EXAM_LIST_BY_EXECUTOR);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXECUTED_EXAM_LIST_BY_CREATOR:
			String currentTeacherID2 = (String) modelWrapperFromClient.getElement();
			List<ExecutedExam> executedExamListByCreator = databaseController
					.getExecutedExamListByCreatorTeacherID(currentTeacherID2);
			modelWrapperToClient = new ModelWrapper<>(executedExamListByCreator, GET_EXECUTED_EXAM_LIST_BY_CREATOR);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case SAVE_APPROVED_STUDENTS:
			List<StudentExecutedExam> approvedStudents = (List<StudentExecutedExam>) modelWrapperFromClient
					.getElements();
			databaseController.saveApprovedStudents(approvedStudents);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case START_EXAM:
			try {
				ExamProcess examProcess = (ExamProcess) modelWrapperFromClient.getElement();
				examProcess.setTeacherClient(client);
				if (!examsInProcess.containsKey(examProcess.getCode())) {
					examsInProcess.put(examProcess.getCode(), examProcess);
					modelWrapperToClient = new ModelWrapper<>(START_EXAM_SUCCESS);
					client.sendToClient(modelWrapperToClient);
				} else {
					modelWrapperToClient = new ModelWrapper<>(START_EXAM_FAILD);
					client.sendToClient(modelWrapperToClient);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case CLOSE_EXAM:
			String code = (String) modelWrapperFromClient.getElement();
			examID = examsInProcess.get(code).getExamId();

			ExamProcess examInProcessTemp = examsInProcess.get(code);
			List<StudentInExam> studenInExamList = studentInExam.get(code);

			if (examInProcessTemp.getType().equals(ExamType.Computerized)) {

				checkAlert(code, examID);

				if (examInProcessTemp.getType().equals(ExamProcess.ExamType.Computerized)) {
					databaseController.insertStudentAnswers(studenInExamList, code);
				}

			}
			try {
				modelWrapperToClient = new ModelWrapper<>("-1", STUDENT_TIME_EXTENSION);
				int finishedStudent = 0;
				if (studenInExamList != null) {
					for (StudentInExam student : studenInExamList) {
						if (student.isFinished())
							finishedStudent++;

						ConnectionToClient studentClient = student.getClient();

						if (studentClient.isAlive())
							studentClient.sendToClient(modelWrapperToClient);
					}
					studenInExamList.clear();
				}
				examInProcessTemp.setFinishedStudentsCount(String.valueOf(finishedStudent));

				databaseController.saveExecutedExam(examInProcessTemp);
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}

			examsInProcess.remove(code);

			break;

		case CREATE_QUESTION:
			Question question = (Question) modelWrapperFromClient.getElement();
			databaseController.saveQuestion(question);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case EDIT_QUESTION:
			Question editedQuestion = (Question) modelWrapperFromClient.getElement();
			databaseController.updateQuestion(editedQuestion);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case EXAM_EXECUTE:
			String studentID = (String) modelWrapperFromClient.getElement();
			List<StudentExecutedExam> testArray = databaseController.getExecutedExamListByStudentID(studentID);
			modelWrapperToClient = new ModelWrapper<StudentExecutedExam>(testArray, EXAM_EXECUTE);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case EXTENSION_REQUEST:
			ExamExtension examExtension = (ExamExtension) modelWrapperFromClient.getElement();
			List<ExamExtension> examExtensionsList;

			if (!examsExtensions.containsKey(examExtension.getCode()))
				examExtensionsList = new ArrayList<>();
			else
				examExtensionsList = examsExtensions.get(examExtension.getCode());

			examExtensionsList.add(examExtension);
			examsExtensions.put(examExtension.getCode(), examExtensionsList);

			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case OVERALL_STATISTICS:
			break;

		case GET_USER:
			List<String> userInfo = (List<String>) modelWrapperFromClient.getElements();
			User user = databaseController.getUser(userInfo.get(0), userInfo.get(1));
			modelWrapperToClient = new ModelWrapper<>(user, GET_USER);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case LOG_IN:
			userInfo = (List<String>) modelWrapperFromClient.getElements();
			boolean loogednIn = false;
			String userID = userInfo.get(0);
			String password = userInfo.get(1);

			//Check if exist in the connected user list
			for (String loggedInUser : connectedUsers) {
				if (loggedInUser.equals(userID)) {
					loogednIn = true;
				}
			}

			if (!loogednIn) {
				user = databaseController.getUser(userID, password);
				modelWrapperToClient = new ModelWrapper<>(user, LOG_IN);
				connectedUsers.add(userID);
				try {
					client.sendToClient(modelWrapperToClient);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				modelWrapperToClient = new ModelWrapper<>(LOG_IN);
				try {
					client.sendToClient(modelWrapperToClient);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			break;

		case LOG_OUT:
			userID = (String) modelWrapperFromClient.getElement();
			connectedUsers.remove(userID);
			modelWrapperToClient = new ModelWrapper<>(LOG_OUT);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_QUESTION_LIST_BY_SUBJECT:
			String subjcet = (String) modelWrapperFromClient.getElement();
			List<Question> questionListBySubject = databaseController.getQuestionList(subjcet);
			modelWrapperToClient = new ModelWrapper<>(questionListBySubject, GET_QUESTION_LIST);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case CREATE_EXAM:
			Exam newExam = (Exam) modelWrapperFromClient.getElement();
			databaseController.saveExam(newExam);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case EDIT_EXAM:
			List<Exam> oldNewExams = (List<Exam>) modelWrapperFromClient.getElements();
			Exam oldExam = oldNewExams.get(0);
			Exam updatedExam = oldNewExams.get(1);
			databaseController.updateExam(oldExam, updatedExam);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAMS_LIST:
			List<Exam> examList = databaseController.getExamList();
			modelWrapperToClient = new ModelWrapper<>(examList, GET_EXAMS_LIST);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAMS_LIST_BY_SUBJECT:
			String subject = (String) modelWrapperFromClient.getElement();
			List<Exam> examListBySubject = databaseController.getExamListBySubject(subject);
			modelWrapperToClient = new ModelWrapper<>(examListBySubject, GET_EXAMS_LIST_BY_SUBJECT);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAMS_LIST_BY_COURSE:
			String course = (String) modelWrapperFromClient.getElement();
			List<Exam> examListByCourse = databaseController.getExamListByCourse(course);
			modelWrapperToClient = new ModelWrapper<>(examListByCourse, GET_EXAMS_LIST_BY_SUBJECT);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case UPLOAD_FILE:
			WordFile file = (WordFile) modelWrapperFromClient.getFile();
			StudentExecutedExam studentExam = (StudentExecutedExam) modelWrapperFromClient.getElement();
			databaseController.UploadFile(studentExam, file);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case INSERT_STUDENT_TO_EXAM:
			// Get code and check if examID exist, if so insert student to exam in DB
			StudentInExam newStudent = (StudentInExam) modelWrapperFromClient.getElement();

			code = newStudent.getCode();
			studentID = newStudent.getStudentID();
			ExamProcess examProcessTemp = null;
			ConnectionToClient teacherClient = null;

			if (examsInProcess.containsKey(code)) {
				List<StudentInExam> temp = studentInExam.get(code);

				if (temp == null) {
					temp = new ArrayList<>();
				}

				newStudent.setClient(client);
				temp.add(newStudent);
				studentInExam.put(code, temp);

				examProcessTemp = examsInProcess.get(code);

				databaseController.insertToExecutedExamByStudent(studentID, examProcessTemp);

				teacherClient = examProcessTemp.getTeacherClient();
				examProcessTemp.setTeacherClient(null);
				modelWrapperToClient = new ModelWrapper<>(examProcessTemp, INSERT_STUDENT_TO_EXAM);
			} else {
				modelWrapperToClient = new ModelWrapper<>(ERROR_INSERT_STUDENT_TO_EXAM);
			}
			try {

				client.sendToClient(modelWrapperToClient);
				if (examProcessTemp != null)
					examProcessTemp.setTeacherClient(teacherClient);
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;

		case CHECK_CODE_BEFORE_INSERTION:
			// get code and check if there is exam
			boolean isFound = false;
			userInfo = (List<String>) modelWrapperFromClient.getElements();
			examCode = (String) userInfo.get(0);
			studentID = (String) userInfo.get(1);
			ExamProcess examProc = examsInProcess.get(examCode);
			if (examProc == null) {
				modelWrapperToClient = new ModelWrapper<>(ERROR_EXAM_NOT_EXIST);
			} else {
				// check if student already did the test
				List<StudentInExam> studentList = studentInExam.get(examCode);
				if (studentList != null) {
					for (StudentInExam student : studentList) {
						if (student.getStudentID().equals(studentID)) {
							modelWrapperToClient = new ModelWrapper<>(ERROR_STUDENT_ALREADY_IN_EXAM);
							isFound = true;
						}

					}
				}
				if (isFound == false) {
					modelWrapperToClient = new ModelWrapper<>(SUCCESSFUL_INSERT_CHECK);
				}

			}

			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAM_ID:
			examCode = (String) modelWrapperFromClient.getElement();
			examID = databaseController.GetExamID(examCode);
			modelWrapperToClient = new ModelWrapper<>(examID, GET_EXAM_ID);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAM_BY_EXAM_ID:
			examID = (String) modelWrapperFromClient.getElement();
			Exam exam = databaseController.GetExamByExamID(examID);
			modelWrapperToClient = new ModelWrapper<>(exam, GET_EXAM_BY_EXAM_ID);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_EXAM_BY_CODE:
			code = (String) modelWrapperFromClient.getElement();
			ExamProcess examProcess = examsInProcess.get(code);
			exam = databaseController.GetExamByExamID(examProcess.getExamId());
			modelWrapperToClient = new ModelWrapper<>(exam, GET_EXAM_BY_CODE);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case INSERT_FINISHED_STUDENT:
			StudentInExam finishedStudent = (StudentInExam) modelWrapperFromClient.getElement();
			code = finishedStudent.getCode();
			examInProcess = examsInProcess.get(code);
			examID = examsInProcess.get(code).getExamId();
			studentID = finishedStudent.getStudentID();
			String finalGrade = finishedStudent.getGrade();
			teacherID = examInProcess.getTeacherID();
			String execDuration = finishedStudent.getExecDuration();

			List<StudentInExam> studentsInExam = studentInExam.get(code);

			for (StudentInExam student : studentsInExam) {
				if (student.getStudentID().equals(studentID)) {
					student.setFinished(true);
					String[] solution = finishedStudent.getSolution();
					student.setSolution(solution);
				}
			}

			databaseController.insertFinishedStudent(studentID, examID, teacherID, finalGrade, execDuration);
			modelWrapperToClient = new ModelWrapper<>(INSERT_FINISHED_STUDENT);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;

		case GET_EXAM_IN_PROCESS:

			code = (String) modelWrapperFromClient.getElement();

			ExamProcess examInPorcess = examsInProcess.get(code);
			ExamProcess newExamInPorcess = new ExamProcess(examInPorcess);
			modelWrapperToClient = new ModelWrapper<>(newExamInPorcess, GET_EXAM_IN_PROCESS);

			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;

		case GET_EXECUTED_EXAM_STUDENT_LIST:
			List<String> parameters = (List<String>) modelWrapperFromClient.getElements();
			String examId = parameters.get(0);
			String date = parameters.get(1);
			teacherID = parameters.get(2);
			List<StudentExecutedExam> studentList = databaseController.getExecutedExamStudentList(examId, date,
					teacherID);
			modelWrapperToClient = new ModelWrapper<>(studentList, GET_EXECUTED_EXAM_STUDENT_LIST);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case UPDATE_EXAM_STATISTIC:
			ExecutedExam executedExam = (ExecutedExam) modelWrapperFromClient.getElement();
			databaseController.updateStatistic(executedExam);
			try {
				client.sendToClient(modelWrapperFromClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GET_SELECTED_ANSWERS:
			userInfo = (List<String>) modelWrapperFromClient.getElements();
			studentID = (String) userInfo.get(0);
			examID = (String) userInfo.get(1);
			date = (String) userInfo.get(2);
			String selectedAnswers = databaseController.getSelectedAnswers(studentID, examID, date);
			modelWrapperToClient = new ModelWrapper<>(selectedAnswers, GET_SELECTED_ANSWERS);
			try {
				client.sendToClient(modelWrapperToClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;

		}

	}

	/**
	 * Method the being called when server has been started, when the server started
	 * there is link between SQL database, indicate the server is connected and
	 * print to log.
	 */
	protected void serverStarted() {
		try {
			databaseController.connectToDatabase();
			isConnected = true;
			serverListener.printToLog("Server listening for connections on port " + getPort());
			serverListener.changeButtonStatus(isConnected);
		} catch (SQLException ex) {
			try {
				this.close();
				serverListener.printToLog("Disconnected");
				serverListener.changeButtonStatus(!isConnected);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method the being called when server has been stopped, when the server stopped
	 * indicate the server is connected and print to log.
	 */
	protected void serverStopped() {
		isConnected = false;
		serverListener.printToLog("Server has stopped listening for connections");
		serverListener.changeButtonStatus(isConnected);
	}

	/** Print to log when new client is entered the server. */
	protected void clientConnected(ConnectionToClient client) {
		serverListener.printToLog("New client connection, ip address: " + client.getInetAddress());

		subjectCourseCollection = databaseController.updateSubjectCollection(subjectCourseCollection);
		ModelWrapper<SubjectCourseCollection> modelWrapperToClient = new ModelWrapper<>(subjectCourseCollection,
				GET_SUBJECT_COURSE_LIST);
		try {
			client.sendToClient(modelWrapperToClient);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Calculate and saving the alert value for each student after executed the
	 * exams.
	 * 
	 * @param code, examID check for each exam that has been executed.
	 * @return
	 */
	public void checkAlert(String code, String examID) {
		if (studentInExam.containsKey(code)) {
			int numOfStudents = studentInExam.get(code).size();
			if (numOfStudents == 0) {
				return;
			}

			List<StudentInExam> studentsList = studentInExam.get(code);
			int wrong_match = 0;
			Integer AlertPercent = 0;
			Exam exam = databaseController.GetExamByExamID(examID);
			int numOfQuestions = exam.getExamQuestions().size();

			if (numOfStudents == 1) {
				String studentID = studentsList.get(0).getStudentID();
				databaseController.updateAlertValue(studentID, examID, "0%");
				studentsList.get(0).setFinished(true);
				return;
			}
			for (int i = 0; i < numOfStudents; i++) {
				Integer[] diff_arr = new Integer[numOfStudents];
				Arrays.fill(diff_arr, new Integer(0));
				for (int k = 0; k < numOfQuestions; k++) {
					for (int j = 0; j < numOfStudents; j++) {
						if (j == i || (studentsList.get(i).getSolution())[k].equals("9"))
							continue;

						if ((Integer.parseInt((studentsList.get(i).getSolution())[k])) != exam.getExamQuestions().get(k)
								.getCorrectAnswer()
								&& ((studentsList.get(i).getSolution())[k])
										.equals((studentsList.get(j).getSolution())[k])) {
							diff_arr[j]++;
						}
					}
				}
				wrong_match = Collections.max(Arrays.asList(diff_arr));
				AlertPercent = (wrong_match * 100) / (numOfQuestions);
				String studentID = studentsList.get(i).getStudentID();
				String alert = AlertPercent.toString() + "%";
				databaseController.updateAlertValue(studentID, examID, alert);
				studentsList.get(i).setFinished(true);
			}
		}
	}

	/**
	 * @return if the server is connected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	/** The method print to the log the client disconnected */
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		serverListener.printToLog("client ip address: " + client.getInetAddress() + "has disconnected from the server");
	}

	/**
	 * @return SubjectCourse object
	 */
	public static SubjectCourseCollection getSubjectCollection() {
		return subjectCourseCollection;
	}

	/**
	 * set SubjectCourse object
	 * 
	 * @param subjectCourseCollection
	 */
	public static void setSubjectCollection(SubjectCourseCollection subjectCourseCollection) {
		Server.subjectCourseCollection = subjectCourseCollection;
	}

	/**
	 * @return exams in process map
	 */
	public static Map<String, ExamProcess> getExamsInProcess() {
		return examsInProcess;
	}

	/**
	 * set exams in process map
	 * 
	 * @param examsInProcess map code to ExamProcess list
	 */
	public static void setExamsInProcess(Map<String, ExamProcess> examsInProcess) {
		Server.examsInProcess = examsInProcess;
	}

	/**
	 * @return exam time extensions map
	 */
	public static Map<String, List<ExamExtension>> getExamsExtensions() {
		return examsExtensions;
	}

	/**
	 * set exam time extensions map
	 * 
	 * @param examsExtensions map code to ExamExtension list
	 */
	public static void setExamsExtensions(Map<String, List<ExamExtension>> examsExtensions) {
		Server.examsExtensions = examsExtensions;
	}

	/**
	 * @return students in exam map
	 */
	public static Map<String, List<StudentInExam>> getStudentInExam() {
		return studentInExam;
	}

	/**
	 * set students in exam map
	 * 
	 * @param studentInExam map code to StudentInExam list
	 */
	public static void setStudentInExam(Map<String, List<StudentInExam>> studentInExam) {
		Server.studentInExam = studentInExam;
	}

}
