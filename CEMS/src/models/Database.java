package models;

import java.io.Serializable;

/**
 * Class that demonstrate database, store on that object all database attribute.
 * 
 */
public class Database implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip;
	private String port;
	private String scheme;
	private String userName;
	private String password;
	
	/**
	 * Constructor for Database
	 * @param ip
	 * @param port
	 * @param scheme
	 * @param userName
	 * @param password
	 */
	public Database(String ip, String port, String scheme, String userName, String password) {
		super();
		this.ip = ip;
		this.port = port;
		this.scheme = scheme;
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * @return ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * set ip
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * set port
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * set scheme
	 * @param scheme
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * set username
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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

}
