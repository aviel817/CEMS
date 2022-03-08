package models;

import java.io.Serializable;

/**
 * Class that represent the User within our CEMS.
 * 
 *
 */

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userID;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private UserType type;
	private ErrorType error;

	public enum ErrorType {
		WRONG_ID, WRONG_PASSWORD
	}

	public enum UserType {
		Student, Teacher, Principal
	}

	
	/**
	 * Constructor
	 */
	public User() {

	}

	/**
	 * Constructor
	 * @param error
	 */
	public User(ErrorType error) {
		this.error = error;
	}

	/**
	 * Constructor
	 * @param userID
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param type
	 */
	public User(String userID, String password, String firstName, String lastName, String email, UserType type) {
		super();
		this.userID = userID;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.type = type;
	}


	/**
	 * @return userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * set userID
	 * @param userID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	
	/**
	 * set first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * set last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return type
	 */
	public UserType getType() {
		return type;
	}

	/**
	 * Set type
	 * @param type
	 */
	public void setType(UserType type) {
		this.type = type;
	}

	/**
	 * @return error
	 */
	public ErrorType getError() {
		return error;
	}

	/**
	 * Set error
	 * @param error
	 */
	public void setError(ErrorType error) {
		this.error = error;
	}

	/**
	 * toString
	 */
	@Override
	public String toString() {
		return "User [userID=" + userID + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", type=" + type + ", error=" + error + "]";
	}

}
