package com.propkaro.propertycenter;

public class KeyPairBoolData {
	private 
	String location_area = "", location_city = "";
	int USER_ID = 0, PROPERTY_ID = 0;
	public boolean isSelected;
//	private ArrayList<String> genre;

	public KeyPairBoolData() {
	}

	public KeyPairBoolData(
			String location_area, String location_city, int USER_ID, 
			int PROPERTY_ID, boolean isSelected){
		this.location_area = location_area;
		this.location_city = location_city;
		this.USER_ID = USER_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		this.isSelected = isSelected;
	}

	public int getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(int USER_ID) {
		this.USER_ID = USER_ID;
	}

	public int getPROPERTY_ID() {
		return PROPERTY_ID;
	}
	public void setPROPERTY_ID(int PROPERTY_ID) {
		this.PROPERTY_ID = PROPERTY_ID;
	}

	public String getLocation_area() {
		return location_area;
	}

	public void setLocation_area(String location_area) {
		this.location_area = location_area;
	}

	public String getLocation_city() {
		return location_city;
	}

	public void setLocation_city(String location_city) {
		this.location_city = location_city;
	}

	public boolean getSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
