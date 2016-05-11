package com.propkaro.mandate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.propkaro.R;
import com.propkaro.propertycenter.KeyPairBoolData;
import com.propkaro.propertycenter.MultiSpinnerSearchFilter;
import com.propkaro.propertycenter.MultiSpinnerSearchFilter.MultiSpinnerSearchListener;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class MandateFragment extends Fragment {
	private static final String TAG = MandateFragment.class.getSimpleName();
	public static ArrayList<MandateSetter> AvailList = new ArrayList<MandateSetter>();
	private int total_pages = 0, current_page = 1,  mPrevTotalItemCount = 0, totalItemCount = 0;
	int pastVisiblesItems, visibleItemCount;
	private boolean isLoadingMore = false;
	private List<KeyPairBoolData> citiesArray = new ArrayList<KeyPairBoolData>();
	String api_allCities = "";
	MultiSpinnerSearchFilter BTN_CITY;
	EditText mSearchEdiText;
	ArrayList<String> api_city = new ArrayList<String>();
	double api_latitude = 0, api_longitude = 0;
	TinyDB db;
	RelativeLayout rel_nodata;
	TextView tv_nodata;
	ListView mListView;
	String temp_city = "0";
	ArrayAdapter<String> adapter_cities;
	static boolean isShowReview = true;
	ViewPager mPager;
	public static MandateRecyclerView adapter;

	private RecyclerView mRecyclerView;
	LinearLayoutManager mLayoutManager;
	private Context ctx;
	private String getMobile_num = "", getEmailID = "", getUserType = "",
			getUserDataID = "", getUserName = "", getFName = "", getLName = "",
			getCity = "", getProfileImage = "";
	CircularProgressView progressView;
	private SharedPreferences mPrefs;
	private CoordinatorLayout mCoordinator;
	private static final String KEY_POSITION = "MandateFragment:Position";
	static int POSITION;
	String API_MANDAT_FEED = 
				"{\"by\":" +
				"{" + 
				"\"property_city\":[\"" 
	//					+ "Faridabad" 
				+ "\"]" + 
				"\"property_listing_parent\":[\"" 
	//			+ "availability" 
				+ "\"]" + 
				"\"property_listing_type\":[\"" 
			//	+ "Sale" 
				+ "\"]" + 
				"\"property_type_name_parent\":[\"" 
				//+ "Residential" 
				+ "\"]" + 
//			"\"keywords\":\"" + "" + "\"" + 
//			"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
//			"\"user_id\":\"" + "20" + "\"" + 
					"}, " +
//					"\"auth_id\":\"" + GridActivity.getUserDataID + "\"" + 
					"}";//SearchUrl
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 ctx= getActivity(); 
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		api_allCities = mPrefs.getString("PREF_KEY_CITIES_10", "");
		db = new TinyDB(ctx);
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rView = inflater.inflate(R.layout.mandate_1x,container, false);
		mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		progressView = (CircularProgressView) rView	.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);
		Button btn_mandated = (Button) rView.findViewById(R.id.btn_mandated);
		btn_mandated.setOnClickListener(new OnClickListener() {
			//
			@Override
			public void onClick(View v) {
				//
				Intent i = new Intent(ctx, PagerActivity.class);
				startActivity(i);
			}
		});
		
		tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		rel_nodata = (RelativeLayout)rView.findViewById(R.id.rel_nodata);
		rel_nodata.setVisibility(View.GONE);
		rel_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				if(Utilities.isInternetOn(ctx)) {
					new MandateTask().execute(API_MANDAT_FEED);
				} else {
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("No Internet Connection, try again ?");
				}
			}
		});
		mRecyclerView = (RecyclerView) rView.findViewById(R.id.list_mandate);
		mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	    adapter = new MandateRecyclerView( getActivity(), AvailList);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		        if(dy > 0) {
		            visibleItemCount = mLayoutManager.getChildCount();
		            totalItemCount = mLayoutManager.getItemCount();
		            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

		            if (isLoadingMore  && current_page <= total_pages) {
		                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
							if(Utilities.isInternetOn(ctx))
								new MandateTask().execute(API_MANDAT_FEED);
		                }
		            }
		        }
		    }
		});
		
		BTN_CITY = (MultiSpinnerSearchFilter) rView.findViewById(R.id.spin_city_type);
