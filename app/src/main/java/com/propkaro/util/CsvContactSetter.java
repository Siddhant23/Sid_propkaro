package com.propkaro.util;

public class CsvContactSetter {
	
	private long id;
	private String idStr = "";
	private String firstName = "";
	private String lastName = "";
	private String mobNumber = "";
	private String emailIdx = "";

	public CsvContactSetter() {
	}
	public CsvContactSetter(long id, String firstName, String lastName, String mobNumber, String emailIdx, String idStr) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobNumber = mobNumber;
		this.emailIdx = emailIdx;
		this.idStr = idStr;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "Student [id=" + id 
				+ ", firstName=" + firstName
				+ ", lastName=" + lastName 
				+ "]";
	}
	public String getMobNumber() {
		return mobNumber;
	}
	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}
	public String getEmailIdx() {
		return emailIdx;
	}
	public void setEmailIdx(String emailIdx) {
		this.emailIdx = emailIdx;
	}
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
}
