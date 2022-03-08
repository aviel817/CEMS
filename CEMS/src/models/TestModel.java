package models;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;

public class TestModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String secondName;
	private JFXButton button;
	
	
	/**
	 * Constructor
	 * @param firstName
	 * @param secondName
	 */
	public TestModel(String firstName, String secondName) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * set firstName
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return secondName
	 */
	public String getSecondName() {
		return secondName;
	}
	/**
	 * set second name
	 * @param secondName
	 */
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	/**
	 * @return button
	 */
	public JFXButton getButton() {
		return button;
	}

	/**
	 * set setButton
	 * @param button
	 */
	public void setButton(JFXButton button) {
		this.button = button;
	}
	
	
}
