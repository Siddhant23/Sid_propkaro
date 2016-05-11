package com.propkaro.post.edit;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.UploadUtils;
import com.propkaro.util.Utilities;

public class Edit_New_Post6 extends Fragment implements OnItemSelectedListener{

	static String TAG = Edit_New_Post6.class.getSimpleName();
	String getBedrooms = "", getBathrooms = "", getBalconies = "",
			getWashrooms = "", getAdd_prce = "", getMntnce_chrg = "",
			getfloorbldng = "", getTotal_floor_bldng = "", getSuper_Area = "",
			getPlot_Area = "", getCarpet_Area = "", getSuper_Unit_Area = "",
			getCarpet_Unit_Area = "", getPlot_Unit_Area = "", getCrore = "",
			get_thousand = "", get_lakh = "", getAdd_prce_area_unit = "",
			getFreq = "";
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area;
	ImageView back0;
	GoogleMap googleMap;

	String getProp_ownership = "", getProp_availblty = "",
			get_furnishing_type = "", get_Transaction_type = "",get_Possession="",
			get_Prop_Title = "", get_Descrption = "",getAmenities="";
	String getUserDataID = "", getUserType = "",api_latitude = "", api_longitude = "";
;
	private Button btn_selectPic, btn_uploadPic;
	private ImageView iv_profile;
	private static int RESULT_LOAD_IMG = 1;
	private static final int SELECT_PICTURE = 0, RESULT_LOAD_IMAGE = 1;
	Bitmap bm;
	String getPropertyID = "", getPropertyImage = "";
	String imgDecodableString;
	SparseBooleanArray amenties = new SparseBooleanArray();
	static final int 
	AMEN_AIRPORT = 0, AMEN_BUS_STAND= 1, AMEN_GYM = 2, AMEN_HOSPITAL = 3, AMEN_INTERNET= 4, 
			AMEN_Market = 5, AMEN_PARK = 6, AMEN_RAILWAY = 7, AMEN_SCHOOL = 8;
	String [] amentiesNames = {"Airport","Bus Stand","Gym","Hospital","Internet","Market","Park","Railway","School"};
	TextView btn_finish;
	Button btn_confirm, btn_bck_3, btn_OK, btn_EDIT;
	EditText et_super_area, et_carpet_area, et_plot_area, et_absolute_prce,et_Descrption,
	et_add_price, et_mntnce_charge,et_bedrooms,et_bathrooms,et_balconics,et_washrooms;
	 
	long getAbsolut_prce ;
	long  getCalculateArea1=0;
	String getRent_purchase = "", getSubmitType = "", getPropertyType = "",
			getCity = "", getLocation = "", 
//			getProjectName = "",
			getCat_PropType = "";
	Context ctx;TinyDB db;
	DisplayImageOptions options;
	SharedPreferences mPrefs;

	final Handler mHandler = new Handler();
	boolean mShowingBack = false;

	View rView;
	Spinner spin_prop_type;
	Button spin_cityType;
	String str_spin_possession[] = {  "Possession","Immediate","3 months", "6 months", "9 months",
			"1 year", "2 year", "3 year", "4 year", "5 year" };

	String str_spin_furnishing[] = { "Furnishing", "Furnished", "Semifurnished",
			"Unfurnished" };
//	ImageButton btn_airport,btn_bus,btn_hospital,btn_internet,btn_market,btn_park,btn_railway_stn,btn_school,btn_gym;
    private CoordinatorLayout mCoordinator;

	Filter filter;
	int position;
	TextView tv_poss, tv_furnishing, tv_trans, tv_availability;
	Spinner spin_poss, spin_trans_type, spin_pvt_oner, spin_avail,
			spin_furnish;
	Spinner bedrooms, bathrooms, balconics, washrooms, floor_bldng,
	total_floor_bldng, freq, saunit, caunit, paunit, add_price_unit,
	core, lakh, thounsands;
	String temp_getfloorbldng = "0", temp_crore = "0", temp_getBedrooms = "0",  temp_getBathrooms = "0", 
			temp_getBalconies = "0", temp_getWashrooms = "0",
			temp_getAdd_prce = "0",   
			temp_getTotal_floor_bldng = "0",  temp_getSuper_Area = "0", 
			temp_getPlot_Area = "0",  temp_getCarpet_Area = "0", 
			temp_getSuper_Unit_Area = "0",  temp_getCarpet_Unit_Area = "0",
			temp_getPlot_Unit_Area ="0",    
			temp_get_thousand = "0",  temp_get_lakh = "0",  temp_get_freq = "0", 
			temp_getAdd_prce_area_unit = "0";
	  String str_spn_addPrice_unit[] = { "Select Unit", "Sq.Ft", "Sq.Yards",
				"Sq.Meter", "Acres", "Marla", "Cents", "Bigha", "Kottah", "Kanal",
				"Grounds", "Ares", "Biswa", "Guntha", "Aankadam", "Hectares",
				"Rood", "Chataks", "Perch" };
	  String str_spn_freq[] = { "Cycle","Monthly", "Annually", "One time" };
	  LinearLayout lnr_spin2,lnr_3, lnr_4 ;
	  String   temp_get_furnishing_type = "0", 
				 temp_get_Possession = "0",temp_getMntce_chrg= "0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_6, container, false);
	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		
		add_price_unit = (Spinner) rView.findViewById(R.id.spin_add_price_unit);
		add_price_unit.setOnItemSelectedListener(this);
		TextView tv_from_step5= (TextView) rView.findViewById(R.id.tv_from_step5);
		tv_from_step5.setText(getSubmitType + " � " + getRent_purchase + " � " + getPropertyType + " � " + getCat_PropType + " � " + getCity);
		lnr_spin2 = (LinearLayout) rView.findViewById(R.id.lnr_spin2);
		lnr_3 = (LinearLayout) rView.findViewById(R.id.lnr_3);
		lnr_4 = (LinearLayout) rView.findViewById(R.id.lnr_4);
		
			et_add_price    = (EditText) rView.findViewById(R.id.et_add_price);
			et_add_price.setText(getCalculateArea1 + "");

			et_mntnce_charge   = (EditText) rView.findViewById(R.id.et_mntnce_charge);

			if(getMntnce_chrg.contains(" -- ")){
				et_mntnce_charge.setText("");
			}else { 
				et_mntnce_charge.setText(getMntnce_chrg + "");
			}
//			et_mntnce_charge.setHint(Html.fromHtml("<font size=\"11\">" +  "</font>" ));    

//			getMntnce_chrg = mPrefs.getString("PREF_KEY_getMntnce_chrg","");
//			et_mntnce_charge.setText(getMntnce_chrg + "");
			   final ImageButton btn_airport = (ImageButton)rView.findViewById(R.id.btn_airport);
			    final ImageButton btn_bus = (ImageButton)rView.findViewById(R.id.btn_bus);
			    final ImageButton btn_gym = (ImageButton)rView.findViewById(R.id.btn_gym);
			    final ImageButton btn_hospital = (ImageButton)rView.findViewById(R.id.btn_hospital);
			    final ImageButton btn_internet = (ImageButton)rView.findViewById(R.id.btn_internet);
			    final ImageButton btn_market = (ImageButton)rView.findViewById(R.id.btn_market);
			    final ImageButton btn_park = (ImageButton)rView.findViewById(R.id.btn_park);
			    final ImageButton btn_railway_stn = (ImageButton)rView.findViewById(R.id.btn_railway_stn);
			    final ImageButton btn_school = (ImageButton)rView.findViewById(R.id.btn_school);
			    
