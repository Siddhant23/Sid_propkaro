package com.propkaro.edit;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.propkaro.R;
import com.propkaro.notifications.GoogleSetter;
import com.propkaro.post.edit.Edit_New_Post5;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class Edit_New_Post4 extends Fragment implements OnItemSelectedListener{

	static String TAG = Edit_New_Post4.class.getSimpleName();
	String getBedrooms = "", getBathrooms = "", getBalconies = "",
			getWashrooms = "", getAdd_prce = "", getMntnce_chrg = "",
			getfloorbldng = "", getTotal_floor_bldng = "", getSuper_Area = "",
			getPlot_Area = "", getCarpet_Area = "", getSuper_Unit_Area = "",
			getCarpet_Unit_Area = "", getPlot_Unit_Area = "", getCrore = "",
			get_thousand = "", get_lakh = "", getAdd_prce_area_unit = "",
			getFreq = "";
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area;
	ImageView back1,back0;
	String api_latitude = "", api_longitude = "";
	Dialog dialog2 ;

	int getAbsolut_prce = 0, getAbsolut_prce1, getAbsolut_prce2,
			getAbsolut_prce3;
	float getCalculateArea = 0;
	String getRent_purchase = "", getSubmitType = "", getPropertyType = "",
			getCity = "", getLocation = "", getPropDescription = "", 
//			getProjectName = "",
			getCat_PropType = "";
	Context ctx;
	String str_prop_type1[] = { "Residential Apartment", "Residential Land",
			"Independent House/Villa", "Independent/Builder Floor",
			"Farmhouse", "Studio Apartment", "Service Apartment",
			"Residential Collaboration", "Other" };

	String str_prop_type2[] = { "Commercial Office/Space", "Commercial Shops",
			"Commercial Land/Inst. Land", "Commercial Showrooms",
			"Agriculture/Farm Land", "Factory", "Warehouse",
			"Office in IT Park", "Hotel/Resorts", "Guest-House/Banquet-Halls",
			"Space in Retail Mall", "Office in Business Park",
			"Business center", "Manufacturing", "Cold Storage", "Time Share",
			"Industrial Lands/Plots", "Commercial Collaboration",
			"Preleased Property", "Paying Guest", "Others" };
	SharedPreferences mPrefs;
	String temp_city = "", temp_getRent_purchase = "", temp_getSubmitType = "",
			temp_getPropertyType = "", temp_getCat_PropType = "", api_allCities = "";

	final Handler mHandler = new Handler();
	TinyDB db;

	Spinner bathrooms, bedrooms, balconics, washrooms, saunit, caunit, paunit,
			core, lakh, thounsands;
	boolean mShowingBack = false;

	// static final String TAG_DES = "hotel_description";
	String temp_getfloorbldng = "0",   temp_getBedrooms = "0",  temp_getBathrooms = "0", 
			temp_getBalconies = "0", temp_getWashrooms = "0",
			temp_getAdd_prce = "0",   
			temp_getTotal_floor_bldng = "0",  temp_getSuper_Area = "0", 
			temp_getPlot_Area = "0",  temp_getCarpet_Area = "0", 
			temp_getSuper_Unit_Area = "0",  temp_getCarpet_Unit_Area = "0",
			temp_getPlot_Unit_Area ="0",    
			temp_get_thousand = "0",  temp_get_lakh = "0",  temp_get_freq = "0", 
			temp_getAdd_prce_area_unit = "0";
	AutoCompleteTextView atv_location, atv_locality;

	View rView;
//	GoogleMap googleMap;
	RadioButton rb1, rb2, rb3, rb4;
	Spinner spin_prop_type;
	Button BTN_CITY;

	String spin_onaer[] = { "Availability", "Requirement" };

	String spin_prop_type1[] = { "Residential Apartment", "Residential Land",
			"Independent House/Villa", "Independent/Builder Floor",
			"Farmhouse", "Studio Apartment", "Service Apartment",
			"Residential Collaboration", "Other" };

	String spin_prop_type2[] = { "Commercial Office/Space", "Commercial Shops",
			"Commercial Land/Inst. Land", "Commercial Showrooms",
			"Agriculture/Farm Land", "Factory", "Warehouse",
			"Office in IT Park", "Hotel/Resorts", "Guest-House/Banquet-Halls",
			"Space in Retail Mall", "Office in Business Park",
			"Business center", "Manufacturing", "Cold Storage", "Time Share",
			"Industrial Lands/Plots", "Commercial Collaboration",
			"Preleased Property", "Paying Guest", "Others" };

	ArrayList<String> api_city = new ArrayList<String>();
	ArrayAdapter<String> adapter_loc;
	ArrayList<GoogleSetter> locationList = new ArrayList<GoogleSetter>();
	EditText mSearchView;
	ListView mListView;
    ArrayAdapter<String> adapter_cities;
	private CoordinatorLayout mCoordinator;
	Button btn_location;
	TextView tv_from_step3,tv_1 ,tv_2,tv_3,tv_4 ,tv_5, tv_6,tv_step4;
	Typeface RobotoLight;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_4, container, false);
		
		tv_from_step3= (TextView) rView.findViewById(R.id.tv_from_step3);
		tv_from_step3.setText(getSubmitType + " � " + getRent_purchase + " � " + getPropertyType);
	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
	    RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 
	    
	    tv_1 = (TextView) rView.findViewById(R.id.tv_1); 
	   		 tv_2 = (TextView) rView.findViewById(R.id.tv_2); 
	   		 tv_3 = (TextView) rView.findViewById(R.id.tv_3); 
	   		 tv_4 = (TextView) rView.findViewById(R.id.tv_4); 
	   		 tv_5 = (TextView) rView.findViewById(R.id.tv_5); 
	   		 tv_6 = (TextView) rView.findViewById(R.id.tv_6); 
	   		 tv_step4 = (TextView) rView.findViewById(R.id.tv_step4); 

