package com.propkaro.filters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.propkaro.R;
import com.propkaro.propertycenter.KeyPairBoolData;
import com.propkaro.propertycenter.MultiSpinnerSearchFilter;
import com.propkaro.propertycenter.MultiSpinnerSearchFilter.MultiSpinnerSearchListener;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class FilterNewFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	String TAG = FilterNewFragment.class.getSimpleName();
//	TinyDB db;
	MultiSpinnerSearchFilter spin_city;
	Spinner spin_property;

	String str_category_all[] = {"Select Property Category","Residential Apartment", "Residential Land",
			"Independent House/Villa", "Independent/Builder Floor",
			"Farm House", "Studio Apartment", "Serviced Apartments",
			"Residential Collaboration", "Other",
			"Commercial Office/Space" ,"Commercial Shops",
			"Commercial Land/Inst. Land", "Commercial Showrooms",
			"Agricultural/Farm Land", "Industrial Lands/Plots", "Factory",
			"Warehouse", "Office In It Park", "Hotel/Resorts",
			"Guest-House/Banquet-Halls", "Space In Retail Mall",
			"Office In Buisness Park", "Buisness Center", "Manufacturing",
			"Cold Storage", "Time Share", "Commercial Collaboration","Preleased Property",
			"Paying Guest" };
	String str_category_commercial[] = {"Select Property Category", "Commercial Office/Space" ,"Commercial Shops",
			"Commercial Land/Inst. Land", "Commercial Showrooms",
			"Agricultural/Farm Land", "Industrial Lands/Plots", "Factory",
			"Warehouse", "Office In It Park", "Hotel/Resorts",
			"Guest-House/Banquet-Halls", "Space In Retail Mall",
			"Office In Buisness Park", "Buisness Center", "Manufacturing",
			"Cold Storage", "Time Share", "Commercial Collaboration","Preleased Property",
			"Paying Guest" };
	
	String str_category_residential[] = {"Select Property Category","Residential Apartment", "Residential Land",
			"Independent House/Villa", "Independent/Builder Floor",
			"Farm House", "Studio Apartment", "Serviced Apartments",
			"Residential Collaboration" };
	
	String str_prop_selectunit[] = {"Select Unit", "Sq.Ft.",
		"Sq. Yards", "Sq. Meter", "Marla", "Cents", "Bigah", "Kottah",
		"Kanal", "Grounds", "Acres", "Biswa", "Guntha", "Aankadam",
		"Hectares", "Roods", "Chataks", "Perch" };
		int getMaxprice =0,getMinprice =0;
	
	String getPost_type = "\"\"", 
			get_Transaction_type = "\"\"", 
			getPropertyType = "\"\"", 
			getCity = "\"\"", 
			getUserType = "\"\"", 
			getStatus = "\"\"", 
			getRoom = "\"\"", 
			getUnit = "\"\"", 
			getMaxArea = "\"\"",
			getMinArea = "\"\"", 
			getLocality = "", 
			api_allCities = "";
 
	private List<KeyPairBoolData> citiesArray = new ArrayList<KeyPairBoolData>();
	private AutoCompleteTextView  atv_locality;
	private Context ctx; TinyDB db;
	private SharedPreferences mPrefs;
	private ImageView iv_availability, iv_requirement, iv_rent, iv_sell, iv_purchase, iv_commercial, iv_residential;
	private RelativeLayout rel_rent, rel_sell, rel_purchase;
	private boolean bool_availability = false, bool_requiremtn = false, bool_commercial = false, bool_residential = false, 
			bool_rent = false, bool_sell = false, bool_purchase = false;
	private View rView;
	private String hasFilter = "";
	private String getUserDataID = "";
	private String getSearchQuery = "";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ctx= getActivity(); mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		db = new TinyDB(ctx);
		api_allCities = mPrefs.getString("PREF_KEY_CITIES_10", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getSearchQuery = db.getString("PREF_SEARCH_QUERY");

//		getPropertyType = db.getString("PREF_KEY_getPropertyType");
//		getCity = db.getString("PREF_KEY_getCity");
//		get_Transaction_type = db.getString("PREF_KEY_get_Transaction_type");
//		getPost_type = db.getString("PREF_KEY_getPost_type");
		if(Utilities.D)Log.d("TAG", "getPost_type===" + getPost_type);
		if(Utilities.D)Log.d("TAG", "get_Transaction_type===" + get_Transaction_type);
		if(Utilities.D)Log.d("TAG", "getCity===" + getCity);
		if(Utilities.D)Log.d("TAG", "getPropertyType===" + getPropertyType);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	rView = inflater.inflate(R.layout.filter_new, container, false);
	spin_city = (MultiSpinnerSearchFilter) rView.findViewById(R.id.spin_cityT);
	spin_city.setVisibility(View.GONE);
	Button btn_clear = (Button)rView.findViewById(R.id.btn_clear);
	Button btn_apply = (Button)rView.findViewById(R.id.btn_apply);
	RelativeLayout rel_availability = (RelativeLayout)rView.findViewById(R.id.rel_availability);
	RelativeLayout rel_requirement = (RelativeLayout)rView.findViewById(R.id.rel_requirement);
	rel_rent = (RelativeLayout)rView.findViewById(R.id.rel_rent);
	rel_sell = (RelativeLayout)rView.findViewById(R.id.rel_sell);
	rel_purchase = (RelativeLayout)rView.findViewById(R.id.rel_purchase);
	RelativeLayout rel_commercial = (RelativeLayout)rView.findViewById(R.id.rel_commercial);
	RelativeLayout rel_residential = (RelativeLayout)rView.findViewById(R.id.rel_residential);
	iv_availability = (ImageView)rView.findViewById(R.id.iv_availability);
	iv_requirement = (ImageView)rView.findViewById(R.id.iv_requirement);
	iv_rent = (ImageView)rView.findViewById(R.id.iv_rent);
	iv_sell = (ImageView)rView.findViewById(R.id.iv_sell);
	iv_purchase = (ImageView)rView.findViewById(R.id.iv_purchase);
	iv_commercial = (ImageView)rView.findViewById(R.id.iv_commercial);
	iv_residential = (ImageView)rView.findViewById(R.id.iv_residential);
	btn_clear.setOnClickListener(this);
	btn_apply.setOnClickListener(this);
	rel_availability.setOnClickListener(this);
	rel_requirement.setOnClickListener(this);
	rel_rent.setOnClickListener(this);
	rel_sell.setOnClickListener(this);
	rel_purchase.setOnClickListener(this);
	rel_commercial.setOnClickListener(this);
	rel_residential.setOnClickListener(this);

	spin_property = (Spinner) rView.findViewById(R.id.spin_propertyT);

	ArrayAdapter<String> adapter_propertyType = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_dropdown_item, str_category_all);
	spin_property.setAdapter(adapter_propertyType);
	spin_property.setOnItemSelectedListener(this);
	
	if(getPost_type.contains("availability")){
		bool_availability = true;
		iv_availability.setImageResource(R.mipmap.img_se_availability);
	}
	if(getPost_type.contains("requirement")){
		bool_requiremtn = true;
		iv_requirement.setImageResource(R.mipmap.img_se_requirement);
	}
	if(get_Transaction_type.contains("rent")){
		bool_rent = true;
		iv_rent.setImageResource(R.mipmap.img_se_rent);
	}
	if(get_Transaction_type.contains("sell")){
		bool_sell = true;
		iv_sell.setImageResource(R.mipmap.img_se_sell);
	}
	if(get_Transaction_type.contains("purchase")){
		bool_purchase = true;
		iv_purchase.setImageResource(R.mipmap.img_se_purchase);
	}
	if(getPropertyType.contains("commercial")){
		bool_commercial = true;
		iv_commercial.setImageResource(R.mipmap.img_se_commercial);
	}
	if(getPropertyType.contains("residential")){
		bool_residential = true;
		iv_residential.setImageResource(R.mipmap.img_se_residential);
	}
	
