package com.propkaro.facilitation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.propkaro.AppController;
import com.propkaro.AppController.TrackerName;
import com.propkaro.rssfeed.VideoViewActivity;
import com.propkaro.util.CircularProgressBar;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.GPSTracker;
import com.propkaro.util.Host;
import com.propkaro.util.PermissionsUtilities;
import com.propkaro.util.Utilities;
import com.propkaro.R;
public class FacilitationCenterFActivity extends AppCompatActivity {
	static final String TAG = FacilitationCenterFActivity.class.getSimpleName();
	int SDK_INT = Build.VERSION.SDK_INT;
	final Handler handler = new Handler();
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	ExpandableHeightListView mListView, mListView2;
	static FacilitationAdapter adapter, adapter2;
	SharedPreferences mPrefs;
	Context ctx;
	CircularProgressView progressView;
	List<FacilitationSetter> AvailList = new ArrayList<FacilitationSetter>();
	List<FacilitationSetter> AvailList_filter = new ArrayList<FacilitationSetter>();
	LinearLayout lnr_whatisfac_center;
	FragmentActivity fActivity;
	AutoCompleteTextView atv_location;
	ArrayAdapter<String> adapter_loc;
	GoogleMap googleMap;
	Circle mCircle;
	Marker mMarker;
	View rView;
	String getLocation = "";
	ImageView plus_sign30;
	static CircularProgressBar circular_progressBar;
	double getLat = 0.0 ,getLng = 0.0;
	Dialog dialog2 ;
Button btn_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Tracker t = ((AppController) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("About Facilitation Center");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		ctx = this;
		fActivity = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		Typeface RobotoLight = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf"); 

		setContentView(R.layout.activity_facilitation);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//        activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        getSupportActionBar().setTitle("Facilitation Center");


//		progressView = (CircularProgressView) findViewById(R.id.progressView);
//		Utilities.startAnimationThreadStuff(500, progressView);
//		progressView.setIndeterminate(true);
//		progressView.setVisibility(View.GONE);
		
		mListView = (ExpandableHeightListView) findViewById(R.id.list_faccility_center);
//		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		ViewGroup headerView = (ViewGroup)inflater.inflate(R.layout.android_facilitation_header, mListView, false);
//		mListView.addHeaderView(headerView, null, false);

		adapter = new FacilitationAdapter(fActivity, AvailList);
		mListView.setAdapter(adapter);
		mListView.setExpanded(true);

		// ///////////////////////
		mListView2 = (ExpandableHeightListView) findViewById(R.id.list_faccility_center2);

		adapter2 = new FacilitationAdapter(fActivity, AvailList_filter);
		mListView2.setAdapter(adapter2);
		mListView2.setExpanded(true);

		Utilities.setListViewHeightBasedOnChildren(mListView2);
		Utilities.setListViewHeightBasedOnChildren(mListView);
		// //////////////////////////////

		Button btn_whatisfaccentre = (Button) findViewById(R.id.btn_whatisfaccentre);
		btn_whatisfaccentre.setTypeface(RobotoLight);
		btn_whatisfaccentre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "onOptionMenu++++++++ clicks.........");
				String url_facilitation = "https://www.propkaro.com/deal-facilitation-center";
//				String url_facilitation = "http://www.propkaro.com/Property-Transaction/deal-facilitation-center";
				Intent i = new Intent(fActivity, VideoViewActivity.class);
				i.putExtra("getURL", url_facilitation);
				i.putExtra("getTitle", "Facilitation Center");
				fActivity.startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		 btn_location = (Button) findViewById(R.id.et_location);
		 btn_location.setTypeface(RobotoLight);
		 btn_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View loc) {
				openLocationDialog(btn_location, loc);
			}
		});
		try {
			initilizeMap(rView);

			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
//			googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(Location location) {
//                    float[] distance = new float[2];
//                    
//                    Log.e(TAG, "setOnMyLocationChangeListener===");
//                    /*
//                    Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
//                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
//                            */
//
//                    Location.distanceBetween( location.getLatitude(), location.getLongitude(),
//                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
//
//                    if( distance[0] > mCircle.getRadius() ){
//                        Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius() , Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
		} catch (Exception e) { e.printStackTrace(); }
		
		getLat = Double.parseDouble(mPrefs.getString("PREF_KEY_LATITUDE", "0.0"));
		getLng = Double.parseDouble(mPrefs.getString("PREF_KEY_LONGITUDE", "0.0"));
		
		if(PermissionsUtilities.getPermissionLocation(fActivity))
			getCurrentLocation();
		
		Log.e(TAG, "getLat: " + getLat + "/getLng: " + getLng );
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if(status == ConnectionResult.SUCCESS){
			if(Utilities.D)Log.v(TAG,"Google Play Services are available");
			if(mCircle != null){
				mCircle.remove();
			}
			LatLng latLng = new LatLng(getLat, getLng);
			mCircle = Utilities.mapCircle(googleMap, latLng, false);
//------------------------------------		
		if(Utilities.isInternetOn(ctx))
			new FacilitationTask().execute("");

		} else {
			if(Utilities.D)Log.v(TAG,"Google Play Services are not available");
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();
		}
	}//endOncrea
	
	private void getCurrentLocation() {
		try {
			GPSTracker gps = new GPSTracker(FacilitationCenterFActivity.this);
	        if(gps.canGetLocation()){
	        	getLat = gps.getLatitude();
	        	getLng = gps.getLongitude();
	        	SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_LATITUDE",  getLat + "");
				editor.putString("PREF_KEY_LONGITUDE", getLng + "");
				editor.commit();
	        }else{
	        	gps.showSettingsAlert();
	        }
		} catch (Exception e) { e.printStackTrace(); }
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public void openLocationDialog(View locView, final View v2) {

		dialog2 = new Dialog(ctx,android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge );
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.list_location_dialog);
//		dialog2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); 
//		dialog2.setCancelable(false);
		atv_location = (AutoCompleteTextView) dialog2.findViewById(R.id.et_location);
		atv_location.requestFocus();
		atv_location.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

				getLocation = "" + atv_location.getText().toString();
				getLocation = getLocation.replaceAll(" ", "%20").replaceAll("\\s+", "+");// type =
				String mapUrl = Host.AutoCompleteUrl + "input=" + getLocation.replaceAll(" ", "%20").replaceAll("\\s+", "+")
						+ "&key=" + Host.API_KEY + "&components=country:IN";
				
				if (Utilities.NetworkCheck(getApplicationContext()))
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
				if (Utilities.NetworkCheck(ctx))
					new PlacesTask().execute(goMapUrl);

				btn_location.setText(getLocation.replaceAll("%20", " "));
				Utilities.hideKeyboard(FacilitationCenterFActivity.this);
				dialog2.dismiss();
			}
		});
		dialog2.show();
	}
			
	@Override
	protected void onResume() {
		super.onResume();
	}

	void initilizeMap(View v) {
		if (googleMap == null) {
			googleMap = ((MapFragment) fActivity.getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(fActivity, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}

	class FacilitationTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(ctx, FacilitationTask.this);
			dialog.show();
			// progressView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
//			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
			FacilitationCenterFActivity.this.finish();
			if(Utilities.D)Log.v(TAG,"Request cancelled !");
		}
		
		@Override
		protected String doInBackground(String... f_url) {

			try {
				String str_send_json = "{\"table\":\"facilitations\",\"multiple\":\"all\"}";

				String UrlBase = Host.FacilitationCenterUrl + "";
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

						FacilitationSetter ch = new FacilitationSetter();
						ch.setTv_name(obj.getString("name"));
						ch.setTv_location(obj.getString("address"));
						ch.setLat(obj.getString("lat"));
						ch.setLng(obj.getString("lng"));
						ch.setLink(obj.getString("link"));
						ch.setContact(obj.getString("contact"));

						AvailList.add(ch);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			// progressView.setVisibility(View.GONE);
			dialog.dismiss();
			
			if (api_code == 200) {
				adapter.notifyDataSetChanged();
				googleMap.clear();
				LatLng latLng = new LatLng(getLat, getLng);
				Log.e(TAG, "Current_latLng====" + latLng);
//				if(latLng != null)
				if(mCircle != null){
					mCircle.remove();
				}
				mCircle = Utilities.mapCircle(googleMap, latLng, false);
				AvailList_filter.clear();
				for (int i = 0; i < AvailList.size(); i++) {
					 LatLng latLngList = new LatLng(
							 Double.parseDouble(AvailList.get(i).getLat()), 
							 Double.parseDouble(AvailList.get(i).getLng()));
					 mMarker = Utilities.mapMarker(googleMap, latLngList, false, AvailList.get(i).getTv_name());
					 if(Utilities.getCircleMarker(mMarker, mCircle)){
						AvailList_filter.add(AvailList.get(i));
					}
				}
				Log.e(TAG, "AvailList_filter.size(): " + AvailList_filter.size());
				adapter2.notifyDataSetChanged();
			} else {
			}
		}
	}
	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		// outState.putInt("currentColor", currentColor);
//		outState.putBoolean("category_frag_added", true);
//	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			// currentColor = savedInstanceState.getInt("currentColor");
			// changeColor(currentColor);
			// ToastHelper.Custom(ctx,
			// "Current page size is saved.", 1);
			if(Utilities.D)Log.v(TAG,"page_size_is_saved");
		}
	}

	@Override
	protected void onStop() {
		super.onStop(); // To change body of overridden methods use File |
						// Settings | File Templates.
		GoogleAnalytics.getInstance(FacilitationCenterFActivity.this).reportActivityStop(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(FacilitationCenterFActivity.this).reportActivityStart(this);
	}

	Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		final String[] TITLES = { "Availability", "Requirement" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			// if(position==0)
			// f = new TCategoryFeaturedFragment();
			Fragment f = new Fragment();
			switch (position) {

			case 0:
				// return f = new FC_fragment();

			default:
				break;
			}
			// else if(position==2)
			// f = new TCategorySpecialFragment();
			return f;
		}
	}

	ActionMode currentActionMode;
	ActionMode.Callback modeCallBack = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Actions");
			mode.getMenuInflater().inflate(R.menu.action_mode, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_edit:
				Toast.makeText(FacilitationCenterFActivity.this, "Editing!",
						Toast.LENGTH_SHORT).show();
				mode.finish(); // Action picked, so close the contextual menu

				return true;
			case R.id.menu_delete:
				mode.finish();

				return true;
			default:

				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			currentActionMode = null; // Clear current action mode
		}
	};

	// --------------------------------------------END
	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			intent.getStringExtra(SearchManager.QUERY);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish(); overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			return true;
		}
		return super.onKeyDown(keyCode, event);
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
						ctx, android.R.layout.simple_dropdown_item_1line, places);
				atv_location.setAdapter(adapter_loc);