//		getCity = mPrefs.getString("PREF_KEY_City_type", "");
//		temp_city = getCity;
		// ////////////////////////////////////////////////////////
	    btn_location = (Button) rView.findViewById(R.id.btn_location);
		 btn_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View loc) {
				openLocationDialog(btn_location, loc);
			}
		});
//		getLocation = mPrefs.getString("PREF_KEY_Location_type", "");
		  
		if(getLocation.contains(" -- ")){
			btn_location.setText("");
		}else { 
			btn_location.setText(getLocation + "");
		}
		back0 = (ImageView) rView.findViewById(R.id.back0);
		back0.setVisibility(View.GONE);
		back0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG,"onclicking ...");
//				SharedPreferences.Editor editor = mPrefs.edit();
//				
//				///////////////////PREF POST 1
//				editor.putString("PREF_KEY_Rent_purchase", getRent_purchase);
//				editor.putString("PREF_KEY_Submit_type", getSubmitType);
//				editor.putString("PREF_KEY_Property_type", getPropertyType);
////				editor.putString("PREF_KEY_City_type", BTN_CITY.getText().toString()+ "");
//				editor.putString("PREF_KEY_Location_type",et_location.getText().toString()+ "");
//				editor.putString("PREF_KEY_Cat_PropType", spin_prop_type.getSelectedItem().toString());
//
//				editor.commit();
				////*********************************///////////////////////////////////   
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		back1 = (ImageView) rView.findViewById(R.id.back1);
		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				EditText et_location = (EditText) rView.findViewById(R.id.et_location);

				getLocation = btn_location.getText().toString();
			 if (BTN_CITY.getText().toString().contains("Select City")) {
				 
				 Utilities.Snack(mCoordinator, "Please enter City");
			} else if (getLocation.length() == 0) {
				
				Utilities.Snack(mCoordinator,"Please enter Detailed location");
			} else {

				Log.d(TAG, "getRent_purchase==" + getRent_purchase);
				Log.d(TAG, "getSubmitType==" + getSubmitType);
				Log.d(TAG, "getLocation==" + getLocation);
//				Log.d(TAG, "et_project_name==" + getProjectName);
				Log.d(TAG, "getCity==" + getCity);
				Log.d(TAG, "getPropertyType==" + getPropertyType);
				Log.d(TAG, "getCat_PropType==" + getCat_PropType);

				// //*********************************///////////////////////////////////
				Bundle args = new Bundle();
				
				args.putString("KEY_property_description", getPropDescription);
				args.putString("KEY_Rent_purchase", getRent_purchase);
				args.putString("KEY_Submit_type", getSubmitType);
				args.putString("KEY_Property_type", getPropertyType);
				args.putString("KEY_City_type", getCity);
				args.putString("KEY_Location_type", getLocation);
//				args.putString("KEY_ProjectName_type", getProjectName);
				args.putString("KEY_Cat_PropType", getCat_PropType);
				args.putString("KEY_api_latitude", api_latitude);
				args.putString("KEY_api_longitude", api_longitude);
				Fragment f = new Edit_New_Post5();
				f.setArguments(args);

				try {
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		});