			    if(getAmenities.contains("Airport") || getAmenities.contains("airport")){
			    	btn_airport.setBackgroundResource(R.mipmap.airport_selected);
					amenties.append(AMEN_AIRPORT, true);
			    } else{
			    	btn_airport.setBackgroundResource(R.mipmap.airport_unselected);
			    	amenties.append(AMEN_AIRPORT, false);
			    }
			    if(getAmenities.contains("Bus") || getAmenities.contains("bus")){
			    	btn_bus.setBackgroundResource(R.mipmap.bus_selected);
					amenties.append(AMEN_BUS_STAND, true);

			    } else{
			    	btn_bus.setBackgroundResource(R.mipmap.bus_unselected);
			    	amenties.append(AMEN_BUS_STAND, false);
			    }

			    if(getAmenities.contains("Gym") || getAmenities.contains("gym")){
			    	btn_gym.setBackgroundResource(R.mipmap.gym_selected);
					amenties.append(AMEN_GYM, true);
			    } else {
			    	btn_gym.setBackgroundResource(R.mipmap.gym_unselected);
		    	amenties.append(AMEN_GYM, false);
			    }
			    if(getAmenities.contains("Hospital") || getAmenities.contains("hospital")){
			    	btn_hospital.setBackgroundResource(R.mipmap.hospital_selected);
					amenties.append(AMEN_HOSPITAL, true);

			    } else {
			    	btn_hospital.setBackgroundResource(R.mipmap.hospital_unselected);
		    	amenties.append(AMEN_HOSPITAL, false);
			    }
			    if(getAmenities.contains("Internet") || getAmenities.contains("internet")){
			    	btn_internet.setBackgroundResource(R.mipmap.internet_selected);
					amenties.append(AMEN_INTERNET, true);

			    } else {
			    	btn_internet.setBackgroundResource(R.mipmap.internet_unselected);
			    amenties.append(AMEN_INTERNET, false);
			    }
			    if(getAmenities.contains("Market") || getAmenities.contains("market")){
			    	btn_market.setBackgroundResource(R.mipmap.market_selected);
					amenties.append(AMEN_Market, true);

			    } else{ 
			    	btn_market.setBackgroundResource(R.mipmap.market_unselected);
		    	amenties.append(AMEN_Market, false);
			    }
			    if(getAmenities.contains("Park") || getAmenities.contains("park")){
			    	btn_park.setBackgroundResource(R.mipmap.park_selected);
					amenties.append(AMEN_PARK, true);

			    } else {
			    	btn_park.setBackgroundResource(R.mipmap.park_unselected);
		    	amenties.append(AMEN_PARK, false);
			    }
			    if(getAmenities.contains("Railway") || getAmenities.contains("railway")){
			    	btn_railway_stn.setBackgroundResource(R.mipmap.rail_selected);
					amenties.append(AMEN_RAILWAY, true);

			    } else {
			    	btn_railway_stn.setBackgroundResource(R.mipmap.rail_unselected);
		    	amenties.append(AMEN_RAILWAY, false);

			    }	
			    if(getAmenities.contains("School") || getAmenities.contains("school")){
			    	btn_school.setBackgroundResource(R.mipmap.school_selected);
					amenties.append(AMEN_SCHOOL, true);

			    } else {
			    	btn_school.setBackgroundResource(R.mipmap.school_unselected);
		    	amenties.append(AMEN_SCHOOL, false);
			    }
//		    	List<String> amen = new ArrayList<>();
//				for(int z1 =0;z1 < 9;z1++){
////					Log.e(TAG, "get_Amenities1@@@=====" + amentiesNames[q]);
//
//					if(amenties.get(z1)){
//						Log.e(TAG, "get_Amenities2#####=====" + amentiesNames[z1]);
//						amen.add(amentiesNames[z1]);
//					}
//				}
//				getAmenities = TextUtils.join(",", amen);
			    	
//-------------------------------------end amenities----------------------------------
			
//			getAdd_prce = mPrefs.getString("PREF_KEY_getAdd_prce","");
//			et_add_price.setText(getAdd_prce + "");
			
//			getFreq = mPrefs.getString("PREF_KEY_getFreq", "");
//			temp_get_freq = getFreq;
//
//			getAdd_prce_area_unit = mPrefs.getString("PREF_KEY_getAdd_prce_area_unit", "");
//			temp_getAdd_prce_area_unit = getAdd_prce_area_unit;
//
//			get_furnishing_type = mPrefs.getString("PREF_KEY_get_furnishing_type", "");
//			temp_get_furnishing_type = get_furnishing_type;
//			
//			get_Possession = mPrefs.getString("PREF_KEY_get_Possession", "");
//			temp_get_Possession = get_Possession;
//			
//			getAmenities = mPrefs.getString("PREF_KEY_getAmenities", "");
			Log.e(TAG, "getAmenities=====================###########" + getAmenities);
//				et_add_price.setText("" +  getCalculateArea1);
				et_add_price.setEnabled(false);
				add_price_unit.setEnabled(false);
			
			if (getCarpet_Area.isEmpty() && getPlot_Area.isEmpty()) {
				Log.d(TAG, "getSuper_Unit_Area@@@==" + getSuper_Unit_Area);

				temp_getAdd_prce_area_unit = getSuper_Unit_Area;

			} else if (getSuper_Area.isEmpty() && getPlot_Area.isEmpty()) {
				Log.d(TAG, "getCarpet_Unit_Area@@@==" + getCarpet_Unit_Area);

				temp_getAdd_prce_area_unit = getCarpet_Unit_Area;
			}
			else if (getSuper_Area.isEmpty() && getCarpet_Area.isEmpty()) {
				Log.d(TAG, "getPlot_Unit_Area@@@==" + getPlot_Unit_Area);

				temp_getAdd_prce_area_unit = getPlot_Unit_Area;

			}
//				// ////////////////////////////////////////////////////////
	  
			btn_airport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_AIRPORT)){
					btn_airport.setBackgroundResource(R.mipmap.airport_unselected);
					amenties.append(AMEN_AIRPORT, false);
				}else{
					btn_airport.setBackgroundResource(R.mipmap.airport_selected);
					amenties.append(AMEN_AIRPORT, true);
				}
			}
		});
