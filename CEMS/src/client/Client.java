package client;

import static common.ModelWrapper.Operation.LOG_OUT;

import java.io.IOException;
import java.util.List;

import common.ModelWrapper;
import common.SubjectCourseCollection;
import models.Exam;
import models.ExamExtension;
import models.ExamProcess;
import models.ExamQuestion;
import models.ExecutedExam;
import models.Question;
import models.StudentExecutedExam;
import models.User;
import ocsf.client.AbstractClient;

//
/**
 * The Client class handle requests from the user and send to the server and
 * vice versa. Each request operation is classified by the Model Wrapper. In
 * addition, this class hold the returned data from the server in variables.
 * 
 * @author -------Group 9-------- Arik Zagdon, Dvir ben simon, Aviel Turgeman,
 *         Shenhav Hezi, Yaakov Shitrit
 * 
 */
public class Client extends AbstractClient {

	/**
	 * Value that's indicating if the server answer back to client, in order to stop
	 * listening on "HandleMessageFromClientUI method.
	 */
	public static boolean awaitResponse = false;

	/** hold the student selected answers of specific test */
	private static String SelectedAnswers;

	/**
	 * Value that hold the list of test that will be shown on table user interface.
	 */
	private static List<Exam> exams;

	/** Value that hold the list of executed tests. */
	private static List<ExecutedExam> execExams;

	/** value that hold the list of student executed exams. */
	private static List<StudentExecutedExam> executedExamStudentList;

	/** value that hold exam id of specific test */
	private static String examID;

	/** value that hold last exam code */
	private static String examCode;

	/** value that hold current executing exam */
	private static Exam exam;

	/**
	 * Value that hold the list of question that will be shown on table user
	 * interface.
	 */
	private static List<Question> questions;

	/** value that hold the current executing exam. */
	private static ExamProcess examProcess;

	/** value that hold the current executing exam questions. */
	private static List<ExamQuestion> examQuestions;

	/** value that hold list of exams with time extension. */
	private static List<ExamExtension> examExtensions;

	/** Value that hold the test the will be shown on editTest user interface. */
	private static Exam editTest;

	/** Value that hold the messages server side sent. */
	private static String serverMessages;

	/** value that represent the time extension to the student exam. */
	private static long timeExtension = 0;

	/** Value that hold the user, use to login and and open appropriate menu. */
	private static SubjectCourseCollection subjectCollection;

	/**
	 * Constructor creating new client connection.
	 * 
	 * @param host: Server host/ip info connection {default: 127.0.0.1}.
	 * @param port: Server port info connection.
	 * @throws IOException handle connection problem by enter wrong server details.
	 */
	public Client(String host, int port) throws IOException {
		super(host, port);
	}