//		getCity = mPrefs.getString("PREF_KEY_City_type", "0");
//		temp_city = getCity;
		
		//////////////////////////////
		
		spin_prop_type = (Spinner) rView.findViewById(R.id.spin_prop_type);
		spin_prop_type.setOnItemSelectedListener(this);
	
		ArrayAdapter<String> bathAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				spin_prop_type1);

		bathAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_prop_type.setAdapter(bathAdapter);
		
		if(getPropertyType.contains("Residential")){
						Log.d(TAG, "rb_For_Residential==="
								+ getPropertyType);


						ArrayAdapter<String> resdAdapter = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_item,
								spin_prop_type1);

						resdAdapter
								.setDropDownViewResource(android.R.layout.simple_list_item_checked);
						spin_prop_type.setAdapter(resdAdapter);
						 
		}
		if(getPropertyType.contains("Commercial")){

						Log.d(TAG, "rb_For_Commercial===" + getPropertyType);

						ArrayAdapter<String> commAdapter = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_item,
								spin_prop_type2);

						commAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
						spin_prop_type.setAdapter(commAdapter);
		}    
		 
		BTN_CITY = (Button) rView.findViewById(R.id.spin_city_type);
		BTN_CITY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCityDialog(BTN_CITY, v);
			}
		});
		if(api_city.size() == 0){
			if(Utilities.D)Log.v(TAG,"api_allCities is empty!");
			if(Utilities.isInternetOn(ctx)){
				new allCitiesTask().execute("");
			} else 
				getActivity().finish();
		} else {
			if(Utilities.D)Log.v(TAG,"api_allCities NOT empty!");
			try {
				BTN_CITY.setVisibility(View.VISIBLE);
				BTN_CITY.setText(temp_city);
				if(temp_city.length() < 1){
					BTN_CITY.setText("Select City");
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		setFont();
		return rView;
	}
	private void setFont() {
		tv_from_step3.setTypeface(RobotoLight);
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_3 .setTypeface(RobotoLight);
		tv_4 .setTypeface(RobotoLight);
		tv_5 .setTypeface(RobotoLight);
		tv_6 .setTypeface(RobotoLight);
		tv_step4.setTypeface(RobotoLight);
		BTN_CITY.setTypeface(RobotoLight);
		btn_location.setTypeface(RobotoLight);
	}
		
	// //////////////////////////////////////////////
	 
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public void openLocationDialog(View locView, final View v2) {

		dialog2 = new Dialog(ctx,android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge );
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.list_location_dialog);
//		dialog2.setCancelable(false);
		atv_location = (AutoCompleteTextView) dialog2.findViewById(R.id.et_location);
//		atv_location.requestFocus();
		atv_location.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

				getLocation = "" + atv_location.getText().toString();
				getLocation = getLocation.replaceAll(" ", "%20").replaceAll("\\s+", "+");// type =
				String mapUrl = Host.AutoCompleteUrl + "input=" + getLocation.replaceAll(" ", "%20").replaceAll("\\s+", "+")
						+ "&key=" + Host.API_KEY + "&components=country:IN";
				
				if (Utilities.NetworkCheck(getActivity()))

				new LocationTask().execute(mapUrl);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		atv_location.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				
				String goMapUrl = Host.DetailsUrl
						+ "reference=" + references[position]
						+ "&sensor=false&key="
						+ Host.API_KEY;
				if (Utilities.NetworkCheck(getActivity()))
					new PlacesTask().execute(goMapUrl);

				btn_location.setText(getLocation.replaceAll("%20", " "));
				Utilities.hideKeyboard(getActivity());
				dialog2.dismiss();
			}
		});
		dialog2.show();
	}
			
	public void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		mHandler.post(new Runnable() {
			@Override
			public void run() {

				try {
					Utilities.addFragment(getActivity(), new Edit_New_Post5(), R.id.container, true, true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void openCityDialog(View v, final View v2) {

		final Dialog dialog = new Dialog(ctx, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.list_search_dialog);

		mSearchView = (EditText) dialog.findViewById(R.id.ListSearch);
		mListView = (ListView) dialog.findViewById(R.id.listViewDialog);
		mListView.setFastScrollEnabled(true);
		 adapter_cities = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				api_city);

		mListView.setAdapter(adapter_cities);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int positionOrigional = api_city.indexOf((String) mListView.getItemAtPosition(position));

				try {
					getCity = api_city.get(positionOrigional);
					// mListView.getItemIdAtPosition(position);
					if (api_city.size()==0) {
//						BTN_CITY.setText("Select City");
					}else {
						Log.e(getCity, getCity + "  "+positionOrigional);
						BTN_CITY.setText(getCity);
//					    BTN_CITY.setText(api_city.get(position));
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ctx, "Some error occurred",
							Toast.LENGTH_SHORT).show();
				}


				dialog.dismiss();
			}
		});

		mListView.setTextFilterEnabled(true);

		// mSearchView.setHint(hint);
		mSearchView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter_cities.getFilter().filter(s.toString());
				adapter_cities.notifyDataSetChanged();
			}
		});
		dialog.show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity(); 
		db = new TinyDB(ctx);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		api_city = db.getListString("PREF_KEY_CITIES_ALL");
		
		getCat_PropType = getActivity().getIntent().getStringExtra("KEY_Cat_PropType");
		Log.e(TAG, "===------------getCat_PropType="+getCat_PropType);
		
		temp_getCat_PropType = getCat_PropType;
		