//	    
//	    
	    btn_bus.setOnClickListener(new OnClickListener() {
			
			@Override
			
			public void onClick(View arg0) {
				if(amenties.get(AMEN_BUS_STAND)){
					btn_bus.setBackgroundResource(R.mipmap.bus_unselected);
					amenties.append(AMEN_BUS_STAND, false);
				}else{
					btn_bus.setBackgroundResource(R.mipmap.bus_selected);
					amenties.append(AMEN_BUS_STAND, true);
				}
			}
		});
	    btn_gym.setOnClickListener(new OnClickListener() {
	    	
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_GYM)){
					btn_gym.setBackgroundResource(R.mipmap.gym_unselected);
					amenties.append(AMEN_GYM, false);
				}else{
					btn_gym.setBackgroundResource(R.mipmap.gym_selected);
					amenties.append(AMEN_GYM, true);
				}
			}
		});
//	    
	    btn_hospital.setOnClickListener(new OnClickListener() {
	    
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_HOSPITAL)){
					btn_hospital.setBackgroundResource(R.mipmap.hospital_unselected);
					amenties.append(AMEN_HOSPITAL, false);
				}else{
					btn_hospital.setBackgroundResource(R.mipmap.hospital_selected);
						amenties.append(AMEN_HOSPITAL, true);
				}
			}
		});
	    
	    btn_internet.setOnClickListener(new OnClickListener() {
	    	
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_INTERNET)){
					btn_internet.setBackgroundResource(R.mipmap.internet_unselected);
					amenties.append(AMEN_INTERNET, false);
				}else{
					btn_internet.setBackgroundResource(R.mipmap.internet_selected);
						amenties.append(AMEN_INTERNET, true);
				}
			}
		});
//	    
	    btn_market.setOnClickListener(new OnClickListener() {
	    	
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_Market)){
					btn_market.setBackgroundResource(R.mipmap.market_unselected);
					amenties.append(AMEN_Market, false);
				}else{
					btn_market.setBackgroundResource(R.mipmap.market_selected);
					amenties.append(AMEN_Market, true);
				}
			}
		});
//	    
	    btn_park.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_PARK)){
					btn_park.setBackgroundResource(R.mipmap.park_unselected);
					amenties.append(AMEN_PARK, false);
				}else{
					btn_park.setBackgroundResource(R.mipmap.park_selected);
						amenties.append(AMEN_PARK, true);
				}
			}
		});
//	  
	    btn_railway_stn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_RAILWAY)){
					btn_railway_stn.setBackgroundResource(R.mipmap.rail_unselected);
					amenties.append(AMEN_RAILWAY, false);
				}else{
					btn_railway_stn.setBackgroundResource(R.mipmap.rail_selected);
						amenties.append(AMEN_RAILWAY, true);
				}
			}
		});
//	    
	    btn_school.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(amenties.get(AMEN_SCHOOL)){
					btn_school.setBackgroundResource(R.mipmap.school_unselected);
					amenties.append(AMEN_SCHOOL, false);
				}else{
					btn_school.setBackgroundResource(R.mipmap.school_selected);
						amenties.append(AMEN_SCHOOL, true);
				}
			}
		});
		spin_poss = (Spinner) rView.findViewById(R.id.spin_possession);
		spin_poss.setOnItemSelectedListener(this);

		// // Creating adapter for spinner
		ArrayAdapter<String> possAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				str_spin_possession);

		possAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_poss.setAdapter(possAdapter);

		spin_furnish = (Spinner) rView.findViewById(R.id.spin_furnishing);
		spin_furnish.setOnItemSelectedListener(this);

		// // Creating adapter for spinner
		ArrayAdapter<String> furnshAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,
				str_spin_furnishing);

		furnshAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_furnish.setAdapter(furnshAdapter);
		add_price_unit = (Spinner) rView
				.findViewById(R.id.spin_add_price_unit);
		add_price_unit.setOnItemSelectedListener(this);
		// add_price_unit.setEnabled(false);
		ArrayAdapter<String> add_price_unitAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_dropdown_item_1line,
				str_spn_addPrice_unit);
		add_price_unitAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		add_price_unit.setAdapter(add_price_unitAdapter);

		freq = (Spinner) rView.findViewById(R.id.spin_freq);
		freq.setOnItemSelectedListener(this);
		ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_dropdown_item_1line, str_spn_freq);
		freqAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		freq.setAdapter(freqAdapter);
		// ////////////////////////////////////////////////////////
		
		back0 = (ImageView) rView.findViewById(R.id.back0);
		back0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();

				if(Utilities.D)Log.v(TAG,"onclicking6 ...");
//				SharedPreferences.Editor editor = mPrefs.edit();
//
//				
//			////PREF POST 3
//				editor.putString("KEY_Calc", getCalculateArea1 +"");
//				editor.putString("PREF_KEY_getMntnce_chrg", et_mntnce_charge.getText().toString()+"");
//				editor.putString("PREF_KEY_getFreq", getFreq);
//				editor.putString("PREF_KEY_getAdd_prce", et_add_price.getText().toString()+"");
//				editor.putString("PREF_KEY_getAdd_prce_area_unit",	getAdd_prce_area_unit);
//				editor.putString("PREF_KEY_get_furnishing_type", get_furnishing_type);
//				editor.putString("PREF_KEY_get_Possession", get_Possession);
//				editor.putString("PREF_KEY_getAmenities", getAmenities);
//				editor.commit();