//				atv_location.requestFocus();
				adapter_loc.notifyDataSetChanged();
			}
		}
	}

	class PlacesTask extends AsyncTask<String, String, String> {
		
		String name1, address1;
		double api_latitude = 0, api_longitude = 0;

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

				api_latitude = Double.parseDouble(locajson.getString("lat"));
				api_longitude = Double.parseDouble(locajson.getString("lng"));

				// CameraPosition cameraPosition = new CameraPosition.Builder()
				// .target(new LatLng(randomLocation[0],
				// randomLocation[1])).zoom(15).build();
				//
				// googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				// -------------------------------------------------------------------------------------------------
				// picurl=jsonresult.getString("icon");*/
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
//			Utilities.mapMarker(googleMap, new LatLng(api_latitude, api_longitude), false, name1);
			googleMap.clear();
			if(mCircle != null){
				mCircle.remove();
			}
			mCircle = Utilities.mapCircle(googleMap, new LatLng(api_latitude, api_longitude), false);
			AvailList_filter.clear();
			for (int i = 0; i < AvailList.size(); i++) {
				 LatLng latLngList = new LatLng(
						 Double.parseDouble(AvailList.get(i).getLat()), 
						 Double.parseDouble(AvailList.get(i).getLng()));
				 mMarker = Utilities.mapMarker(googleMap, latLngList, false, AvailList.get(i).getTv_name());
				 if(Utilities.getCircleMarker(mMarker, mCircle)){
						AvailList_filter.add(AvailList.get(i));
				 }
			}
			Log.e(TAG, "AvailList_filter.size(): " + AvailList_filter.size());
			adapter2.notifyDataSetChanged();
		}
	}

	class GetCurrentLocationTask extends AsyncTask<String, String, String> {
		String api_message = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				GPSTracker gps = new GPSTracker(FacilitationCenterFActivity.this);
		        if(gps.canGetLocation()){
		        	getLat = gps.getLatitude();
		        	getLng = gps.getLongitude();
		        	SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("PREF_KEY_LATITUDE",  getLat + "");
					editor.putString("PREF_KEY_LONGITUDE", getLng + "");
					editor.commit();
		        }else{
		        	gps.showSettingsAlert();
		        }
			} catch (Exception e) { e.printStackTrace(); }
			return api_message;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			if(Utilities.D)Log.d(TAG, "Location Found !" + getLat + " " + getLng);
//			Utilities.Snack(mCoordinator, "Location Found !");

			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
			if(status == ConnectionResult.SUCCESS){
				if(Utilities.D)Log.v(TAG,"Google Play Services are available");
				if(mCircle != null){
					mCircle.remove();
				}
				LatLng latLng = new LatLng(getLat, getLng);
				mCircle = Utilities.mapCircle(googleMap, latLng, false);
	//------------------------------------		
			} else{			
				if(Utilities.D)Log.v(TAG,"Google Play Services are not available");
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, fActivity, requestCode);
		        dialog.show();
			}
		}
	}
}