//--------------------City--------------------------	
	if(api_allCities.length() == 0){
		if(Utilities.D)Log.v(TAG,"api_allCities is empty!");
		if(Utilities.isInternetOn(ctx)){
			new citiesTask().execute("");
		} else 
			getActivity().finish();
	} else {
		if(Utilities.D)Log.v(TAG,"api_allCities NOT empty!");
		try {
			JSONObject reader = new JSONObject(api_allCities);
			JSONArray data = (JSONArray) reader.get("data");
			for (int i = 0; i < data.length(); i++) {
				JSONObject obj = data.getJSONObject(i);
				
				KeyPairBoolData h = new KeyPairBoolData();
				h.setUSER_ID(Integer.parseInt(obj.getString("city_id")));
				h.setLocation_area(obj.getString("location_area"));
				h.setLocation_city(obj.getString("location_city"));
				citiesArray.add(h);
			}
			if(citiesArray.size() != 0){
			    spin_city.setVisibility(View.VISIBLE);
				spin_city.setItems(citiesArray, "Select City", -1, new MultiSpinnerSearchListener() {
					
					@Override
					public void onItemsSelected(List<KeyPairBoolData> items) {
						getCity = "";
						for(int i=0; i < citiesArray.size(); i++) {
							if(items.get(i).getSelected()) {
								getCity = getCity + "\"" + citiesArray.get(i).getLocation_city() + "\", ";
							}
						}
						if(getCity.length() > 2)
							getCity = getCity.substring(0, getCity.length() - 2);			
						if(Utilities.D)Log.e(TAG, "Nehu==========getCity: " + getCity);
					}
				});
			}

		} catch (Exception e) { e.printStackTrace(); }
	}
	return rView;
}// oNCREATE
		
	class citiesTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			spin_city.setVisibility(View.GONE);
		    
			dialog = Utilities.showProgressDialog(getActivity());
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String CITIES_API =  "{\"table\":\"cities\",\"by\":{\"is_active\":\"1\"},\"type\":\"ASC\",\"order_by\":\"location_city\",\"multiple\":\"All\"}";

				String UrlBase = Host.TestCitySelect + "";
				String jsonString = Utilities.sendData(UrlBase, CITIES_API);
				api_allCities = jsonString;
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONArray data = (JSONArray) reader.get("data");
					
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						
						KeyPairBoolData h = new KeyPairBoolData();
						h.setUSER_ID(Integer.parseInt(obj.getString("city_id")));
						h.setLocation_area(obj.getString("location_area"));
						h.setLocation_city(obj.getString("location_city"));
						citiesArray.add(h);
					}
				} else {
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
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_CITIES_10", api_allCities);
				editor.commit();
				if(citiesArray.size() != 0){
				    spin_city.setVisibility(View.VISIBLE);
					spin_city.setItems(citiesArray, "Select City", -1, new MultiSpinnerSearchListener() {
						
						@Override
						public void onItemsSelected(List<KeyPairBoolData> items) {
							getCity = "";
							for(int i=0; i < citiesArray.size(); i++) {
								if(items.get(i).getSelected()) {
									getCity = getCity + "\"" + citiesArray.get(i).getLocation_city() + "\", ";
								}
							}
							if(getCity.length() > 2)
								getCity = getCity.substring(0, getCity.length() - 2);			
							if(Utilities.D)Log.e(TAG, "Nehu==========getCity: " + getCity);
						}
					});
				}
			} else {
				Toast.makeText(ctx, "No cities found !", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_clear:
			getPost_type = "";
			get_Transaction_type = "";
			getCity = "";
			getPropertyType = "\"\"";
			hasFilter = getPost_type + get_Transaction_type + getCity + getPropertyType;
			if(Utilities.D)Log.e(TAG, "hasFilter--------"+hasFilter);
//			if(hasFilter.length() > 2){
//				db.putBoolean("PREF_KEY_bool_hasFilter", true);
//		        GridActivity.mFab.setRippleColor(Color.GREEN);
//		        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.YellowGreen));
//			} else{
//		        db.putBoolean("PREF_KEY_bool_hasFilter", false);
//		        GridActivity.mFab.setRippleColor(Color.RED);
//		        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.Orange));
//			}
//				
//			db.putString("PREF_KEY_getPropertyType", "");
//			db.putString("PREF_KEY_getCity", "");
//			db.putString("PREF_KEY_get_Transaction_type", "");
//			db.putString("PREF_KEY_getPost_type", "");
			iv_availability.setImageResource(R.mipmap.img_un_availability);
			iv_requirement.setImageResource(R.mipmap.img_un_requirement);
			iv_rent.setImageResource(R.mipmap.img_un_rent);
			iv_sell.setImageResource(R.mipmap.img_un_sell);
			iv_purchase.setImageResource(R.mipmap.img_un_purchase);
			iv_commercial.setImageResource(R.mipmap.img_un_commercial);
			iv_residential.setImageResource(R.mipmap.img_un_residential);
			spin_property.setSelection(0);
			spin_city.setSelection(0);
			if(citiesArray.size() != 0){
			    spin_city.setVisibility(View.VISIBLE);
				spin_city.setItems(citiesArray, "Select City", -1, new MultiSpinnerSearchListener() {

					@Override
					public void onItemsSelected(List<KeyPairBoolData> items) {
						getCity = "";
						for(int i=0; i < citiesArray.size(); i++) {
							if(items.get(i).getSelected()) {
								getCity = getCity + "\"" + citiesArray.get(i).getLocation_city() + "\", ";
							}
						}
						if(getCity.length() > 2)
							getCity = getCity.substring(0, getCity.length() - 2);			
						if(Utilities.D)Log.e(TAG, "Nehu==========getCity: " + getCity);
					}
				});
			}
			
			break;
		case R.id.btn_apply:
			hasFilter = getPost_type + get_Transaction_type + getCity + getPropertyType;
			if(Utilities.D)Log.e(TAG, "hasFilter--------"+hasFilter);
//			if(hasFilter.length() > 2){
//				db.putBoolean("PREF_KEY_bool_hasFilter", true);
//		        GridActivity.mFab.setRippleColor(Color.GREEN);
//		        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.YellowGreen));
//			} else{
//		        db.putBoolean("PREF_KEY_bool_hasFilter", false);
//		        GridActivity.mFab.setRippleColor(Color.RED);
//		        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.Orange));
//			}
//			
//			db.putString("PREF_KEY_getPropertyType", getPropertyType);
//			db.putString("PREF_KEY_getCity", getCity);
//			db.putString("PREF_KEY_get_Transaction_type", get_Transaction_type);
//			db.putString("PREF_KEY_getPost_type", getPost_type);
			if(Utilities.D)Log.d("TAG", "getPost_type===" + getPost_type);
			if(Utilities.D)Log.d("TAG", "get_Transaction_type===" + get_Transaction_type);
			if(Utilities.D)Log.d("TAG", "getCity===" + getCity);
			if(Utilities.D)Log.d("TAG", "getPropertyType===" + getPropertyType);
//---------------------------------------------------------------------------
			String API_FILTER = 
					"{\"by\": {"+
		    "\"property_city\": [" + getCity + "]," + //\"Delhi\"
		    "\"property_listing_parent\": [" + getPost_type + "]," + //\"availability\",\"requirement\"
//		    "\"user_type\": [" +getUserType + "]," + //\"Agent\"
//		    "\"property_availability\": [" +getStatus+ "]," +
//		     "\"no_of_bedrooms\":[" + getRoom + "]," +
//		    "\"minprice\":" + getMinprice + "," +
//		    "\"maxprice\":" + getMaxprice + "," +
//		    "\"unit\": " + getUnit + "," +
//		    "\"minarea\":" + getMinArea + "," +
//		    "\"maxarea\":" + getMaxArea + "," +
//		    "\"property_location\": \"" + getLocality + "\"," +
			"\"property_type_name\": " + getPropertyType + "," +
			"\"keywords\": \"" + getSearchQuery + "\"," +
			"\"property_listing_type\":[" + get_Transaction_type+ "]" + //
		    "}" +
		    "}";
			if(Utilities.D)Log.v(TAG,"FILTER_API=" + API_FILTER);
//---------------------------------------------------------------------------	
			Bundle args = new Bundle();
			args.putString("KEY_API_FILTER", API_FILTER);
			
			Fragment f = new FilterFragment();
			f.setArguments(args);
			
			Utilities.addFragment(getActivity(), f, R.id.container, true, true);

			break;
		case R.id.rel_availability:
			iv_rent.setImageResource(R.mipmap.img_un_rent);
			iv_sell.setImageResource(R.mipmap.img_un_sell);
			iv_purchase.setImageResource(R.mipmap.img_un_purchase);
			bool_rent = false;
			bool_sell = false;
			bool_purchase = false;
			get_Transaction_type = "";
			if(bool_availability == false){
				bool_availability = true;
				iv_availability.setImageResource(R.mipmap.img_se_availability);
				
				if(bool_requiremtn){
					getPost_type = "\"availability\", \"requirement\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.VISIBLE);
				} else{
					getPost_type = "\"availability\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.GONE);
				}
			} else{
				bool_availability = false;
				iv_availability.setImageResource(R.mipmap.img_un_availability);
				if(bool_requiremtn){
					getPost_type = "\"requirement\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.GONE);
					rel_purchase.setVisibility(View.VISIBLE);
				} else{
					getPost_type = "";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.VISIBLE);
				}
			}
			if(Utilities.D)Log.v(TAG, "getPostType-----------"+getPost_type);
			break;
		case R.id.rel_requirement:
			iv_rent.setImageResource(R.mipmap.img_un_rent);
			iv_sell.setImageResource(R.mipmap.img_un_sell);
			iv_purchase.setImageResource(R.mipmap.img_un_purchase);
			bool_rent = false;
			bool_sell = false;
			bool_purchase = false;
			get_Transaction_type = "";
			if(bool_requiremtn == false){
				bool_requiremtn = true;
				iv_requirement.setImageResource(R.mipmap.img_se_requirement);
				if(bool_availability){
					getPost_type = "\"availability\", \"requirement\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.VISIBLE);
				} else{
					getPost_type = "\"requirement\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.GONE);
					rel_purchase.setVisibility(View.VISIBLE);
				}
			}else {
				bool_requiremtn = false;
				iv_requirement.setImageResource(R.mipmap.img_un_requirement);
				if(bool_availability){
					getPost_type = "\"availability\"";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.GONE);
				} else{
					getPost_type = "";
					rel_rent.setVisibility(View.VISIBLE);
					rel_sell.setVisibility(View.VISIBLE);
					rel_purchase.setVisibility(View.VISIBLE);
				}
			}
			if(Utilities.D)Log.v(TAG, "getPostType-----------"+getPost_type);
			break;
		case R.id.rel_rent:
			if(bool_rent == false){
				bool_rent = true;
				iv_rent.setImageResource(R.mipmap.img_se_rent);
				if(bool_sell && bool_purchase)
					get_Transaction_type = "\"rent\", \"sale\", \"purchase\"";
				else if(bool_sell && bool_purchase == false)
					get_Transaction_type = "\"rent\", \"sale\"";
				else if(bool_sell == false && bool_purchase)
					get_Transaction_type = "\"rent\", \"purchase\"";
				else
					get_Transaction_type = "\"rent\"";
			}else{
				bool_rent = false;
				iv_rent.setImageResource(R.mipmap.img_un_rent);
				if(bool_sell && bool_purchase)
					get_Transaction_type = "\"sale\", \"purchase\"";
				else if(bool_sell && bool_purchase == false)
					get_Transaction_type = "\"sale\"";
				else if(bool_sell == false && bool_purchase)
					get_Transaction_type = "\"purchase\"";
				else
					get_Transaction_type = "";
			}
			break;
		case R.id.rel_sell:
			if(bool_sell == false){
				bool_sell = true;
				iv_sell.setImageResource(R.mipmap.img_se_sell);
				if(bool_rent && bool_purchase)
					get_Transaction_type = "\"rent\", \"sale\", \"purchase\"";
				else if(bool_rent && bool_purchase == false)
					get_Transaction_type = "\"rent\", \"sale\"";
				else if(bool_rent == false && bool_purchase)
					get_Transaction_type = "\"rent\", \"sale\"";
				else
					get_Transaction_type = "\"sale\"";
			}else{
				bool_sell = false;
				iv_sell.setImageResource(R.mipmap.img_un_sell);
				if(bool_rent && bool_purchase)
					get_Transaction_type = "\"rent\", \"purchase\"";
				else if(bool_rent && bool_purchase == false)
					get_Transaction_type = "\"rent\"";
				else if(bool_rent == false && bool_purchase)
					get_Transaction_type = "\"purchase\"";
				else
					get_Transaction_type = "";
			}
			break;
		case R.id.rel_purchase:
			if(bool_purchase == false){
				bool_purchase = true;
				iv_purchase.setImageResource(R.mipmap.img_se_purchase);
				if(bool_rent && bool_sell)
					get_Transaction_type = "\"rent\", \"sale\", \"purchase\"";
				else if(bool_rent && bool_sell == false)
					get_Transaction_type = "\"rent\", \"purchase\"";
				else if(bool_rent == false && bool_sell)
					get_Transaction_type = "\"sale\", \"purchase\"";
				else
					get_Transaction_type = "\"purchase\"";
			}else{
				bool_purchase = false;
				iv_purchase.setImageResource(R.mipmap.img_un_purchase);
				if(bool_rent && bool_sell)
					get_Transaction_type = "\"rent\", \"sale\"";
				else if(bool_rent && bool_sell == false)
					get_Transaction_type = "\"rent\"";
				else if(bool_rent == false && bool_sell)
					get_Transaction_type = "\"sale\"";
				else
					get_Transaction_type = "";
			}
			break;
		case R.id.rel_commercial:
			if(bool_commercial == false){
				bool_commercial = true;
				iv_commercial.setImageResource(R.mipmap.img_se_commercial);
				if(bool_residential){
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_all);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"\"";
				}else{
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_commercial);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"commercial\"";
				}
			}else{
				bool_commercial = false;
				iv_commercial.setImageResource(R.mipmap.img_un_commercial);
				if(bool_residential){
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_residential);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"residential\"";
				}else{
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_all);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"\"";
				}
			}
			if(Utilities.D)Log.e(TAG, "getPropertyType=======commercial======="+getPropertyType);
			break;
		case R.id.rel_residential:
			if(bool_residential == false){
				bool_residential = true;
				iv_residential.setImageResource(R.mipmap.img_se_residential);
				if(bool_commercial){
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_all);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"\"";
				}else{
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_residential);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"residential\"";
				}
			}else{
				bool_residential = false;
				iv_residential.setImageResource(R.mipmap.img_un_residential);
				if(bool_commercial){
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_commercial);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"commercial\"";
				}else{
					ArrayAdapter<String> adapter_categoryCommercial = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_dropdown_item, str_category_all);
					spin_property.setAdapter(adapter_categoryCommercial);
					getPropertyType = "\"\"";
				}
			}
			if(Utilities.D)Log.e(TAG, "getPropertyType=======residential======="+getPropertyType);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		String item = parent.getItemAtPosition(position).toString();

		if(Utilities.D)Log.d("TAG", "spin_item===" + item);
		if(Utilities.D)Log.d("TAG", " position====" + position);

//		if (parent.getId() == R.id.spin_cityT) {
//
//			if(position == 0)
//				getCity = "";
//			else
//				getCity = item;
//			if(Utilities.D)Log.d("TAG", "getCity===" + getCity);
//		}

		if (parent.getId() == R.id.spin_propertyT) {

			if(position == 0){
				if(Utilities.D)Log.d(TAG, "getPropertyType,position=0");
//				getPropertyType = "\"\"";
			} else
				getPropertyType = "\"" + item + "\"";
			if(Utilities.D)Log.d("TAG", "getPropertyType===" + getPropertyType);
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