//		BTN_CITY.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				openCityDialog(BTN_CITY, v);
//			}
//		});
		int[] colors = { android.R.color.holo_blue_dark,
				android.R.color.holo_red_dark, android.R.color.holo_green_dark,
				android.R.color.holo_orange_dark, android.R.color.holo_purple,
				android.R.color.holo_red_dark, android.R.color.holo_green_dark,
				android.R.color.holo_orange_dark, };
		
		if(Utilities.isInternetOn(ctx)){
			current_page = 1;
			AvailList.clear();
			new MandateTask().execute(API_MANDAT_FEED);
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
				    BTN_CITY.setVisibility(View.VISIBLE);
					BTN_CITY.setItems(citiesArray, "Select City", -1, new MultiSpinnerSearchListener() {
						
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
							String	API_MANDAT_CITY_FEED = "{\"by\":{\"property_city\":[" + getCity + "]}}";
							if(Utilities.isInternetOn(getActivity())){
								current_page = 1;
								AvailList.clear();
								adapter.notifyDataSetChanged();
								new MandateTask().execute(API_MANDAT_CITY_FEED);
							}
						}
					});
				}

			} catch (Exception e) { e.printStackTrace(); }
		}
		if (AvailList.isEmpty()) {
			if(Utilities.isInternetOn(ctx)){
				current_page = 1;
				new MandateTask().execute(API_MANDAT_FEED);
			} else {
				rel_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("No Internet Connection, try again ?");
			}
		}
		
		return rView;
	}
	public static MandateFragment newInstance(int pos) {
		MandateFragment fragment = new MandateFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		MandateFragment.POSITION = pos;
		Log.e(TAG, "Position=" + pos);

		return fragment;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(db.getBoolean("shouldReload", false)){
			if(Utilities.isInternetOn(ctx)){
				current_page = 1;
				AvailList.clear();
				new MandateTask().execute(API_MANDAT_FEED);
			} 
		}
	}
	class MandateTask extends AsyncTask<String, String, String> {
		int api_code = 0, api_total = 0,listing_value = 0,broker_revenue = 0;
		public String api_message = "", api_data = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(Utilities.D)Log.e(TAG, "---------------------------check");
			isLoadingMore = false;
			progressView.setVisibility(View.VISIBLE);
			rel_nodata.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String UrlBase = Host.MandateUrl + "page/" + current_page + "/"; ;
				String jsonString = Utilities.sendData(UrlBase, f_url[0]);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_total = meta.getInt("count");
					total_pages = api_total;
					listing_value = meta.getInt("listing_value");
					broker_revenue = meta.getInt("broker_revenue");
	                
//					if (api_lastProperty > OFFSET)
//	                    OFFSET = api_lastProperty;
	                
//					if(Utilities.D)Log.v(TAG,"offSet-=" + OFFSET);
					
					JSONArray data = (JSONArray) reader.get("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						Host.parseMandate(ctx, obj, AvailList, false);
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return f_url[0];
		}

		@Override
		protected void onPostExecute(String file_url) {
			
			progressView.setVisibility(View.GONE);
			db.putBoolean("shouldReload", false);
			isLoadingMore = true;

			if (api_code == 200) {
				if(AvailList.isEmpty()){
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("No listings found !");
				}
				mPrevTotalItemCount = totalItemCount;
				current_page = current_page + 1;
				adapter.notifyDataSetChanged();
				

			} else if(api_code == 400){
				isLoadingMore = false;
				if(AvailList.isEmpty()){
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("No listings found !");
				}
				Utilities.Snack(mCoordinator, api_message);
			} else {
				if(AvailList.isEmpty()){
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("Something went wrong, please try again !");
				}
				Utilities.Snack(mCoordinator, "Something went wrong !");
			}
		}
	}//endAvaillist
	
	class citiesTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			BTN_CITY.setVisibility(View.GONE);
		    
			dialog = Utilities.showProgressDialog(getActivity(), citiesTask.this);
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
				    BTN_CITY.setVisibility(View.VISIBLE);
					BTN_CITY.setItems(citiesArray, "Select City", -1, new MultiSpinnerSearchListener() {
						
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
							String	API_MANDAT_CITY_FEED = "{\"by\":{\"property_city\":[" + getCity + "]}}";
							if(Utilities.isInternetOn(getActivity())){
								current_page = 1;
								AvailList.clear();
								adapter.notifyDataSetChanged();
								new MandateTask().execute(API_MANDAT_CITY_FEED);
							}
						}
					});
				}
			} else {
				Toast.makeText(ctx, "No cities found !", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
