package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.gui.ComputerizedGradeApproveStudentListController;
import junit.framework.TestCase;
import models.StudentExecutedExam;

class StatisticsCalculationTestClient extends TestCase{

	private static List<StudentExecutedExam> executedExamStudentList;
	
	

	@BeforeEach
	protected
	void setUp() throws Exception {
		executedExamStudentList = new ArrayList<>();
		StudentExecutedExam student1 = new StudentExecutedExam("Algebra II", "80", "16/06/2021");
		StudentExecutedExam student2 = new StudentExecutedExam("Algebra II", "75", "16/06/2021");
		StudentExecutedExam student3 = new StudentExecutedExam("Algebra II", "56", "16/06/2021");
		StudentExecutedExam student4 = new StudentExecutedExam("Algebra II", "50", "16/06/2021");
		StudentExecutedExam student5 = new StudentExecutedExam("Algebra II", "94", "16/06/2021");
		executedExamStudentList.add(student1);
		executedExamStudentList.add(student2);
		executedExamStudentList.add(student3);
		executedExamStudentList.add(student4);
		executedExamStudentList.add(student5);
	}

	@AfterEach
	protected
	void tearDown() throws Exception {

	}

	/**
	 * Test getAvarageAndMedian function average calculation input: Student exam
	 * list with grades : [80,75,56,50,94]
	 *  expected: average = 71.0
	 */
	@Test
	void getAvarageAndMedianSuccessAvarageTest() {
		ComputerizedGradeApproveStudentListController controller = new ComputerizedGradeApproveStudentListController();
		assertEquals(5, executedExamStudentList.size());
		double[] avgAndMedian = controller.getAvarageAndMedian(executedExamStudentList);
		double avarage = avgAndMedian[0];
		assertEquals(avarage, 71.0);
	}

	/**
	 * Test getAvarageAndMedian function odd median calculation input: Student exam
	 * list with grades : [80,75,56,50,94] expected: median = 75
	 */
	@Test
	void getAvarageAndMedianSuccessOddMedianTest() {
		ComputerizedGradeApproveStudentListController controller = new ComputerizedGradeApproveStudentListController();
		assertEquals(5, executedExamStudentList.size());
		double[] avgAndMedian = controller.getAvarageAndMedian(executedExamStudentList);
		double median = avgAndMedian[1];
		assertEquals(median, 75.0);
	}

	/**
	 * Test getAvarageAndMedian function even median calculation input: Student exam
	 * list with grades : [80,75,56,50,94,100] expected: median = 77.5
	 */
	@Test
	void getAvarageAndMedianSuccessEvenMedianTest() {
		StudentExecutedExam student6 = new StudentExecutedExam("Algebra II", "100", "16/06/2021");
		executedExamStudentList.add(student6);
		assertEquals(6, executedExamStudentList.size());
		ComputerizedGradeApproveStudentListController controller = new ComputerizedGradeApproveStudentListController();
		double[] avgAndMedian = controller.getAvarageAndMedian(executedExamStudentList);
		double median = avgAndMedian[1];
		assertEquals(median, 77.5);
	}

	/**
	 * Test getAvarageAndMedian function calculation with null grade input: Student
	 * exam list with grades : [80,75,56,50,94,null] expected: throw Exception
	 */
	@Test
	void getAvarageAndMedianNullGradeStudentTest() {
		StudentExecutedExam nullStudent = new StudentExecutedExam("Algebra II", null, "16/06/2021");
		ComputerizedGradeApproveStudentListController controller = new ComputerizedGradeApproveStudentListController();
		executedExamStudentList.add(nullStudent);
		assertEquals(6, executedExamStudentList.size());
		try {
			controller.getAvarageAndMedian(executedExamStudentList);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test getAvarageAndMedian function average median calculation, when the list
	 * is empty input: empty student list expected: average = 0 median = 0
	 */
	@Test
	void getAvarageAndMedianNoStudentsTest() {
		ComputerizedGradeApproveStudentListController controller = new ComputerizedGradeApproveStudentListController();
		executedExamStudentList.clear();
		assertEquals(0, executedExamStudentList.size());
		double[] avgAndMedian = controller.getAvarageAndMedian(executedExamStudentList);
		double avarage = avgAndMedian[0];
		double median = avgAndMedian[1];
		assertEquals(avarage, 0.0);
		assertEquals(median, 0.0);
	}
	
	
	
	

}
