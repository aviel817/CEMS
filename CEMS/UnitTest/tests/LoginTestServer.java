package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import models.Database;
import models.User;
import models.User.ErrorType;
import server.DatabaseController;
import server.ServerEventListener;

class LoginTestServer extends TestCase {
	

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
	 * method under testing: GetUser 
	 * Connect to database, testing if the user pull from database 
	 * input: userID = 204459093 , password = 1234 
	 * Expected: user = {userID = 204459093 , firstName = Arik , lastName = Zagdon , Email = Arikz15@gmail.com}
	 */
	@Test
	void databaseControllerGetUserSuccessTest() {
			User user = dbController.getUser("204459093", "1234");
			assertEquals(user.getUserID(), "204459093");
			assertEquals(user.getFirstName(), "Arik");
			assertEquals(user.getLastName(), "Zagdon");
			assertEquals(user.getEmail(), "Arikz15@gmail.com");

	}

	/**
	 * method under testing: GetUser 
	 * Connect to database, testing if user ins't exist. 
	 * input: userID = 1234 , password = 0000 // user ID isn't exist in database 
	 * Expected: ErrorType.WRONG_ID // indicating that entered id isn't exist
	 */
	@Test
	void databaseControllerUserNotExist() {

			User user = dbController.getUser("1234", "0000");
			assertEquals(ErrorType.WRONG_ID, user.getError());

	}

	/**
	 * method under testing: GetUser 
	 * Connect to database, testing if user enter wrong password 
	 * input: userID = 204459093 , password = 0000 // userID and password isn't match 
	 * Expected: ErrorType.WRONG_PASSWORD // indicating that entered id and password doesn't match.
	 */
	@Test
	void databaseControllerWrongPassword() {

			User user = dbController.getUser("204459093", "0000");
			assertEquals(ErrorType.WRONG_PASSWORD, user.getError());

	}

	/**
	 * method under testing: GetUser 
	 * Connect to database, testing if user enter null on the id and password 
	 * input: userID = null , password = null 
	 * Expected: throw exception
	 */
	@Test
	void databaseControllerNullUser() {

			User user = dbController.getUser(null, null);
			assertNull(user);

	}
	

}