//		getCity = getActivity().getIntent().getStringExtra("KEY_City_type");
		api_latitude = getActivity().getIntent().getStringExtra("KEY_api_latitude");
		api_longitude = getActivity().getIntent().getStringExtra("KEY_api_longitude");
		
		getPropDescription = getActivity().getIntent().getStringExtra("KEY_property_description");
		getSubmitType = getActivity().getIntent().getStringExtra("KEY_Submit_type");
		getRent_purchase = getActivity().getIntent().getStringExtra("KEY_Rent_purchase");
		getPropertyType = getActivity().getIntent().getStringExtra("KEY_Property_type");
		getCat_PropType = getActivity().getIntent().getStringExtra("KEY_Cat_PropType");
		getCity = getActivity().getIntent().getStringExtra("KEY_City_type");
		temp_city = getCity;
		getLocation = getActivity().getIntent().getStringExtra("KEY_Location_type");

//	Bundle b = this.getArguments();
//	getSubmitType = b.getString("KEY_Submit_type");
//	getRent_purchase = b.getString("KEY_Rent_purchase");
//	getPropertyType = b.getString("KEY_Property_type");
	
	if(Utilities.D)Log.d(TAG, "=====getSubmitType=" + getSubmitType);
	if(Utilities.D)Log.d(TAG, "=====getRent_purchase=" + getRent_purchase);
	if(Utilities.D)Log.d(TAG, "=====getPropertyType=" + getPropertyType);
	}
	class PlacesTask extends AsyncTask<String, String, String> {
		String name1, address1;

		@Override
		protected String doInBackground(String... params) {

			String result = Utilities.readJsonGoogle(ctx, params[0]);
			JSONObject jsonobj;
			try {
				jsonobj = new JSONObject(result);
				JSONObject jsonresult = jsonobj.getJSONObject("result");
				JSONObject geojson = jsonresult.getJSONObject("geometry");
				JSONObject locajson = geojson.getJSONObject("location");

				name1 = jsonresult.getString("name");
				address1 = jsonresult.getString("formatted_address");
				if(Utilities.D)Log.v(TAG,"name1=" + name1);
				if(Utilities.D)Log.v(TAG,"address1=" + address1);

				api_latitude = locajson.getString("lat");
				api_longitude = locajson.getString("lng");

				Log.v(TAG, "api_latitude="+api_latitude);
				Log.v(TAG, "api_longitude="+api_longitude);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}

	String places[], references[];
	class LocationTask extends AsyncTask<String, String, String> {
		String result = null;
		JSONObject jsonobj;
		String place, reference;
		String status = "";

		@Override
		protected void onPreExecute() {
			if(Utilities.D)Log.v(TAG,"locationTask preExecuteing..");
		};

		@Override
		protected String doInBackground(String... arg0) {

			if(Utilities.D)Log.v(TAG,"doInBackground doInBackground..");
			try {
				String locationResult = Utilities.readJsonGoogle(ctx, arg0[0]);
				jsonobj = new JSONObject(locationResult);

				JSONArray jas = jsonobj.getJSONArray("predictions");
				status = jsonobj.getString("status");
				if(status.equals("OK")){
					places = new String[jas.length()];
					references = new String[jas.length()];
					for (int i = 0; i < jas.length(); i++) {
						JSONObject job = jas.getJSONObject(i);
						place = job.getString("description");
						reference = job.getString("reference");
						places[i] = place;
						references[i] = reference;
					}
				}
			} catch (Exception e) { e.printStackTrace(); }

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if(Utilities.D)Log.v(TAG,"locationTask PostExecuteing..");
			if(status.contains("OK")){
				adapter_loc = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_dropdown_item_1line, places);
				atv_location.setAdapter(adapter_loc);
				atv_location.requestFocus();
				adapter_loc.notifyDataSetChanged();
			}
		}
	}
	
	class allCitiesTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			BTN_CITY.setVisibility(View.GONE);
			dialog = Utilities.showProgressDialog(getActivity(), allCitiesTask.this);
			dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			getActivity().finish();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String CITIES_10 =  "{\"table\":\"cities\",\"by\":{\"is_active\":\"1\"},\"type\":\"ASC\",\"order_by\":\"location_city\",\"multiple\":\"All\"}";
				String CITIES_API =  "{\"table\":\"cities\",\"by\":{\"is_active\":\"0\"},\"type\":\"ASC\",\"order_by\":\"location_city\",\"multiple\":\"All\"}";
				
				String jsonString = Utilities.sendData(Host.TestCitySelect, CITIES_10);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONArray data = (JSONArray) reader.get("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						api_city.add(obj.getString("location_city"));
					}
			//-----------------------------------------------------------
					jsonString = Utilities.sendData(Host.TestCitySelect, CITIES_API);
					JSONObject readers = new JSONObject(jsonString);
					JSONObject metas = (JSONObject) readers.get("meta");
					if (metas.getInt("code") == 200) {
						JSONArray datas = (JSONArray) readers.get("data");
						for (int i = 0; i < datas.length(); i++) {
							JSONObject obj = datas.getJSONObject(i);
							api_city.add(obj.getString("location_city"));
						}
					} 
				} 
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();

			if (api_code == 200) {
				db.putListString("PREF_KEY_CITIES_ALL", api_city);
				if (api_city.size() != 0) {
					BTN_CITY.setVisibility(View.VISIBLE);
				}
			} else {
				Toast.makeText(ctx, "No cities found, please try again !", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String item = parent.getItemAtPosition(position).toString();
		String spin_item = item;

		Log.d("TAG", "spin_item===" + spin_item + "\nposition====" + position);

		if (parent.getId() == R.id.btn_location) {
			getLocation = spin_item;
			Log.d("TAG", "getLocation===" + getLocation);
		}
		if (parent.getId() == R.id.spin_floor_buildng) {
			Log.d(TAG, "getfloorbldng===" + getfloorbldng);
			getfloorbldng = item;

			

		}
//		if (parent.getId() == R.id.spin_city_type) {
//			getCity = spin_item;
			Log.d("TAG", "getCity===" + getCity);
//			for (int i = 0; i < api_allCities.length; i++) {
//				if (temp_getfloorbldng.equals(str_spn_floor_bldng[i])) {
//					floor_bldng.setSelection(i);
//					if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spn_floor_bldng[i]);
//					if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getfloorbldng);
//					temp_getfloorbldng = "";
//
//				}
//			}

//		}
			if (parent.getId() == R.id.spin_prop_type) {
				getCat_PropType = spin_item;
				Log.d("TAG", "getCat_PropType===" + getCat_PropType);
				
				if (getPropertyType.contains("Residential")){
					for (int i = 0; i < (str_prop_type1).length; i++) {
						if (temp_getCat_PropType.equals(str_prop_type1[i])) {
	//FIXME: temp_getPropertyType get crashed app here

							spin_prop_type.setSelection(i);
							if(Utilities.D)Log.v(TAG,"str_prop_type1[i]=" + str_prop_type1[i]);
							if(Utilities.D)Log.v(TAG,"getPropertyType=" + getPropertyType);
							temp_getCat_PropType = "";
							Log.e(TAG, "temp_getCat_PropType@@@@#$%%========+ temp_getCat_PropType");
						}
					}
						} else if(getPropertyType.contains("Commercial")){
							for (int i = 0; i < (str_prop_type2).length; i++) {
								if (temp_getCat_PropType.equals(str_prop_type2[i])) {
			//FIXME: temp_getPropertyType get crashed app here

									spin_prop_type.setSelection(i);
									if(Utilities.D)Log.v(TAG,"str_prop_type2[i]=" + str_prop_type2[i]);
									if(Utilities.D)Log.v(TAG,"getPropertyType=" + getPropertyType);
									temp_getCat_PropType = "";
									Log.e(TAG, "temp_getCat_PropType@@@@#$%%========+ temp_getCat_PropType");
								}
							}
				}
			}
		}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	 
}