//				Bundle args = new Bundle();
//
//				args.putString("KEY_Rent_purchase", getRent_purchase);
//				args.putString("KEY_Submit_type", getSubmitType);
//				args.putString("KEY_Property_type", getPropertyType);
//				args.putString("KEY_City_type", getCity);
//				args.putString("KEY_Location_type", getLocation);
//				args.putString("KEY_Cat_PropType", getCat_PropType);
//
//				args.putString("KEY_getSuper_Area", getSuper_Area);
//				args.putString("KEY_getPlot_Area", getPlot_Area);
//				args.putString("KEY_getCarpet_Area", getCarpet_Area);
//				args.putString("KEY_getAbsolut_prce", "" + getAbsolut_prce);
//				args.putString("KEY_getAdd_prce", getAdd_prce);
//				args.putString("KEY_getMntnce_chrg", getMntnce_chrg);
//				args.putString("KEY_getfloorbldng", getfloorbldng);
//				args.putString("KEY_getTotal_floor_bldng", getTotal_floor_bldng);
//
//				args.putString("KEY_getBedrooms", getBedrooms);
//				args.putString("KEY_getBathrooms", getBathrooms);
//				args.putString("KEY_getBalconies", getBalconies);
//				args.putString("KEY_getWashrooms", getWashrooms);
//
//				args.putString("KEY_getSuper_Unit_Area", getSuper_Unit_Area);
//				args.putString("KEY_getCarpet_Unit_Area", getCarpet_Unit_Area);
//				args.putString("KEY_getPlot_Unit_Area", getPlot_Unit_Area);
//				args.putString("KEY_getAdd_prce_area_unit",	getAdd_prce_area_unit);
//				args.putString("KEY_getFreq", getFreq);
//
//				Fragment f = new New_Post6();
//				f.setArguments(args);

			}
		});
		btn_finish = (TextView) rView.findViewById(R.id.btn_finish);
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMntnce_chrg = et_mntnce_charge.getText().toString();
				 List<String> amen = new ArrayList<>();
					for(int z =0;z < 9;z++){
//						Log.e(TAG, "get_Amenities1@@@=====" + amentiesNames[q]);

						if(amenties.get(z)){
							Log.e(TAG, "get_Amenities2#####=====" + amentiesNames[z]);
							amen.add(amentiesNames[z]);
						}
					}
					getAmenities = TextUtils.join(",", amen);
			   
				// //////////////Validation 2 of proop3////////////////
			 
 					// ----------------post_1
					Log.d(TAG, "getRent_purchase==" + getRent_purchase);
					Log.d(TAG, "getSubmitType==" + getSubmitType);
					Log.d(TAG, "getCat_PropType==" + getCat_PropType);
					Log.d(TAG, "getLocation==" + getLocation);
					Log.d(TAG, "getCity==" + getCity);
					Log.d(TAG, "getPropertyType==" + getPropertyType);

					// ----------------post_2
					Log.d(TAG, "getCalculateArea1==" + getCalculateArea1);
					Log.d(TAG, "getAbsolut_prce==" + getAbsolut_prce);

					Log.d(TAG, "getBedrooms!!==" + getBedrooms);
					Log.d(TAG, "getBathrooms!!==" + getBathrooms);
					Log.d(TAG, "getBalconies!!==" + getBalconies);
					Log.d(TAG, "getWashrooms!!==" + getWashrooms);
					Log.d(TAG, "getSuper_Area==" + getSuper_Area);
					Log.d(TAG, "getPlot_Area==" + getPlot_Area);
					Log.d(TAG, "getCarpet_Area==" + getCarpet_Area);
					Log.d(TAG, "getSuper_Unit_Area==" + getSuper_Unit_Area);
					Log.d(TAG, "getCarpet_Unit_Area==" + getCarpet_Unit_Area);
					Log.d(TAG, "getPlot_Unit_Area==" + getPlot_Unit_Area);
				/////////////////////////////////////////////
					
					// ----------------post_3
					Log.d(TAG, "getProp_ownership==" + getProp_ownership);
					Log.d(TAG, "getProp_availblty==" + getProp_availblty);
					Log.d(TAG, "get_Transaction_type==" + get_Transaction_type);
					Log.d(TAG, "get_Possession==" + get_Possession);
					Log.d(TAG, "get_furnishing_type==" + get_furnishing_type);
					Log.d(TAG, "getFreq==" + getFreq);

					Log.d(TAG, "getMntnce_chrg==" + getMntnce_chrg);
					Log.d(TAG, "get_Prop_Title==" + get_Prop_Title);
					Log.d(TAG, "get_Descrption==" + get_Descrption);
					Log.d(TAG, "get_Amenities==" + getAmenities);

					Log.d(TAG, "api_latitude==" + api_latitude);
					Log.d(TAG, "api_longitude==" + api_longitude);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
					alertDialogBuilder.setTitle("Post Property");
					alertDialogBuilder
							.setMessage("Are you sure you want to post?")
							.setCancelable(true);
					
//							if(PostPropertyActivity.isShowReview){
//							
//								PostPropertyActivity.isShowReview = false;
								alertDialogBuilder.setNegativeButton("CANCEL",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
//												openDialog(ctx);
											}
										});
//							}

					alertDialogBuilder.setPositiveButton("CONFIRM",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									
									if(Utilities.NetworkCheck(getActivity()))
										new PostPropertyTask().execute("");
								}
							});
					AlertDialog alert = alertDialogBuilder.create();
					alert.show();
				}
		});
		//---------------------------for uploading images----------------------------
				iv_profile = (ImageView) rView.findViewById(R.id.iv_profile);
				iv_profile.setImageResource(R.mipmap.home);

