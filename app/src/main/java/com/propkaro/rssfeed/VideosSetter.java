package com.propkaro.rssfeed;

import java.util.ArrayList;

public class VideosSetter {
	private String title, description, duration, thumbnailUrl, url, file, vType, setYCode, click_type;
	private int year, id;
	private double rating;
	private ArrayList<String> genre;

	public VideosSetter() {
	}

	public VideosSetter(String name, String description, String duration, String thumbnailUrl, 
			String url, String file, String vType, String click_type, String setYCode, 
			int year, int id, double rating,
			ArrayList<String> genre) {
		this.title = name;
		this.description = description;
		this.duration = duration;
		this.thumbnailUrl = thumbnailUrl;
		this.url = url;
		this.file = file;
		this.vType = vType;
		this.click_type = click_type;
		this.setYCode = setYCode;
		this.year = year;
		this.id = id;
		this.rating = rating;
//		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFile() {
		return file;
	}

	public void setVType(String vType) {
		this.vType = vType;
	}

	public String getVType() {
		return vType;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getClick() {
		return click_type;
	}

	public void setClick(String click_type) {
		this.click_type = click_type;
	}

	public String getYCode() {
		return setYCode;
	}

	public void setYCode(String setYCode) {
		this.setYCode = setYCode;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}

//	public ArrayList<String> getGenre() {
//		return genre;
//	}
//
//	public void setGenre(ArrayList<String> genre) {
//		this.genre = genre;
//	}

}
