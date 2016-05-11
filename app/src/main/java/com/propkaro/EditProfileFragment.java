package com.propkaro;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;
public class EditProfileFragment extends Fragment implements OnItemSelectedListener {
	static String TAG = EditProfileFragment.class.getSimpleName();
	
	String getFirstName= "", getLastName = "", getEmailID = "", getFName = "", getLName = "",
			getUserName = "", getMobile_num = "", getConfirmPassword = "",
	getCompanyName = "", getCompanyAddress = "", getUserType = "", getCity = "", 
	getUserDataID = "", getabout_cmp = "", getProfileImage = "", getAboutU = "", getUseCompanyName = "";
	ImageView iv_profile; 
	int m;
	Spinner spin_userType;
	Button BTN_CITY, btn_ChangePicture, btn_DeletePicture, btn_changePassword,btn_save;
	EditText et_fName, et_lName, et_phone, et_password, et_email, et_cmp_name, et_cmp_addr, et_about_cmp, et_about_me;
	TextView textView_pc,tv_name, tv_userType,ic_cs1,ic_csy1,ic_c3fsy1;
	CheckBox cb_useCompanyName;
	Context ctx;
    private CoordinatorLayout mCoordinator;
	String str_userType[] = { "Agent", "Builder", "Individual" };
	SharedPreferences mPrefs; TinyDB db;
	String temp_userType = "";
	ArrayList<String> api_city = new ArrayList<String>();
	private EditText mSearchView;
	private ListView mListView;
	ArrayAdapter<String> adapter_cities;
	Typeface RobotoLight;

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity(); 
		db = new TinyDB(ctx);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		api_city = db.getListString("PREF_KEY_CITIES_ALL");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		
		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
//		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		getUseCompanyName = mPrefs.getString("PREF_KEY_USE_COMPANY_NAME", "");
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");

		if(Utilities.D)Log.v(TAG,"getMobile_num=" + getMobile_num);
		if(Utilities.D)Log.v(TAG,"getEmailID=" + getEmailID);
		if(Utilities.D)Log.v(TAG,"getUserType=" + getUserType);
		if(Utilities.D)Log.v(TAG,"getUserID=" + getUserDataID);
		if(Utilities.D)Log.v(TAG,"getCity=" + getCity);
		if(Utilities.D)Log.v(TAG,"getUserName=" + getUserName);
		if(Utilities.D)Log.v(TAG,"getUseCompanyName=" + getUseCompanyName);
		if(Utilities.D)Log.v(TAG,"getProfileImage=" + getProfileImage);

		et_phone.setEnabled(false);
		et_password.setVisibility(View.GONE);

		ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, AvailPCFragment.animateFirstListener);

		textView_pc.setText("");
    	textView_pc.setVisibility(View.GONE);
    	if(getProfileImage.contains("default")){
    		textView_pc.setVisibility(View.VISIBLE);
			if(getFName.length() > 0){
    			textView_pc.getBackground().setColorFilter(Utilities.getRandomColor(ctx,getFName), PorterDuff.Mode.SRC_IN);
				textView_pc.setText(String.valueOf(getFName.charAt(0)).toUpperCase());
			} else {
				textView_pc.setVisibility(View.GONE);
			}
    	}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.edit_profile, container, false);
		Toolbar mToolbar = (Toolbar) rView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//        activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                getActivity().finish();
            }
        });
        activity.getSupportActionBar().setTitle("Profile Detail");
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");

		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		temp_userType = getUserType;
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");

		getCompanyName = mPrefs.getString("PREF_KEY_COMPANY_NAME", "");
		getUseCompanyName = mPrefs.getString("PREF_KEY_USE_COMPANY_NAME", "");
		getCompanyAddress = mPrefs.getString("PREF_KEY_COMPANY_ADDRESS", "");
		getAboutU = mPrefs.getString("PREF_KEY_ABOUT_U", "");

		getFirstName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
		getLastName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
		
		if(Utilities.D)Log.v(TAG,"getMobile_num=" + getMobile_num);
		if(Utilities.D)Log.v(TAG,"getEmailID=" + getEmailID);
		if(Utilities.D)Log.v(TAG,"getUserType=" + getUserType);
		if(Utilities.D)Log.v(TAG,"getUserID=" + getUserDataID);
		if(Utilities.D)Log.v(TAG,"getFirstName=" + getFirstName);
		if(Utilities.D)Log.v(TAG,"getProfileImage=" + getProfileImage);
		
		iv_profile = (ImageView)rView.findViewById(R.id.iv_profile);
		textView_pc = (TextView) rView.findViewById(R.id.textView_pc);
		
		ic_cs1= (TextView) rView.findViewById(R.id.ic_cs1);
		ic_csy1= (TextView) rView.findViewById(R.id.ic_csy1);
		ic_c3fsy1= (TextView) rView.findViewById(R.id.ic_c3fsy1);
		
		btn_ChangePicture = (Button)rView.findViewById(R.id.btn_ChangePicture);
		btn_changePassword = (Button)rView.findViewById(R.id.btn_changePassword);
		btn_changePassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(ctx)){
					Intent i = new Intent(getActivity(), ChangePasswdActivity.class);
					i.putExtra("KEY_getDataID", getUserDataID);
					startActivity(i);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					
					//FIXME: DELETE FOLLOWING CHECKING..
//					String number = "1234567890";
//					try {
//						Utilities.getSha256Base64(number);
//						Utilities.getHashBase64(number);
//						Utilities.getShaHexBase64(number);
//					} catch (Exception e) { e.printStackTrace(); }
				}
			}
		});
		btn_DeletePicture = (Button)rView.findViewById(R.id.btn_DeletePicture);