//				try {
//				File file = ImageLoader.getInstance().getDiscCache().get(getPropertyImage);
//				if(Utilities.D)Log.v(TAG,"" + file.exists());
//
//				if (!file.exists()) {
//					DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc().build();
//					ImageLoader.getInstance().displayImage(getPropertyImage, iv_profile, options);
//				} else {
//					iv_profile.setImageURI(Uri.parse(file.getAbsolutePath()));
//				}
//			} catch (Exception e) { e.printStackTrace(); }


				btn_selectPic = (Button)rView.findViewById(R.id.btn_selectPic);
				btn_uploadPic = (Button)rView.findViewById(R.id.btn_uploadPic);
				
				btn_selectPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(Utilities.D)Log.v(TAG,"Onclick selecting..");
						pickPhoto(arg0);
					}
				});
				
				btn_uploadPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(Utilities.D)Log.v(TAG,"Onclick uploading..");
						
						if(Utilities.isInternetOn(ctx)){
							try {
								BitmapDrawable drawable = (BitmapDrawable) iv_profile.getDrawable();
								bm = drawable.getBitmap();
								if(bm != null)
									sendPhoto(Utilities.compressImage(bm));
								else
									Utilities.Snack(mCoordinator, "Please select a photo");
							} catch (Exception e) { 
								e.printStackTrace();
								Utilities.Snack(mCoordinator, "Picture size is too large");
							}
						}
					}
				});

				if (Utilities.isInternetOn(ctx)){
					new PROPERTY_IMG_Task().execute(getPropertyID);
				}
				
		return rView;
	}

	class GetMapsLatLngTask extends AsyncTask<String, String, String> {
		Dialog dialog;
		double api_lat = 0.0, api_lng = 0.0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String LOCALITY_API = "http://maps.googleapis.com/maps/api/geocode/json?libraries=places&region=in&language=en&sensor=false&address="
						+ f_url[0];

				String jsonString = Utilities.readJson(ctx, LOCALITY_API);
				JSONObject reader = new JSONObject(jsonString);
				String status = reader.getString("status");

				if (status.equals("OK")) {
					JSONArray results = (JSONArray) reader.get("results");
					JSONObject geometry = (JSONObject) results.getJSONObject(0)
							.get("geometry");
					JSONObject location = new JSONObject(
							geometry.getString("location"));
					api_lat = Double.parseDouble(location.getString("lat"));
					api_lng = Double.parseDouble(location.getString("lng"));
					if(Utilities.D)Log.v(TAG,"api_lat=" + api_lat);
					if(Utilities.D)Log.v(TAG,"api_lng=" + api_lng);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			if (googleMap == null) {
				if(Utilities.D)Log.v(TAG,"Sorry, Unable to create Map !");
			} else {
				try {
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(api_lat, api_lng)).title("");
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					marker.draggable(true);
					googleMap.addMarker(marker);

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(api_lat, api_lng)).zoom(15)
							.build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	// //////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();db = new TinyDB(ctx);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.home)
		.showImageForEmptyUri(R.mipmap.home)
		.showImageOnFail(R.mipmap.home)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();

		 mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		
		getPropertyID = getActivity().getIntent().getStringExtra("KEY_PropertyID");
		Log.e(TAG, "#getPropertyID=" + getPropertyID);
		
		getPropertyImage = getActivity().getIntent().getStringExtra("KEY_PropertyImage");
		Log.e(TAG, "#######getPropertyImage=" + getPropertyImage);
		
		getMntnce_chrg = getActivity().getIntent().getStringExtra("KEY_getMntnce_chrg");
		temp_getMntce_chrg = getMntnce_chrg;
		
		get_furnishing_type = getActivity().getIntent().getStringExtra("KEY_furnishing_type");
		temp_get_furnishing_type = get_furnishing_type;

		get_Possession = getActivity().getIntent().getStringExtra("KEY_property_possession");
		temp_get_Possession = get_Possession;
		
		getFreq = getActivity().getIntent().getStringExtra("KEY_getFreq");
		temp_get_freq = getFreq;

		getAmenities = getActivity().getIntent().getStringExtra("KEY_Amenities");
		
		Bundle b = this.getArguments();
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		
		getCalculateArea1 = b.getLong("KEY_Calc");
		getSubmitType = b.getString("KEY_Submit_type");
		getRent_purchase = b.getString("KEY_Rent_purchase");
		getPropertyType = b.getString("KEY_Property_type");
		getLocation = b.getString("KEY_Location_type");
		getCat_PropType = b.getString("KEY_Cat_PropType");
		getCity = b.getString("KEY_City_type");

		// -----------get post 2 data
		getSuper_Area = b.getString("KEY_property_super_area");
		getPlot_Area = b.getString("KEY_property_plot_area");
		getCarpet_Area = b.getString("KEY_property_carpet_area");
		
		getSuper_Unit_Area = b.getString("KEY_getSuper_Unit_Area");
		getCarpet_Unit_Area = b.getString("KEY_getCarpet_Unit_Area");
		getPlot_Unit_Area = b.getString("KEY_getPlot_Unit_Area");

		getBedrooms = b.getString("KEY_getBedrooms");
		getBathrooms = b.getString("KEY_getBathrooms");
		getBalconies = b.getString("KEY_getBalconies");
		getWashrooms = b.getString("KEY_getWashrooms");

		getfloorbldng = b.getString("KEY_getfloorbldng");
		getTotal_floor_bldng = b.getString("KEY_getTotal_floor_bldng");

		getAbsolut_prce = b.getLong("KEY_getAbsolut_prce");
		get_Descrption= b.getString("KEY_get_Descrption");

		get_Transaction_type = b.getString("KEY_Transaction_type");
		getProp_availblty = b.getString("KEY_Prop_availblty" );
		getProp_ownership = b.getString("KEY_Prop_ownership");
		
		api_latitude = b.getString("KEY_api_latitude");
		api_longitude = b.getString("KEY_api_longitude");
		
		if(Utilities.D)Log.i(TAG, "api_latitude="+api_latitude);
		if(Utilities.D)Log.i(TAG, "api_longitude="+api_longitude);

		if(Utilities.D)Log.d(TAG, "=====getAbsolut_prce=" + getAbsolut_prce);

		if(Utilities.D)Log.d(TAG, "=====getCalculateAreaahh=" + getCalculateArea1);
		if(Utilities.D)Log.d(TAG, "=====getSuper_Area=" + getSuper_Area);
		if(Utilities.D)Log.d(TAG, "=====getCarpet_Area=" + getCarpet_Area);
		if(Utilities.D)Log.d(TAG, "=====getPlot_Area=" + getPlot_Area);
		if(Utilities.D)Log.d(TAG, "=====getSuper_Unit_Area=" + getSuper_Unit_Area);
		if(Utilities.D)Log.d(TAG, "=====getPlot_Unit_Area=" + getPlot_Unit_Area);
		if(Utilities.D)Log.d(TAG, "=====getCarpet_Unit_Area=" + getCarpet_Unit_Area);
		if(Utilities.D)Log.d(TAG, "=====getSubmitType=" + getSubmitType);
		if(Utilities.D)Log.d(TAG, "=====getRent_purchase=" + getRent_purchase);
		if(Utilities.D)Log.d(TAG, "=====getPropertyType=" + getPropertyType);
		if(Utilities.D)Log.d(TAG, "=====getCat_PropType=" + getCat_PropType);
		if(Utilities.D)Log.d(TAG, "=====getCity=" + getCity);

		if(Utilities.D)Log.d(TAG, "=====getBedrooms=" + getBedrooms);
		if(Utilities.D)Log.d(TAG, "=====getBathrooms=" + getBathrooms);
		if(Utilities.D)Log.d(TAG, "=====getBalconies=" + getBalconies);
		if(Utilities.D)Log.d(TAG, "=====getWashrooms=" + getWashrooms);
		
		if(Utilities.D)Log.d(TAG, "=====getAmenities123=" + getAmenities);

	}
	public void openDialog(Context ctx) {
		final Dialog customdialog = new Dialog(ctx);
		customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customdialog.setContentView(R.layout.custom_dialog_listing);
		customdialog.show();


		btn_OK = (Button) customdialog.findViewById(R.id.btn_OK);
		btn_OK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// if(Utilities.D)Log.v(TAG,"openDialog.cancel()");
				customdialog.dismiss();
				if(Utilities.NetworkCheck(getActivity()))
					new PostPropertyTask().execute("");
			}
		});
		 
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		String item = parent.getItemAtPosition(position).toString();

		Log.d("TAG", "item===" + item + "\nposition====" + position);
		// //////////////////////////////////////////////
		if (parent.getId() == R.id.spin_add_price_unit) {
			Log.d(TAG, "getAdd_prce_area_unit===" + getAdd_prce_area_unit);
			getAdd_prce_area_unit = item;
			for (int i = 0; i < str_spn_addPrice_unit.length; i++) {
				if (temp_getAdd_prce_area_unit.equals(str_spn_addPrice_unit[i])) {
					add_price_unit.setSelection(i);
					if(Utilities.D)Log.v(TAG,"add_price_unit[i]=" + str_spn_addPrice_unit[i]);
					if(Utilities.D)Log.v(TAG,"temp_getAdd_prce_area_unit=" + temp_getAdd_prce_area_unit);
					temp_getAdd_prce_area_unit = "";

				}
			}

			// ?????????????????
//			add_price_unit.setSelection(position);
		}
		if (parent.getId() == R.id.spin_furnishing) {
			Log.d("TAG", "get_furnishing_type===" + get_furnishing_type);
			get_furnishing_type = item;
			for (int i = 0; i < str_spin_furnishing.length; i++) {
				if (temp_get_furnishing_type.equals(str_spin_furnishing[i])) {
					spin_furnish.setSelection(i);
					if(Utilities.D)Log.v(TAG,"spin_poss[i]=" + str_spin_furnishing[i]);
					if(Utilities.D)Log.v(TAG,"temp_get_furnishing_type=" + temp_get_furnishing_type);
					temp_get_furnishing_type = "";

				}
			}

		}
		if (parent.getId() == R.id.spin_possession) {
			Log.d("TAG", "get_Possession===" + get_Possession);
			get_Possession = item;
			for (int i = 0; i < str_spin_possession.length; i++) {
				if (temp_get_Possession.equals(str_spin_possession[i])) {
					spin_poss.setSelection(i);
					if(Utilities.D)Log.v(TAG,"spin_poss" + str_spin_possession[i]);
					if(Utilities.D)Log.v(TAG,"temp_get_Possession=" + temp_get_Possession);
					temp_get_Possession = "";

				}
			}

		}
		if (parent.getId() == R.id.spin_freq) {
			getFreq = item;
			Log.d(TAG, "getFreq===" + getFreq);
			for (int i = 0; i < str_spn_freq.length; i++) {
				if (temp_get_freq.equals(str_spn_freq[i])) {
					freq.setSelection(i);
					if(Utilities.D)Log.v(TAG,"freq[i]=" + str_spn_freq[i]);
					if(Utilities.D)Log.v(TAG,"temp_get_freq=" + temp_get_freq);
					temp_get_freq = "";

				}
			}
		}

		
		if (getRent_purchase.contains("Rent")|| (getRent_purchase).contains("Sale")
				|| (getRent_purchase).contains("Purchase")) {

			if (getCat_PropType.contains("Residential Apartment")

//					
							|| getCat_PropType.contains("Independent/Builder Floor")
							|| getCat_PropType.contains("Farmhouse")
							|| getCat_PropType.contains("Service Apartment")
							|| getCat_PropType.contains("Other")) {

				Log.d("TAG", "#########getRent_purchase===" + "stud3");
				 
				lnr_spin2.setVisibility(View.GONE);
			}

		}

		if (getRent_purchase.contains("Rent")|| (getRent_purchase).contains("Sale")
				|| (getRent_purchase).contains("Purchase")){

			if( getCat_PropType.contains("Residential Land")
			|| getCat_PropType.contains("Independent House/Villa")
							|| getCat_PropType.contains("Residential Collaboration"))
							  {

				Log.d("TAG", "#########getRent_purchase===" + "stud3");
				 
				lnr_4.setVisibility(View.GONE);
			}

		}
		if (getRent_purchase.contains("Rent")|| (getRent_purchase).contains("Sale")
				|| (getRent_purchase).contains("Purchase")){

			if (getCat_PropType.contains("Commercial Land/Inst. Land")
					|| (getCat_PropType.contains("Agriculture/Farm Land"))
					|| (getCat_PropType.contains("Warehouse"))
					|| (getCat_PropType.contains("Industrial Lands/Plots"))
					|| (getCat_PropType.contains("Commercial Collaboration"))) {

				Log.d("TAG", "#########getRent_purchase===" + "stud3");
				lnr_spin2.setVisibility(View.GONE);
			}

		}

		if (getRent_purchase.contains("Rent")|| (getRent_purchase).contains("Sale")
				|| (getRent_purchase).contains("Purchase")){
			Log.d("TAG", "#########getRent_purchase===" + "stud7");

			if (getCat_PropType.contains("Commercial Office/Space")
					|| getCat_PropType.contains("Commercial Shops")
					|| getCat_PropType.contains("Commercial Showrooms")
					|| getCat_PropType.contains("Factory")
					|| getCat_PropType.contains("Office in IT Park")
					|| getCat_PropType.contains("Hotel/Resorts")
					|| getCat_PropType.contains("Guest-House/Banquet-Halls")
					|| getCat_PropType.contains("Space in Retail Mall")
					|| getCat_PropType.contains("Office in Business Park")
					|| getCat_PropType.contains("Business center")
					|| getCat_PropType.contains("Manufacturing")
					|| getCat_PropType.contains("Cold Storage")
					|| getCat_PropType.contains("Time Share")
					|| getCat_PropType.contains("Preleased Property")
					|| getCat_PropType.contains("Paying Guest")
					|| getCat_PropType.contains("Others")) {

				Log.d("TAG", "#########getRent_purchase===" + "stud3");
			}
		}
		if (getRent_purchase.contains("Rent")|| (getRent_purchase).contains("Sale")
				|| (getRent_purchase).contains("Purchase")){
			Log.d("TAG", "#########getRent_purchase===" + "stud7");

			if (getCat_PropType.contains("Commercial Office/Space")
					|| getCat_PropType.contains("Commercial Shops")
					|| getCat_PropType.contains("Commercial Showrooms")
					|| getCat_PropType.contains("Factory")
					|| getCat_PropType.contains("Office in IT Park")
					|| getCat_PropType.contains("Hotel/Resorts")
					|| getCat_PropType.contains("Guest-House/Banquet-Halls")
					|| getCat_PropType.contains("Space in Retail Mall")
					|| getCat_PropType.contains("Office in Business Park")
					|| getCat_PropType.contains("Business center")
					|| getCat_PropType.contains("Manufacturing")
					|| getCat_PropType.contains("Cold Storage")
					|| getCat_PropType.contains("Time Share")
					|| getCat_PropType.contains("Preleased Property")
					|| getCat_PropType.contains("Paying Guest")
					|| getCat_PropType.contains("Others")) {

				Log.d("TAG", "#########getRent_purchase===" + "stud3");
			}
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

//	public void flipCard() {
//		if (mShowingBack) {
//			getFragmentManager().popBackStack();
//			return;
//		}
//
//		mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//
//				try {
//					Utilities.addFragment(getActivity(), new PostPropertyFragment2(), R.id.container, true, true);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	protected String checkNull(String str){
		if(str.equalsIgnoreCase("Select Unit"))
			str = "";
		
		return str;
	}
	
	
	protected String checkPropertyonFloor(String str){
		if(str.equalsIgnoreCase("Property on Floor"))
			str = "";
		
		return str;
	}
	protected String checkTotalfloor(String str){
		if(str.equalsIgnoreCase("Total floor"))
			str = "";
		
		return str;
	}
	
	protected String checkOwnership(String str){
		if(str.equalsIgnoreCase("Ownership"))
			str = "";
		
		return str;
	}
	protected String checkPossession(String str){
		if(str.equalsIgnoreCase("Possession"))
			str = "";
		
		return str;
	}
	
	protected String checkFurnishing(String str){
		if(str.equalsIgnoreCase("Furnishing"))
			str = "";
		
		return str;
	}
	protected String checkCycle(String str){
		if(str.equalsIgnoreCase("Cycle"))
			str = "";
		
		return str;
	}
	protected String checkAvailability(String str){
		if(str.equalsIgnoreCase("Availability"))
			str = "";
		
		return str;
	}
	protected String checkTransaction(String str2){
		if(str2.equalsIgnoreCase("Transaction"))
			str2 = "";
		
		return str2;
	}
	class PostPropertyTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", userId = "", userName = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = Utilities.showProgressDialog(getActivity(), PostPropertyTask.this);
			dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Utilities.Snack(mCoordinator, "Request cancelled !");
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
				// String getRent_purchase = "", getSubmitType = "",
				// getPropertyType = "", getCity = "", getLocation = "";


				String POST_PROPERTY_USER = "{\"property_title\":\""
						+ get_Prop_Title

						+ "\", \"user_id\":\""
						+ getUserDataID

						+ "\", \"property_added_on\":\""
						+ timeString

