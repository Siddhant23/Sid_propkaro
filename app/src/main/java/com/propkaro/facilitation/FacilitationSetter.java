package com.propkaro.facilitation;

public class FacilitationSetter {
	private String tv_name, thumbnailUrl, profileUrl, tv_location, tv_area,
			tv_rate, tv_postedBy, tv_userType, tv_postedTime, lat, lng,
			contact, link, tv_proprty_listing_parent;
	int USER_ID = 0, PROPERTY_ID = 0;

	// private ArrayList<String> genre;

	public FacilitationSetter() {
	}

	public FacilitationSetter(String thumbnailUrl, String profileUrl, int USER_ID,
			int PROPERTY_ID, String tv_title, String tv_proprty_listing_parent,
			String tv_name, String lat, String lng, String contact, String link) {
		this.thumbnailUrl = thumbnailUrl;
		this.profileUrl = profileUrl;
		this.USER_ID = USER_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		this.tv_name = tv_name;
		this.lat = lat;
		this.lng = lng;
		this.contact = contact;
		this.link = link;
		this.setTv_proprty_listing_parent(tv_proprty_listing_parent);
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

	public String getTv_title() {
		return tv_name;
	}

	public void setTv_title(String tv_name) {
		this.tv_name = tv_name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getTv_location() {
		return tv_location;
	}

	public void setTv_location(String tv_location) {
		this.tv_location = tv_location;
	}

	public String getTv_area() {
		return tv_area;
	}

	public void setTv_area(String tv_area) {
		this.tv_area = tv_area;
	}

	public String getTv_rate() {
		return tv_rate;
	}

	public void setTv_rate(String tv_rate) {
		this.tv_rate = tv_rate;
	}

	public String getTv_postedBy() {
		return tv_postedBy;
	}

	public void setTv_postedBy(String tv_postedBy) {
		this.tv_postedBy = tv_postedBy;
	}

	public String getTv_userType() {
		return tv_userType;
	}

	public void setTv_userType(String tv_userType) {
		this.tv_userType = tv_userType;
	}

	public String getTv_postedTime() {
		return tv_postedTime;
	}

	public void setTv_postedTime(String tv_postedTime) {
		this.tv_postedTime = tv_postedTime;
	}

	public String getTv_proprty_listing_parent() {
		return tv_proprty_listing_parent;
	}

	public void setTv_proprty_listing_parent(String tv_proprty_listing_parent) {
		this.tv_proprty_listing_parent = tv_proprty_listing_parent;
	}

	public String getTv_name() {
		return tv_name;
	}

	public void setTv_name(String tv_name) {
		this.tv_name = tv_name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
