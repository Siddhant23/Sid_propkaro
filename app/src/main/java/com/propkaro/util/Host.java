package com.propkaro.util;

import android.content.Context;
import android.util.Log;

import com.propkaro.mandate.MandateSetter;
import com.propkaro.propertycenter.AvailPCSetter;

import org.json.JSONObject;

import java.util.ArrayList;

public final class Host {
	public static String TAG = Host.class.getSimpleName();
	public static final String FACEBOOK_ID = "343771982485455"; // PROPKARO
	public static final String TOCKEN = "C%8<o1R2A1*G*X,9]5a85459C6Xzx!Q&y>mZ[4s7A=~5u"; // TOCKEN_KEY
	public static final String SECRET = "aqcfrtyg4567";
	public static final String INDIA_CODE = "91";
	//*****************Marsemellow Permissions**************************
	public static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
	public static final int LOCATION_PERMISSIONS_REQUEST = 2;
	public static final int ACCOUNTS_PERMISSIONS_REQUEST = 3;
	public static final int WRITE_STORAGE_PERMISSIONS_REQUEST = 4;
	
	public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 111;
	//*****************Marsemellow Permissions Ends**************************
	public static String rssUrl = "http://economictimes.indiatimes.com/rssfeeds/1058830.cms";
	public static String MainApi = "https://www.propkaro.com/api/";

	public static String MainUrl = "http://api.propkarodev.com:4032/";
	static String ApiVer = "/v1/";
	public static String UserRegister = "user/register" + ApiVer;
	public static String UserOTP = "user/verifyOtp" + ApiVer;
	public static String UserOTPResend = "user/resendOtp" + ApiVer;
	public static String UserLogin = "user/login" + ApiVer;
	public static String UserReLogin = "user/relogin" + ApiVer;
	public static String UserUploadPic = "user/uploadPic" + ApiVer;
	public static String UserDetails = "user/details" + ApiVer;

	public static String LocalityUrl = "https://www.propkaro.com/api/timeline/search-location/";
	public static String AutoCompleteUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
	public static String DetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?";
	public static String API_KEY = "AIzaSyC7GMNzBOLg-L_FuXUD9NuzviHvjkUWpBg";//"AIzaSyCNyBDdq-5uA-zi8Y8U4oMCzMuBioLDi_M";
	
	public static String LoginUrl = "https://www.propkaro.com/api/auth/login/";
	public static String DeleteUrl = "https://www.propkaro.com/api/rest/delete/";
	public static String UpdateUrl = "https://www.propkaro.com/api/rest/put/";
	public static String GetUrl = "https://www.propkaro.com/api/rest/get/";
	public static String PostUrl = "https://www.propkaro.com/api/rest/post/";

	public static String SearchUrl = "https://www.propkaro.com/api/secure/filter-property/";//get, post, put, delete
	public static String SearchFriendUrl = "https://www.propkaro.com/api/timeline/search-friends/";
	public static String ForgotUrl = "https://www.propkaro.com/api/secure/forgot-password/";//get, post, put, delete
	
	public static String TimelineUrl = "https://www.propkaro.com/api/timeline/get-activities/";
	public static String GetAllCommentsUrl = "https://www.propkaro.com/api/timeline/get-all-comments/";
	
	public static String ProfileImgPath = "https://www.propkaro.com/images/profile_images/";
	public static String ImgsPath = "https://www.propkaro.com/property_images/uploads/";
	
	public static String ImgDemoPath = "https://www.propkaro.com/property_images/uploads/144629900130966.jpg";
	public static String ImgProfilePath = "https://www.propkaro.com/images/profile_images/default.png";
	
	public static String ErrorAll = "/error/1";
	public static String LocalityApi = "https://www.propkaro.com/api/timeline/search-property-location/";
	public static String UploadProfileUrl = "https://www.propkaro.com/api/secure/upload-image/";//
	public static String UploadCsvUrl = "https://www.propkaro.com/api/secure/upload-csv/";//
	
	public static String AllNotificationsUrl = "https://www.propkaro.com/api/timeline/get-cmn/";
	
	public static String getUserDetailsUrl = "https://www.propkaro.com/api/timeline/get-user-details/";
	public static String getAllMessagesUrl = "https://www.propkaro.com/api/timeline/get-all-messages/";
	public static String getInboxMessagesUrl = "https://www.propkaro.com/api/timeline/get-inbox-messages/";
	public static String getConnectionUrl = "https://www.propkaro.com/api/timeline/get-connections/";
	public static String getSuggestedFrinedUrl = "https://www.propkaro.com/api/timeline/get-suggested-friends/";
	public static String FacilitationCenterUrl = "https://www.propkaro.com/api/rest/get/json/eyJ0YWJsZSI6ImZhY2lsaXRhdGlvbnMiLCJtdWx0aXBsZSI6IkFsbCJ9";
	public static String TestCitySelect = "https://www.propkaro.com/api/rest/get/";
	public static String ShortListUrl = "https://www.propkaro.com/api/timeline/get-short-lists/";
	public static String MandateUrl = "https://www.propkaro.com/api/secure/filter-mandate-property/";
	