//						+ "\", \"amenities\":\""
//						+ getAmenities

						+ "\", \"property_listing_type\":\""
						+ getRent_purchase

						+ "\", \"property_listing_parent\":\""
						+ getSubmitType
						//
						+ "\", \"property_floor\":\""
						+ checkPropertyonFloor(getfloorbldng)
						//

						// + "\", \"user_type\":\""
						// + getUserType
						// //
						+ "\", \"building_total_floors\":\""
						+ checkTotalfloor(getTotal_floor_bldng)

//						+ "\", \"property_type\":\""
//						+ getPropertyType

						+ "\", \"property_type_name\":\""
						+ getCat_PropType
						//
//						+ "\", \"property_type_parent\":\""
//						+ getSubmitType
						// //
						+ "\", \"property_type_name_parent\":\""
						+ getPropertyType
						//
						+ "\", \"property_landmark\":\""
						+ getLocation

						+ "\", \"property_city\":\""
						+ getCity
						 
						 + "\", \"property_longitude\":\""
						 + api_longitude 

						 + "\", \"property_latitude\":\""
						 + api_latitude 

						// + "\", \"property_area\":\""
						// + getUserType

						+ "\", \"property_description\":\""
						+ get_Descrption

						+ "\", \"property_super_area\":\""
						+ getSuper_Area

						+ "\", \"property_super_area_unit\":\""
						+ checkNull(getSuper_Unit_Area)

						// + "\", \"property_carpet_area_unit\":\""
						// + getCarpet_Unit_Area

						// + "\", \"property_carpet_area\":\""
						// + getCarpet_Area

						+ "\", \"property_built_area\":\""
						+ getCarpet_Area

						+ "\", \"property_built_area_unit\":\""
						+ checkNull(getCarpet_Unit_Area)
						//
						+ "\", \"property_plot_area\":\"" 
						+ (getPlot_Area)

						+ "\", \"property_plot_area_unit\":\""
						+ checkNull(getPlot_Unit_Area)

						+ "\", \"no_of_bedrooms\":\"" 
						+ getBedrooms

						+ "\", \"no_of_bathrooms\":\"" 
						+ getBathrooms

						+ "\", \"no_of_washrooms\":\"" 
						+ getWashrooms

						+ "\", \"no_of_balcony\":\"" 
						+ getBalconies

						+ "\", \"property_furnishing_type\":\""
						+ checkFurnishing(get_furnishing_type)
