package com.propkaro.timeline;

public class MyTimelineSetter {
	private String thumbnailUrl, image, 
		activity_id, activity_type_id, event_id, posted_by, type, shared_by,comment_by, 
		activity_datetime, like, content, imageUrl,activity_comment,
		share_content, status, timestamp, type_id, type_name, id, fname, lname, 
		connect_from = "", connect_to = "", share = "", total_likes, 
		comments = "", property_details, property_images;
		int USER_ID = 0, PROPERTY_ID = 0,comment_id =0, color;
		boolean bool_enable, bool_hasLike;
//	private ArrayList<String> genre;

	public MyTimelineSetter() {
	}

	public MyTimelineSetter(
			String thumbnailUrl, String image, int USER_ID, int PROPERTY_ID,int comment_id, 
			String activity_id, String activity_type_id, String event_id, String posted_by, String type, 
			String shared_by, String like, String content, 
			String share_content, String status, String timestamp, String type_id, String type_name, 
			String fname, String lname, String share, String total_likes, String comments, String id, String connect_from, String connect_to, String property_details, String property_images, String imageUrl, 
			String comment_by, String activity_comment, int color, boolean bool_enable, boolean bool_hasLike
			) {
		super();
		this.thumbnailUrl = thumbnailUrl;
		this.image = image;
		
		this.activity_comment = activity_comment;
		this.comment_by = comment_by;
		this.imageUrl = imageUrl;
		this.USER_ID = USER_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		
		this.comment_id = comment_id;
		this.activity_id = activity_id;
		this.activity_type_id = activity_type_id;
		this.event_id = event_id;
		this.posted_by = posted_by;
		this.type = type;
		this.shared_by = shared_by;
		this.id = id;
		this.like = like;
		this.content = content;
		this.share_content = share_content;
		this.status = status;
		this.timestamp = timestamp;
		this.type_id = type_id;
		this.type_name = type_name;
		this.fname = fname;
		this.lname = lname;
		this.connect_from = connect_from;
		this.connect_to = connect_to;
		this.share = share;
		this.total_likes = total_likes;
		this.comments = comments;
		this.property_details = property_details;
		this.property_images = property_images;
		this.color = color;
		this.bool_enable = bool_enable;
		this.bool_hasLike = bool_hasLike;
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
	
	public int getcomment_id() {
		return comment_id;
	}
	public void setcomment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity_type_id() {
		return activity_type_id;
	}

	public void setActivity_type_id(String activity_type_id) {
		this.activity_type_id = activity_type_id;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getPosted_by() {
		return posted_by;
	}

	public void setPosted_by(String posted_by) {
		this.posted_by = posted_by;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShared_by() {
		return shared_by;
	}

	public void setShared_by(String shared_by) {
		this.shared_by = shared_by;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getTotal_likes() {
		return total_likes;
	}

	public void setTotal_likes(String total_likes) {
		this.total_likes = total_likes;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActivity_datetime() {
		return activity_datetime;
	}

	public void setActivity_datetime(String activity_datetime) {
		this.activity_datetime = activity_datetime;
	}

	public String getConnect_to() {
		return connect_to;
	}

	public void setConnect_to(String connect_to) {
		this.connect_to = connect_to;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConnect_from() {
		return connect_from;
	}

	public void setConnect_from(String connect_from) {
		this.connect_from = connect_from;
	}

	public String getProperty_details() {
		return property_details;
	}

	public void setProperty_details(String property_details) {
		this.property_details = property_details;
	}

	public String getProperty_images() {
		return property_images;
	}

	public void setProperty_images(String property_images) {
		this.property_images = property_images;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getComment_by() {
		return comment_by;
	}

	public void setComment_by(String comment_by) {
		this.comment_by = comment_by;
	}

	public String getActivity_comment() {
		return activity_comment;
	}

	public void setActivity_comment(String activity_comment) {
		this.activity_comment = activity_comment;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean getBool_enable() {
		return bool_enable;
	}

	public void setBool_enable(boolean bool_enable) {
		this.bool_enable = bool_enable;
	}

	public boolean getBool_hasLike() {
		return bool_hasLike;
	}

	public void setBool_hasLike(boolean bool_hasLike) {
		this.bool_hasLike = bool_hasLike;
	}

}