	/**
	 * Function calls when server send message, the server send messages throw this
	 * function.
	 * 
	 * @param msg: get the returning message from server
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromServer(Object msg) {

		if (msg != null) {
			ModelWrapper<?> modelWrapperFromServer = (ModelWrapper<?>) msg;
			switch (modelWrapperFromServer.getOperation()) {
			case GET_EXECUTED_EXAM_LIST_BY_STUDENT:
				executedExamStudentList = (List<StudentExecutedExam>) modelWrapperFromServer.getElements();
				modelWrapperFromServer.getElement();
				break;

			case GET_EXECUTED_EXAM_LIST_BY_COURSE:
				execExams = (List<ExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case GET_QUESTION_LIST:
				questions = (List<Question>) modelWrapperFromServer.getElements();
				break;

			case LOAD_QUESTION_LIST:
				break;

			case GET_EXTENSION_REQUESTS:
				examExtensions = (List<ExamExtension>) modelWrapperFromServer.getElements();
				break;

			case GET_EXECUTED_EXAM_LIST_BY_TEACHER:
				execExams = (List<ExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case ENTERED_WRONG_ID:
				break;

			case SAVE_APPROVED_STUDENTS:
				setServerMessages("Students has been approved suucessfully");
				break;

			case GET_EXECUTED_EXAM_LIST_BY_EXECUTOR:
				execExams = (List<ExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case START_EXAM_SUCCESS:
				setServerMessages("Exam has been started");
				break;
			case START_EXAM_FAILD:
				setServerMessages("Can't start exam");
				break;

			case CLOSE_EXAM:
				setServerMessages("Exam has been stopped");
				break;

			case CREATE_QUESTION:
				setServerMessages("Question has been saved suucessfully");
				break;

			case EDIT_QUESTION:
				setServerMessages("Question has been updated suucessfully");
				break;

			case EXAM_EXECUTE:
				executedExamStudentList = (List<StudentExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case EXTENSION_REQUEST:
				setServerMessages("Extension has been sent suucessfully");
				break;

			case OVERALL_STATISTICS:
				break;

			case GET_USER:
				User user = (User) modelWrapperFromServer.getElement();
				ClientController.getClientUI().setUser(user);
				break;

			case LOG_IN:
				user = (User) modelWrapperFromServer.getElement();
				ClientController.getClientUI().setUser(user);
				break;

			case LOG_OUT:
				setServerMessages("Logged out..");
				break;

			case GET_SUBJECT_COURSE_LIST:
				subjectCollection = (SubjectCourseCollection) modelWrapperFromServer.getElement();
				break;

			case GET_QUESTION_LIST_BY_SUBJECT:
				questions = (List<Question>) modelWrapperFromServer.getElements();
				break;

			case CREATE_EXAM:
				setServerMessages("Exam has been saved successfully");
				break;

			case GET_EXAMS_LIST:
				exams = (List<Exam>) modelWrapperFromServer.getElements();
				break;

			case GET_EXAMS_LIST_BY_SUBJECT:
				exams = (List<Exam>) modelWrapperFromServer.getElements();
				break;

			case GET_EXAMS_LIST_BY_COURSE:
				exams = (List<Exam>) modelWrapperFromServer.getElements();
				break;

			case UPLOAD_FILE:
				setServerMessages("FileUploaded.");
				break;

			case INSERT_STUDENT_TO_EXAM:
				examProcess = (ExamProcess) modelWrapperFromServer.getElement();
				setServerMessages("Student entered exam successfully");
				break;

			case ERROR_INSERT_STUDENT_TO_EXAM:
				setServerMessages("Invalid code.");
				break;

			case GET_EXAM_ID:
				setExamID((String) modelWrapperFromServer.getElement());
				break;

			case GET_QUESTION_LIST_BY_EXAM_ID:
				examQuestions = (List<ExamQuestion>) modelWrapperFromServer.getElements();
				break;

			case GET_QUESTION_LIST_BY_CODE:
				examQuestions = (List<ExamQuestion>) modelWrapperFromServer.getElements();
				break;

			case GET_EXAM_BY_EXAM_ID:
				Client.setExam((Exam) modelWrapperFromServer.getElement());
				break;

			case GET_EXAM_BY_CODE:
				Client.setExam((Exam) modelWrapperFromServer.getElement());
				break;

			case STUDENT_TIME_EXTENSION:
				Client.setTimeExtension(Long.parseLong((String) modelWrapperFromServer.getElement()));
				break;

			case EXTENSION_CONFIRM:
				setServerMessages("Time Extension has been confirmed");
				break;

			case GET_EXECUTED_EXAM_LIST_BY_CREATOR:
				execExams = (List<ExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case GET_EXAM_IN_PROCESS:
				examProcess = (ExamProcess) modelWrapperFromServer.getElement();
				break;

			case GET_EXECUTED_EXAM_STUDENT_LIST:
				executedExamStudentList = (List<StudentExecutedExam>) modelWrapperFromServer.getElements();
				break;

			case SUCCESSFUL_INSERT_CHECK:
				setServerMessages("Check passed successfully.");
				break;

			case ERROR_STUDENT_ALREADY_IN_EXAM:
				setServerMessages("Student already did this exam.");
				break;

			case ERROR_EXAM_NOT_EXIST:
				setServerMessages("Invalid code.");
				break;

			case GET_SELECTED_ANSWERS:
				SelectedAnswers = (String) modelWrapperFromServer.getElement();
				break;

			default:
				break;

			}
		}

		awaitResponse = false;

	}

	/**
	 * Function that starting when the client user interface send message to the
	 * server in order to get services back.
	 * 
	 * @param msg: object that client send to server in order to get message back.
	 */
	public void handleMessageFromClientUI(Object msg) {

		try {
			openConnection();
			awaitResponse = true;
			sendToServer(msg);

			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			quit();
		}

	}

	/**
	 * Function that's close all server communications and terminate the client
	 * application.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * @return exam list
	 */
	public static List<Exam> getExams() {
		return exams;
	}

	/**
	 * get exam list and set it
	 * 
	 * @param exams
	 */
	public static void setExams(List<Exam> exams) {
		Client.exams = exams;
	}

	/**
	 * @return current editing exam
	 */
	public static Exam getEditTest() {
		return editTest;
	}

	/**
	 * set current editing exam
	 * 
	 * @param editTest
	 */
	public static void setEditTest(Exam editTest) {
		Client.editTest = editTest;
	}