//						Log.e(TAG, "checkNull(get_furnishing_type)")

						+ "\", \"transaction_type\":\""
						+ checkTransaction(get_Transaction_type)

						+ "\", \"property_ownership\":\"" 
						+ checkOwnership(getProp_ownership)

						+ "\", \"property_availability\":\""
						+ checkAvailability(getProp_availblty)

						+ "\", \"property_location\":\"" + ""
//						getProjectName

						+ "\", \"property_maintenance_amount\":\""
						+ getMntnce_chrg

						+ "\", \"property_maintenance_frequency\":\"" 
						+ checkCycle(getFreq)

						+ "\", \"expected_price\":\""
						+ getAbsolut_prce

						+ "\", \"expected_unit_price\":\"" + 
						getCalculateArea1

						+ "\", \"expected_unit_price_unit\":\""
						+ getAdd_prce_area_unit

						// + "\", \"call_preference\":\""
						// + getUserType

						+ "\", \"property_possession\":\"" 
						+ checkPossession(get_Possession)

						// + "\", \"property_subject\":\""
						// + getUserType
						//
						// + "\", \"is_live\":\""
						// + getUserType
						//
						 + "\", \"amenities\":\""
						 + getAmenities
						//
						// + "\", \"property_added_on\":\""
						// + getUserType

						+ "\"}";

				String str_send_json = "{\"table\":\"properties\",\"by\":{\"property_id\":\""
						+ getPropertyID
						+ "\"},\"params\":"
						+ POST_PROPERTY_USER + "}";

				String UrlBase = Host.UpdateUrl + "";
				String jsonString = Utilities.sendData(UrlBase,	str_send_json);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					// userId = data.getString("user_id");
					// userName = data.getString("user_name");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();

			if (api_code == 200) {
				db.putBoolean("shouldReload", true);
				SharedPreferences.Editor editor = mPrefs.edit();

				editor.putString("PREF_KEY_Rent_purchase", "");
				editor.putString("PREF_KEY_Submit_type", "");
				editor.putString("PREF_KEY_Property_type", "");
				editor.putString("PREF_KEY_City_type", "");
				editor.putString("PREF_KEY_Location_type", "");
//				editor.putString("PREF_KEY_ProjectName_type", "");
				editor.putString("PREF_KEY_Cat_PropType", "");
				
				///////////////////PREF POST 2
				editor.putString("PREF_KEY_getSuper_Area","");
				editor.putString("PREF_KEY_getPlot_Area", "");
				editor.putString("PREF_KEY_getCarpet_Area", "");
				editor.putString("PREF_KEY_getAbsolut_prce",  "");
				editor.putString("PREF_KEY_getAdd_prce", "");
				editor.putString("PREF_KEY_getMntnce_chrg", "");
				editor.putString("PREF_KEY_getfloorbldng", "");
				editor.putString("PREF_KEY_getTotal_floor_bldng", "");

				editor.putString("PREF_KEY_getBedrooms", "");
				editor.putString("PREF_KEY_getBathrooms", "");
				editor.putString("PREF_KEY_getBalconies", "");
				editor.putString("PREF_KEY_getWashrooms", "");

				editor.putString("PREF_KEY_getSuper_Unit_Area", "");
				editor.putString("PREF_KEY_getCarpet_Unit_Area", "");
				editor.putString("PREF_KEY_getPlot_Unit_Area", "");
				editor.putString("PREF_KEY_getAdd_prce_area_unit",	"");
				editor.putString("PREF_KEY_getFreq", "");

				editor.putString("PREF_KEY_getProp_ownership", "");
				editor.putString("PREF_KEY_getProp_availblty", "");
				editor.putString("PREF_KEY_get_furnishing_type", "");
				editor.putString("PREF_KEY_get_Transaction_type", "");
				editor.putString("PREF_KEY_get_Possession", "");
				editor.putString("PREF_KEY_get_Prop_Title", "");
				editor.putString("PREF_KEY_get_Descrption", "");
				
				editor.putString("PREF_KEY_getAmenities", "");

				editor.commit();

//				Bundle args = new Bundle();
//				args.putString("KEY_getPropertyId", api_data);
//
//				Fragment f = new UploadPicFragment1();
//				f.setArguments(args);
//
//				Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
				
				if(Utilities.isInternetOn(ctx)){
					try {
						BitmapDrawable drawable = (BitmapDrawable) iv_profile.getDrawable();
						bm = drawable.getBitmap();
						if(bm != null)
							sendPhoto(Utilities.compressImage(bm));
						else
							Utilities.Snack(mCoordinator, "Please select a photo");
					} catch (Exception e) {
						e.printStackTrace();
						Utilities.Snack(mCoordinator, "Please select a photo");
					}
				}

			} else {
				Utilities.Snack(mCoordinator, "Something went wrong, please try again !"	);

			}
		}
	}
	//----------------------------------------------images upload------------------
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		btn_selectPic.setEnabled(true);
		try {
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				imgDecodableString = cursor.getString(columnIndex);
				cursor.close();
				
				BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inJustDecodeBounds = false;
//		      	this options allow android to claim the bitmap memory if it runs low on memory
		        options.inPurgeable = true;
		        options.inInputShareable = true;
		        options.inTempStorage = new byte[16 * 1024];
		        Utilities.calculateInSampleSize(options, 200, 200);
		        
		        try {
//		          load the bitmap from its path
		        	bm = BitmapFactory.decodeFile(imgDecodableString, options);
		        } catch (OutOfMemoryError exception) { exception.printStackTrace();
		            Utilities.Snack(mCoordinator, "Out of memory error !");
		        }
				int nh = (int) ( bm.getHeight() * (200.0 / bm.getWidth()) );
				Bitmap scaled = Bitmap.createScaledBitmap(bm, 200, nh, true);

	            try {
	                ExifInterface exif = new ExifInterface(imgDecodableString);
	                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
	                Log.d("EXIF", "Exif: " + orientation);
	                Matrix matrix = new Matrix();
	                if (orientation == 6) {
	                    matrix.postRotate(90);
	                }
	                else if (orientation == 3) {
	                    matrix.postRotate(180);
	                }
	                else if (orientation == 8) {
	                    matrix.postRotate(270);
	                }
	                scaled = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true); // rotating bitmap
	            }
	            catch (Exception e) {
	            }
				
				iv_profile.setImageBitmap(scaled);
				btn_selectPic.setText("CHANGE PICTURE");
			}
		} catch (Exception e) {
			if(Utilities.D)Log.v(TAG,"Something went wrong");
		}
	}
	
    public void pickPhoto(View view) {
		Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    
	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadPicTask().execute(bitmap);
	}
	
	private class UploadPicTask extends AsyncTask<Bitmap, Void, Void> {
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			Utilities.Snack(mCoordinator, "Uploading please wait..");
			dialog = Utilities.showProgressDialog(ctx);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						
						UploadPicTask.this.cancel(true);
						dialog.dismiss();
						if(Utilities.D)Log.v(TAG,"Dialog cancelled on KEYCODE_BACK");
					}
					return false;
				}
			});

			dialog.show();
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Utilities.Snack(mCoordinator, "Request cancelled !");
		}
		protected Void doInBackground(Bitmap... bitmaps) {
			if (bitmaps[0] == null)
				return null;
			
			getActivity().setProgress(0);
			if(Utilities.D)Log.v(TAG,"doInBackground=");
			String urlBase = Host.UploadProfileUrl;
			String params = "{\"id\":\"" + getPropertyID + "\"}";
			String jsonData = "{\"table\":\"property_images\", \"params\":" + params + "}";

			File file = null;
			UploadUtils.MultipartFileUploader(file, bm, urlBase, jsonData, getUserDataID, getPropertyID, true, true);
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			
			if(Utilities.D)Log.v(TAG,"onProgressUpdate=" + values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			Utilities.Snack(mCoordinator, "Property posted successfully !");

			if(Utilities.D)Log.v(TAG,"onPostExecute(): Uploaded succesfully !");
			getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			getActivity().finish();
		}
	}
	class PROPERTY_IMG_Task extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data, url_propertyImage;
