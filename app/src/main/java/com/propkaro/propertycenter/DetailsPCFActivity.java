package com.propkaro.propertycenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.MainActivity;
import com.propkaro.R;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

public class DetailsPCFActivity extends AppCompatActivity {
	private static final String TAG = DetailsPCFActivity.class.getSimpleName();
	private Context ctx;
	private GoogleMap googleMap;
	private String getUserId = "";
	private LinearLayout lnr_call,  lnr_msg, lnr_pin;
	private ImageView iv_thumbnail,iv_image;
	DisplayImageOptions options;

	private RelativeLayout rl_layout;
	private LinearLayout lnr_btns,lnr_descirpin;
	private TextView tv_no_of_bathrooms, tv_property_description,tv_no_of_balcony, tv_no_of_washrooms,tv_locality_proj_name,
			tv_area, tv_property_maintenance_amount, tv_property_possession,tv_amenities,
			tv_property_furnishing_type, tv_transaction_type,tv_property_type_name_parent,
			tv_property_ownership, tv_property_availability, tv_postedTime,
			tv_property_listing_type, tv_property_city, tv_property_landmark,tv_dessc,
			tv_bedrooms, tv_title, tv_postedBy, tv_userType,textView_pc,tv_bath,
			tv_expected_unit_price, tv_expected_unit_price_unit,tv_bal,tv_mnt,
			tv_property_type_name,tv_titlebarText,tv_ContractType,tv_UnitPrice;

	private List<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	private FragmentActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this; activity = this;
		setContentView(R.layout.detail_property_center);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
//                onBackPressed();
            	ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
            	List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

