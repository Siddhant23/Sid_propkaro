package com.propkaro.mandate;

public class MandateSetter {
	private String tv_title = "", thumbnailUrl, imageUrl, NEW_PROPERTY_ID ,
			tv_location, tv_area, tv_rate, tv_postedBy, tv_userType, tv_revenue,
			tv_postedTime, tv_proprty_listing_parent, phone_no,
			property_location,amenities,
			no_of_bathrooms, no_of_bedrooms, no_of_washrooms, property_type_name,
			no_of_balcony, property_maintenance_amount, property_possession, 
			property_furnishing_type, transaction_type, property_ownership,
			property_availability, property_listing_type, property_description, 
			property_city, property_landmark, expected_unit_price, expected_unit_price_unit,
			property_type_name_parent, property_type_parent, property_area, 
			property_latitude, property_longitude, company_name, use_company_name,
			
			property_super_area, property_built_area, property_plot_area, 
			property_super_area_unit, property_built_area_unit, property_plot_area_unit, 
			property_floor, building_total_floors, expected_price, add_price, add_price_unit, 
			property_maintenance_frequency;
	private boolean isShortlisted = false, isBoolEnable = false;
	
	int USER_ID = 0,PROPERTY_ID = 0, OTHER_ID = 0, MANDATE_PROPERTY_ID = 0 , color;
//	private ArrayList<String> genre;

	public MandateSetter() {
	}