//		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

//			dialog = Utilities.showProgressDialog(ctx, PROPERTY_IMG_Task.this);
//			dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String str_send_json = "{\"by\":{\"property_id\":\""
						+ f_url[0]
						+ "\"}" +
						",\"type\":\"ASC\"" +
						",\"table\":\"property_images\",\"multiple\":\"All\"}";

				// String str_send_json =
				// "{\"table\":\"properties\",\"multiple\":\"all\"}";

				if(Utilities.D)Log.v(TAG,"str_send_json=" + str_send_json);
				String UrlBase = Host.GetUrl;
				String jsonString = Utilities.sendData(UrlBase, str_send_json);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONArray data = (JSONArray) reader.get("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						
//						if(i == data.length()-1){
						if(obj.getString("image").contains("http")){
							url_propertyImage = obj.getString("image");
						} else if(obj.getString("image").contains("uploads")){
							url_propertyImage = Host.MainUrl + obj.getString("image");
						} else{
							url_propertyImage = Host.ImgsPath + obj.getString("image");
						}

//					url_propertyImage = obj.getString("id");
//					url_propertyImage = obj.getString("user_id");
//					url_propertyImage = obj.getString("property_id");
//					url_propertyImage = obj.getString("session_id");

//						}

				}}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return url_propertyImage;
		}

		@Override
		protected void onPostExecute(String file_url) {

//			dialog.dismiss();
			if (api_code == 200) {
//				file_url = Host.MainUrl + file_url;
				url_propertyImage = file_url;
				if(Utilities.D)Log.v(TAG,"file_url:" + file_url);
				ImageLoader.getInstance().displayImage(file_url, iv_profile, options, AvailPCFragment.animateFirstListener);
				// try {
				// File file2 =
				// ImageLoader.getInstance().getDiscCache().get(file_url);
				// if (!file2.exists()) {
				//
				// DisplayImageOptions options = new
				// DisplayImageOptions.Builder().cacheOnDisc().build();
				// ImageLoader.getInstance().displayImage(file_url, iv_image,
				// options);
				// } else {
				// iv_image.setImageURI(Uri.parse(file2.getAbsolutePath()));
				// }
				// } catch (Exception e) { e.printStackTrace(); }

			} else if(api_code == 400){
			} else {
			}
		}
	}
}