	public static void parseProperty(Context ctx, JSONObject obj, ArrayList<AvailPCSetter> AvailList, boolean isRefresh){
		try {
//		if(obj.getString("property_listing_parent").contains("Availability")){
		AvailPCSetter ch = new AvailPCSetter();

		if (obj.getString("user_id").equals("") || obj.getString("user_id").equals(null)
				|| obj.getString("user_id").equals("null")) {
		} else {
			ch.setUSER_ID(Integer.parseInt(obj.getString("user_id")));
		}
		if (obj.getString("property_id").equals("") || obj.getString("property_id").equals(null)
				|| obj.getString("property_id").equals("null")) {
		} else {
			ch.setPROPERTY_ID(Integer.parseInt(obj.getString("property_id")));
		}

		if(obj.getString("image").contains("http"))
			ch.setImageUrl(obj.getString("image"));
		else
			ch.setImageUrl(Host.MainUrl + obj.getString("image"));

		if(obj.getString("image").contains("http"))
			ch.setThumbnailUrl(obj.getString("image"));
		else
			ch.setThumbnailUrl(Host.MainUrl + obj.getString("image"));

		if (obj.getString("property_added_on").equals("")
				|| obj.getString("property_added_on").equals(null)
				|| obj.getString("property_added_on").equals("null")) {
			ch.setTv_postedTime(" -- ");
		} else {
			ch.setTv_postedTime(obj.getString("property_added_on"));
		}

		if (obj.getString("property_location").equals("")
				|| obj.getString("property_location").equals(null)
				|| obj.getString("property_location").equals("null")) {
			ch.setTv_location(" -- ");
		} else {
			ch.setTv_location(obj.getString("property_location"));
		}

		//---------------------add super, carpet, plot areas here-------------------------
		if (obj.getString("property_maintenance_frequency").equals("")
				|| obj.getString("property_maintenance_frequency").equals(null)
				|| obj.getString("property_maintenance_frequency").contains("Select")) {
			ch.setProperty_maintenance_frequency("");
		} else {
			ch.setProperty_maintenance_frequency(obj.getString("property_maintenance_frequency"));
		}
		ch.setProperty_super_area(obj.getString("property_super_area"));
		ch.setProperty_built_area(obj.getString("property_built_area"));
		ch.setProperty_plot_area(obj.getString("property_plot_area"));
		ch.setProperty_super_area_unit(obj.getString("property_super_area_unit"));
		ch.setProperty_built_area_unit(obj.getString("property_built_area_unit"));
		ch.setProperty_plot_area_unit(obj.getString("property_plot_area_unit"));
		ch.setBuilding_total_floors(obj.getString("building_total_floors"));

		String temp_area = " -- ";
		if (!(obj.getString("property_super_area")).isEmpty()
				&& !(obj.getString("property_super_area").equals("null"))) {
			temp_area = obj.getString("property_super_area") +" "+obj.getString("property_super_area_unit");
			ch.setTv_area(temp_area);
		} else if(!(obj.getString("property_built_area")).isEmpty()
				&& !(obj.getString("property_built_area").equals("null"))) {
			temp_area = obj.getString("property_built_area") +" "+obj.getString("property_built_area_unit");
			ch.setTv_area(temp_area);
		} else if(!(obj.getString("property_plot_area")).isEmpty()
				&& !(obj.getString("property_plot_area").equals("null"))) {
			temp_area = obj.getString("property_plot_area") +" "+obj.getString("property_plot_area_unit");
			ch.setTv_area(temp_area);
		} else {
			ch.setTv_area(temp_area);
		}

		if (obj.getString("expected_price").equals("")
				|| obj.getString("expected_price").equals(null)
				|| obj.getString("expected_price").equals("null")) {
			ch.setExpected_unit_price(" -- ");
			ch.setTv_rate(" -- ");
		} else {
			ch.setExpected_unit_price(Host.rupeeFormat(obj.getString("expected_price")));
			ch.setTv_rate(Host.rupeeFormat(obj.getString("expected_price")));
		}
		if (obj.getString("phone_no").equals("")
				|| obj.getString("phone_no").equals(null)
				|| obj.getString("phone_no").equals("null")) {
			ch.setPhone_no(" -- ");
		} else {
			ch.setPhone_no(obj.getString("phone_no"));
		}
		//-----------------
		if (obj.getString("property_floor").equals("")
				|| obj.getString("property_floor").equals(null)
				|| obj.getString("property_floor").equals("null")) {
			ch.setProperty_floor(" -- ");
		} else {
			ch.setProperty_floor(obj.getString("property_floor"));
		}

		if (obj.getString("no_of_bathrooms").equals("")
				|| obj.getString("no_of_bathrooms").equals(null)
				|| obj.getString("no_of_bathrooms").equals("null")) {
			ch.setNo_of_bathrooms(" -- ");
		} else {
			ch.setNo_of_bathrooms(obj.getString("no_of_bathrooms"));
		}
		if (obj.getString("no_of_bedrooms").contains("Select")){
			ch.setNo_of_bedrooms(" -- ");
		}
		if (obj.getString("no_of_bedrooms").equals("")
				|| obj.getString("no_of_bedrooms").equals(null)
				|| obj.getString("no_of_bedrooms").equals("null")) {
			ch.setNo_of_bedrooms(" -- ");
		} else {
			ch.setNo_of_bedrooms(obj.getString("no_of_bedrooms"));
		}
		if (obj.getString("no_of_washrooms").equals("")
				|| obj.getString("no_of_washrooms").equals(null)
				|| obj.getString("no_of_washrooms").equals("null")) {
			ch.setNo_of_washrooms(" -- ");
		} else {
			ch.setNo_of_washrooms(obj.getString("no_of_washrooms"));
		}
		if (obj.getString("no_of_balcony").equals("")
				|| obj.getString("no_of_balcony").equals(null)
				|| obj.getString("no_of_balcony").equals("null")) {
			ch.setNo_of_balcony(" -- ");
		} else {
			ch.setNo_of_balcony(obj.getString("no_of_balcony"));
		}
		//-------------------------
		if (obj.getString("property_maintenance_amount").equals("")
				|| obj.getString("property_maintenance_amount").equals(null)
				|| obj.getString("property_maintenance_amount").equals("null")) {
			ch.setProperty_maintenance_amount(" -- ");
		} else {
			ch.setProperty_maintenance_amount(obj.getString("property_maintenance_amount"));
		}

		if (obj.getString("property_possession").equals("")
				|| obj.getString("property_possession").equals(null)
				|| obj.getString("property_possession").equals("null")) {
			ch.setProperty_possession(" -- ");
		} else {
			ch.setProperty_possession(obj.getString("property_possession"));
		}

		if (obj.getString("property_furnishing_type").equals("")
				|| obj.getString("property_furnishing_type").equals(null)
				|| obj.getString("property_furnishing_type").equals("null")) {
			ch.setProperty_furnishing_type(" -- ");
		} else {
			ch.setProperty_furnishing_type(obj.getString("property_furnishing_type"));
		}


		if (obj.getString("transaction_type").equals("")
				|| obj.getString("transaction_type").equals(null)
				|| obj.getString("transaction_type").equals("null")) {
			ch.setTransaction_type(" -- ");
		} else {
			ch.setTransaction_type(obj.getString("transaction_type"));
		}


		if (obj.getString("property_ownership").equals("")
				|| obj.getString("property_ownership").equals(null)
				|| obj.getString("property_ownership").equals("null")) {
			ch.setProperty_ownership(" -- ");
		} else {
			ch.setProperty_ownership(obj.getString("property_ownership"));
		}

		if (obj.getString("property_availability").equals("")
				|| obj.getString("property_availability").equals(null)
				|| obj.getString("property_availability").equals("null")) {
			ch.setProperty_availability(" -- ");
		} else {
			ch.setProperty_availability(obj.getString("property_availability"));
		}

		if (obj.getString("property_listing_type").equals("")
				|| obj.getString("property_listing_type").equals(null)
				|| obj.getString("property_listing_type").equals("null")) {
			ch.setProperty_listing_type(" -- ");
		} else {
			ch.setProperty_listing_type(obj.getString("property_listing_type"));
		}
		//------------------------
		if (obj.getString("property_description").equals("")
				|| obj.getString("property_description").equals(null)
				|| obj.getString("property_description").equals("null")) {
			ch.setProperty_description(" -- ");
		} else {
			ch.setProperty_description(obj.getString("property_description"));
		}

		if (obj.getString("property_city").equals("")
				|| obj.getString("property_city").equals(null)
				|| obj.getString("property_city").equals("null")) {
			ch.setProperty_city(" -- ");
		} else {
			ch.setProperty_city(obj.getString("property_city"));
		}

		if (obj.getString("property_landmark").equals("")
				|| obj.getString("property_landmark").equals(null)
				|| obj.getString("property_landmark").equals("null")) {
			ch.setProperty_landmark(" -- ");
		} else {
			ch.setProperty_landmark(obj.getString("property_landmark"));
		}
		//------------------------------
		if (obj.getString("expected_price").equals("")
				|| obj.getString("expected_price").equals(null)
				|| obj.getString("expected_price").equals("null")) {
			ch.setExpected_unit_price("" + " -- ");
		} else {
			ch.setExpected_unit_price("" + obj.getString("expected_price"));
		}

		if (obj.getString("expected_unit_price_unit").equals("")
				|| obj.getString("expected_unit_price_unit").equals(null)
				|| obj.getString("expected_unit_price_unit").equals("null")) {
			ch.setExpected_unit_price_unit(" -- ");
		} else {
			ch.setExpected_unit_price_unit(obj.getString("expected_unit_price_unit"));
		}

		if (obj.getString("property_type_name_parent").equals("")
				|| obj.getString("property_type_name_parent").equals(null)
				|| obj.getString("property_type_name_parent").equals("null")) {
			ch.setProperty_type_name_parent(" -- ");
		} else {
			ch.setProperty_type_name_parent(obj
					.getString("property_type_name_parent"));
		}

		if (obj.getString("property_type_name").equals("")
				|| obj.getString("property_type_name").equals(null)
				|| obj.getString("property_type_name").equals(
						"null")) {
			ch.setProperty_type_name(" -- ");
		} else {
			ch.setProperty_type_name(obj
					.getString("property_type_name"));
		}

		if (obj.getString("property_type_parent").equals("")
				|| obj.getString("property_type_parent").equals(null)
				|| obj.getString("property_type_parent").equals("null")) {
			ch.setProperty_type_parent(" -- ");
		} else {
			ch.setProperty_type_parent(obj.getString("property_type_parent"));
		}
		if (obj.getString("property_area").equals("")
				|| obj.getString("property_area").equals(null)
				|| obj.getString("property_area").equals("null")) {
			ch.setProperty_area(" -- ");
		} else {
			ch.setProperty_area(obj.getString("property_area"));
		}
		//----------------------------------
		if (obj.getString("property_title").equals("")
				|| obj.getString("property_title").equals(null)
				|| obj.getString("property_title").equals("null")) {
			ch.setTv_title(" -- ");
		} else {
			ch.setTv_title(obj.getString("property_title"));
		}
		if (obj.getString("property_listing_parent").equals("")
				|| obj.getString("property_listing_parent").equals(null)
				|| obj.getString("property_listing_parent").equals("null")) {
			ch.setTv_proprty_listing_parent(" -- ");
		} else {
			ch.setTv_proprty_listing_parent(obj.getString("property_listing_parent"));
		}
		if (obj.getString("amenities").equals("")
				|| obj.getString("amenities").equals(null)
				|| obj.getString("amenities").equals("null")) {
			ch.setAmenities(" -- ");
		} else {
			ch.setAmenities(obj.getString("amenities"));
		}

		if (obj.getString("fname").equals("")
				|| obj.getString("fname").equals(null)
				|| obj.getString("fname").contains("--")
				|| obj.getString("fname").equals("null")) {
			ch.setTv_postedBy(" -- ");
			ch.setTv_postedBy(obj.getString("company_name"));
		} else {
			ch.setTv_postedBy(Utilities.makeFirstLetterCaps(obj.getString("fname"))
					+ " " + Utilities.makeFirstLetterCaps(obj.getString("lname")));
		}
		if (obj.getString("user_type").equals("")
				|| obj.getString("user_type").equals(null)
				|| obj.getString("user_type").equals("null")) {
			ch.setTv_userType(" -- ");
		} else {
			ch.setTv_userType(obj.getString("user_type"));
		}
		if (obj.getString("property_latitude").equals("")
				|| obj.getString("property_latitude").equals(null)
				|| obj.getString("property_latitude").equals("null")) {
			ch.setProperty_latitude("0.0");
		} else {
			ch.setProperty_latitude(obj.getString("property_latitude"));
		}
		if (obj.getString("property_longitude").equals("")
				|| obj.getString("property_longitude").equals(null)
				|| obj.getString("property_longitude").equals("null")) {
			ch.setProperty_longitude("0.0");
		} else {
			ch.setProperty_longitude(obj.getString("property_longitude"));
		}

//		if (obj.getString("broker_revenue").equals("")
//				|| obj.getString("broker_revenue").equals(null)
//				|| obj.getString("broker_revenue").equals("null")) {
//			ch.setBroker_revenue("--");
//		} else {
//			ch.setBroker_revenue(obj.getString("broker_revenue"));
//		}

//------------------------------added extras------------------------------------
		if (obj.getString("use_company_name").equals("")
				|| obj.getString("use_company_name").equals(null)
				|| obj.getString("use_company_name").equals("null")) {
			ch.setUse_company_name("0");
		} else {
			ch.setUse_company_name(obj.getString("use_company_name"));
		}
		if (obj.getString("company_name").equals("")
				|| obj.getString("company_name").equals(null)
				|| obj.getString("company_name").equals("null")) {
			ch.setCompany_name(" -- ");
			ch.setCompany_name(obj.getString("fname"));
		} else {
			ch.setCompany_name(obj.getString("company_name"));
		}

		if(obj.getInt("is_shortlist") == 0)
			ch.setIsShortlisted(false);
		else if(obj.getInt("is_shortlist") == 1)
			ch.setIsShortlisted(true);

		ch.setIsBoolEnable(true);
		ch.setColor(Utilities.getRandomColor(ctx,obj.getString("fname")));

		if(isRefresh)
			AvailList.add(0, ch);
		else
			AvailList.add(ch);
		} catch (Exception e) { e.printStackTrace(); }
	}
//------------------------part2------------------------------
	public static void parseShortlist(Context ctx, JSONObject obj, ArrayList<AvailPCSetter> AvailList, boolean isRefresh){
		try {
//			if(obj.getString("property_listing_parent").contains("Availability")){
			AvailPCSetter ch = new AvailPCSetter();

			if (obj.getString("user_id").equals("") || obj.getString("user_id").equals(null)
					|| obj.getString("user_id").equals("null")) {
			} else {
				ch.setUSER_ID(Integer.parseInt(obj.getString("user_id")));
			}
			if (obj.getString("property_id").equals("") || obj.getString("property_id").equals(null)
					|| obj.getString("property_id").equals("null")) {
			} else {
				ch.setPROPERTY_ID(Integer.parseInt(obj.getString("property_id")));
			}

			if(obj.getString("image").contains("http"))
				ch.setImageUrl(obj.getString("image"));
			else
				ch.setImageUrl(Host.MainUrl + obj.getString("image"));

			if(obj.getString("image").contains("http"))
				ch.setThumbnailUrl(obj.getString("image"));
			else
				ch.setThumbnailUrl(Host.MainUrl + obj.getString("image"));

			if (obj.getString("property_added_on").equals("")
					|| obj.getString("property_added_on").equals(null)
					|| obj.getString("property_added_on").equals("null")) {
				ch.setTv_postedTime(" -- ");
			} else {
				ch.setTv_postedTime(obj.getString("property_added_on"));
			}

			if (obj.getString("property_location").equals("")
					|| obj.getString("property_location").equals(null)
					|| obj.getString("property_location").equals("null")) {
				ch.setTv_location(" -- ");
			} else {
				ch.setTv_location(obj.getString("property_location"));
			}
//---------------------add super, carpet, plot areas here-------------------------
			if (obj.getString("property_maintenance_frequency").equals("")
					|| obj.getString("property_maintenance_frequency").equals(null)
					|| obj.getString("property_maintenance_frequency").contains("Select")) {
				ch.setProperty_maintenance_frequency("");
			} else {
				ch.setProperty_maintenance_frequency(obj.getString("property_maintenance_frequency"));
			}
			ch.setProperty_super_area(obj.getString("property_super_area"));
			ch.setProperty_built_area(obj.getString("property_built_area"));
			ch.setProperty_plot_area(obj.getString("property_plot_area"));
			ch.setProperty_super_area_unit(obj.getString("property_super_area_unit"));
			ch.setProperty_built_area_unit(obj.getString("property_built_area_unit"));
			ch.setProperty_plot_area_unit(obj.getString("property_plot_area_unit"));
			ch.setBuilding_total_floors(obj.getString("building_total_floors"));

			String temp_area = " -- ";
			if (!(obj.getString("property_super_area")).isEmpty()
					&& !(obj.getString("property_super_area").equals("null"))) {
				temp_area = obj.getString("property_super_area") +" "+obj.getString("property_super_area_unit");
				ch.setTv_area(temp_area);
			} else if(!(obj.getString("property_built_area")).isEmpty()
					&& !(obj.getString("property_built_area").equals("null"))) {
				temp_area = obj.getString("property_built_area") +" "+obj.getString("property_built_area_unit");
				ch.setTv_area(temp_area);
			} else if(!(obj.getString("property_plot_area")).isEmpty()
					&& !(obj.getString("property_plot_area").equals("null"))) {
				temp_area = obj.getString("property_plot_area") +" "+obj.getString("property_plot_area_unit");
				ch.setTv_area(temp_area);
			} else {
				ch.setTv_area(temp_area);
			}

//			if (obj.getString("phone_no").equals("")
//					|| obj.getString("phone_no").equals(null)
//					|| obj.getString("phone_no").equals("null")) {
//				ch.setPhone_no(" -- ");
//			} else {
//				ch.setPhone_no(obj.getString("phone_no"));
//			}
			//-----------------
			if (obj.getString("property_floor").equals("")
					|| obj.getString("property_floor").equals(null)
					|| obj.getString("property_floor").equals("null")) {
				ch.setProperty_floor(" -- ");
			} else {
				ch.setProperty_floor(obj.getString("property_floor"));
			}

			if (obj.getString("no_of_bathrooms").equals("")
					|| obj.getString("no_of_bathrooms").equals(null)
					|| obj.getString("no_of_bathrooms").equals("null")) {
				ch.setNo_of_bathrooms(" -- ");
			} else {
				ch.setNo_of_bathrooms(obj.getString("no_of_bathrooms"));
			}
			if (obj.getString("no_of_bedrooms").contains("Select")){
				ch.setNo_of_bedrooms(" -- ");
			}
			if (obj.getString("no_of_bedrooms").equals("")
					|| obj.getString("no_of_bedrooms").equals(null)
					|| obj.getString("no_of_bedrooms").equals("null")) {
				ch.setNo_of_bedrooms(" -- ");
			} else {
				ch.setNo_of_bedrooms(obj.getString("no_of_bedrooms"));
			}
			if (obj.getString("no_of_washrooms").equals("")
					|| obj.getString("no_of_washrooms").equals(null)
					|| obj.getString("no_of_washrooms").equals("null")) {
				ch.setNo_of_washrooms(" -- ");
			} else {
				ch.setNo_of_washrooms(obj.getString("no_of_washrooms"));
			}
			if (obj.getString("no_of_balcony").equals("")
					|| obj.getString("no_of_balcony").equals(null)
					|| obj.getString("no_of_balcony").equals("null")) {
				ch.setNo_of_balcony(" -- ");
			} else {
				ch.setNo_of_balcony(obj.getString("no_of_balcony"));
			}
			//-------------------------
			if (obj.getString("property_maintenance_amount").equals("")
					|| obj.getString("property_maintenance_amount").equals(null)
					|| obj.getString("property_maintenance_amount").equals("null")) {
				ch.setProperty_maintenance_amount(" -- ");
			} else {
				ch.setProperty_maintenance_amount(obj.getString("property_maintenance_amount"));
			}

			if (obj.getString("property_possession").equals("")
					|| obj.getString("property_possession").equals(null)
					|| obj.getString("property_possession").equals("null")) {
				ch.setProperty_possession(" -- ");
			} else {
				ch.setProperty_possession(obj.getString("property_possession"));
			}

			if (obj.getString("property_furnishing_type").equals("")
					|| obj.getString("property_furnishing_type").equals(null)
					|| obj.getString("property_furnishing_type").equals("null")) {
				ch.setProperty_furnishing_type(" -- ");
			} else {
				ch.setProperty_furnishing_type(obj.getString("property_furnishing_type"));
			}


			if (obj.getString("transaction_type").equals("")
					|| obj.getString("transaction_type").equals(null)
					|| obj.getString("transaction_type").equals("null")) {
				ch.setTransaction_type(" -- ");
			} else {
				ch.setTransaction_type(obj.getString("transaction_type"));
			}


			if (obj.getString("property_ownership").equals("")
					|| obj.getString("property_ownership").equals(null)
					|| obj.getString("property_ownership").equals("null")) {
				ch.setProperty_ownership(" -- ");
			} else {
				ch.setProperty_ownership(obj.getString("property_ownership"));
			}

			if (obj.getString("property_availability").equals("")
					|| obj.getString("property_availability").equals(null)
					|| obj.getString("property_availability").equals("null")) {
				ch.setProperty_availability(" -- ");
			} else {
				ch.setProperty_availability(obj.getString("property_availability"));
			}

			if (obj.getString("property_listing_type").equals("")
					|| obj.getString("property_listing_type").equals(null)
					|| obj.getString("property_listing_type").equals("null")) {
				ch.setProperty_listing_type(" -- ");
			} else {
				ch.setProperty_listing_type(obj.getString("property_listing_type"));
			}
			//------------------------
			if (obj.getString("property_description").equals("")
					|| obj.getString("property_description").equals(null)
					|| obj.getString("property_description").equals("null")) {
				ch.setProperty_description(" -- ");
			} else {
				ch.setProperty_description(obj.getString("property_description"));
			}

			if (obj.getString("property_city").equals("")
					|| obj.getString("property_city").equals(null)
					|| obj.getString("property_city").equals("null")) {
				ch.setProperty_city(" -- ");
			} else {
				ch.setProperty_city(obj.getString("property_city"));
			}

			if (obj.getString("property_landmark").equals("")
					|| obj.getString("property_landmark").equals(null)
					|| obj.getString("property_landmark").equals("null")) {
				ch.setProperty_landmark(" -- ");
			} else {
				ch.setProperty_landmark(obj.getString("property_landmark"));
			}
			//------------------------------
			if (obj.getString("expected_price").equals("")
					|| obj.getString("expected_price").equals(null)
					|| obj.getString("expected_price").equals("null")) {
				ch.setExpected_unit_price(" -- ");
				ch.setTv_rate(" -- ");
			} else {
				ch.setExpected_unit_price(Host.rupeeFormat(obj.getString("expected_price")));
				ch.setTv_rate(Host.rupeeFormat(obj.getString("expected_price")));
			}

			if (obj.getString("expected_unit_price_unit").equals("")
					|| obj.getString("expected_unit_price_unit").equals(null)
					|| obj.getString("expected_unit_price_unit").equals("null")) {
				ch.setExpected_unit_price_unit(" -- ");
			} else {
				ch.setExpected_unit_price_unit(obj.getString("expected_unit_price_unit"));
			}

			if (obj.getString("property_type_name_parent").equals("")
					|| obj.getString("property_type_name_parent").equals(null)
					|| obj.getString("property_type_name_parent").equals(
							"null")) {
				ch.setProperty_type_name_parent(" -- ");
			} else {
				ch.setProperty_type_name_parent(obj
						.getString("property_type_name_parent"));
			}

			if (obj.getString("property_type_name").equals("")
					|| obj.getString("property_type_name").equals(null)
					|| obj.getString("property_type_name").equals(
							"null")) {
				ch.setProperty_type_name(" -- ");
			} else {
				ch.setProperty_type_name(obj
						.getString("property_type_name"));
			}

			if (obj.getString("property_type_parent").equals("")
					|| obj.getString("property_type_parent").equals(null)
					|| obj.getString("property_type_parent").equals("null")) {
				ch.setProperty_type_parent(" -- ");
			} else {
				ch.setProperty_type_parent(obj.getString("property_type_parent"));
			}
			if (obj.getString("property_area").equals("")
					|| obj.getString("property_area").equals(null)
					|| obj.getString("property_area").equals("null")) {
				ch.setProperty_area(" -- ");
			} else {
				ch.setProperty_area(obj.getString("property_area"));
			}
			//----------------------------------
			if (obj.getString("property_title").equals("")
					|| obj.getString("property_title").equals(null)
					|| obj.getString("property_title").equals("null")) {
				ch.setTv_title(" -- ");
			} else {
				ch.setTv_title(obj.getString("property_title"));
			}
			if (obj.getString("property_listing_parent").equals("")
					|| obj.getString("property_listing_parent").equals(null)
					|| obj.getString("property_listing_parent").equals("null")) {
				ch.setTv_proprty_listing_parent(" -- ");
			} else {
				ch.setTv_proprty_listing_parent(obj.getString("property_listing_parent"));
			}

			if (obj.getString("fname").equals("")
					|| obj.getString("fname").equals(null)
					|| obj.getString("fname").contains("--")
					|| obj.getString("fname").equals("null")) {
				ch.setTv_postedBy(" -- ");
				ch.setTv_postedBy(obj.getString("company_name"));
			} else {
				ch.setTv_postedBy(Utilities.makeFirstLetterCaps(obj.getString("fname"))
						+ " " + Utilities.makeFirstLetterCaps(obj.getString("lname")));
			}
			if (obj.getString("user_type").equals("")
					|| obj.getString("user_type").equals(null)
					|| obj.getString("user_type").equals("null")) {
				ch.setTv_userType(" -- ");
			} else {
				ch.setTv_userType(obj.getString("user_type"));
			}
			if (obj.getString("property_latitude").equals("")
					|| obj.getString("property_latitude").equals(null)
					|| obj.getString("property_latitude").equals("null")) {
				ch.setProperty_latitude("0.0");
			} else {
				ch.setProperty_latitude(obj.getString("property_latitude"));
			}
			if (obj.getString("property_longitude").equals("")
					|| obj.getString("property_longitude").equals(null)
					|| obj.getString("property_longitude").equals("null")) {
				ch.setProperty_longitude("0.0");
			} else {
				ch.setProperty_longitude(obj.getString("property_longitude"));
			}

//			if (obj.getString("broker_revenue").equals("")
//					|| obj.getString("broker_revenue").equals(null)
//					|| obj.getString("broker_revenue").equals("null")) {
//				ch.setBroker_revenue("--");
//			} else {
//				ch.setBroker_revenue(obj.getString("broker_revenue"));
//			}

//------------------------------added extras------------------------------------
//			if (obj.getString("use_company_name").equals("")
//					|| obj.getString("use_company_name").equals(null)
//					|| obj.getString("use_company_name").equals("null")) {
//				ch.setUse_company_name("0");
//			} else {
//				ch.setUse_company_name(obj.getString("use_company_name"));
//			}
//			if (obj.getString("company_name").equals("")
//					|| obj.getString("company_name").equals(null)
//					|| obj.getString("company_name").equals("null")) {
//				ch.setCompany_name(" -- ");
//			} else {
//				ch.setCompany_name(obj.getString("company_name"));
//			}

//			if(obj.getInt("is_shortlist") == 0)
//				ch.setIsShortlisted(false);
//			else if(obj.getInt("is_shortlist") == 1)
				ch.setIsShortlisted(true);

			ch.setIsBoolEnable(true);
			ch.setColor(Utilities.getRandomColor(ctx,obj.getString("fname")));
//-----------------------------end extras--------------------------------------
		if(isRefresh)
			AvailList.add(0, ch);
		else
			AvailList.add(ch);
		} catch (Exception e) { e.printStackTrace(); }
	}
	public static void parseMandate(Context ctx, JSONObject obj, ArrayList<MandateSetter> AvailList, boolean isRefresh){
		try {
//		if(obj.getString("property_listing_parent").contains("Availability")){
		MandateSetter ch = new MandateSetter();

		if (obj.getString("user_id").equals("") || obj.getString("user_id").equals(null)
				|| obj.getString("user_id").equals("null")) {
		} else {
			ch.setUSER_ID(Integer.parseInt(obj.getString("user_id")));
		}
		if (obj.getString("property_id").equals("") || obj.getString("property_id").equals(null)
				|| obj.getString("property_id").equals("null")) {
		} else {
			ch.setPROPERTY_ID(Integer.parseInt(obj.getString("property_id")));
		}
		///////////////////////////////Mandate  data////////////////////////////////////
		if (obj.getString("other_id").equals("") || obj.getString("other_id").equals(null)
				|| obj.getString("other_id").equals("null")) {
		} else {
			ch.setOTHER_ID(Integer.parseInt(obj.getString("other_id")));
		}
		if (obj.getString("new_id").equals("") || obj.getString("new_id").equals(null)
				|| obj.getString("new_id").equals("null")) {
		} else {
			ch.setNEW_PROPERTY_ID(obj.getString("new_id"));
		}
		if (obj.getString("broker_revenue").equals("")
				|| obj.getString("broker_revenue").equals(null)
				|| obj.getString("broker_revenue").equals("null")) {
			ch.setTv_revenue(" -- ");
		} else {
			ch.setTv_revenue(Host.rupeeFormat(obj.getString("broker_revenue")));
		}

		//////////////////////////////////////////////////////////////////////////
//		if(obj.getString("image").contains("http"))
//			ch.setImageUrl(obj.getString("image"));
//		else
//			ch.setImageUrl(Host.MainUrl + obj.getString("image"));
//
//		if(obj.getString("image").contains("http"))
//			ch.setThumbnailUrl(obj.getString("image"));
//		else
//			ch.setThumbnailUrl(Host.MainUrl + obj.getString("image"));
//
		if (obj.getString("property_added_on").equals("")
				|| obj.getString("property_added_on").equals(null)
				|| obj.getString("property_added_on").equals("null")) {
			ch.setTv_postedTime(" -- ");
		} else {
			ch.setTv_postedTime(obj.getString("property_added_on"));
		}

		if (obj.getString("property_location").equals("")
				|| obj.getString("property_location").equals(null)
				|| obj.getString("property_location").equals("null")) {
			ch.setTv_location(" -- ");
		} else {
			ch.setTv_location(obj.getString("property_location"));
		}


		String temp_area = " -- ";
		if (!(obj.getString("property_super_area")).isEmpty()
				&& !(obj.getString("property_super_area").equals("null"))) {
			temp_area = obj.getString("property_super_area") +" "+obj.getString("property_super_area_unit");
			ch.setTv_area(temp_area);
		} else if(!(obj.getString("property_built_area")).isEmpty()
				&& !(obj.getString("property_built_area").equals("null"))) {
			temp_area = obj.getString("property_built_area") +" "+obj.getString("property_built_area_unit");
			ch.setTv_area(temp_area);
		} else if(!(obj.getString("property_plot_area")).isEmpty()
				&& !(obj.getString("property_plot_area").equals("null"))) {
			temp_area = obj.getString("property_plot_area") +" "+obj.getString("property_plot_area_unit");
			ch.setTv_area(temp_area);
		} else {
			ch.setTv_area(temp_area);
		}

		if (obj.getString("expected_price").equals("")
				|| obj.getString("expected_price").equals(null)
				|| obj.getString("expected_price").equals("null")) {
			ch.setExpected_unit_price(" -- ");
			ch.setTv_rate(" -- ");
		} else {
			ch.setExpected_unit_price(Host.rupeeFormat(obj.getString("expected_price")));
			ch.setTv_rate(Host.rupeeFormat(obj.getString("expected_price")));
		}

//		if (obj.getString("broker_revenue").equals("")
//				|| obj.getString("broker_revenue").equals(null)
//				|| obj.getString("broker_revenue").equals("null")) {
//			ch.setTv_revenue(" -- ");
//		} else {
//			ch.setTv_revenue(Host.rupeeFormat(obj.getString("broker_revenue")));
//		}

//		if (obj.getString("phone_no").equals("")
//				|| obj.getString("phone_no").equals(null)
//				|| obj.getString("phone_no").equals("null")) {
//			ch.setPhone_no(" -- ");
//		} else {
//			ch.setPhone_no(obj.getString("phone_no"));
//		}
		//-----------------
		if (obj.getString("property_floor").equals("")
				|| obj.getString("property_floor").equals(null)
				|| obj.getString("property_floor").equals("null")) {
			ch.setProperty_floor(" -- ");
		} else {
			ch.setProperty_floor(obj.getString("property_floor"));
		}

		if (obj.getString("no_of_bathrooms").equals("")
				|| obj.getString("no_of_bathrooms").equals(null)
				|| obj.getString("no_of_bathrooms").equals("null")) {
			ch.setNo_of_bathrooms(" -- ");
		} else {
			ch.setNo_of_bathrooms(obj.getString("no_of_bathrooms"));
		}
		if (obj.getString("no_of_bedrooms").contains("Select")){
			ch.setNo_of_bedrooms(" -- ");
		}
		if (obj.getString("no_of_bedrooms").equals("")
				|| obj.getString("no_of_bedrooms").equals(null)
				|| obj.getString("no_of_bedrooms").equals("null")) {
			ch.setNo_of_bedrooms(" -- ");
		} else {
			ch.setNo_of_bedrooms(obj.getString("no_of_bedrooms"));
		}
		if (obj.getString("no_of_washrooms").equals("")
				|| obj.getString("no_of_washrooms").equals(null)
				|| obj.getString("no_of_washrooms").equals("null")) {
			ch.setNo_of_washrooms(" -- ");
		} else {
			ch.setNo_of_washrooms(obj.getString("no_of_washrooms"));
		}
		if (obj.getString("no_of_balcony").equals("")
				|| obj.getString("no_of_balcony").equals(null)
				|| obj.getString("no_of_balcony").equals("null")) {
			ch.setNo_of_balcony(" -- ");
		} else {
			ch.setNo_of_balcony(obj.getString("no_of_balcony"));
		}
		//-------------------------
		if (obj.getString("property_maintenance_amount").equals("")
				|| obj.getString("property_maintenance_amount").equals(null)
				|| obj.getString("property_maintenance_amount").equals("null")) {
			ch.setProperty_maintenance_amount(" -- ");
		} else {
			ch.setProperty_maintenance_amount(obj.getString("property_maintenance_amount")
					+ " " + obj.getString("property_maintenance_frequency"));
		}

		if (obj.getString("property_possession").equals("")
				|| obj.getString("property_possession").equals(null)
				|| obj.getString("property_possession").equals("null")) {
			ch.setProperty_possession(" -- ");
		} else {
			ch.setProperty_possession(obj.getString("property_possession"));
		}

		if (obj.getString("property_furnishing_type").equals("")
				|| obj.getString("property_furnishing_type").equals(null)
				|| obj.getString("property_furnishing_type").equals("null")) {
			ch.setProperty_furnishing_type(" -- ");
		} else {
			ch.setProperty_furnishing_type(obj.getString("property_furnishing_type"));
		}


		if (obj.getString("transaction_type").equals("")
				|| obj.getString("transaction_type").equals(null)
				|| obj.getString("transaction_type").equals("null")) {
			ch.setTransaction_type(" -- ");
		} else {
			ch.setTransaction_type(obj.getString("transaction_type"));
		}


		if (obj.getString("property_ownership").equals("")
				|| obj.getString("property_ownership").equals(null)
				|| obj.getString("property_ownership").equals("null")) {
			ch.setProperty_ownership(" -- ");
		} else {
			ch.setProperty_ownership(obj.getString("property_ownership"));
		}

		if (obj.getString("property_availability").equals("")
				|| obj.getString("property_availability").equals(null)
				|| obj.getString("property_availability").equals("null")) {
			ch.setProperty_availability(" -- ");
		} else {
			ch.setProperty_availability(obj.getString("property_availability"));
		}

		if (obj.getString("property_listing_type").equals("")
				|| obj.getString("property_listing_type").equals(null)
				|| obj.getString("property_listing_type").equals("null")) {
			ch.setProperty_listing_type(" -- ");
		} else {
			ch.setProperty_listing_type(obj.getString("property_listing_type"));
		}
		//------------------------
		if (obj.getString("property_description").equals("")
				|| obj.getString("property_description").equals(null)
				|| obj.getString("property_description").equals("null")) {
			ch.setProperty_description(" -- ");
		} else {
			ch.setProperty_description(obj.getString("property_description"));
		}

		if (obj.getString("property_city").equals("")
				|| obj.getString("property_city").equals(null)
				|| obj.getString("property_city").equals("null")) {
			ch.setProperty_city(" -- ");
		} else {
			ch.setProperty_city(obj.getString("property_city"));
		}

		if (obj.getString("property_landmark").equals("")
				|| obj.getString("property_landmark").equals(null)
				|| obj.getString("property_landmark").equals("null")) {
			ch.setProperty_landmark(" -- ");
		} else {
			ch.setProperty_landmark(obj.getString("property_landmark"));
		}
		//------------------------------
		if (obj.getString("expected_price").equals("")
				|| obj.getString("expected_price").equals(null)
				|| obj.getString("expected_price").equals("null")) {
			ch.setExpected_unit_price("" + " -- ");
		} else {
			ch.setExpected_unit_price("" + obj.getString("expected_price"));
		}
//		if (obj.getString("broker_revenue").equals("")
//				|| obj.getString("broker_revenue").equals(null)
//				|| obj.getString("broker_revenue").equals("null")) {
//			ch.setTv_revenue(" -- ");
//		} else {
//			ch.setTv_revenue(Host.rupeeFormat(obj.getString("broker_revenue")));
//		}
		if (obj.getString("expected_unit_price_unit").equals("")
				|| obj.getString("expected_unit_price_unit").equals(null)
				|| obj.getString("expected_unit_price_unit").equals("null")) {
			ch.setExpected_unit_price_unit(" -- ");
		} else {
			ch.setExpected_unit_price_unit(obj.getString("expected_unit_price_unit"));
		}

		if (obj.getString("property_type_name").equals("")
				|| obj.getString("property_type_name").equals(null)
				|| obj.getString("property_type_name").equals("null")) {
			ch.setProperty_type_name_parent(" -- ");
		} else {
			ch.setProperty_type_name_parent(obj.getString("property_type_name"));
		}

		if (obj.getString("property_type_parent").equals("")
				|| obj.getString("property_type_parent").equals(null)
				|| obj.getString("property_type_parent").equals("null")) {
			ch.setProperty_type_parent(" -- ");
		} else {
			ch.setProperty_type_parent(obj.getString("property_type_parent"));
		}
		if (obj.getString("property_area").equals("")
				|| obj.getString("property_area").equals(null)
				|| obj.getString("property_area").equals("null")) {
			ch.setProperty_area(" -- ");
		} else {
			ch.setProperty_area(obj.getString("property_area"));
		}
		//----------------------------------
		if (obj.getString("property_title").equals("")
				|| obj.getString("property_title").equals(null)
				|| obj.getString("property_title").equals("null")) {
			ch.setTv_title(" -- ");
		} else {
			ch.setTv_title(obj.getString("property_title"));
		}
		if (obj.getString("property_listing_parent").equals("")
				|| obj.getString("property_listing_parent").equals(null)
				|| obj.getString("property_listing_parent").equals("null")) {
			ch.setTv_proprty_listing_parent(" -- ");
		} else {
			ch.setTv_proprty_listing_parent(obj.getString("property_listing_parent"));
		}
		if (obj.getString("amenities").equals("")
				|| obj.getString("amenities").equals(null)
				|| obj.getString("amenities").equals("null")) {
			ch.setAmenities(" -- ");
		} else {
			ch.setAmenities(obj.getString("amenities"));
		}

//		if (obj.getString("fname").equals("")
//				|| obj.getString("fname").equals(null)
//				|| obj.getString("fname").equals("null")) {
//			ch.setTv_postedBy(" -- ");
//		} else {
//			ch.setTv_postedBy(Utilities.makeFirstLetterCaps(obj.getString("fname"))
//					+ " " + Utilities.makeFirstLetterCaps(obj.getString("lname")));
//		}
//		if (obj.getString("user_type").equals("")
//				|| obj.getString("user_type").equals(null)
//				|| obj.getString("user_type").equals("null")) {
//			ch.setTv_userType(" -- ");
//		} else {
//			ch.setTv_userType(obj.getString("user_type"));
//		}
		if (obj.getString("property_latitude").equals("")
				|| obj.getString("property_latitude").equals(null)
				|| obj.getString("property_latitude").equals("null")) {
			ch.setProperty_latitude("0.0");
		} else {
			ch.setProperty_latitude(obj.getString("property_latitude"));
		}
		if (obj.getString("property_longitude").equals("")
				|| obj.getString("property_longitude").equals(null)
				|| obj.getString("property_longitude").equals("null")) {
			ch.setProperty_longitude("0.0");
		} else {
			ch.setProperty_longitude(obj.getString("property_longitude"));
		}
//------------------------------added extras------------------------------------
//		if (obj.getString("use_company_name").equals("")
//				|| obj.getString("use_company_name").equals(null)
//				|| obj.getString("use_company_name").equals("null")) {
//			ch.setUse_company_name("0");
//		} else {
//			ch.setUse_company_name(obj.getString("use_company_name"));
//		}
//		if (obj.getString("company_name").equals("")
//				|| obj.getString("company_name").equals(null)
//				|| obj.getString("company_name").equals("null")) {
//			ch.setCompany_name(" -- ");
//		} else {
//			ch.setCompany_name(obj.getString("company_name"));
//		}
//
//		if(obj.getInt("is_shortlist") == 0)
//			ch.setIsShortlisted(false);
//		else if(obj.getInt("is_shortlist") == 1)
//			ch.setIsShortlisted(true);
//
//		ch.setIsBoolEnable(true);
//		ch.setColor(Utilities.getRandomColor(ctx,obj.getString("fname")));
//
		if(isRefresh)
			AvailList.add(0, ch);
		else
			AvailList.add(ch);
		} catch (Exception e) { e.printStackTrace(); }
	}