	public MandateSetter(
			String thumbnailUrl, String imageUrl, int USER_ID, int PROPERTY_ID,int OTHER_ID ,int MANDATE_PROPERTY_ID, 
			String tv_title, String tv_proprty_listing_parent, String NEW_PROPERTY_ID ,
			String tv_location, String tv_area, String tv_rate, String tv_postedBy, 
			String tv_userType, String tv_postedTime, 
			String no_of_bathrooms, String no_of_bedrooms, String no_of_washrooms, String no_of_balcony, 
			String property_maintenance_amount, String property_possession, 
			String property_furnishing_type, String transaction_type, String property_ownership, 
			String property_availability, String property_listing_type, 
			String property_description, String property_city, 
			String property_landmark, String expected_unit_price, String expected_unit_price_unit, 
			String property_type_name_parent, String property_type_parent, 
			String property_area, String property_latitude, String property_longitude, String phone_no, String company_name, String use_company_name, String add_price, String property_location, String property_type_name, String property_super_area, String property_built_area, String property_plot_area, String property_super_area_unit, String property_built_area_unit, String property_plot_area_unit, String property_floor, String building_total_floors, String expected_price, 
			String add_price_unit, String property_maintenance_frequency, int color, 
			boolean isShortlisted, boolean isBoolEnable, String tv_revenue) {
		this.thumbnailUrl = thumbnailUrl;
		this.imageUrl = imageUrl;
		this.USER_ID = USER_ID;
		this.OTHER_ID = OTHER_ID;
		this.MANDATE_PROPERTY_ID = MANDATE_PROPERTY_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		this.NEW_PROPERTY_ID = NEW_PROPERTY_ID;
		this.tv_title = tv_title;
		this.tv_location = tv_location;
		this.tv_area = tv_area;
		this.tv_rate = tv_rate;
		this.tv_postedBy = tv_postedBy;
		this.tv_userType = tv_userType;
		this.tv_postedTime = tv_postedTime;
		this.tv_proprty_listing_parent = tv_proprty_listing_parent;
		this.phone_no = phone_no;
		this.add_price = add_price;
		this.property_location = property_location;
		this.tv_revenue = tv_revenue;
		
		
		this.no_of_bathrooms = no_of_bathrooms;
		this.no_of_bedrooms = no_of_bedrooms;
		this.no_of_washrooms = no_of_washrooms;
		this.no_of_balcony = no_of_balcony;
		
		this.property_maintenance_amount = property_maintenance_amount;
		this.property_possession = property_possession;
		this.property_furnishing_type = property_furnishing_type;
		this.transaction_type = transaction_type;
		this.property_ownership = property_ownership;
		this.property_availability = property_availability;
		this.property_listing_type = property_listing_type;
		this.property_description = property_description;
		this.property_city = property_city;
		this.property_landmark = property_landmark;
		this.expected_unit_price = expected_unit_price;
		this.expected_unit_price_unit = expected_unit_price_unit;
		this.property_type_name_parent = property_type_name_parent;
		this.property_type_parent = property_type_parent;
		this.property_area = property_area;
		this.property_type_name = property_type_name;

		this.property_latitude = property_latitude;
		this.property_longitude = property_longitude;
		this.company_name = company_name;
		this.use_company_name = use_company_name;

		this.property_super_area = property_super_area;
		this.property_built_area = property_built_area;
		this.property_plot_area = property_plot_area;
		this.property_super_area_unit = property_super_area_unit;
		this.property_built_area_unit = property_built_area_unit;
		this.property_plot_area_unit = property_plot_area_unit;
		this.property_floor = property_floor;
		this.building_total_floors = building_total_floors;
		this.expected_price = expected_price;
		this.add_price_unit = add_price_unit;
		this.property_maintenance_frequency = property_maintenance_frequency;
		this.color = color;
		this.isShortlisted = isShortlisted;
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
	
	public int getOTHER_ID() {
		return OTHER_ID;
	}
	public void setOTHER_ID(int OTHER_ID) {
		this.OTHER_ID = OTHER_ID;
	}
	
	public int getMANDATE_PROPERTY_ID() {
		return MANDATE_PROPERTY_ID;
	}
	public void setMANDATE_PROPERTY_ID(int MANDATE_PROPERTY_ID) {
		this.MANDATE_PROPERTY_ID = MANDATE_PROPERTY_ID;
	}
	
	public String getNEW_PROPERTY_ID() {
		return NEW_PROPERTY_ID;
	}
	public void setNEW_PROPERTY_ID(String NEW_PROPERTY_ID) {
		this.NEW_PROPERTY_ID = NEW_PROPERTY_ID;
	}
	
	public String getTv_title() {
		return tv_title;
	}

	public void setTv_title(String tv_title) {
		this.tv_title = tv_title;
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
		
		MandateSetter other = (MandateSetter) obj;
		if (PROPERTY_ID != other.PROPERTY_ID)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Product [USER_ID=" + USER_ID + ", id_package=" + PROPERTY_ID 
				+ ", tv_title=" + tv_title + ", thumbnailUrl=" + thumbnailUrl 
				+ ", tv_location=" + tv_location + ", tv_area=" + tv_area 
				+ ", tv_rate=" + tv_rate + ", tv_postedBy=" + tv_postedBy 
				+ ", tv_userType=" + tv_userType 
				+ ", tv_postedTime=" + tv_postedTime 
				+ ", no_of_bathrooms=" + no_of_bathrooms 
				+ ", no_of_bedrooms=" + no_of_bedrooms 
				+ ", no_of_washrooms=" + no_of_washrooms 
				+ ", no_of_balcony=" + no_of_balcony 
				+ ", property_maintenance_amount=" + property_maintenance_amount 
				+ ", property_possession=" + property_possession 
				+ ", property_furnishing_type=" + property_furnishing_type 
				+ ", transaction_type=" + transaction_type 
				+ ", property_availability=" + property_availability 
				+ ", property_listing_type=" + property_listing_type 
				+ ", property_ownership=" + property_ownership 
				+ ", property_description=" + property_description 
				+ ", property_city=" + property_city 
				+ ", property_landmark=" + property_landmark 
 				+ ", expected_unit_price=" + expected_unit_price 
				+ ", expected_unit_price_unit=" + expected_unit_price_unit 
				+ ", property_type_name_parent=" + property_type_name_parent 
				+ ", property_type_parent=" + property_type_parent 
				+ ", property_area=" + property_area 
 				+ ", property_location=" + property_location 
 				+ ", amenities=" + amenities 
				+ ", property_latitude=" + property_latitude 
				+ ", property_longitude=" + property_longitude
				+ ", property_super_area=" + property_super_area
				+ ", property_built_area=" + property_built_area
				+ ", property_plot_area=" + property_plot_area
				+ ", property_super_area_unit=" + property_super_area_unit 
				+ ", property_built_area_unit=" + property_built_area_unit 
				+ ", property_plot_area_unit=" + property_plot_area_unit 
				+ ", property_floor=" + property_floor 
				+ ", building_total_floors=" + building_total_floors 
				+ ", expected_price=" + expected_price 
				+ ", add_price_unit=" + add_price_unit 
				+ ", add_price=" + add_price 
				+ "]";
	}

	public String getNo_of_bathrooms() {
		return no_of_bathrooms;
	}

	public void setNo_of_bathrooms(String no_of_bathrooms) {
		this.no_of_bathrooms = no_of_bathrooms;
	}

	public String getNo_of_bedrooms() {
		return no_of_bedrooms;
	}

	public void setNo_of_bedrooms(String no_of_bedrooms) {
		this.no_of_bedrooms = no_of_bedrooms;
	}

	public String getNo_of_washrooms() {
		return no_of_washrooms;
	}

	public void setNo_of_washrooms(String no_of_washrooms) {
		this.no_of_washrooms = no_of_washrooms;
	}

	public String getNo_of_balcony() {
		return no_of_balcony;
	}

	public void setNo_of_balcony(String no_of_balcony) {
		this.no_of_balcony = no_of_balcony;
	}

	public String getProperty_maintenance_amount() {
		return property_maintenance_amount;
	}

	public void setProperty_maintenance_amount(
			String property_maintenance_amount) {
		this.property_maintenance_amount = property_maintenance_amount;
	}

	public String getProperty_possession() {
		return property_possession;
	}

	public void setProperty_possession(String property_possession) {
		this.property_possession = property_possession;
	}

	public String getProperty_furnishing_type() {
		return property_furnishing_type;
	}

	public void setProperty_furnishing_type(String property_furnishing_type) {
		this.property_furnishing_type = property_furnishing_type;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getProperty_availability() {
		return property_availability;
	}

	public void setProperty_availability(String property_availability) {
		this.property_availability = property_availability;
	}

	public String getProperty_listing_type() {
		return property_listing_type;
	}

	public void setProperty_listing_type(String property_listing_type) {
		this.property_listing_type = property_listing_type;
	}

	public String getProperty_ownership() {
		return property_ownership;
	}

	public void setProperty_ownership(String property_ownership) {
		this.property_ownership = property_ownership;
	}

	
	
	public String getProperty_type_name() {
		return property_type_name;
	}

	public void setProperty_type_name(String property_type_name) {
		this.property_type_name = property_type_name;
	}
	public String getProperty_description() {
		return property_description;
	}

	public void setProperty_description(String property_description) {
		this.property_description = property_description;
	}

	public String getProperty_city() {
		return property_city;
	}

	public void setProperty_city(String property_city) {
		this.property_city = property_city;
	}

	public String getProperty_landmark() {
		return property_landmark;
	}

	public void setProperty_landmark(String property_landmark) {
		this.property_landmark = property_landmark;
	}

	public String getExpected_unit_price() {
		return expected_unit_price;
	}

	public void setExpected_unit_price(String expected_unit_price) {
		this.expected_unit_price = expected_unit_price;
	}

	public String getExpected_unit_price_unit() {
		return expected_unit_price_unit;
	}

	public void setExpected_unit_price_unit(String expected_unit_price_unit) {
		this.expected_unit_price_unit = expected_unit_price_unit;
	}

	public String getProperty_type_name_parent() {
		return property_type_name_parent;
	}

	public void setProperty_type_name_parent(String property_type_name_parent) {
		this.property_type_name_parent = property_type_name_parent;
	}

	public String getProperty_type_parent() {
		return property_type_parent;
	}

	public void setProperty_type_parent(String property_type_parent) {
		this.property_type_parent = property_type_parent;
	}

	public String getProperty_area() {
		return property_area;
	}

	public void setProperty_area(String property_area) {
		this.property_area = property_area;
	}

	public String getProperty_latitude() {
		return property_latitude;
	}

	public void setProperty_latitude(String property_latitude) {
		this.property_latitude = property_latitude;
	}

	public String getProperty_longitude() {
		return property_longitude;
	}

	public void setProperty_longitude(String property_longitude) {
		this.property_longitude = property_longitude;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getUse_company_name() {
		return use_company_name;
	}

	public void setUse_company_name(String use_company_name) {
		this.use_company_name = use_company_name;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAdd_price() {
		return add_price;
	}

	public void setAdd_price(String add_price) {
		this.add_price = add_price;
	}

	public String getProperty_location() {
		return property_location;
	}

	public void setProperty_location(String property_location) {
		this.property_location = property_location;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public String getProperty_super_area() {
		return property_super_area;
	}

	public void setProperty_super_area(String property_super_area) {
		this.property_super_area = property_super_area;
	}

	public String getProperty_built_area() {
		return property_built_area;
	}

	public void setProperty_built_area(String property_built_area) {
		this.property_built_area = property_built_area;
	}

	public String getProperty_plot_area() {
		return property_plot_area;
	}

	public void setProperty_plot_area(String property_plot_area) {
		this.property_plot_area = property_plot_area;
	}

	public String getProperty_super_area_unit() {
		return property_super_area_unit;
	}

	public void setProperty_super_area_unit(String property_super_area_unit) {
		this.property_super_area_unit = property_super_area_unit;
	}

	public String getProperty_built_area_unit() {
		return property_built_area_unit;
	}

	public void setProperty_built_area_unit(String property_built_area_unit) {
		this.property_built_area_unit = property_built_area_unit;
	}

	public String getProperty_plot_area_unit() {
		return property_plot_area_unit;
	}

	public void setProperty_plot_area_unit(String property_plot_area_unit) {
		this.property_plot_area_unit = property_plot_area_unit;
	}

	public String getProperty_floor() {
		return property_floor;
	}

	public void setProperty_floor(String property_floor) {
		this.property_floor = property_floor;
	}

	public String getBuilding_total_floors() {
		return building_total_floors;
	}

	public void setBuilding_total_floors(String building_total_floors) {
		this.building_total_floors = building_total_floors;
	}

	public String getExpected_price() {
		return expected_price;
	}

	public void setExpected_price(String expected_price) {
		this.expected_price = expected_price;
	}

	public String getAdd_price_unit() {
		return add_price_unit;
	}

	public void setAdd_price_unit(String add_price_unit) {
		this.add_price_unit = add_price_unit;
	}

	public String getProperty_maintenance_frequency() {
		return property_maintenance_frequency;
	}

	public void setProperty_maintenance_frequency(
			String property_maintenance_frequency) {
		this.property_maintenance_frequency = property_maintenance_frequency;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean getIsShortlisted() {
		return isShortlisted;
	}

	public void setIsShortlisted(boolean isShortlisted) {
		this.isShortlisted = isShortlisted;
	}

	public boolean getIsBoolEnable() {
		return isBoolEnable;
	}

	public void setIsBoolEnable(boolean isBoolEnable) {
		this.isBoolEnable = isBoolEnable;
	}

	public String getTv_revenue() {
		return tv_revenue;
	}

	public void setTv_revenue(String tv_revenue) {
		this.tv_revenue = tv_revenue;
	}

	 
}
