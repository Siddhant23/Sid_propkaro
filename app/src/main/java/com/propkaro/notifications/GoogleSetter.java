package com.propkaro.notifications;

public class GoogleSetter {
	private String description = "", id = "", matched_substrings = "", place_id = "", reference, terms, types;
	
	private boolean isBoolEnable = false;
	
	int USER_ID = 0, PROPERTY_ID = 0, color;

	public GoogleSetter() {
	}

	public GoogleSetter(
			String title, String message, String type, String time, 
			String thumbnailUrl, String imageUrl, int USER_ID, int PROPERTY_ID, 
			int color, boolean isBoolEnable, String description, String id, String matched_substrings, String place_id, String reference, String terms) {

		this.USER_ID = USER_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		
		this.color = color;
		this.isBoolEnable = isBoolEnable;

		this.description = description;
		this.id = id;
		this.matched_substrings = matched_substrings;
		this.place_id = place_id;
		this.reference = reference;
		this.terms = terms;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + PROPERTY_ID;
		return result;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean getIsBoolEnable() {
		return isBoolEnable;
	}

	public void setIsBoolEnable(boolean isBoolEnable) {
		this.isBoolEnable = isBoolEnable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatched_substrings() {
		return matched_substrings;
	}

	public void setMatched_substrings(String matched_substrings) {
		this.matched_substrings = matched_substrings;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

}
