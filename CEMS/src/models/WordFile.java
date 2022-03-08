package models;

import java.io.Serializable;

/**
 * Class that  represent a word - file which we getting within student upload / download word file.
 * 
 *
 */

public class WordFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private int size = 0;
	public byte[] mybytearray;


	/**
	 * Constructor
	 */
	public WordFile() {
		super();
	}

	/**
	 * Constructor
	 * @param name
	 * @param size
	 * @param mybytearray
	 */
	public WordFile(String name, int size, byte[] mybytearray) {
		this.name = name;
		this.size = size;
		this.mybytearray = mybytearray;
	}


	/**
	 * set array size
	 * @param size
	 */
	public void initArray(int size) {
		mybytearray = new byte[size];
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * set size
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return mybytearray
	 */
	public byte[] getMybytearray() {
		return mybytearray;
	}

	/**
	 * set byte array
	 * @param mybytearray
	 */
	public void setMybytearray(byte[] mybytearray) {
		for (int i = 0; i < mybytearray.length; i++)
			this.mybytearray[i] = mybytearray[i];
	}

	/**
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