            	if(taskList.get(0).numActivities > 1){
        			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            	} else {
            		Intent i = new Intent(DetailsPCFActivity.this, MainActivity.class);
            		startActivity(i);finish();
            	}
            }
        });
        getSupportActionBar().setTitle("Property Detail");
		Typeface RobotoLight = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Roboto-Light.ttf"); 

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.home)
		.showImageForEmptyUri(R.mipmap.home)
		.showImageOnFail(R.mipmap.home)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();

		getUserId = getIntent().getExtras().getString("KEY_PROPERTY_ID");
		if(Utilities.D)Log.v(TAG,"getUserId===" + getUserId);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_postedBy = (TextView) findViewById(R.id.tv_postedBy);
		tv_postedBy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((AvailList.get(0).getUSER_ID() + "").length() != 0){
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", AvailList.get(0).getUSER_ID() + "");
					activity.startActivity(i);  activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		tv_userType = (TextView) findViewById(R.id.tv_userType);
		tv_amenities= (TextView) findViewById(R.id.tv_amenities);
		tv_locality_proj_name = (TextView) findViewById(R.id.tv_locality_proj_name);
		tv_no_of_bathrooms = (TextView) findViewById(R.id.tv_no_of_bathrooms);
		tv_bal = (TextView) findViewById(R.id.tv_bal);
		tv_property_description= (TextView) findViewById(R.id.tv_property_description);
		tv_bedrooms = (TextView) findViewById(R.id.tv_no_of_bedrooms);
		tv_area = (TextView) findViewById(R.id.tv_area);
		// tv_prop_area_top_addr = (TextView)findViewById(R.id.tv_prop_area_top_addr);
		tv_no_of_balcony = (TextView) findViewById(R.id.tv_no_of_balcony);
		tv_mnt= (TextView) findViewById(R.id.tv_mnt);
		tv_no_of_washrooms = (TextView) findViewById(R.id.tv_no_of_washrooms);
		tv_property_maintenance_amount = (TextView) findViewById(R.id.tv_property_maintenance_amount);
		tv_property_possession = (TextView) findViewById(R.id.tv_property_possession);
		tv_property_furnishing_type = (TextView) findViewById(R.id.tv_property_furnishing_type);
		tv_transaction_type = (TextView) findViewById(R.id.tv_transaction_type);
		tv_property_ownership = (TextView) findViewById(R.id.tv_property_ownership);
		tv_property_availability = (TextView) findViewById(R.id.tv_property_availability);
		tv_property_listing_type = (TextView) findViewById(R.id.tv_listed_user_id);
		tv_postedTime = (TextView) findViewById(R.id.tv_postedTime);
		tv_ContractType= (TextView) findViewById(R.id.tv_ContractType);
		tv_UnitPrice= (TextView) findViewById(R.id.tv_UnitPrice);
		tv_bath= (TextView) findViewById(R.id.tv_bath);
		tv_property_city = (TextView) findViewById(R.id.tv_property_city);
		tv_property_landmark = (TextView) findViewById(R.id.tv_property_landmark);
		tv_dessc = (TextView) findViewById(R.id.tv_dessc);
		tv_expected_unit_price = (TextView) findViewById(R.id.tv_expected_unit_price);
		tv_expected_unit_price_unit = (TextView) findViewById(R.id.tv_expected_unit_price_unit);
		tv_property_type_name = (TextView) findViewById(R.id.tv_property_type_name);
		tv_property_type_name_parent= (TextView) findViewById(R.id.tv_property_type_name_parent);
		iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
		iv_thumbnail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((AvailList.get(0).getUSER_ID() + "").length() != 0){
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", AvailList.get(0).getUSER_ID() + "");
					activity.startActivity(i);  activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		iv_image = (ImageView) findViewById(R.id.iv_image);

		textView_pc = (TextView) findViewById(R.id.textView_pc);
    	textView_pc.setText("");
    	textView_pc.setVisibility(View.GONE);
    	    
//    	tv_title.setTypeface(RobotoLight);
    	tv_property_landmark .setTypeface(RobotoLight);
    	tv_postedTime .setTypeface(RobotoLight);
    	tv_expected_unit_price.setTypeface(RobotoLight);
    	tv_area .setTypeface(RobotoLight);
    	tv_expected_unit_price_unit .setTypeface(RobotoLight);
    	tv_property_type_name.setTypeface(RobotoLight);
    	tv_bedrooms.setTypeface(RobotoLight);
    	tv_bath .setTypeface(RobotoLight);
    	tv_ContractType.setTypeface(RobotoLight);
    	tv_UnitPrice.setTypeface(RobotoLight);
    	tv_no_of_bathrooms.setTypeface(RobotoLight);
    	tv_bal.setTypeface(RobotoLight);
    	tv_no_of_balcony.setTypeface(RobotoLight);
    	tv_no_of_washrooms.setTypeface(RobotoLight);
    	tv_mnt.setTypeface(RobotoLight);
    	tv_property_description.setTypeface(RobotoLight);
//    	tv_dessc.setTypeface(RobotoLight);
    	
		rl_layout = (RelativeLayout) findViewById(R.id.rl_1);
		rl_layout.setVisibility(View.GONE);
		lnr_btns = (LinearLayout) findViewById(R.id.lnr_tab);
		lnr_btns.setGravity(Gravity.CENTER);
		lnr_btns.setWeightSum(4);
		lnr_btns.setVisibility(View.GONE);
		lnr_descirpin=  (LinearLayout) findViewById(R.id.lnr_descirpin);
		if (Utilities.isInternetOn(ctx));
			new DETAILS_PC_Task().execute(getUserId);

		try {
			initilizeMap();

			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

//			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		lnr_call = (LinearLayout) findViewById(R.id.lnr_call);
		lnr_call.setGravity(Gravity.CENTER);
		lnr_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if (AvailList.get(0).getTv_userType().equals("individual")) {
//					Toast.makeText(ctx, "Undable to call", Toast.LENGTH_SHORT).show();
//				} else{
					if (AvailList.get(0).getPhone_no().isEmpty()) {
						Toast.makeText(ctx, "Number not found !",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:"
								+ AvailList.get(0).getPhone_no()));
						startActivity(intent);
					}
				}
//		}
		});
		lnr_msg = (LinearLayout) findViewById(R.id.lnr_msg);
		lnr_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PackageManager pm = activity.getPackageManager();
				try {
					Intent waIntent = new Intent(Intent.ACTION_SEND);
					waIntent.setType("text/plain");

					String text = 
							"\uD83D\uDC49"
							+ " "
                           + AvailList.get(0).getTv_proprty_listing_parent()//Availability  \uD83D for pin red
                           + " for "
                           + (AvailList.get(0).getProperty_listing_type()//Sale
//                           +"\u260e"
                           + " \n"
                           + " \n"
//                           +"\uDCCC"
                           + "\uD83C\uDFE0"
                           + " "
                           + AvailList.get(0).getProperty_type_name()//Residential
                           + " \n"
                           + " \n"
                           +"\uD83D\uDCB0"
                           + " "
		                       + AvailList.get(0).getTv_rate()
//		                       + " @ "
//		                       + m.getAdd_price()
//		                       + " / "
//		                       + m.getAdd_price_unit()
                           + " \n"
                           + " \n"
//                           + " of "
                           + "\uD83D\uDCCC"
                           + " "
                           + AvailList.get(0).getProperty_landmark()
                           + " \n"
                           + " \n"
                           +"\u260e"
                           + " "
                           + "Call : "
                           + " "
                           + AvailList.get(0).getTv_postedBy()
                           + " "
                           + "@ "
                           + AvailList.get(0).getPhone_no()
                           + "\n"
                           + " \nhttps://play.google.com/store/apps/details?id=com.propkaro"
	                   );
//					PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
					// Check if package exists or not. If not then code
					// in catch block will be called
//					waIntent.setPackage("com.whatsapp");

					waIntent.putExtra(Intent.EXTRA_TEXT, text);
					activity.startActivity(Intent.createChooser(waIntent, "Share with"));

				} catch (Exception e) {e.printStackTrace();}
			}
		});

		lnr_pin = (LinearLayout) findViewById(R.id.lnr_pin);
		lnr_pin.setVisibility(View.GONE);
		lnr_pin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});

	}// oncreaview

	public void onClickWhatsApp(View view) {
		PackageManager pm = getPackageManager();
		try {
			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			String text = 
					AvailList.get(0)
					.getTv_proprty_listing_parent()
					+ " of "
					+ AvailList.get(0).getProperty_type_name()
					+ " for "
					+ (AvailList.get(0).getProperty_listing_type() + " in " + AvailList
							.get(0).getProperty_landmark())
							+ " \nhttps://play.google.com/store/apps/details?id=com.propkaro";
			
			PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
			// Check if package exists or not. If not then code
			// in catch block will be called
			waIntent.setPackage("com.whatsapp");

			waIntent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(waIntent, "Share with"));

		} catch (NameNotFoundException e) {
			Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * creating random position around a location for testing purpose only
	 */
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);

		if (keyCode == KeyEvent.KEYCODE_BACK) {
        	ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        	List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        	if(taskList.get(0).numActivities > 1){
    			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        	} else {
        		Intent i = new Intent(DetailsPCFActivity.this, MainActivity.class);
        		startActivity(i);finish();
        	}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class DETAILS_PC_Task extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(ctx, DETAILS_PC_Task.this);
			dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String api_availability = "{\"by\":{" +
				// "\"property_listing_parent\":\"" + "Availability" + "\"" +
						"\"property_id\":\"" + f_url[0] + "\"" +
						// "\"user_id\":\"" + "20" + "\"" +
						"}}";// SearchUrl

				String str_send_json = "{\"by\":{\"property_id\":\"" + f_url[0]
						+ "\"},\"table\":\"properties\",\"multiple\":\"1\"}";

				// String str_send_json =
				// "{\"table\":\"properties\",\"multiple\":\"all\"}";

				String UrlBase = Host.SearchUrl;
				String jsonString = Utilities.sendData(UrlBase,
						api_availability);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					AvailList.clear();

					JSONArray jsonArray = (JSONArray) reader.get("data");
					JSONObject obj = jsonArray.getJSONObject(0);

					AvailPCSetter ch = new AvailPCSetter();

					if (obj.getString("user_id").equals("")
							|| obj.getString("user_id").equals(null)
							|| obj.getString("user_id").equals("null")) {
					} else {
						ch.setUSER_ID(Integer.parseInt(obj.getString("user_id")));
					}
					if (obj.getString("property_id").equals("")
							|| obj.getString("property_id").equals(null)
							|| obj.getString("property_id").equals("null")) {
					} else {
						ch.setPROPERTY_ID(Integer.parseInt(obj
								.getString("property_id")));
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
							|| obj.getString("property_added_on")
									.equals("null")) {
						ch.setTv_postedTime(" -- ");
					} else {
						ch.setTv_postedTime(obj.getString("property_added_on"));
					}

					if (obj.getString("property_location").equals("")
							|| obj.getString("property_location").equals(null)
							|| obj.getString("property_location")
									.equals("null")) {
						ch.setTv_location(" -- ");
					} else {
						ch.setTv_location(obj.getString("property_location"));
					}

					String temp_area = " -- ";
					if (!(obj.getString("property_super_area")).isEmpty()
							&& !(obj.getString("property_super_area")
									.equals("null"))) {
						temp_area = obj.getString("property_super_area") + " "
								+ obj.getString("property_super_area_unit");
						ch.setTv_area(temp_area);
					} else if (!(obj.getString("property_built_area")).isEmpty()
							&& !(obj.getString("property_built_area")
									.equals("null"))) {
						temp_area = obj.getString("property_built_area") + " "
								+ obj.getString("property_built_area_unit");
						ch.setTv_area(temp_area);
					} else if (!(obj.getString("property_plot_area")).isEmpty()
							&& !(obj.getString("property_plot_area")
									.equals("null"))) {
						temp_area = obj.getString("property_plot_area") + " "
								+ obj.getString("property_plot_area_unit");
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
					// -----------------
					if (obj.getString("no_of_bathrooms").equals("")
							|| obj.getString("no_of_bathrooms").equals(null)
							|| obj.getString("no_of_bathrooms").equals("null")) {
						ch.setNo_of_bathrooms(" -- ");
					} else {
						ch.setNo_of_bathrooms(obj.getString("no_of_bathrooms"));
					}
					/////////////////
					if (obj.getString("no_of_bedrooms").contains("Select")){
						ch.setNo_of_bedrooms(" -- ");
					}
					if (obj.getString("no_of_bedrooms").equals("")
							|| obj.getString("no_of_bedrooms").equals(null)
							|| obj.getString("no_of_bedrooms").equals("Select")) {
						ch.setNo_of_bedrooms(" -- ");
					} else {
						ch.setNo_of_bedrooms(obj.getString("no_of_bedrooms"));
					}
					if (obj.getString("property_description").equals("")
							|| obj.getString("property_description").equals(null)
							|| obj.getString("property_description").equals("null")) {
						ch.setProperty_description(" -- ");
					} else {
						ch.setProperty_description(obj.getString("property_description"));
					}
					
					if (obj.getString("property_location").equals("")
							|| obj.getString("property_location").equals(null)
							|| obj.getString("property_location").equals("null")) {
						ch.setProperty_location(" -- ");
					} else {
						ch.setProperty_location(obj.getString("property_location"));
					}
					if (obj.getString("no_of_washrooms").equals("")
							|| obj.getString("no_of_washrooms").equals(null)
							|| obj.getString("no_of_washrooms").equals("null")) {
						ch.setNo_of_washrooms(" -- ");
					} else {
						ch.setNo_of_washrooms(obj.getString("no_of_washrooms"));
					}
					if (obj.getString("amenities").equals("")
							|| obj.getString("amenities").equals(null)
							|| obj.getString("amenities").equals("null")) {
						ch.setAmenities(" -- ");
					} else {
						ch.setAmenities(obj.getString("amenities"));
					}
					
					
					if (obj.getString("no_of_balcony").equals("")
							|| obj.getString("no_of_balcony").equals(null)
							|| obj.getString("no_of_balcony").equals("null")) {
						ch.setNo_of_balcony(" -- ");
					} else {
						ch.setNo_of_balcony(obj.getString("no_of_balcony"));
					}
					// -------------------------
					
					if (obj.getString("property_maintenance_frequency").equals("")
							|| obj.getString("property_maintenance_frequency").equals(null)
							|| obj.getString("property_maintenance_frequency").contains("null")) {
						ch.setProperty_maintenance_frequency("");
					} else {
						ch.setProperty_maintenance_frequency(obj.getString("property_maintenance_frequency"));
					}
					if (obj.getString("property_maintenance_amount").equals("")
							|| obj.getString("property_maintenance_amount")
									.equals(null)
							|| obj.getString("property_maintenance_amount")
									.equals("null")) {
						ch.setProperty_maintenance_amount(" -- ");
					} else {
						ch.setProperty_maintenance_amount(obj.getString("property_maintenance_amount"));
					}
					
					if (obj.getString("property_possession").contains("Select")){
						ch.setProperty_possession(" -- ");
					}
					if (obj.getString("property_possession").equals("")
							|| obj.getString("property_possession")
									.equals(null)
							|| obj.getString("property_possession").equals(
									"Select")) {
						ch.setProperty_possession(" -- ");
					} else {
						ch.setProperty_possession(obj
								.getString("property_possession"));
					}
					if (obj.getString("property_furnishing_type").contains("Select")){
						ch.setProperty_furnishing_type(" -- ");
					}

					if (obj.getString("property_furnishing_type").equals("")
							|| obj.getString("property_furnishing_type")
									.equals(null)
							|| obj.getString("property_furnishing_type")
									.equals("Select")) {
						ch.setProperty_furnishing_type(" -- ");
					} else {
						ch.setProperty_furnishing_type(Utilities.makeFirstLetterCaps(obj
								.getString("property_furnishing_type")));
					}
					
					if (obj.getString("transaction_type").contains("Select")){
						ch.setTransaction_type(" -- ");
					}

					if (obj.getString("transaction_type").equals("")
							|| obj.getString("transaction_type").equals(null)
							|| obj.getString("transaction_type").equals("Select")) {
						ch.setTransaction_type(" -- ");
					} else {
						ch.setTransaction_type(obj
								.getString("transaction_type"));
					}

					if (obj.getString("add_price").equals("")
							|| obj.getString("add_price").equals(null)
							|| obj.getString("add_price").equals(
									"null")) {
						ch.setAdd_price(" -- ");
					} else {
						ch.setAdd_price(obj.getString("add_price"));
					}
					if (obj.getString("property_ownership").contains("Select")){
						ch.setProperty_ownership(" -- ");
					}
					
					if (obj.getString("property_ownership").equals("")
							|| obj.getString("property_ownership").equals(null)
							|| obj.getString("property_ownership").equals(
									"Select")) {
						ch.setProperty_ownership(" -- ");
					} else {
						ch.setProperty_ownership(obj
								.getString("property_ownership"));
					}
					if (obj.getString("property_availability").contains("Select")){
						ch.setProperty_availability(" -- ");
					}

					if (obj.getString("property_availability").equals("")
							|| obj.getString("property_availability").equals(
									null)
							|| obj.getString("property_availability").equals(
									"Select")) {
						ch.setProperty_availability(" -- ");
					} else {
						ch.setProperty_availability(obj
								.getString("property_availability"));
					}

					if (obj.getString("property_listing_type").equals("")
							|| obj.getString("property_listing_type").equals(
									null)
							|| obj.getString("property_listing_type").equals(
									"null")) {
						ch.setProperty_listing_type(" -- ");
					} else {
						ch.setProperty_listing_type(obj
								.getString("property_listing_type"));
					}
					// ------------------------
					if (obj.getString("property_description").equals("")
							|| obj.getString("property_description").equals(
									null)
							|| obj.getString("property_description").equals(
									"null")) {
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
							|| obj.getString("property_landmark")
									.equals("null")) {
						ch.setProperty_landmark(" -- ");
					} else {
						ch.setProperty_landmark(obj.getString("property_landmark"));
					}
					// ------------------------------
					if (obj.getString("expected_price").equals("")
							|| obj.getString("expected_price").equals(null)
							|| obj.getString("expected_price").equals("null")) {
						ch.setExpected_unit_price(" -- ");
						ch.setTv_rate(" -- ");
					} else {
						String price = (Host.rupeeFormat(obj.getString("expected_price")));
						ch.setExpected_unit_price("" + price);
						ch.setTv_rate("" + price);
					}
///////////////////////Sid currency////////////////
					if (obj.getString("expected_unit_price").equals("")
							|| obj.getString("expected_unit_price").equals(null)
							|| obj.getString("expected_unit_price")
									.equals("null")) {
						ch.setExpected_unit_price_unit(" -- ");
					} else {
						ch.setExpected_unit_price_unit(obj.getString("expected_unit_price")+ " / "
								+ obj.getString("expected_unit_price_unit"));
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
							|| obj.getString("property_type_parent").equals(
									null)
							|| obj.getString("property_type_parent").equals(
									"null")) {
						ch.setProperty_type_parent(" -- ");
					} else {
						ch.setProperty_type_parent(obj
								.getString("property_type_parent"));
					}
					if (obj.getString("property_area").equals("")
							|| obj.getString("property_area").equals(null)
							|| obj.getString("property_area").equals("null")) {
						ch.setProperty_area(" -- ");
					} else {
						ch.setProperty_area(obj.getString("property_area"));
					}
					// ----------------------------------
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
							|| obj.getString("fname").equals("null")) {
						ch.setTv_postedBy(" -- ");
					} else {
						ch.setTv_postedBy(Utilities.makeFirstLetterCaps(obj.getString("fname"))
								+ " "
								+ Utilities.makeFirstLetterCaps(obj.getString("lname")));
					}
					if (obj.getString("user_type").equals("")
							|| obj.getString("user_type").equals(null)
							|| obj.getString("user_type").equals("null")) {
						ch.setTv_userType(" -- ");
					} else {
						ch.setTv_userType(Utilities.makeFirstLetterCaps(obj.getString("user_type")));
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
					// ------------------------------added // extras------------------------------------
					if (obj.getString("use_company_name").equals("")
							|| obj.getString("use_company_name").equals(null)
							|| obj.getString("use_company_name").equals("null")) {
						ch.setUse_company_name("0");
					} else {
						ch.setUse_company_name(obj
								.getString("use_company_name"));
					}
					if (obj.getString("company_name").equals("")
							|| obj.getString("company_name").equals(null)
							|| obj.getString("company_name").equals("null")) {
						ch.setCompany_name(" -- ");
					} else {
						ch.setCompany_name(obj.getString("company_name"));
					}

					ch.setColor(Utilities.getRandomColor(ctx, (obj.getString("fname"))));
					
					AvailList.add(ch);
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
				if(Utilities.D)Log.v(TAG,"onPostExecute().Success...");

				rl_layout.setVisibility(View.VISIBLE);
				lnr_btns.setVisibility(View.VISIBLE);
//				AvailList.get(0)
				if(AvailList.get(0).getThumbnailUrl().contains("default")){
		    		textView_pc.setVisibility(View.VISIBLE);
		    		if((AvailList.get(0).getUse_company_name() + "").contains("1")){
		        		if(AvailList.get(0).getTv_postedBy().length() > 0){
		        			textView_pc.getBackground().setColorFilter(AvailList.get(0).getColor(), PorterDuff.Mode.SRC_IN);
//		        			textView_pc.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, Utilities.getRandomColor(activity)));
		        			textView_pc.setText(String.valueOf(AvailList.get(0).getTv_postedBy().charAt(0)).toUpperCase());
		        		} else {
		            		textView_pc.setVisibility(View.GONE);
		        		}
		    		} else{
		    			if(AvailList.get(0).getTv_postedBy().length() > 0){
		        			textView_pc.getBackground().setColorFilter(AvailList.get(0).getColor(), PorterDuff.Mode.SRC_IN);
		    				textView_pc.setText(String.valueOf(AvailList.get(0).getTv_postedBy().charAt(0)).toUpperCase());
		    			} else {
		    				textView_pc.setVisibility(View.GONE);
		    			}
		    		}
		        }

				String temp_getAddress = AvailList.get(0).getProperty_landmark() + " " + AvailList.get(0).getProperty_city();
				if(Utilities.D)Log.v(TAG,"------temp_getAddress=" + temp_getAddress);
				new GetMapsLatLngTask().execute(temp_getAddress.replace(" ", "%20"));

				ImageLoader.getInstance().displayImage(AvailList.get(0).getThumbnailUrl(), iv_thumbnail, AvailPCFragment.animateFirstListener);
				if(Utilities.D)Log.v(TAG, "AvailList.get(0).getImageUrl()======="+AvailList.get(0).getImageUrl());
				if(!AvailList.get(0).getImageUrl().contains("http") || AvailList.get(0).getImageUrl().contains("default"))
					iv_image.setVisibility(View.GONE);
				else 
					ImageLoader.getInstance().displayImage(AvailList.get(0).getImageUrl(), iv_image, options, AvailPCFragment.animateFirstListener);
				
				if (Utilities.isInternetOn(ctx))
					new PROPERTY_IMG_Task().execute((AvailList.get(0).getPROPERTY_ID() + ""));

				tv_title.setText(
//						AvailList.get(0)
//						.getTv_proprty_listing_parent()
//						+ " of "
//						+
						AvailList.get(0).getProperty_type_name()
						+ " for "
						+ (AvailList.get(0).getProperty_listing_type())) ;
//								+ " in " + AvailList
//								.get(0).getProperty_landmark()));
				
//				String desc = " � " + AvailList.get(0).getProperty_description() ;
				
				Log.e(TAG, "descr++===" + AvailList.get(0).getProperty_description());
				tv_property_description.setText(AvailList.get(0).getProperty_description());
				if( AvailList.get(0).getProperty_description().contains("--")
						|| AvailList.get(0).getProperty_description().length() < 1){
					lnr_descirpin.setVisibility(View.GONE);
				}else{
					lnr_descirpin.setVisibility(View.VISIBLE);
				}

				tv_area.setText(AvailList.get(0).getTv_area());
				tv_no_of_bathrooms.setText(AvailList.get(0).getNo_of_bathrooms());
				
				tv_amenities.setText(AvailList.get(0).getAmenities());//FIXME: setamem
				tv_locality_proj_name.setText(AvailList.get(0).getProperty_location());
				tv_bedrooms.setText(AvailList.get(0).getNo_of_bedrooms());
				tv_no_of_balcony.setText(AvailList.get(0).getNo_of_balcony());
				tv_no_of_washrooms.setText(AvailList.get(0).getNo_of_washrooms());
				// tv_area.setText(AvailList.get(0).getProperty_area());
				tv_property_maintenance_amount.setText(AvailList.get(0).getProperty_maintenance_amount()
						+ " " + (AvailList.get(0).getProperty_maintenance_frequency()));
				tv_property_possession.setText(AvailList.get(0).getProperty_possession());
				tv_property_furnishing_type.setText(AvailList.get(0).getProperty_furnishing_type());
				tv_transaction_type.setText(AvailList.get(0).getTransaction_type());
				tv_property_ownership.setText(AvailList.get(0).getProperty_ownership());
				tv_property_availability.setText(AvailList.get(0).getProperty_availability());

				tv_property_listing_type.setText(AvailList.get(0).getTv_userType());

				tv_property_type_name.setText(AvailList.get(0).getProperty_listing_type());

//				tv_property_area.setText(AvailList.get(0).getProperty_area());
				tv_property_type_name_parent.setText(AvailList.get(0).getProperty_type_name_parent());
				tv_property_city.setText(AvailList.get(0).getProperty_city());
				tv_property_landmark.setText("in " + AvailList.get(0).getProperty_landmark());
				tv_expected_unit_price.setText(AvailList.get(0).getExpected_unit_price());
				tv_property_type_name_parent.setText(AvailList.get(0).getProperty_type_name_parent());
				
				tv_expected_unit_price_unit.setText(AvailList.get(0).getExpected_unit_price_unit());
				try {
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateTime = sdf2.parse("" + AvailList.get(0).getTv_postedTime());
//					temp_date = "" + sdf1.format(dateTime);
					tv_postedTime.setText(Utilities.getTimeAgo(dateTime, ctx));
				} catch (Exception e) { e.printStackTrace(); }
				
			
				if((AvailList.get(0).getUse_company_name() + "").contains("1")){
					tv_postedBy.setText(Utilities.makeFirstLetterCaps(AvailList.get(0).getCompany_name()+""));
//					if(Utilities.D)Log.v(TAG,"#########check#########m.getCompany_name()"+m.getUse_company_name()+"="+m.getCompany_name());
				} else{
					tv_postedBy.setText(Utilities.makeFirstLetterCaps(AvailList.get(0).getTv_postedBy()+""));
//					if(Utilities.D)Log.v(TAG,"#########check#########m.getTv_postedBy()"+m.getUse_company_name()+"="+m.getTv_postedBy());
				}
				tv_userType.setText("( " + Utilities.makeFirstLetterCaps(AvailList.get(0).getTv_userType()) + " )");

				if (googleMap == null) {
					if(Utilities.D)Log.v(TAG,"Sorry, Unable to create Map !");
				} else {
					try {
						Double lat = Double.parseDouble(AvailList.get(0).getProperty_latitude());
						Double lng = Double.parseDouble(AvailList.get(0).getProperty_longitude());
						if(Utilities.D)Log.v(TAG,"getProperty_latitude=" + lat);
						if(Utilities.D)Log.v(TAG,"getProperty_longitude=" + lng);
						MarkerOptions marker = new MarkerOptions().position(
								new LatLng(lat, lng)).title("");
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
						googleMap.addMarker(marker);

						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(lat, lng)).zoom(15).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//----------------------start amenities selection---------------------------
				ImageView iv_ava_req = (ImageView) findViewById(R.id.iv_ava_req);
				if(AvailList.get(0).getTv_proprty_listing_parent().contains("Requirement")){
					iv_ava_req.setBackgroundResource(R.mipmap.requirement_icon_1);
				} else if(AvailList.get(0).getTv_proprty_listing_parent().contains("Availability")){
					iv_ava_req.setBackgroundResource(R.mipmap.availability_icon_1);
				}

			    final ImageButton btn_airport = (ImageButton)findViewById(R.id.btn_airport);
			    final ImageButton btn_bus = (ImageButton)findViewById(R.id.btn_bus);
			    final ImageButton btn_gym = (ImageButton)findViewById(R.id.btn_gym);
			    final ImageButton btn_hospital = (ImageButton)findViewById(R.id.btn_hospital);
			    final ImageButton btn_internet = (ImageButton)findViewById(R.id.btn_internet);
			    final ImageButton btn_market = (ImageButton)findViewById(R.id.btn_market);
			    final ImageButton btn_park = (ImageButton)findViewById(R.id.btn_park);
			    final ImageButton btn_railway_stn = (ImageButton)findViewById(R.id.btn_railway_stn);
			    final ImageButton btn_school = (ImageButton)findViewById(R.id.btn_school);
			    if(AvailList.get(0).getAmenities().contains("Airport") || AvailList.get(0).getAmenities().contains("airport")){
			    	btn_airport.setBackgroundResource(R.mipmap.airport_selected);
			    } else
			    	btn_airport.setBackgroundResource(R.mipmap.airport_unselected);

			    if(AvailList.get(0).getAmenities().contains("Bus") || AvailList.get(0).getAmenities().contains("bus")){
			    	btn_bus.setBackgroundResource(R.mipmap.bus_selected);
			    } else
			    	btn_bus.setBackgroundResource(R.mipmap.bus_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("Gym") || AvailList.get(0).getAmenities().contains("gym")){
			    	btn_gym.setBackgroundResource(R.mipmap.gym_selected);
			    } else
			    	btn_gym.setBackgroundResource(R.mipmap.gym_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("Hospital") || AvailList.get(0).getAmenities().contains("hospital")){
			    	btn_hospital.setBackgroundResource(R.mipmap.hospital_selected);
			    } else
			    	btn_hospital.setBackgroundResource(R.mipmap.hospital_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("Internet") || AvailList.get(0).getAmenities().contains("internet")){
			    	btn_internet.setBackgroundResource(R.mipmap.internet_selected);
			    } else
			    	btn_internet.setBackgroundResource(R.mipmap.internet_unselected);
//----			    	
			    if(AvailList.get(0).getAmenities().contains("Market") || AvailList.get(0).getAmenities().contains("market")){
			    	btn_market.setBackgroundResource(R.mipmap.market_selected);
			    } else
			    	btn_market.setBackgroundResource(R.mipmap.market_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("Park") || AvailList.get(0).getAmenities().contains("park")){
			    	btn_park.setBackgroundResource(R.mipmap.park_selected);
			    } else
			    	btn_park.setBackgroundResource(R.mipmap.park_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("Railway") || AvailList.get(0).getAmenities().contains("railway")){
			    	btn_railway_stn.setBackgroundResource(R.mipmap.rail_selected);
			    } else
			    	btn_railway_stn.setBackgroundResource(R.mipmap.rail_unselected);
			    	
			    if(AvailList.get(0).getAmenities().contains("School") || AvailList.get(0).getAmenities().contains("school")){
			    	btn_school.setBackgroundResource(R.mipmap.school_selected);
			    } else
			    	btn_school.setBackgroundResource(R.mipmap.school_unselected);
			    	
//-------------------------------------end amenities----------------------------------
			} else {

			}
		}
	}

	class PROPERTY_IMG_Task extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data, url_propertyImage = "";
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
				String jsonString = Utilities.sendData(UrlBase,
						str_send_json);
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
//					}
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
				url_propertyImage = file_url;
				if(Utilities.D)Log.v(TAG,"file_url:" + file_url);
				
				ImageLoader.getInstance().displayImage(file_url, iv_image, options, AvailPCFragment.animateFirstListener);

			} else if(api_code == 400){
			} else {
			}
		}
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
				String LOCALITY_API = 
						"http://maps.googleapis.com/maps/api/geocode/json?libraries=places&region=in&language=en&sensor=false&address="
						+ f_url[0];

				String jsonString = Utilities.readJson(ctx, LOCALITY_API);
				JSONObject reader = new JSONObject(jsonString);
				String status = reader.getString("status");

				if (status.equals("OK")) {
					JSONArray results = (JSONArray) reader.get("results");
					JSONObject geometry = (JSONObject) results.getJSONObject(0).get("geometry");
					JSONObject location = new JSONObject(geometry.getString("location"));
					api_lat = Double.parseDouble(location.getString("lat"));
					api_lng = Double.parseDouble(location.getString("lng"));
					if(Utilities.D)Log.v(TAG,"api_lat="+api_lat);
					if(Utilities.D)Log.v(TAG,"api_lng="+api_lng);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			if(googleMap == null){
				if(Utilities.D)Log.v(TAG,"Sorry, Unable to create Map !");
			}else{
				try {
        			MarkerOptions marker = new MarkerOptions().position(new LatLng(api_lat, api_lng)).title("");
        			marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        			marker.draggable(true);
        			googleMap.addMarker(marker);

        			CameraPosition cameraPosition = new CameraPosition.Builder()
        			.target(new LatLng(api_lat, api_lng)).zoom(14).build();

        			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}
}