//		btn_ChangePicture.setVisibility(View.GONE);
//		btn_DeletePicture.setVisibility(View.GONE);
		
		et_about_me = (EditText)rView.findViewById(R.id.et_about_me);
		et_about_me.setText(getAboutU);
		
//		String getContacts = mPrefs.getString("PREF_KEY_CONTACTS", "");
//		et_about_me.setText(getContacts);
		
		tv_name = (TextView) rView.findViewById(R.id.tv_name);
		et_fName = (EditText) rView.findViewById(R.id.et_fName);
		
		getLastName = getLastName.trim();
		if(getLastName.length() < 1){
			et_fName.setText(getFirstName);
		} else {
			et_fName.setText(getFirstName + " " + getLastName);
		}
		
		et_fName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_name.setText(et_fName.getText().toString());
				if(et_fName.getText().toString().length() == 0)
					tv_name.setText(getFirstName + " " + getLastName);
				
				getLastName = "";
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		tv_name.setText(getFirstName + " " + getLastName);
		
		tv_userType = (TextView) rView.findViewById(R.id.tv_userType);
		tv_userType.setText(Utilities.makeFirstLetterCaps(getUserType));

		et_lName = (EditText) rView.findViewById(R.id.et_lName);
		et_lName.setText(getLastName);

		et_phone = (EditText) rView.findViewById(R.id.et_phone);
		et_phone.setText(getMobile_num);

		et_password = (EditText) rView.findViewById(R.id.et_password);

		et_email = (EditText) rView.findViewById(R.id.et_email);
		et_email.setText(getEmailID);
		et_email.setEnabled(false);

		et_cmp_addr = (EditText) rView.findViewById(R.id.et_cmp_addr);
		et_cmp_addr.setText(getCompanyAddress);

		et_cmp_name = (EditText) rView.findViewById(R.id.et_cmp_name);
		et_cmp_name.setText(getCompanyName);
		
		final ImageView ic_1 = (ImageView) rView.findViewById(R.id.ic_1);
		final ImageView ic_2 = (ImageView) rView.findViewById(R.id.ic_2);
		final ImageView ic_3 = (ImageView) rView.findViewById(R.id.ic_3);
		
		switch(getUserType){
		case "Individual":
	        ic_1.setImageResource(R.mipmap.individual_selected_icon);
	        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
	        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
			break;
		case "Builder":
	        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
	        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
	        ic_3.setImageResource(R.mipmap.builder_selceted_icon);
			break;
		case "Agent":
	        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
	        ic_2.setImageResource(R.mipmap.agent_selected_icon);
	        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
			break;
		case "individual":
	        ic_1.setImageResource(R.mipmap.individual_selected_icon);
	        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
	        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
			break;
		case "builder":
	        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
	        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
	        ic_3.setImageResource(R.mipmap.builder_selceted_icon);
			break;
		case "agent":
	        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
	        ic_2.setImageResource(R.mipmap.agent_selected_icon);
	        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
			break;
		}

		ic_1.setOnTouchListener(new OnTouchListener() {

		    @SuppressLint("ClickableViewAccessibility") 
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString()); 
			        
		        ic_1.setImageResource(R.mipmap.individual_selected_icon);
		        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
		        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
				getUserType = "Individual";
		        return false;
		    }
		    });
		ic_2.setOnTouchListener(new OnTouchListener() {

		    @SuppressLint("ClickableViewAccessibility")
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString());
			        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
			        ic_2.setImageResource(R.mipmap.agent_selected_icon);
			        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
					getUserType = "Agent"; 
			        
		        return false;
		    }
		    });
		ic_3.setOnTouchListener(new OnTouchListener() {

		    @SuppressLint("ClickableViewAccessibility") @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString());
			        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
			        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
			        ic_3.setImageResource(R.mipmap.builder_selceted_icon);
			        
					getUserType = "Builder";
					
		        return false;
		    }
		    });
		cb_useCompanyName = (CheckBox)rView.findViewById(R.id.cb_useCompanyName);
		if(Utilities.D)Log.v(TAG,"getUseCompanyName=" + getUseCompanyName);
		if(getUseCompanyName.equals("1")){
			cb_useCompanyName.setChecked(true);
		} else 
			cb_useCompanyName.setChecked(false);
		
		btn_DeletePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		        alertDialogBuilder.setTitle("Alert");
		        alertDialogBuilder.setMessage("Are you sure to remove picture !")
		        .setCancelable(true)
		        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int id){
		            	dialog.cancel();
						if(Utilities.isInternetOn(ctx))
							new DeletePictureTask().execute("");
		            }
		        });
		        
		        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int id){
		                dialog.cancel();
		            }
		        });
		        AlertDialog alert = alertDialogBuilder.create();
		        alert.show();
			}
		});

		btn_ChangePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Bundle args = new Bundle();
				args.putString("KEY_getDataID", getUserDataID);
				args.putString("KEY_getUserType", getUserType);
				args.putString("KEY_isGridOpen", "no");
				
				Fragment f = new UploadPicFragment();
				f.setArguments(args);

				Utilities.addFragment(getActivity(), f, R.id.container, true, true);
			}
		});

		btn_save = (Button) rView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(cb_useCompanyName.isChecked())
					getUseCompanyName = "1";
				else
					getUseCompanyName = "0";
				
				getAboutU = et_about_me.getText().toString();
				getCompanyAddress = et_cmp_addr.getText().toString();
				getCompanyName = et_cmp_name.getText().toString();
				getFirstName = et_fName.getText().toString();
				getLastName = et_lName.getText().toString();
				getMobile_num = et_phone.getText().toString();
