package common;

import java.io.Serializable;
import java.util.List;

import models.WordFile;

/**
 * Generic class that wrap all of CEMS Projects model, in order to make
 * transfers between client and server available and indicate which operation
 * has been made.
 * 
 * @param <E> can be any class model in project {Test,Question,Teacher,...etc}
 */
public class ModelWrapper<E> implements Serializable {

	/** Serial version */
	private static final long serialVersionUID = 1L;
	/** Store class model element, in order to transfer it via client/server */
	private E element;
	/**
	 * Store list of class model element, in order to transfer it via client/server
	 */
	private List<E> elements;
	/** Store elements array of type E */
	private E[] elements2;
	/** Store WordFile file */
	private WordFile file;
	/** Which operation should be executed in server/client */
	private Operation operation;

	public enum Operation {
		CREATE_EXAM, UPDATE_TEST, ENTERED_WRONG_ID, ERROR, LOAD_TEST, LOAD_QUESTION_LIST, OVERALL_STATISTICS,
		GET_EXECUTED_EXAM_LIST_BY_COURSE, GET_EXECUTED_EXAM_LIST_BY_STUDENT, GET_EXECUTED_EXAM_LIST_BY_TEACHER,
		EXAM_EXTENSION_REQUEST, EXAM_EXECUTE, CREATE_QUESTION, START_EXAM, TEST_STATISTICS, GET_USER,
		GET_SUBJECT_COURSE_LIST, GET_COURSE_LIST, GET_QUESTION_LIST, GET_EXAMS_LIST_BY_SUBJECT,
		GET_EXAMS_LIST_BY_COURSE, GET_EXAMS_LIST, CLOSE_EXAM, GET_QUESTION_LIST_BY_SUBJECT, EXTENSION_REQUEST,
		UPLOAD_FILE, GET_EXAM_ID, GET_QUESTION_LIST_BY_EXAM_ID, INSERT_STUDENT_TO_EXAM, GET_EXAM_BY_EXAM_ID,
		START_EXAM_SUCCESS, START_EXAM_FAILD, GET_EXECUTED_EXAM_LIST_BY_CREATOR, ERROR_INSERT_STUDENT_TO_EXAM,
		STUDENT_TIME_EXTENSION, INSERT_FINISHED_STUDENT, GET_EXECUTED_EXAM_LIST_BY_EXECUTOR, GET_EXAM_IN_PROCESS,
		GET_EXECUTED_EXAM_STUDENT_LIST, SAVE_APPROVED_STUDENTS, UPDATE_EXAM_STATISTIC, GET_QUESTION_LIST_BY_CODE,
		GET_EXAM_BY_CODE, SUCCESSFUL_INSERT_CHECK, ERROR_STUDENT_ALREADY_IN_EXAM, ERROR_EXAM_NOT_EXIST,
		CHECK_CODE_BEFORE_INSERTION, GET_EXTENSION_REQUESTS, EXTENSION_CONFIRM, GET_SELECTED_ANSWERS, EDIT_QUESTION,
		EDIT_EXAM, LOG_OUT, LOG_IN
	};

	/**
	 * All of server/client operation
	 */

	/**
	 * Constructor of creating model wrapper with one element
	 * 
	 * @param operation project operation that's need to be executed
	 */
	public ModelWrapper(Operation operation) {
		this.operation = operation;
	}

	/**
	 * Constructor of creating model wrapper with one element
	 * 
	 * @param element   project model that's need to be wrapped
	 * @param operation project operation that's need to be executed
	 */
	public ModelWrapper(E element, Operation operation) {
		this.element = element;
		this.operation = operation;
	}

	/**
	 * Constructor of creating model wrapper with list of elements
	 * 
	 * @param elements  project models list that's need to be wrapped
	 * @param operation project operation that's need to be executed
	 */
	public ModelWrapper(List<E> elements, Operation operation) {
		this.elements = elements;
		this.operation = operation;
	}

	/**
	 * Constructor of the class ModelWrapper with list and array
	 * 
	 * @param elements1 list of E elements
	 * @param elements2 array of E elements
	 * @param operation operation that need to be executed
	 */
	public ModelWrapper(List<E> elements1, E[] elements2, Operation operation) {
		super();
		this.elements = elements1;
		this.elements2 = elements2;
		this.operation = operation;
	}

	/**
	 * Constructor of the class ModelWrapper with WordFile and E element
	 * 
	 * @param elements1 WordFile file
	 * @param elements2 E element
	 * @param operation operation that need to be executed
	 */
	public ModelWrapper(WordFile elements1, E elements2, Operation operation) {
		super();
		this.file = elements1;
		this.element = elements2;
		this.operation = operation;
	}

	/**
	 * @return the element transferred by the class
	 */
	public E getElement() {
		return element;
	}

	/**
	 * set the element
	 * 
	 * @param element
	 */
	public void setElement(E element) {
		this.element = element;
	}

	/**
	 * @return the operation transferred by the class
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * set the operation
	 * 
	 * @param operation
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @return list of E elements
	 */
	public List<E> getElements() {
		return elements;
	}

	/**
	 * @return E element
	 */
	public E getAdditonalElement() {
		return element;
	}

	/**
	 * set list of E elements
	 * 
	 * @param elements
	 */
	public void setElements(List<E> elements) {
		this.elements = elements;
	}

	/**
	 * @return Array of E elements
	 */
	public E[] getElements2() {
		return elements2;
	}

	/**
	 * set Array of E elements
	 * 
	 * @param elements2
	 */
	public void setElements2(E[] elements2) {
		this.elements2 = elements2;
	}

	/**
	 * @return WordFile file
	 */
	public WordFile getFile() {
		return file;
	}

	/**
	 * set WordFile file
	 * 
	 * @param file
	 */
	public void setFile(WordFile file) {
		this.file = file;
	}

}
