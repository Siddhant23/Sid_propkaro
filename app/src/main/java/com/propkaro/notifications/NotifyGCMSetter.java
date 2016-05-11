package com.propkaro.notifications;

public class NotifyGCMSetter {
	private String title = "", message = "", type = "", time = "", thumbnailUrl, imageUrl;
	
	private boolean isBoolEnable = false;
	
	int USER_ID = 0, PROPERTY_ID = 0, color;

	public NotifyGCMSetter() {
	}

	public NotifyGCMSetter(
			String title, String message, String type, String time, 
			String thumbnailUrl, String imageUrl, int USER_ID, int PROPERTY_ID, 
			int color, boolean isBoolEnable) {

		this.title = title;
		this.message = message;
		this.type = type;
		this.time = time;
		this.thumbnailUrl = thumbnailUrl;
		this.imageUrl = imageUrl;
		
		this.USER_ID = USER_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		
		this.color = color;
		this.isBoolEnable = isBoolEnable;
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

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + PROPERTY_ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		NotifyGCMSetter other = (NotifyGCMSetter) obj;
		if (PROPERTY_ID != other.PROPERTY_ID)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Product [USER_ID=" + USER_ID + ", id_package=" + PROPERTY_ID 
				+ ", imageUrl=" + imageUrl + ", thumbnailUrl=" + thumbnailUrl 
				+ ", title=" + title + ", message=" + message 
				+ ", type=" + type + ", time =" + time 
				+ "]";
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