//				getPassword = et_password.getText().toString();
				
				String result = getFirstName.substring(getFirstName.lastIndexOf(" ") + 1);
				if(Utilities.D)Log.i(TAG, " -------------------------------------------- "+result);
				
				ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, AvailPCFragment.animateFirstListener);
				
		    	textView_pc.setText("");
		    	textView_pc.setVisibility(View.GONE);
		    	if(getProfileImage.contains("default")){
		    		textView_pc.setVisibility(View.VISIBLE);
	    			if(getFName.length() > 0){
	        			textView_pc.getBackground().setColorFilter(Utilities.getRandomColor(ctx,getFirstName), PorterDuff.Mode.SRC_IN);
	    				textView_pc.setText(String.valueOf(getFName.charAt(0)).toUpperCase());
	    			} else {
	    				textView_pc.setVisibility(View.GONE);
	    			}
		    	}
		    	
		    	getLastName = "";
		    	
				if(cb_useCompanyName.isChecked() == true) {
					if (getFirstName.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Full Name !");
					} else if (getMobile_num.length() < 10) {
						Utilities.Snack(mCoordinator, "Please enter 10 digit mobile number !");
					}else if(et_cmp_name.getText().toString().isEmpty()){
						Utilities.Snack(mCoordinator, "Please enter company name!");
					}else if(BTN_CITY.getText().toString().contains("Select City")){
						Utilities.Snack(mCoordinator, "Please Select city !");
					} else {
						if (Utilities.isInternetOn(ctx) == true)
							new EditProfileTask().execute("");
					}
				} else{
					
					if(getFirstName.contains("  ")){
						getFirstName = getFirstName.trim();
//						getFirstName = getFirstName.replace("  ", "");
					}
					
					if (getFirstName.length() == 0 || getFirstName.equals(" ") || getFirstName.equals("  ")) {
						Utilities.Snack(mCoordinator, "Please enter Full Name! ");
					} else if (getMobile_num.length() < 10) {
						Utilities.Snack(mCoordinator, "Please enter 10 digit mobile number !");
					}else if(BTN_CITY.getText().toString().contains("Select City")){
						Utilities.Snack(mCoordinator, "Please select city !");
					} else {
						if (Utilities.isInternetOn(ctx) == true)
							new EditProfileTask().execute("");
					}
				}
			}
		});
		
		BTN_CITY = (Button) rView.findViewById(R.id.spin_city);
		BTN_CITY.setVisibility(View.GONE);
		spin_userType = (Spinner) rView.findViewById(R.id.spin_userType);
		BTN_CITY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCityDialog(BTN_CITY, v);
			}
		});
		spin_userType.setOnItemSelectedListener(this);
		ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_userType);

		userAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		spin_userType.setAdapter(userAdapter);
		
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
				BTN_CITY.setText(getCity);
				if(getCity.length() < 1){
					BTN_CITY.setText("Select City");
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		setFont();
		return rView;
	}
	private void setFont() {
		BTN_CITY.setTypeface(RobotoLight);
		textView_pc .setTypeface(RobotoLight);
		btn_ChangePicture.setTypeface(RobotoLight); 
		btn_DeletePicture.setTypeface(RobotoLight); 
		btn_changePassword.setTypeface(RobotoLight);
		et_fName.setTypeface(RobotoLight); 
		et_lName.setTypeface(RobotoLight); 
		et_phone.setTypeface(RobotoLight);
		et_password.setTypeface(RobotoLight);
		et_email.setTypeface(RobotoLight);
		et_cmp_name.setTypeface(RobotoLight);
		et_cmp_addr.setTypeface(RobotoLight);
		et_about_me.setTypeface(RobotoLight);
		tv_name.setTypeface(RobotoLight); 
		tv_userType.setTypeface(RobotoLight);
		cb_useCompanyName.setTypeface(RobotoLight);
		ic_cs1.setTypeface(RobotoLight);
		ic_csy1.setTypeface(RobotoLight);
		ic_c3fsy1.setTypeface(RobotoLight);
//		btn_save.setTypeface(RobotoLight);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String item = parent.getItemAtPosition(position).toString();

		if(Utilities.D)Log.v(TAG,"Item=" + item);
		if(Utilities.D)Log.v(TAG,"Position=" + position);

		if (parent.getId() == R.id.spin_userType) {
			getUserType = item.toLowerCase();
			for (int i = 0; i < str_userType.length; i++) {
				if (temp_userType.contains(str_userType[i].toLowerCase())) {
					spin_userType.setSelection(i);
					if(Utilities.D)Log.v(TAG,"spin_userType[i]=" + str_userType[i]);
					if(Utilities.D)Log.v(TAG,"temp_userType=" + temp_userType);
					temp_userType = "";
				}
			}

			if (position == 2) {
				et_cmp_name.setVisibility(View.GONE);
				et_cmp_addr.setVisibility(View.GONE);
				cb_useCompanyName.setVisibility(View.GONE);
			} else {
				et_cmp_name.setVisibility(View.VISIBLE);
				et_cmp_addr.setVisibility(View.VISIBLE);
				cb_useCompanyName.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	class EditProfileTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String userId = "", userName = "", api_message = "",
				api_otp = "", api_dataID = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(getActivity());
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String EDIT_PROFILE_USER = "{\"fname\":\"" + Utilities.makeFirstLetterCaps(getFirstName)
						+ "\", \"lname\":\"" + Utilities.makeFirstLetterCaps(getLastName)
						+ "\", \"phone_no\":\"" + getMobile_num
						+ "\", \"about_u\":\"" + getAboutU
						+ "\", \"use_company_name\":\"" + getUseCompanyName
//						+ "\", \"password\":\"" + getPassword
						+ "\", \"company_name\":\"" + Utilities.makeFirstLetterCaps(getCompanyName)
						+ "\", \"company_address\":\"" + Utilities.makeFirstLetterCaps(getCompanyAddress)
						+ "\", \"user_type\":\"" + getUserType.toLowerCase()
						+ "\", \"city\":\"" + getCity
						+ "\"}";
				
				String str_send_json = "{\"by\":{\"id\":\"" + getUserDataID
						+ "\"}, \"table\":\"registration\", \"params\":"
						+ EDIT_PROFILE_USER + "}";
				
				if(Utilities.D)Log.v(TAG,"str_send_json="+str_send_json);
				String UrlBase = Host.UpdateUrl + "";
				String jsonString = Utilities.sendData(UrlBase, str_send_json);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				// api_otp = meta.getString("otp");

				if (api_code == 200) {
					api_dataID = reader.getString("data");
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

				Bundle args = new Bundle();
				args.putString("KEY_getMobNumber", getMobile_num);
				args.putString("KEY_getFName", getFirstName);
				args.putString("KEY_getLName", getLastName);
				args.putString("KEY_getUserType", getUserType);
				args.putString("KEY_getCityType", getCity);

				args.putString("KEY_dataID", api_dataID);
				// args.putString("KEY_getOTP", api_otp);

				getActivity().finish();
				Utilities.Snack(mCoordinator, "Profile update: " + api_message);
				
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_MOB_NUM", getMobile_num);
				editor.putString("PREF_KEY_USER_TYPE", getUserType);
				editor.putString("PREF_KEY_FIRST_NAME", getFirstName);
				editor.putString("PREF_KEY_LAST_NAME", getLastName);
				editor.putString("PREF_KEY_USER_NAME", getFirstName + " " + getLastName);
				editor.putString("PREF_KEY_CITY_NAME", getCity);
				
				editor.putString("PREF_KEY_COMPANY_NAME", getCompanyName);
				editor.putString("PREF_KEY_USE_COMPANY_NAME", getUseCompanyName);
				editor.putString("PREF_KEY_COMPANY_ADDRESS", getCompanyAddress);
				editor.commit();
			} else if(api_code == 400){
				getActivity().finish();
//				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			}else {
//				getActivity().finish();
				Utilities.Snack(mCoordinator, "Update failed: " + api_message);
			}
		}
	}
	public void openCityDialog(View v, final View v2) {
		final Dialog dialog;
		dialog = new Dialog(ctx, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.list_search_dialog);

		mSearchView = (EditText) dialog.findViewById(R.id.ListSearch);
		mListView = (ListView) dialog.findViewById(R.id.listViewDialog);
		mListView.setFastScrollEnabled(true);
		adapter_cities = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_expandable_list_item_1,
				api_city);

		mListView.setAdapter(adapter_cities);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int positionOrigional = api_city.indexOf((String) mListView.getItemAtPosition(position));
				try {
					getCity = api_city.get(positionOrigional);
					Log.e(getCity, getCity + " = "+positionOrigional);
					if (getCity.length() == 0) {
						BTN_CITY.setText("Select City");
					}else {
						BTN_CITY.setText(getCity);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Utilities.Snack(mCoordinator, "Some error occurred !");
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
	class DeletePictureTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String userId = "", userName = "", api_message = "", api_otp = "", api_dataID = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(getActivity());
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String EDIT_PROFILE_USER = "{" +
						"\"image\":\"" + "/images/profile_images/default.jpg"
						+ "\"}";
				
				String str_send_json = "{\"by\":{\"id\":\"" + getUserDataID
						+ "\"}, \"table\":\"registration\", \"params\":"
						+ EDIT_PROFILE_USER + "}";
				
				if(Utilities.D)Log.v(TAG,"str_send_json="+str_send_json);
				String UrlBase = Host.UpdateUrl + "";
				String jsonString = Utilities.sendData(UrlBase, str_send_json);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_dataID = reader.getString("data");
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

				Utilities.Snack(mCoordinator, api_message);
				getProfileImage = Host.MainUrl + "/images/profile_images/default.jpg";

				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_PROFILE_IMAGE", getProfileImage);
				editor.putString("PREF_KEY_USER_TYPE", getUserType);
				editor.commit();

				ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, AvailPCFragment.animateFirstListener);
		    	textView_pc.setText("");

		    	textView_pc.setVisibility(View.GONE);
		    	if(getProfileImage.contains("default")){
		    		textView_pc.setVisibility(View.VISIBLE);
	    			if(getFName.length() > 0){
	        			textView_pc.getBackground().setColorFilter(Utilities.getRandomColor(ctx,getFName), PorterDuff.Mode.SRC_IN);
//		      			textView_pc.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, Utilities.getRandomColor(activity)));
	    				textView_pc.setText(String.valueOf(getFName.charAt(0)).toUpperCase());
	    			} else {
	    				textView_pc.setVisibility(View.GONE);
	    			}
		    	}

			} else {

				Utilities.Snack(mCoordinator, api_message);
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
				db.putString("PREF_KEY_CITIES10", jsonString);
				
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
				Utilities.printKeyHash(getActivity());
				db.putListString("PREF_KEY_CITIES_ALL", api_city);
				if (api_city.size() != 0) {
					BTN_CITY.setVisibility(View.VISIBLE);
				}
			} else {
				Utilities.Snack(mCoordinator, "No cities found, please try again !");
				getActivity().finish();
			}
		}
	}
}