	public static String rupeeFormat(String pr) {
		String Rs = "\u20B9 ", X = "#.##", THOUSAND =  " Thousand", LAKH =  " Lakh", CRORE = " Crore";
		switch (pr.length()) {
		case 4:
			X = Float.parseFloat(pr) / 1000 + "";
			if(Utilities.D)Log.i(TAG,   X + THOUSAND + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(),3)) + THOUSAND;
		case 5:
			X = Float.parseFloat(pr) / 1000 + "";
			if(Utilities.D)Log.i(TAG,   X + THOUSAND + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(),4)) + THOUSAND;
		case 6:
			X = Float.parseFloat(pr) / 100000 + "";
			if(Utilities.D)Log.i(TAG,   X + LAKH + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(),3)) + LAKH;

		case 7:
			X = Float.parseFloat(pr) / 100000 + "";
			if(Utilities.D)Log.i(TAG,   X + LAKH + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(), 4)) + LAKH;
		case 8:
			X = Float.parseFloat(pr) / 10000000 + "";
			if(Utilities.D)Log.i(TAG,   X + CRORE + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(), 3)) + CRORE;
		case 9:
			X = Float.parseFloat(pr) / 10000000 + "";
			if(Utilities.D)Log.i(TAG,   X + CRORE + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(), 4)) + CRORE;
		case 10:
			X = Float.parseFloat(pr) / 10000000 + "";
			if(Utilities.D)Log.i(TAG,   X + CRORE + " by=" + pr);
			return Rs + X.substring(0, Math.min(X.length(), 5)) + CRORE;
		default:
			return pr;
		}
	}
	public static boolean checkUserIds(String my_id, String other_id){
		if(my_id.equals(other_id))
			return true;
		else
			return false;
	}
}