	/**
	 * @return current server message
	 */
	public static String getServerMessages() {
		return serverMessages;
	}

	/**
	 * set current server message
	 * 
	 * @param serverMessages
	 */
	public static void setServerMessages(String serverMessages) {
		Client.serverMessages = serverMessages;
	}

	/**
	 * @return subject and course list
	 */
	public static SubjectCourseCollection getSubjectCollection() {
		return subjectCollection;
	}

	/**
	 * set subject and course list
	 * 
	 * @param subjectCollection
	 */
	public static void setSubjectCollection(SubjectCourseCollection subjectCollection) {
		Client.subjectCollection = subjectCollection;
	}

	/**
	 * @return question list by subject
	 */
	public static List<Question> getQuestions() {
		return questions;
	}

	/**
	 * set question list by subject
	 * 
	 * @param questions
	 */
	public static void setQuestions(List<Question> questions) {
		Client.questions = questions;
	}

	/**
	 * @return list of exectued exams
	 */
	public static List<ExecutedExam> getExecExams() {
		return execExams;
	}

	/**
	 * set list of exectued exams
	 * 
	 * @param execExams
	 */
	public static void setExecExams(List<ExecutedExam> execExams) {
		Client.execExams = execExams;
	}

	/**
	 * @return current exam id
	 */
	public static String getExamID() {
		return examID;
	}

	/**
	 * set current exam id
	 * 
	 * @param examID
	 */
	public static void setExamID(String examID) {
		Client.examID = examID;
	}

	/**
	 * @return current exam code
	 */
	public static String getExamCode() {
		return examCode;
	}

	/**
	 * set current exam code
	 * 
	 * @param examCode
	 */
	public static void setExamCode(String examCode) {
		Client.examCode = examCode;
	}

	/**
	 * @return the requested exam
	 */
	public static Exam getExam() {
		return exam;
	}

	/**
	 * set the requested exam
	 * 
	 * @param exam
	 */
	public static void setExam(Exam exam) {
		Client.exam = exam;
	}

	/**
	 * @return time extension of current exam
	 */
	public static long getTimeExtension() {
		return timeExtension;
	}

	/**
	 * set time extension of current exam
	 * 
	 * @param timeExtension
	 */
	public static void setTimeExtension(long timeExtension) {
		Client.timeExtension = timeExtension;
	}

	/**
	 * @return the requested question list
	 */
	public static List<ExamQuestion> getExamQuestions() {
		return examQuestions;
	}

	/**
	 * set the requested question list
	 * 
	 * @param examQuestions
	 */
	public static void setExamQuestions(List<ExamQuestion> examQuestions) {
		Client.examQuestions = examQuestions;
	}

	/**
	 * @return the requested exam process
	 */
	public static ExamProcess getExamProcess() {
		return examProcess;
	}

	/**
	 * @return the list of exams with time extension
	 */
	public static List<ExamExtension> getExamExtensions() {
		return examExtensions;
	}

	/**
	 * set the list of exams with time extension
	 * 
	 * @param examExtensions
	 */
	public static void setExamExtensions(List<ExamExtension> examExtensions) {
		Client.examExtensions = examExtensions;
	}

	/**
	 * set the requested exam process
	 * 
	 * @param examProcess
	 */
	public static void setExamProcess(ExamProcess examProcess) {
		Client.examProcess = examProcess;
	}

	/**
	 * @return list of exectued exams by request
	 */
	public static List<StudentExecutedExam> getExecutedExamStudentList() {
		return executedExamStudentList;
	}

	/**
	 * set list of exectued exams by request
	 * 
	 * @param executedExamStudentList
	 */
	public static void setExecutedExamStudentList(List<StudentExecutedExam> executedExamStudentList) {
		Client.executedExamStudentList = executedExamStudentList;
	}

	/**
	 * @return selected answers by student of specific exam
	 */
	public static String getSelectedAnswers() {
		return SelectedAnswers;
	}

	/**
	 * set selected answers by student of specific exam
	 * 
	 * @param selectedAnswers
	 */
	public static void setSelectedAnswers(String selectedAnswers) {
		SelectedAnswers = selectedAnswers;
	}

	/**
	 * Send request to the server in order to remove the user from the connected
	 * user list
	 */
	public static void logOutClient() {
		ClientUI clientUI = ClientController.getClientUI();
		String userID = clientUI.getUser().getUserID();
		ModelWrapper<String> modelWrapper = new ModelWrapper<>(userID, LOG_OUT);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);
	}

}
