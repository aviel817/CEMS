package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import models.Database;
import models.ExecutedExam;
import models.StudentExecutedExam;
import server.DatabaseController;
import server.ServerEventListener;

class StatisticsCalculationServerTest extends TestCase {
	
	

	private Database database;
	private DatabaseController dbController;

	@BeforeEach
	protected
	void setUp() throws Exception {
		database = new Database("127.0.0.1", "3306", "cems", "root", "1234");
		dbController = new DatabaseController(database, new ServerEventListener() {

			@Override
			public void printToLog(String logs) {
				System.out.println(logs);
			}

			@Override
			public void changeButtonStatus(boolean status) {
				System.out.println(status);
			}
		});

		dbController.connectToDatabase();

	}

	@AfterEach
	protected
	void tearDown() throws Exception {
	}

	/**
	 * Test getExecutedExamLisyByExecutorTeacherIDSucceedTest method that returns
	 * all the Exams that executed by TeacherID input: TeacherID "308315035"
	 * expected: NOT empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamLisyByExecutorTeacherIDSucceedTest() {
		List<ExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByExecutorTeacherID("308315035");
		assertFalse(examList.isEmpty());

	}

	/**
	 * Test getExecutedExamLisyByExecutorTeacherIDSucceedTest method that returns
	 * all the Exams that executed by TeacherID check if inserted techaerID that not
	 * existing input: TeacherID : "0000" 
	 * expected: Empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamLisyByExecutorTeacherIDNotExistingTeacherID() {

		List<ExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByExecutorTeacherID("0000");
		assertTrue(examList.isEmpty());

	}

	/**
	 * Test getExecutedExamLisyByExecutorTeacherIDSucceedTest method that returns
	 * all the Exams that executed by TeacherID check if inserted techaerID is null
	 * input: TeacherID : null expected: Empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamLisyByExecutorTeacherIDNullInserted() {

		List<ExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByExecutorTeacherID(null);
		assertTrue(examList.isEmpty());

	}
	
	
	/**
	 * Test getExecutedExamListByStudentID method that returns
	 * all the Exams that executed by Student 
	 * input: StudentId : "1"
	 *  expected: Not empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamListByStudentIDSucceed() {
		
		List<StudentExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByStudentID("1");
		assertFalse(examList.isEmpty());

	}
	
	
	/**
	 * Test getExecutedExamListByStudentID method that returns
	 * all the Exams that executed by Student, this test studentID not existing
	 * input: StudentId : "0000"
	 *  expected: empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamListByStudentIDNotExisting() {
		
		List<StudentExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByStudentID("0000");
		assertTrue(examList.isEmpty());

	}
	
	
	
	/**
	 * Test getExecutedExamListByStudentID method that returns
	 * all the Exams that executed by Student, this test studentID is null
	 * input: StudentId : null
	 *  expected: empty list of ExecutedExam.
	 */
	@Test
	void getExecutedExamListByStudentIDNull() {
		
		List<StudentExecutedExam> examList = new ArrayList<>();
		examList = dbController.getExecutedExamListByStudentID(null);
		assertTrue(examList.isEmpty());

	}
	
	
	

}
